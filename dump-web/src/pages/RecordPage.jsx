import { useState, useMemo } from 'react'
import { useDumpDiary } from '../context/DumpDiaryContext.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import Calendar from '../components/Calendar.jsx'
import BristolScale from '../components/BristolScale.jsx'
import { SYMPTOM_TAGS, getBristolType } from '../lib/bristol.js'
import { todayKey } from '../lib/date.js'

const OWN_COLOR = '#F4A5B8'
const PARTNER_COLOR = '#8ECAE6'

function formatTimeShort(timeStr) {
  if (!timeStr) return ''
  return timeStr.slice(0, 5)
}

function RecordPage() {
  const { user } = useAuth()
  const { bowelLogs, addBowelLog, updateBowelLog, deleteBowelLog } = useDumpDiary()

  const [selectedDate, setSelectedDate] = useState(() => todayKey())
  const [time, setTime] = useState('')
  const [durationMinutes, setDurationMinutes] = useState(0)
  const [bristolType, setBristolType] = useState(null)
  const [symptoms, setSymptoms] = useState([])
  const [notes, setNotes] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [saved, setSaved] = useState(false)

  // Only current user's entries for the selected date
  const myTodayEntries = useMemo(() => {
    return bowelLogs
      .filter((l) => String(l.date).slice(0, 10) === String(selectedDate).slice(0, 10) && l.userId === user?.id)
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
  }, [bowelLogs, selectedDate, user?.id])

  // Partner's entries for calendar dot coloring — kept separate
  const calendarEntries = useMemo(() => {
    return bowelLogs.map((l) => ({
      ...l,
      _isOwn: l.userId === user?.id,
    }))
  }, [bowelLogs, user?.id])

  function clearForm() {
    setTime('')
    setDurationMinutes(0)
    setBristolType(null)
    setSymptoms([])
    setNotes('')
    setEditingId(null)
  }

  function handleSelectDate(date) {
    setSelectedDate(date)
    setSaved(false)
    clearForm()
  }

  function handleEditEntry(log) {
    setTime(log.time || '')
    setDurationMinutes(Math.round((log.durationSeconds || 0) / 60))
    setBristolType(log.bristolType || null)
    setSymptoms(log.symptoms || [])
    setNotes(log.notes || '')
    setEditingId(log.id)
    setSaved(false)
  }

  function handleCancelEdit() {
    clearForm()
  }

  function handleSave() {
    const durationSeconds = durationMinutes * 60
    if (editingId) {
      updateBowelLog(editingId, {
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
    clearForm()
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  function handleDelete(id) {
    deleteBowelLog(id)
    if (editingId === id) clearForm()
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  function toggleSymptom(key) {
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
        entries={calendarEntries}
        selectedDate={selectedDate}
        onSelectDate={handleSelectDate}
      />

      {/* Today's existing entries */}
      {myTodayEntries.length > 0 && (
        <div className="today-entries">
          <h3 className="section-title">当天记录</h3>
          <div className="log-list">
            {myTodayEntries.map((log) => {
              const bristol = getBristolType(log.bristolType)
              const isEditing = editingId === log.id
              return (
                <div key={log.id} className={`log-list-item${isEditing ? ' editing' : ''}`} style={{ borderLeftColor: OWN_COLOR }}>
                  <div className="log-item-main">
                    <span className="log-item-time">{formatTimeShort(log.time) || '未记时间'}</span>
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
                  <div className="log-item-actions">
                    <button type="button" className="ghost small" onClick={() => handleEditEntry(log)}>
                      <span className="material-symbols-outlined">edit</span>
                    </button>
                    <button type="button" className="log-del-btn" onClick={() => handleDelete(log.id)}>
                      <span className="material-symbols-outlined">delete</span>
                    </button>
                  </div>
                </div>
              )
            })}
          </div>
        </div>
      )}

      {/* Record form */}
      <div className="record-form">
        <h3 className="section-title">{editingId ? '编辑记录' : '新增记录'}</h3>

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
              <button type="button" className="stepper-btn" onClick={() => setDurationMinutes((v) => Math.max(0, v - 1))}>-</button>
              <span className="stepper-value">{durationMinutes}</span>
              <button type="button" className="stepper-btn" onClick={() => setDurationMinutes((v) => v + 1)}>+</button>
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
            {editingId ? '更新' : '添加记录'}
          </button>
          {editingId && (
            <button type="button" className="ghost" onClick={handleCancelEdit}>
              取消编辑
            </button>
          )}
        </div>
      </div>

      {saved && <div className="toast">保存成功</div>}
    </main>
  )
}

export default RecordPage
