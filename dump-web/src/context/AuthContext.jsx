/* eslint-disable react-refresh/only-export-components */
import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { IS_CLOUD_MODE, supabase } from '../lib/supabase.js'

const AUTH_KEY = 'dump-diary-auth-v1'
const USERS_KEY = 'dump-diary-users-v1'
const USERS_TABLE = 'app_users'
const ADMIN_USERNAME = '小茭'
const ADMIN_PASSWORD = 'zxq121800'

const AuthContext = createContext(null)

function readStoredUser() {
  try {
    const raw = localStorage.getItem(AUTH_KEY)
    if (!raw) return null
    const parsed = JSON.parse(raw)
    if (!parsed?.username || !parsed?.role) return null
    return parsed
  } catch {
    return null
  }
}

function readStoredUsers() {
  try {
    const raw = localStorage.getItem(USERS_KEY)
    if (!raw) return []
    const parsed = JSON.parse(raw)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

function normalizeUsers(users) {
  return users
    .filter((user) => user && user.username)
    .map((user) => ({
      id: user.id || `user-${Date.now()}-${Math.random()}`,
      username: String(user.username).trim(),
      nickname: String(user.nickname || ''),
      password: String(user.password || ''),
      role: user.role === 'admin' ? 'admin' : 'member',
      status: user.status === 'approved' || user.status === 'rejected' ? user.status : 'pending',
      avatar: user.avatar || '',
      matchCode: String(user.matchCode || user.match_code || ''),
      createdAt: user.createdAt || user.created_at || new Date().toISOString(),
      reviewedAt: user.reviewedAt || user.reviewed_at || '',
      reviewedBy: user.reviewedBy || user.reviewed_by || '',
    }))
}

function normalizeCloudUsers(rows) {
  return normalizeUsers(
    rows.map((row) => ({
      id: row.id,
      username: row.username,
      nickname: row.nickname,
      password: row.password,
      role: row.role,
      status: row.status,
      avatar: row.avatar,
      match_code: row.match_code,
      created_at: row.created_at,
      reviewed_at: row.reviewed_at,
      reviewed_by: row.reviewed_by,
    })),
  )
}

function toCloudPatch(user) {
  return {
    username: user.username,
    nickname: user.nickname || '',
    password: user.password,
    role: user.role,
    status: user.status,
    avatar: user.avatar || '',
    match_code: user.matchCode || '',
    created_at: user.createdAt,
    reviewed_at: user.reviewedAt || null,
    reviewed_by: user.reviewedBy || null,
  }
}

async function fetchCloudUsers() {
  if (!supabase) return []
  const { data, error } = await supabase
    .from(USERS_TABLE)
    .select('*')
    .order('created_at', { ascending: false })
  if (error) throw error
  return normalizeCloudUsers(data || [])
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => readStoredUser())
  const [users, setUsers] = useState(() =>
    IS_CLOUD_MODE ? [] : normalizeUsers(readStoredUsers()),
  )
  const [isSyncing, setIsSyncing] = useState(false)
  const [syncError, setSyncError] = useState('')

  const usersRef = useRef(users)
  useEffect(() => {
    usersRef.current = users
  }, [users])

  useEffect(() => {
    if (user) {
      localStorage.setItem(AUTH_KEY, JSON.stringify(user))
      return
    }
    localStorage.removeItem(AUTH_KEY)
  }, [user])

  useEffect(() => {
    if (IS_CLOUD_MODE) return
    localStorage.setItem(USERS_KEY, JSON.stringify(users))
  }, [users])

  const refreshUsers = useCallback(async () => {
    if (!IS_CLOUD_MODE) return usersRef.current
    setIsSyncing(true)
    try {
      const nextUsers = await fetchCloudUsers()
      setUsers(nextUsers)
      setSyncError('')
      return nextUsers
    } catch {
      setSyncError('云端同步失败，请稍后重试。')
      return usersRef.current
    } finally {
      setIsSyncing(false)
    }
  }, [])

  useEffect(() => {
    if (!IS_CLOUD_MODE) return undefined

    let isMounted = true
    refreshUsers().then((nextUsers) => {
      if (!isMounted) return
      setUsers(nextUsers)
    })
    const timer = window.setInterval(() => {
      refreshUsers()
    }, 8000)

    return () => {
      isMounted = false
      window.clearInterval(timer)
    }
  }, [refreshUsers])

  const login = useCallback(
    async (username, password, matchCode = '') => {
      const normalizedUsername = username.trim()
      const normalizedMatchCode = String(matchCode || '').trim()

      if (normalizedUsername === ADMIN_USERNAME && password === ADMIN_PASSWORD) {
        const nextUser = {
          id: 'admin',
          username: ADMIN_USERNAME,
          nickname: ADMIN_USERNAME,
          role: 'admin',
          avatar: '',
          matchCode: normalizedMatchCode,
        }
        setUser(nextUser)
        if (IS_CLOUD_MODE) await refreshUsers()
        return { ok: true, user: nextUser }
      }

      const sourceUsers = IS_CLOUD_MODE ? await refreshUsers() : usersRef.current
      const found = sourceUsers.find(
        (item) => item.username === normalizedUsername && item.password === password,
      )

      if (!found) {
        return { ok: false, message: '用户名或密码错误' }
      }

      if (found.status === 'pending') {
        return { ok: false, message: '账号待管理员审核，请稍后再试。' }
      }

      if (found.status === 'rejected') {
        return { ok: false, message: '账号审核未通过，请联系管理员。' }
      }

      const nextUser = {
        id: found.id,
        username: found.username,
        nickname: found.nickname || found.username,
        role: found.role,
        avatar: found.avatar || '',
        matchCode: normalizedMatchCode || found.matchCode || '',
      }
      setUser(nextUser)
      return { ok: true, user: nextUser }
    },
    [refreshUsers],
  )

  const register = useCallback(
    async ({ username, password, nickname = '', avatar = '', matchCode = '' }) => {
      const normalizedUsername = username.trim()
      const normalizedNickname = String(nickname || '').trim()
      const normalizedMatchCode = String(matchCode || '').trim()

      if (!normalizedUsername || !password.trim()) {
        return { ok: false, message: '请填写用户名和密码。' }
      }

      if (normalizedUsername === ADMIN_USERNAME) {
        return { ok: false, message: '该用户名已被占用。' }
      }

      const sourceUsers = IS_CLOUD_MODE ? await refreshUsers() : usersRef.current
      const exists = sourceUsers.some((item) => item.username === normalizedUsername)
      if (exists) {
        return { ok: false, message: '该用户名已被注册。' }
      }

      const newUser = {
        id: `user-${Date.now()}`,
        username: normalizedUsername,
        nickname: normalizedNickname || normalizedUsername,
        password,
        role: 'member',
        status: 'pending',
        avatar,
        matchCode: normalizedMatchCode,
        createdAt: new Date().toISOString(),
        reviewedAt: '',
        reviewedBy: '',
      }

      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase.from(USERS_TABLE).insert([toCloudPatch(newUser)])
        if (error) {
          const duplicated =
            error.code === '23505' || String(error.message || '').includes('duplicate')
          return {
            ok: false,
            message: duplicated ? '该用户名已被注册。' : '注册失败，请稍后再试。',
          }
        }
        await refreshUsers()
      } else {
        setUsers((prev) => [...prev, newUser])
      }

      return { ok: true, message: '注册成功，等待管理员审核后可登录。' }
    },
    [refreshUsers],
  )

  const reviewUser = useCallback(
    async (id, action) => {
      const targetAction = action === 'approved' ? 'approved' : 'rejected'
      const nextReviewedAt = new Date().toISOString()
      const nextReviewedBy = user?.username || ADMIN_USERNAME

      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase
          .from(USERS_TABLE)
          .update({
            status: targetAction,
            reviewed_at: nextReviewedAt,
            reviewed_by: nextReviewedBy,
          })
          .eq('id', id)
        if (error) {
          return { ok: false, message: '审核失败，请稍后重试。' }
        }
        await refreshUsers()
        return { ok: true }
      }

      setUsers((prev) =>
        prev.map((item) =>
          item.id === id
            ? { ...item, status: targetAction, reviewedAt: nextReviewedAt, reviewedBy: nextReviewedBy }
            : item,
        ),
      )
      return { ok: true }
    },
    [refreshUsers, user?.username],
  )

  const setUserAvatar = useCallback(
    async (id, avatar) => {
      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase.from(USERS_TABLE).update({ avatar }).eq('id', id)
        if (error) {
          return { ok: false, message: '头像更新失败，请稍后重试。' }
        }
        setUser((prev) => (prev?.id === id ? { ...prev, avatar: avatar || '' } : prev))
        await refreshUsers()
        return { ok: true }
      }

      setUsers((prev) =>
        prev.map((item) => (item.id === id ? { ...item, avatar } : item)),
      )
      setUser((prev) => (prev?.id === id ? { ...prev, avatar: avatar || '' } : prev))
      return { ok: true }
    },
    [refreshUsers],
  )

  const updateCurrentNickname = useCallback(
    async (nickname) => {
      if (!user) return { ok: false, message: '请先登录。' }
      if (user.role === 'admin') {
        setUser((prev) => (prev ? { ...prev, nickname } : prev))
        return { ok: true, message: '昵称已更新。' }
      }

      const next = String(nickname || '').trim()
      if (!next) return { ok: false, message: '昵称不能为空。' }

      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase
          .from(USERS_TABLE)
          .update({ nickname: next })
          .eq('id', user.id)
        if (error) {
          return { ok: false, message: '修改失败，请稍后重试。' }
        }
        setUser((prev) => (prev ? { ...prev, nickname: next } : prev))
        await refreshUsers()
        return { ok: true, message: '昵称修改成功。' }
      }

      setUsers((prev) =>
        prev.map((item) => (item.id === user.id ? { ...item, nickname: next } : item)),
      )
      setUser((prev) => (prev ? { ...prev, nickname: next } : prev))
      return { ok: true, message: '昵称修改成功。' }
    },
    [refreshUsers, user],
  )

  const updateCurrentMatchCode = useCallback(
    async (matchCode) => {
      if (!user) return { ok: false, message: '请先登录。' }
      if (user.role === 'admin') {
        setUser((prev) => (prev ? { ...prev, matchCode } : prev))
        return { ok: true, message: '配对码已更新。' }
      }

      const next = String(matchCode || '').trim()

      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase
          .from(USERS_TABLE)
          .update({ match_code: next })
          .eq('id', user.id)
        if (error) {
          return { ok: false, message: '修改失败，请稍后重试。' }
        }
        setUser((prev) => (prev ? { ...prev, matchCode: next } : prev))
        await refreshUsers()
        return { ok: true, message: '配对码设置成功。' }
      }

      setUsers((prev) =>
        prev.map((item) => (item.id === user.id ? { ...item, matchCode: next } : item)),
      )
      setUser((prev) => (prev ? { ...prev, matchCode: next } : prev))
      return { ok: true, message: '配对码设置成功。' }
    },
    [refreshUsers, user],
  )

  const updateCurrentPassword = useCallback(
    async (currentPassword, nextPassword) => {
      if (!user) return { ok: false, message: '请先登录。' }
      if (user.role === 'admin') {
        return { ok: false, message: '管理员账号不支持在此修改密码。' }
      }

      const current = String(currentPassword || '').trim()
      const next = String(nextPassword || '').trim()

      if (!current || !next) {
        return { ok: false, message: '请填写当前密码和新密码。' }
      }

      if (next.length < 6) {
        return { ok: false, message: '新密码至少 6 位。' }
      }

      const sourceUsers = IS_CLOUD_MODE ? await refreshUsers() : usersRef.current
      const target = sourceUsers.find((item) => item.id === user.id)
      if (!target) {
        return { ok: false, message: '用户不存在，请重新登录。' }
      }

      if (target.password !== current) {
        return { ok: false, message: '当前密码错误。' }
      }

      if (current === next) {
        return { ok: false, message: '新密码不能与当前密码相同。' }
      }

      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase
          .from(USERS_TABLE)
          .update({ password: next })
          .eq('id', user.id)
        if (error) {
          return { ok: false, message: '修改失败，请稍后重试。' }
        }
        await refreshUsers()
        return { ok: true, message: '密码修改成功。' }
      }

      setUsers((prev) =>
        prev.map((item) => (item.id === user.id ? { ...item, password: next } : item)),
      )
      return { ok: true, message: '密码修改成功。' }
    },
    [refreshUsers, user],
  )

  const updateCurrentAvatar = useCallback(
    async (avatar) => {
      if (!user) return { ok: false, message: '请先登录。' }
      if (user.role === 'admin') {
        return { ok: false, message: '管理员账号不支持在此修改头像。' }
      }
      return setUserAvatar(user.id, avatar || '')
    },
    [setUserAvatar, user],
  )

  const resetUserPassword = useCallback(
    async (id, nextPassword) => {
      if (user?.role !== 'admin') {
        return { ok: false, message: '仅管理员可重置密码。' }
      }

      const next = String(nextPassword || '').trim()
      if (!next) return { ok: false, message: '请输入新密码。' }
      if (next.length < 6) return { ok: false, message: '新密码至少 6 位。' }

      if (IS_CLOUD_MODE && supabase) {
        const { error } = await supabase
          .from(USERS_TABLE)
          .update({
            password: next,
            reviewed_at: new Date().toISOString(),
            reviewed_by: user.username || ADMIN_USERNAME,
          })
          .eq('id', id)
        if (error) {
          return { ok: false, message: '重置失败，请稍后重试。' }
        }
        await refreshUsers()
        return { ok: true, message: '密码已重置。' }
      }

      setUsers((prev) =>
        prev.map((item) =>
          item.id === id
            ? {
                ...item,
                password: next,
                reviewedAt: new Date().toISOString(),
                reviewedBy: user.username || ADMIN_USERNAME,
              }
            : item,
        ),
      )
      return { ok: true, message: '密码已重置。' }
    },
    [refreshUsers, user],
  )

  const logout = useCallback(() => setUser(null), [])

  const value = useMemo(
    () => ({
      user,
      users,
      login,
      register,
      reviewUser,
      setUserAvatar,
      updateCurrentNickname,
      updateCurrentMatchCode,
      updateCurrentPassword,
      updateCurrentAvatar,
      resetUserPassword,
      refreshUsers,
      logout,
      isAuthenticated: Boolean(user),
      isAdmin: user?.role === 'admin',
      isSyncing,
      syncError,
      storageMode: IS_CLOUD_MODE ? 'cloud' : 'local',
    }),
    [
      isSyncing,
      login,
      logout,
      refreshUsers,
      register,
      reviewUser,
      setUserAvatar,
      updateCurrentNickname,
      updateCurrentMatchCode,
      updateCurrentPassword,
      updateCurrentAvatar,
      resetUserPassword,
      syncError,
      user,
      users,
    ],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
