export function formatDate(date) {
  const offsetDate = new Date(date.getTime() - date.getTimezoneOffset() * 60_000)
  return offsetDate.toISOString().slice(0, 10)
}

function pad(num) {
  return String(num).padStart(2, '0')
}

export function getMonthDays(year, month) {
  const firstDay = new Date(year, month - 1, 1)
  const startDayOfWeek = firstDay.getDay()
  const daysInMonth = new Date(year, month, 0).getDate()
  const daysInPrevMonth = new Date(year, month - 1, 0).getDate()

  const days = []

  for (let i = startDayOfWeek - 1; i >= 0; i--) {
    days.push({
      date: `${year}-${pad(month - 1 || 12)}-${pad(daysInPrevMonth - i)}`,
      isCurrentMonth: false,
    })
  }

  for (let d = 1; d <= daysInMonth; d++) {
    days.push({
      date: `${year}-${pad(month)}-${pad(d)}`,
      isCurrentMonth: true,
    })
  }

  const remaining = 42 - days.length
  for (let d = 1; d <= remaining; d++) {
    const nextMonth = month + 1 > 12 ? 1 : month + 1
    const nextYear = month + 1 > 12 ? year + 1 : year
    days.push({
      date: `${nextYear}-${pad(nextMonth)}-${pad(d)}`,
      isCurrentMonth: false,
    })
  }

  return days
}

export function getMonthRange(year, month) {
  const start = `${year}-${pad(month)}-01`
  const end = `${year}-${pad(month)}-${pad(new Date(year, month, 0).getDate())}`
  return { start, end }
}

export function isSameDay(d1, d2) {
  if (!d1 || !d2) return false
  return String(d1).slice(0, 10) === String(d2).slice(0, 10)
}

export function todayKey() {
  return formatDate(new Date())
}
