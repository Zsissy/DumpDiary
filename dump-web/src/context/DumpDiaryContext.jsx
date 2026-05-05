/* eslint-disable react-refresh/only-export-components */
import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { IS_CLOUD_MODE, supabase } from '../lib/supabase.js'
import { useAuth } from './AuthContext.jsx'

const ROOM_TABLE = 'app_sync_rooms'
const ROOM_LOCAL_PREFIX = 'dump-diary-room-v1'
const ROOM_DB_NAME = 'dump-diary-room-cache-v1'
const ROOM_DB_STORE = 'rooms'

function getDefaultRoomData() {
  return {
    bowelLogs: [],
    updatedAt: '',
  }
}

function normalizeRoomData(payload) {
  return {
    bowelLogs: Array.isArray(payload?.bowelLogs || payload?.bowel_logs)
      ? (payload.bowelLogs || payload.bowel_logs)
      : [],
    updatedAt: payload?.updatedAt || payload?.updated_at || '',
  }
}

function toCloudRow(roomCode, roomData, updatedAt = new Date().toISOString()) {
  return {
    room_code: roomCode,
    bowel_logs: roomData.bowelLogs || [],
    updated_at: updatedAt,
  }
}

function toLocalStorageKey(roomCode) {
  return `${ROOM_LOCAL_PREFIX}-${encodeURIComponent(roomCode)}`
}

function readLocalRoom(roomCode) {
  try {
    const raw = localStorage.getItem(toLocalStorageKey(roomCode))
    if (!raw) return getDefaultRoomData()
    return normalizeRoomData(JSON.parse(raw))
  } catch {
    return getDefaultRoomData()
  }
}

function writeLocalRoom(roomCode, roomData) {
  localStorage.setItem(toLocalStorageKey(roomCode), JSON.stringify(roomData))
}

function openRoomDb() {
  return new Promise((resolve, reject) => {
    if (typeof indexedDB === 'undefined') {
      resolve(null)
      return
    }
    const request = indexedDB.open(ROOM_DB_NAME, 1)
    request.onupgradeneeded = () => {
      const db = request.result
      if (!db.objectStoreNames.contains(ROOM_DB_STORE)) {
        db.createObjectStore(ROOM_DB_STORE, { keyPath: 'roomCode' })
      }
    }
    request.onsuccess = () => resolve(request.result)
    request.onerror = () => reject(request.error)
  })
}

async function readCachedRoom(roomCode) {
  const local = readLocalRoom(roomCode)
  try {
    const db = await openRoomDb()
    if (!db) return local
    const indexedValue = await new Promise((resolve, reject) => {
      const transaction = db.transaction(ROOM_DB_STORE, 'readonly')
      const store = transaction.objectStore(ROOM_DB_STORE)
      const request = store.get(roomCode)
      request.onsuccess = () => resolve(request.result?.data || null)
      request.onerror = () => reject(request.error)
    })
    db.close()
    if (!indexedValue) return local
    const indexed = normalizeRoomData(indexedValue)
    const indexedTime = new Date(indexed.updatedAt || 0).getTime()
    const localTime = new Date(local.updatedAt || 0).getTime()
    return indexedTime >= localTime ? indexed : local
  } catch {
    return local
  }
}

async function writeCachedRoom(roomCode, roomData) {
  const normalized = normalizeRoomData(roomData)
  try {
    writeLocalRoom(roomCode, normalized)
  } catch {
    // ignore localStorage overflow
  }
  try {
    const db = await openRoomDb()
    if (!db) return
    await new Promise((resolve, reject) => {
      const transaction = db.transaction(ROOM_DB_STORE, 'readwrite')
      const store = transaction.objectStore(ROOM_DB_STORE)
      const request = store.put({ roomCode, data: normalized })
      request.onsuccess = () => resolve(true)
      request.onerror = () => reject(request.error)
    })
    db.close()
  } catch {
    // ignore IndexedDB failures
  }
}

