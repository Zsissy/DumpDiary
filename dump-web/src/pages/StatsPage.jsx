import { useState, useMemo } from 'react'
import { useDumpDiary, computeStats as computeStatsLocal } from '../context/DumpDiaryContext.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import StatsCharts from '../components/StatsCharts.jsx'
import { getBristolType } from '../lib/bristol.js'

const USER_COLORS = ['#C49A6C', '#7BA7BC']

function formatTime(timeStr) {
  if (!timeStr) return ''
  return timeStr.slice(0, 5)
}

function StatsPage() {
  const { user } = useAuth()
  const {
    getStatsByMonth, getStatsByYear, getPartnerLogs,
    getLogsByMonth, getLogsByYear, deleteBowelLog, refreshRoom,
    bowelLogs, isRoomSyncing,
  } = useDumpDiary()

  const [viewMode, setViewMode] = useState('monthly')
  const [selectedYear, setSelectedYear] = useState(() => new Date().getFullYear())
  const [selectedMonth, setSelectedMonth] = useState(() => new Date().getMonth() + 1)
  const [deletedId, setDeletedId] = useState(null)
  const [refreshMsg, setRefreshMsg] = useState('')

  const hasPartner = Boolean(user?.matchCode)
  const [showPartner, setShowPartner] = useState(hasPartner)

  const ownLogs = useMemo(() => {
    const all = viewMode === 'monthly'
      ? getLogsByMonth(selectedYear, selectedMonth)
      : getLogsByYear(selectedYear)
    return all.filter((l) => l.userId === user?.id)
  }, [viewMode, selectedYear, selectedMonth, getLogsByMonth, getLogsByYear, bowelLogs, user?.id])

  const partnerLogs = useMemo(() => {
    if (!showPartner || !hasPartner) return []
    const all = getPartnerLogs()
    return viewMode === 'monthly'
      ? all.filter((l) => String(l.date).startsWith(`${selectedYear}-${String(selectedMonth).padStart(2, '0')}`))
      : all.filter((l) => String(l.date).startsWith(`${selectedYear}-`))
  }, [showPartner, hasPartner, viewMode, selectedYear, selectedMonth, getPartnerLogs, bowelLogs])

  const ownStats = useMemo(() => {
    return computeStatsLocal(ownLogs)
  }, [ownLogs])

  const mostCommon = getBristolType(ownStats.mostCommonType)
  const hourLabel = ownStats.peakHour !== null
    ? `${ownStats.peakHour}:00 - ${ownStats.peakHour + 1}:00`
    : '-'

  const allDisplayLogs = useMemo(() => {
    const combined = [...ownLogs]
    if (showPartner && hasPartner) {
      const partnerIds = new Set(combined.map((l) => l.id))
      for (const l of partnerLogs) {
        if (!partnerIds.has(l.id)) combined.push(l)
      }
    }
    combined.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    return combined
  }, [ownLogs, partnerLogs, showPartner, hasPartner])

  const userIds = useMemo(() => {
    const ids = []
    for (const l of allDisplayLogs) {
      if (!ids.includes(l.userId)) ids.push(l.userId)
    }
    return ids
  }, [allDisplayLogs])

  function getUserLabel(log) {
    if (!hasPartner) return ''
    if (log.userId === user?.id) return '我'
    return '伴侣'
  }

  function getUserColor(log) {
    if (!hasPartner) return USER_COLORS[0]
    const idx = userIds.indexOf(log.userId)
    return USER_COLORS[idx >= 0 ? idx % USER_COLORS.length : 0]
  }

  const handleDelete = async (id) => {
    deleteBowelLog(id)
    setDeletedId(id)
    setTimeout(() => setDeletedId(null), 2000)
  }

  const handleRefresh = async () => {
    await refreshRoom()
    setRefreshMsg('已刷新')
    setTimeout(() => setRefreshMsg(''), 2000)
  }

  return (
    <main className="page stats-page">
      <div className="page-header">
        <h2>统计</h2>
        <button type="button" className="ghost small" onClick={handleRefresh} disabled={isRoomSyncing}>
          <span className="material-symbols-outlined">refresh</span>
          {isRoomSyncing ? '刷新中...' : '刷新数据'}
        </button>
      </div>

      <div className="stats-controls">
        <div className="mode-toggle">
          <button
            type="button"
            className={viewMode === 'monthly' ? 'primary' : 'ghost'}
            onClick={() => setViewMode('monthly')}
          >
            月度
          </button>
          <button
            type="button"
            className={viewMode === 'yearly' ? 'primary' : 'ghost'}
            onClick={() => setViewMode('yearly')}
          >
            年度
          </button>
        </div>

        <div className="period-picker">
          <button type="button" className="stepper-btn" onClick={() => {
            if (viewMode === 'monthly') {
              if (selectedMonth === 1) { setSelectedYear((y) => y - 1); setSelectedMonth(12) }
              else setSelectedMonth((m) => m - 1)
            } else {
              setSelectedYear((y) => y - 1)
            }
          }}>
            <span className="material-symbols-outlined">chevron_left</span>
          </button>
          <span className="period-label">
            {viewMode === 'monthly' ? `${selectedYear}年 ${selectedMonth}月` : `${selectedYear}年`}
          </span>
          <button type="button" className="stepper-btn" onClick={() => {
            if (viewMode === 'monthly') {
              if (selectedMonth === 12) { setSelectedYear((y) => y + 1); setSelectedMonth(1) }
              else setSelectedMonth((m) => m + 1)
            } else {
              setSelectedYear((y) => y + 1)
            }
          }}>
            <span className="material-symbols-outlined">chevron_right</span>
          </button>
        </div>

        {hasPartner && (
          <div className="partner-toggle">
            <label className="toggle-label">
              <input
                type="checkbox"
                checked={showPartner}
                onChange={(e) => setShowPartner(e.target.checked)}
              />
              <span>查看伴侣数据</span>
            </label>
          </div>
        )}
      </div>

      <div className="stats-metrics">
        <div className="metric-card">
          <span className="metric-value">{ownStats.totalCount}</span>
          <span className="metric-label">总次数</span>
        </div>
        <div className="metric-card">
          <span className="metric-value">{ownStats.activeDays}</span>
          <span className="metric-label">活跃天数</span>
        </div>
        <div className="metric-card">
          <span className="metric-value" style={{ color: mostCommon?.itemColor || '#999' }}>
            {mostCommon?.label || '-'}
          </span>
          <span className="metric-label">最常见形态</span>
        </div>
        <div className="metric-card">
          <span className="metric-value">
            {ownStats.avgDuration > 0 ? `${Math.floor(ownStats.avgDuration / 60)}分` : '-'}
          </span>
          <span className="metric-label">平均时长</span>
        </div>
        <div className="metric-card">
          <span className="metric-value">{hourLabel}</span>
          <span className="metric-label">高峰时段</span>
        </div>
      </div>

      <StatsCharts
        ownLogs={ownLogs}
        partnerLogs={partnerLogs}
        viewMode={viewMode}
      />

      <div className="log-list-section">
        <h3 className="section-title">记录明细</h3>
        {allDisplayLogs.length === 0 && (
          <p className="empty-hint">暂无记录</p>
        )}
        <div className="log-list">
          {allDisplayLogs.map((log) => {
            const bristol = getBristolType(log.bristolType)
            const isOwn = log.userId === user?.id
            const color = getUserColor(log)
            return (
              <div key={log.id} className={`log-list-item${deletedId === log.id ? ' deleting' : ''}`} style={{ borderLeftColor: color }}>
                <div className="log-item-main">
                  <div className="log-item-date">
                    <span>{log.date}</span>
                    {formatTime(log.time) && <span className="log-item-time">{formatTime(log.time)}</span>}
                  </div>
                  {hasPartner && (
                    <span className="log-item-user" style={{ color }}>{getUserLabel(log)}</span>
                  )}
                  <span className="log-item-type" style={{ color: bristol?.itemColor || '#999' }}>
                    {bristol?.label || `Type ${log.bristolType}`}
                  </span>
                  {log.durationSeconds > 0 && (
                    <span className="log-item-dur">{Math.floor(log.durationSeconds / 60)}分钟</span>
                  )}
                  {log.symptoms?.length > 0 && (
                    <span className="log-item-symptoms">{log.symptoms.join(' · ')}</span>
                  )}
                  {log.notes && <span className="log-item-notes">{log.notes}</span>}
                </div>
                {isOwn && (
                  <button
                    type="button"
                    className="log-del-btn"
                    onClick={() => handleDelete(log.id)}
                    title="删除此记录"
                  >
                    <span className="material-symbols-outlined">delete</span>
                  </button>
                )}
              </div>
            )
          })}
        </div>
      </div>

      {deletedId && <div className="toast">已删除</div>}
      {refreshMsg && <div className="toast">{refreshMsg}</div>}
    </main>
  )
}

export default StatsPage
