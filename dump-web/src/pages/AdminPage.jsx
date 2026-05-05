import { useState } from 'react'
import { useAuth } from '../context/AuthContext.jsx'

function AdminPage() {
  const { users, reviewUser, resetUserPassword, setUserAvatar, refreshUsers, isSyncing } = useAuth()
  const [filter, setFilter] = useState('pending')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [resetId, setResetId] = useState(null)
  const [resetPassword, setResetPassword] = useState('')

  const filteredUsers = users.filter((u) => {
    if (filter === 'all') return true
    return u.status === filter
  })

  const handleReview = async (id, action) => {
    setError('')
    const result = await reviewUser(id, action)
    if (result.ok) {
      setMessage(action === 'approved' ? '已通过' : '已拒绝')
      setTimeout(() => setMessage(''), 2000)
    } else {
      setError(result.message || '审核失败')
    }
  }

  const handleResetPassword = async (id) => {
    setError('')
    const result = await resetUserPassword(id, resetPassword)
    if (result.ok) {
      setMessage('密码已重置')
      setResetId(null)
      setResetPassword('')
      setTimeout(() => setMessage(''), 2000)
    } else {
      setError(result.message || '重置失败')
    }
  }

  const handleAvatarChange = async (id, e) => {
    const file = e.target.files?.[0]
    if (!file) return
    try {
      const avatar = await new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.onload = () => resolve(String(reader.result || ''))
        reader.onerror = () => reject(new Error('读取失败'))
        reader.readAsDataURL(file)
      })
      const result = await setUserAvatar(id, avatar)
      if (result.ok) {
        setMessage('头像已更新')
        setTimeout(() => setMessage(''), 2000)
      } else {
        setError(result.message || '更新失败')
      }
    } catch {
      setError('头像读取失败')
    }
  }

  const pendingCount = users.filter((u) => u.status === 'pending').length

  return (
    <main className="page admin-page">
      <div className="page-header">
        <h2>用户管理</h2>
        <button type="button" className="ghost" onClick={refreshUsers} disabled={isSyncing}>
          <span className="material-symbols-outlined">refresh</span>
          {isSyncing ? '同步中...' : '刷新'}
        </button>
      </div>

      <div className="admin-stats">
        <span>总用户: {users.length}</span>
        <span className="pending-badge">待审核: {pendingCount}</span>
        <span>已通过: {users.filter((u) => u.status === 'approved').length}</span>
        <span>已拒绝: {users.filter((u) => u.status === 'rejected').length}</span>
      </div>

      <div className="admin-filters">
        {[
          { key: 'all', label: '全部' },
          { key: 'pending', label: '待审核' },
          { key: 'approved', label: '已通过' },
          { key: 'rejected', label: '已拒绝' },
        ].map((f) => (
          <button
            key={f.key}
            type="button"
            className={filter === f.key ? 'primary' : 'ghost'}
            onClick={() => setFilter(f.key)}
          >
            {f.label}
          </button>
        ))}
      </div>

      {filteredUsers.length === 0 && (
        <p className="empty-hint">暂无用户</p>
      )}

      <div className="admin-list">
        {filteredUsers.map((u) => (
          <div key={u.id} className={`admin-card status-${u.status}`}>
            <div className="admin-card-header">
              <div className="admin-avatar">
                {u.avatar ? (
                  <img src={u.avatar} alt={u.nickname || u.username} />
                ) : (
                  <span className="material-symbols-outlined">person</span>
                )}
              </div>
              <div className="admin-info">
                <strong>{u.nickname || u.username}</strong>
                <span className="admin-username">@{u.username}</span>
                {u.matchCode && <span className="admin-matchcode">配对码: {u.matchCode}</span>}
                <span className={`status-chip chip-${u.status}`}>
                  {u.status === 'approved' ? '已通过' : u.status === 'rejected' ? '已拒绝' : '待审核'}
                </span>
              </div>
            </div>

            <div className="admin-actions">
              <label className="upload-dropzone small">
                <input type="file" accept="image/*" className="sr-only" onChange={(e) => handleAvatarChange(u.id, e)} />
                <span>头像</span>
              </label>

              {u.status === 'pending' && (
                <>
                  <button type="button" className="primary small" onClick={() => handleReview(u.id, 'approved')}>
                    通过
                  </button>
                  <button type="button" className="danger small" onClick={() => handleReview(u.id, 'rejected')}>
                    拒绝
                  </button>
                </>
              )}

              {resetId === u.id ? (
                <div className="reset-row">
                  <input
                    type="text"
                    value={resetPassword}
                    onChange={(e) => setResetPassword(e.target.value)}
                    placeholder="新密码"
                    className="small-input"
                  />
                  <button type="button" className="primary small" onClick={() => handleResetPassword(u.id)}>
                    确认
                  </button>
                  <button type="button" className="ghost small" onClick={() => { setResetId(null); setResetPassword('') }}>
                    取消
                  </button>
                </div>
              ) : (
                <button type="button" className="ghost small" onClick={() => { setResetId(u.id); setResetPassword('') }}>
                  重置密码
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {message && <div className="toast">{message}</div>}
      {error && <div className="toast toast-error">{error}</div>}
    </main>
  )
}

export default AdminPage