function pickNewerRoomData(primary, secondary) {
  const primaryTime = new Date(primary?.updatedAt || 0).getTime()
  const secondaryTime = new Date(secondary?.updatedAt || 0).getTime()
  return secondaryTime > primaryTime ? secondary : primary
}

function resolveRoomCode(user) {
  const matchCode = String(user?.matchCode || '').trim()
  if (matchCode) return `pair:${matchCode}`
  return `user:${user?.id || user?.username || 'default'}`
}

async function fetchCloudRoom(roomCode) {
  if (!supabase) return getDefaultRoomData()
  const { data, error } = await supabase
    .from(ROOM_TABLE)
    .select('*')
    .eq('room_code', roomCode)
    .maybeSingle()
  if (error) throw error
  if (data) return normalizeRoomData(data)
  const base = getDefaultRoomData()
  const now = new Date().toISOString()
  const { error: upsertError } = await supabase
    .from(ROOM_TABLE)
    .upsert(toCloudRow(roomCode, base, now), { onConflict: 'room_code' })
  if (upsertError) throw upsertError
  return { ...base, updatedAt: now }
}

function generateId() {
  return `log-${Date.now()}-${Math.random().toString(36).slice(2, 9)}`
}

const DumpDiaryContext = createContext(null)

export function DumpDiaryProvider({ children }) {
  const { user, isAuthenticated } = useAuth()
  const roomCode = useMemo(() => resolveRoomCode(user), [user])

  const [bowelLogs, setBowelLogs] = useState([])
  const [isRoomReady, setIsRoomReady] = useState(false)
  const [isRoomSyncing, setIsRoomSyncing] = useState(false)
  const [roomSyncError, setRoomSyncError] = useState('')

  const skipNextCloudWriteRef = useRef(false)
  const lastRemoteUpdatedAtRef = useRef('')
  const bowelLogsRef = useRef(bowelLogs)
  bowelLogsRef.current = bowelLogs

  // Hydrate on mount / user change
  useEffect(() => {
    let cancelled = false

    const hydrateRoom = async () => {
      setIsRoomReady(false)
      setRoomSyncError('')
      const local = await readCachedRoom(roomCode)

      if (IS_CLOUD_MODE && supabase && isAuthenticated) {
        setIsRoomSyncing(true)
        try {
          const remote = await fetchCloudRoom(roomCode)
          if (cancelled) return
          const nextRoom = pickNewerRoomData(remote, local)
          skipNextCloudWriteRef.current = true
          lastRemoteUpdatedAtRef.current = remote.updatedAt || ''
          setBowelLogs(nextRoom.bowelLogs)
          writeCachedRoom(roomCode, nextRoom)
        } catch {
          if (cancelled) return
          setBowelLogs(local.bowelLogs)
          setRoomSyncError('共享空间加载失败，已回退本地数据。')
        } finally {
          if (!cancelled) setIsRoomSyncing(false)
        }
      } else {
        setBowelLogs(local.bowelLogs)
      }

      if (!cancelled) setIsRoomReady(true)
    }

    hydrateRoom()

    return () => { cancelled = true }
  }, [isAuthenticated, roomCode])

  // Write to local cache on data change
  useEffect(() => {
    if (!isRoomReady) return
    writeCachedRoom(roomCode, {
      bowelLogs,
      updatedAt: new Date().toISOString(),
    })
  }, [bowelLogs, isRoomReady, roomCode])

  // Debounced cloud push
  useEffect(() => {
    if (!isRoomReady) return undefined
    if (!IS_CLOUD_MODE || !supabase || !isAuthenticated) return undefined

    if (skipNextCloudWriteRef.current) {
      skipNextCloudWriteRef.current = false
      return undefined
    }

    const timer = window.setTimeout(async () => {
      setIsRoomSyncing(true)
      const now = new Date().toISOString()
      writeCachedRoom(roomCode, { bowelLogs, updatedAt: now })
      const { error } = await supabase
        .from(ROOM_TABLE)
        .upsert(toCloudRow(roomCode, { bowelLogs }, now), { onConflict: 'room_code' })
      if (error) {
        setRoomSyncError('共享空间同步失败，请稍后重试。')
      } else {
        setRoomSyncError('')
        lastRemoteUpdatedAtRef.current = now
      }
      setIsRoomSyncing(false)
    }, 450)

    return () => window.clearTimeout(timer)
  }, [bowelLogs, isAuthenticated, isRoomReady, roomCode])

  // Polling sync
  useEffect(() => {
    if (!isRoomReady) return undefined
    if (!IS_CLOUD_MODE || !supabase || !isAuthenticated) return undefined

    let stopped = false
    const poll = async () => {
      const { data, error } = await supabase
        .from(ROOM_TABLE)
        .select('*')
        .eq('room_code', roomCode)
        .maybeSingle()
      if (stopped || error || !data) return

      const remote = normalizeRoomData(data)
      const remoteTime = new Date(remote.updatedAt || 0).getTime()
      if (remoteTime <= new Date(lastRemoteUpdatedAtRef.current || 0).getTime()) return

      const local = await readCachedRoom(roomCode)
      const nextRoom = pickNewerRoomData(remote, local)
      lastRemoteUpdatedAtRef.current = remote.updatedAt || ''
      skipNextCloudWriteRef.current = true
      setBowelLogs(nextRoom.bowelLogs)
      writeCachedRoom(roomCode, nextRoom)
    }

    const timer = window.setInterval(poll, 3200)
    return () => {
      stopped = true
      window.clearInterval(timer)
    }
  }, [isAuthenticated, isRoomReady, roomCode])

  const refreshRoom = useCallback(async () => {
    setIsRoomSyncing(true)
    try {
      if (IS_CLOUD_MODE && supabase && isAuthenticated) {
        const remote = await fetchCloudRoom(roomCode)
        const local = await readCachedRoom(roomCode)
        const nextRoom = pickNewerRoomData(remote, local)
        skipNextCloudWriteRef.current = true
        lastRemoteUpdatedAtRef.current = remote.updatedAt || ''
        setBowelLogs(nextRoom.bowelLogs)
        writeCachedRoom(roomCode, nextRoom)
        setRoomSyncError('')
      } else {
        const local = await readCachedRoom(roomCode)
        setBowelLogs(local.bowelLogs)
      }
    } catch {
      setRoomSyncError('刷新失败，请稍后重试。')
    } finally {
      setIsRoomSyncing(false)
    }
  }, [isAuthenticated, roomCode])

  const addBowelLog = useCallback((entry) => {
    const now = new Date().toISOString()
    const newLog = {
      id: generateId(),
      userId: user?.id || 'unknown',
      date: entry.date,
      time: entry.time || '',
      durationSeconds: Number(entry.durationSeconds) || 0,
      bristolType: Number(entry.bristolType) || 4,
      symptoms: Array.isArray(entry.symptoms) ? entry.symptoms : [],
      notes: String(entry.notes || ''),
      createdAt: now,
      updatedAt: now,
    }
    setBowelLogs((prev) => [...prev, newLog])
    return newLog
  }, [user?.id])

  const updateBowelLog = useCallback((id, updates) => {
    const now = new Date().toISOString()
    setBowelLogs((prev) =>
      prev.map((log) =>
        log.id === id ? { ...log, ...updates, updatedAt: now } : log,
      ),
    )
  }, [])

  const deleteBowelLog = useCallback((id) => {
    setBowelLogs((prev) => prev.filter((log) => log.id !== id))
  }, [])

  const getLogsByDate = useCallback((date) => {
    return bowelLogsRef.current.filter((log) => String(log.date).slice(0, 10) === String(date).slice(0, 10))
  }, [])

  const getLogsByMonth = useCallback((year, month) => {
    const prefix = `${year}-${String(month).padStart(2, '0')}`
    return bowelLogsRef.current.filter((log) => String(log.date).startsWith(prefix))
  }, [])

  const getLogsByYear = useCallback((year) => {
    return bowelLogsRef.current.filter((log) => String(log.date).startsWith(`${year}-`))
  }, [])

  const getStatsByMonth = useCallback((year, month) => {
    const logs = getLogsByMonth(year, month)
    return computeStats(logs)
  }, [getLogsByMonth])

  const getStatsByYear = useCallback((year) => {
    const logs = getLogsByYear(year)
    return computeStats(logs)
  }, [getLogsByYear])

  const getPartnerLogs = useCallback(() => {
    if (!user?.matchCode) return []
    return bowelLogsRef.current.filter((log) => log.userId !== user?.id)
  }, [user?.id, user?.matchCode])

  const value = useMemo(
    () => ({
      bowelLogs,
      isRoomReady,
      isRoomSyncing,
      roomSyncError,
      addBowelLog,
      updateBowelLog,
      deleteBowelLog,
      refreshRoom,
      getLogsByDate,
      getLogsByMonth,
      getLogsByYear,
      getStatsByMonth,
      getStatsByYear,
      getPartnerLogs,
    }),
    [
      bowelLogs, isRoomReady, isRoomSyncing, roomSyncError,
      addBowelLog, updateBowelLog, deleteBowelLog, refreshRoom,
      getLogsByDate, getLogsByMonth, getLogsByYear,
      getStatsByMonth, getStatsByYear, getPartnerLogs,
    ],
  )

  return (
    <DumpDiaryContext.Provider value={value}>
      {children}
    </DumpDiaryContext.Provider>
  )
}

