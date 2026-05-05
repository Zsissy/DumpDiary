import { useState, useRef, useCallback } from 'react'
import { getMonthDays, todayKey, isSameDay } from '../lib/date.js'
import { getBristolType } from '../lib/bristol.js'

const WEEKDAY_LABELS = ['一', '二', '三', '四', '五', '六', '日']

function Calendar({ entries = [], selectedDate, onSelectDate }) {
  const today = todayKey()
  const [viewYear, setViewYear] = useState(() => {
    const d = selectedDate ? new Date(selectedDate) : new Date()
    return d.getFullYear()
  })
  const [viewMonth, setViewMonth] = useState(() => {
    const d = selectedDate ? new Date(selectedDate) : new Date()
    return d.getMonth() + 1
  })

  const touchStartX = useRef(0)
  const days = getMonthDays(viewYear, viewMonth)

  const goToPrevMonth = useCallback(() => {
    if (viewMonth === 1) {
      setViewYear((y) => y - 1)
      setViewMonth(12)
    } else {
      setViewMonth((m) => m - 1)
    }
  }, [viewMonth])

  const goToNextMonth = useCallback(() => {
    if (viewMonth === 12) {
      setViewYear((y) => y + 1)
      setViewMonth(1)
    } else {
      setViewMonth((m) => m + 1)
    }
  }, [viewMonth])

  const handleTouchStart = (e) => {
    touchStartX.current = e.touches[0].clientX
  }
  const handleTouchEnd = (e) => {
    const delta = e.changedTouches[0].clientX - touchStartX.current
    if (Math.abs(delta) > 60) {
      if (delta > 0) goToPrevMonth()
      else goToNextMonth()
    }
  }

  const entriesByDate = {}
  for (const entry of entries) {
    const key = String(entry.date).slice(0, 10)
    if (!entriesByDate[key]) entriesByDate[key] = []
    entriesByDate[key].push(entry)
  }

  return (
    <div className="calendar">
      <div className="calendar-header">
        <button type="button" className="cal-nav-btn" onClick={goToPrevMonth}>
          <span className="material-symbols-outlined">chevron_left</span>
        </button>
        <span className="cal-title">{viewYear}年 {viewMonth}月</span>
        <button type="button" className="cal-nav-btn" onClick={goToNextMonth}>
          <span className="material-symbols-outlined">chevron_right</span>
        </button>
      </div>

      <div className="cal-weekdays">
        {WEEKDAY_LABELS.map((label) => (
          <div key={label} className="cal-weekday">{label}</div>
        ))}
      </div>

      <div
        className="cal-grid"
        onTouchStart={handleTouchStart}
        onTouchEnd={handleTouchEnd}
      >
        {days.map((day) => {
          const dayEntries = entriesByDate[day.date] || []
          const isToday = isSameDay(day.date, today)
          const isSelected = isSameDay(day.date, selectedDate)

          return (
            <button
              key={day.date}
              type="button"
              className={`cal-day${day.isCurrentMonth ? '' : ' outside'}${isToday ? ' today' : ''}${isSelected ? ' selected' : ''}`}
              onClick={() => onSelectDate(day.date)}
            >
              <span className="cal-day-num">{parseInt(day.date.split('-')[2], 10)}</span>
              {dayEntries.length > 0 && (
                <div className="cal-dots">
                  {dayEntries.slice(0, 3).map((entry, i) => {
                    const bt = getBristolType(entry.bristolType)
                    return (
                      <span
                        key={i}
                        className="cal-dot"
                        style={{ background: bt?.itemColor || '#999' }}
                      />
                    )
                  })}
                  {dayEntries.length > 3 && (
                    <span className="cal-dot-more">+{dayEntries.length - 3}</span>
                  )}
                </div>
              )}
            </button>
          )
        })}
      </div>
    </div>
  )
}

export default Calendar
