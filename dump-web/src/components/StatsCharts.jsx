import { useEffect, useRef } from 'react'
import * as echarts from 'echarts/core'
import { LineChart, BarChart, PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, TitleComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { getBristolType } from '../lib/bristol.js'

echarts.use([LineChart, BarChart, PieChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent, CanvasRenderer])

function StatsCharts({ ownLogs = [], partnerLogs = [], viewMode = 'monthly' }) {
  const freqChartRef = useRef(null)
  const typeChartRef = useRef(null)
  const hourChartRef = useRef(null)
  const freqInstanceRef = useRef(null)
  const typeInstanceRef = useRef(null)
  const hourInstanceRef = useRef(null)

  const hasPartner = partnerLogs.length > 0

  useEffect(() => {
    if (!freqChartRef.current) return
    if (!freqInstanceRef.current) {
      freqInstanceRef.current = echarts.init(freqChartRef.current)
    }
    const chart = freqInstanceRef.current

    const getData = (logs) => {
      if (viewMode === 'monthly') {
        const days = new Array(31).fill(0)
        logs.forEach((log) => {
          const d = parseInt(String(log.date).split('-')[2], 10)
          if (d >= 1 && d <= 31) days[d - 1]++
        })
        return days
      } else {
        const months = new Array(12).fill(0)
        logs.forEach((log) => {
          const m = parseInt(String(log.date).split('-')[1], 10)
          if (m >= 1 && m <= 12) months[m - 1]++
        })
        return months
      }
    }

    const xLabels = viewMode === 'monthly'
      ? Array.from({ length: 31 }, (_, i) => `${i + 1}日`)
      : Array.from({ length: 12 }, (_, i) => `${i + 1}月`)

    const series = [
      { name: '我', type: 'line', data: getData(ownLogs), smooth: true, lineStyle: { color: '#C49A6C' }, itemStyle: { color: '#C49A6C' } },
    ]
    if (hasPartner) {
      series.push({ name: '伴侣', type: 'line', data: getData(partnerLogs), smooth: true, lineStyle: { color: '#7BA7BC' }, itemStyle: { color: '#7BA7BC' } })
    }

    chart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: series.map((s) => s.name), bottom: 0, textStyle: { color: '#8B7355' } },
      grid: { left: 12, right: 12, top: 12, bottom: 36 },
      xAxis: { type: 'category', data: xLabels, axisLabel: { color: '#8B7355', fontSize: 11 } },
      yAxis: { type: 'value', axisLabel: { color: '#8B7355' }, splitLine: { lineStyle: { color: '#EDE3D4' } } },
      series,
    }, { notMerge: true })

    const handleResize = () => chart.resize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [ownLogs, partnerLogs, viewMode, hasPartner])

  useEffect(() => {
    if (!typeChartRef.current) return
    if (!typeInstanceRef.current) {
      typeInstanceRef.current = echarts.init(typeChartRef.current)
    }
    const chart = typeInstanceRef.current

    const ownCounts = [0, 0, 0, 0, 0, 0, 0]
    ownLogs.forEach((log) => {
      if (log.bristolType >= 1 && log.bristolType <= 7) ownCounts[log.bristolType - 1]++
    })

    const pieData = ownCounts
      .map((count, i) => {
        const bt = getBristolType(i + 1)
        return { value: count, name: bt?.label || `Type ${i + 1}`, itemStyle: { color: bt?.itemColor || '#999' } }
      })
      .filter((d) => d.value > 0)

    chart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}次 ({d}%)' },
      legend: { orient: 'vertical', right: 0, top: 'middle', textStyle: { color: '#8B7355', fontSize: 11 } },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['35%', '50%'],
        data: pieData,
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      }],
    }, { notMerge: true })

    const handleResize = () => chart.resize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [ownLogs])

  useEffect(() => {
    if (!hourChartRef.current) return
    if (!hourInstanceRef.current) {
      hourInstanceRef.current = echarts.init(hourChartRef.current)
    }
    const chart = hourInstanceRef.current

    const buckets = new Array(24).fill(0)
    ownLogs.forEach((log) => {
      if (log.time) {
        const h = parseInt(log.time.split(':')[0], 10)
        if (h >= 0 && h < 24) buckets[h]++
      }
    })

    const xLabels = Array.from({ length: 24 }, (_, i) => `${i}:00`)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: 12, right: 12, top: 12, bottom: 28 },
      xAxis: { type: 'category', data: xLabels, axisLabel: { color: '#8B7355', fontSize: 10, interval: 3 } },
      yAxis: { type: 'value', axisLabel: { color: '#8B7355' }, splitLine: { lineStyle: { color: '#EDE3D4' } } },
      series: [{ type: 'bar', data: buckets, itemStyle: { color: '#D4A76A', borderRadius: [4, 4, 0, 0] } }],
    }, { notMerge: true })

    const handleResize = () => chart.resize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [ownLogs])

  useEffect(() => {
    return () => {
      freqInstanceRef.current?.dispose()
      typeInstanceRef.current?.dispose()
      hourInstanceRef.current?.dispose()
    }
  }, [])

  return (
    <div className="stats-charts">
      <div className="chart-card">
        <h3 className="chart-title">排便次数</h3>
        <div ref={freqChartRef} className="chart-box" />
      </div>
      <div className="chart-card">
        <h3 className="chart-title">形态分布</h3>
        <div ref={typeChartRef} className="chart-box" />
      </div>
      <div className="chart-card">
        <h3 className="chart-title">时段分布</h3>
        <div ref={hourChartRef} className="chart-box" />
      </div>
    </div>
  )
}

export default StatsCharts
