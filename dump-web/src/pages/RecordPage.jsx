import { useState, useMemo } from 'react'
import { useDumpDiary } from '../context/DumpDiaryContext.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import Calendar from '../components/Calendar.jsx'
import BristolScale from '../components/BristolScale.jsx'
import { SYMPTOM_TAGS } from '../lib/bristol.js'
import { todayKey } from '../lib/date.js'

function RecordPage() {
  const { user } = useAuth()
  const { bowelLogs, addBowelLog, updateBowelLog, deleteBowelLog, getLogsByDate } = useDumpDiary()

  const [selectedDate, setSelectedDate] = useState(() => todayKey())
  const [time, setTime] = useState('')
  const [durationMinutes, setDurationMinutes] = useState(0)
  const [bristolType, setBristolType] = useState(null)
  const [symptoms, setSymptoms] = useState([])
  const [notes, setNotes] = useState('')
  const [saved, setSaved] = useState(false)

  const todayEntries = useMemo(() => getLogsByDate(selectedDate), [getLogsByDate, selectedDate])

  const editingLog = todayEntries.length > 0 ? todayEntries[0] : null

  const handleSelectDate = (date) => {
    setSelectedDate(date)
    setSaved(false)
    const logsOnDate = bowelLogs.filter((l) => String(l.date).slice(0, 10) === date)
    if (logsOnDate.length > 0) {
      const log = logsOnDate[0]
      setTime(log.time || '')
      setDurationMinutes(Math.round((log.durationSeconds || 0) / 60))
      setBristolType(log.bristolType || null)
      setSymptoms(log.symptoms || [])
      setNotes(log.notes || '')
    } else {
      setTime('')
      setDurationMinutes(0)
      setBristolType(null)
      setSymptoms([])
      setNotes('')
    }
  }

  const handleSave = () => {
    const durationSeconds = durationMinutes * 60
    if (editingLog) {
      updateBowelLog(editingLog.id, {
        date: selectedDate,
        time,
        durationSeconds,
        bristolType: bristolType || 4,
        symptoms,
        notes,
      })
    } else {
      addBowelLog({
        date: selectedDate,
        time,
        durationSeconds,
        bristolType: bristolType || 4,
        symptoms,
        notes,
      })
    }
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  const handleDelete = () => {
    if (!editingLog) return
    deleteBowelLog(editingLog.id)
    setTime('')
    setDurationMinutes(0)
    setBristolType(null)
    setSymptoms([])
    setNotes('')
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  const toggleSymptom = (key) => {
    setSymptoms((prev) =>
      prev.includes(key) ? prev.filter((k) => k !== key) : [...prev, key],
    )
  }

  return (
    <main className="page record-page">
      <div className="page-header">
        <h2>记录</h2>
        <span className="page-subtitle">你好，{user?.nickname || user?.username}</span>
      </div>

      <Calendar
        entries={bowelLogs}
        selectedDate={selectedDate}
        onSelectDate={handleSelectDate}
      />

      <div className="record-form">
        <div className="form-row">
          <div className="form-field">
            <label className="field-label">时间</label>
            <input
              type="time"
              value={time}
              onChange={(e) => setTime(e.target.value)}
              className="form-input"
            />
          </div>
          <div className="form-field">
            <label className="field-label">时长（分钟）</label>
            <div className="duration-stepper">
              <button type="button" className="stepper-btn" onClick={() => setDurationMinutes((v) => Math.max(0, v - 1))}>
                -
              </button>
              <span className="stepper-value">{durationMinutes}</span>
              <button type="button" className="stepper-btn" onClick={() => setDurationMinutes((v) => v + 1)}>
                +
              </button>
            </div>
          </div>
        </div>

        <div className="form-field">
          <label className="field-label">布里斯托分型</label>
          <BristolScale value={bristolType} onChange={setBristolType} />
        </div>

        <div className="form-field">
          <label className="field-label">症状标签</label>
          <div className="symptom-tags">
            {SYMPTOM_TAGS.map((tag) => {
              const isActive = symptoms.includes(tag.key)
              return (
                <button
                  key={tag.key}
                  type="button"
                  className={`sym-tag${isActive ? ' active' : ''}`}
                  onClick={() => toggleSymptom(tag.key)}
                >
                  {tag.label}
                </button>
              )
            })}
          </div>
        </div>

        <div className="form-field">
          <label className="field-label">备注</label>
          <textarea
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            placeholder="有什么想记录的吗..."
            className="form-textarea"
            rows={3}
          />
        </div>

        <div className="form-actions">
          <button type="button" className="primary" onClick={handleSave}>
            保存
          </button>
          {editingLog && (
            <button type="button" className="danger" onClick={handleDelete}>
              删除
            </button>
          )}
        </div>

        {saved && (
          <div className="toast">保存成功</div>
        )}
      </div>
    </main>
  )
}

export default RecordPage
