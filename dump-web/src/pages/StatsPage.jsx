import { useState, useMemo } from 'react'
import { useDumpDiary } from '../context/DumpDiaryContext.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import StatsCharts from '../components/StatsCharts.jsx'
import { getBristolType } from '../lib/bristol.js'

function StatsPage() {
  const { user } = useAuth()
  const { getStatsByMonth, getStatsByYear, getPartnerLogs, getLogsByMonth, getLogsByYear } = useDumpDiary()

  const [viewMode, setViewMode] = useState('monthly')
  const [showPartner, setShowPartner] = useState(false)
  const [selectedYear, setSelectedYear] = useState(() => new Date().getFullYear())
  const [selectedMonth, setSelectedMonth] = useState(() => new Date().getMonth() + 1)

  const hasPartner = Boolean(user?.matchCode)

  const ownLogs = useMemo(() => {
    return viewMode === 'monthly'
      ? getLogsByMonth(selectedYear, selectedMonth)
      : getLogsByYear(selectedYear)
  }, [viewMode, selectedYear, selectedMonth, getLogsByMonth, getLogsByYear])

  const partnerLogs = useMemo(() => {
    if (!showPartner || !hasPartner) return []
    const all = getPartnerLogs()
    return viewMode === 'monthly'
      ? all.filter((l) => String(l.date).startsWith(`${selectedYear}-${String(selectedMonth).padStart(2, '0')}`))
      : all.filter((l) => String(l.date).startsWith(`${selectedYear}-`))
  }, [showPartner, hasPartner, viewMode, selectedYear, selectedMonth, getPartnerLogs])

  const ownStats = useMemo(() => {
    return viewMode === 'monthly'
      ? getStatsByMonth(selectedYear, selectedMonth)
      : getStatsByYear(selectedYear)
  }, [viewMode, selectedYear, selectedMonth, getStatsByMonth, getStatsByYear])

  const mostCommon = getBristolType(ownStats.mostCommonType)
  const hourLabel = ownStats.peakHour !== null
    ? `${ownStats.peakHour}:00 - ${ownStats.peakHour + 1}:00`
    : '-'

  return (
    <main className="page stats-page">
      <div className="page-header">
        <h2>统计</h2>
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
    </main>
  )
}

export default StatsPage