export function useDumpDiary() {
  const context = useContext(DumpDiaryContext)
  if (!context) {
    throw new Error('useDumpDiary must be used within DumpDiaryProvider')
  }
  return context
}

function computeStats(logs) {
  if (!logs.length) {
    return {
      totalCount: 0,
      activeDays: 0,
      mostCommonType: null,
      avgDuration: 0,
      peakHour: null,
      typeCounts: [0, 0, 0, 0, 0, 0, 0],
      hourlyBuckets: new Array(24).fill(0),
    }
  }

  const typeCounts = [0, 0, 0, 0, 0, 0, 0]
  const hourlyBuckets = new Array(24).fill(0)
  const days = new Set()
  let totalDuration = 0
  let durationCount = 0

  for (const log of logs) {
    const t = log.bristolType
    if (t >= 1 && t <= 7) typeCounts[t - 1]++
    if (log.date) days.add(String(log.date).slice(0, 10))
    if (log.durationSeconds > 0) {
      totalDuration += log.durationSeconds
      durationCount++
    }
    if (log.time) {
      const hour = parseInt(log.time.split(':')[0], 10)
      if (!isNaN(hour) && hour >= 0 && hour < 24) hourlyBuckets[hour]++
    }
  }

  let mostCommonType = 1
  for (let i = 1; i < 7; i++) {
    if (typeCounts[i] > typeCounts[mostCommonType - 1]) mostCommonType = i + 1
  }

  let peakHour = null
  let peakCount = 0
  for (let h = 0; h < 24; h++) {
    if (hourlyBuckets[h] > peakCount) {
      peakCount = hourlyBuckets[h]
      peakHour = h
    }
  }

  return {
    totalCount: logs.length,
    activeDays: days.size,
    mostCommonType: typeCounts[mostCommonType - 1] > 0 ? mostCommonType : null,
    avgDuration: durationCount > 0 ? Math.round(totalDuration / durationCount) : 0,
    peakHour: peakCount > 0 ? peakHour : null,
    typeCounts,
    hourlyBuckets,
  }
}
