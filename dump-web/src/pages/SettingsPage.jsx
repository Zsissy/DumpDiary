import { useState } from 'react'
import { useAuth } from '../context/AuthContext.jsx'

function readAsDataUrl(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(String(reader.result || ''))
    reader.onerror = () => reject(new Error('文件读取失败'))
    reader.readAsDataURL(file)
  })
}

function SettingsPage() {
  const {
    user, logout,
    updateCurrentNickname, updateCurrentMatchCode,
    updateCurrentPassword, updateCurrentAvatar,
  } = useAuth()

  const [nickname, setNickname] = useState(user?.nickname || '')
  const [matchCodeInput, setMatchCodeInput] = useState(user?.matchCode || '')
  const [currentPassword, setCurrentPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [showCurPwd, setShowCurPwd] = useState(false)
  const [showNewPwd, setShowNewPwd] = useState(false)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const handleAvatarChange = async (e) => {
    const file = e.target.files?.[0]
    if (!file) return
    try {
      const avatar = await readAsDataUrl(file)
      const result = await updateCurrentAvatar(avatar)
      if (result.ok) setMessage(result.message || '头像更新成功')
      else setError(result.message || '更新失败')
    } catch {
      setError('头像读取失败')
    }
  }

  const handleUpdateNickname = async () => {
    setError('')
    setMessage('')
    const result = await updateCurrentNickname(nickname)
    if (result.ok) setMessage(result.message || '昵称已更新')
    else setError(result.message || '更新失败')
  }

  const handleUpdateMatchCode = async () => {
    setError('')
    setMessage('')
    const result = await updateCurrentMatchCode(matchCodeInput)
    if (result.ok) setMessage(result.message || '配对码已更新')
    else setError(result.message || '更新失败')
  }

  const handleUpdatePassword = async () => {
    setError('')
    setMessage('')
    const result = await updateCurrentPassword(currentPassword, newPassword)
    if (result.ok) {
      setMessage(result.message || '密码修改成功')
      setCurrentPassword('')
      setNewPassword('')
    } else {
      setError(result.message || '修改失败')
    }
  }

  return (
    <main className="page settings-page">
      <div className="page-header">
        <h2>设置</h2>
      </div>

      <div className="settings-sections">
        <section className="settings-card">
          <h3>头像</h3>
          <div className="avatar-section">
            {user?.avatar ? (
              <img src={user.avatar} alt="头像" className="avatar-preview" />
            ) : (
              <div className="avatar-placeholder">
                <span className="material-symbols-outlined">person</span>
              </div>
            )}
            <label className="upload-dropzone">
              <input type="file" accept="image/*" className="sr-only" onChange={handleAvatarChange} />
              <span>更换头像</span>
            </label>
          </div>
        </section>

        <section className="settings-card">
          <h3>昵称</h3>
          <div className="settings-row">
            <input
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              placeholder="输入昵称"
            />
            <button type="button" className="primary" onClick={handleUpdateNickname}>
              保存
            </button>
          </div>
        </section>

        <section className="settings-card">
          <h3>修改密码</h3>
          <div className="settings-col">
            <div className="pwd-wrap">
              <input
                type={showCurPwd ? 'text' : 'password'}
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                placeholder="当前密码"
              />
              <button type="button" className="pwd-toggle" onClick={() => setShowCurPwd((v) => !v)} tabIndex={-1}>
                <span className="material-symbols-outlined">{showCurPwd ? 'visibility_off' : 'visibility'}</span>
              </button>
            </div>
            <div className="pwd-wrap">
              <input
                type={showNewPwd ? 'text' : 'password'}
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="新密码（至少6位）"
              />
              <button type="button" className="pwd-toggle" onClick={() => setShowNewPwd((v) => !v)} tabIndex={-1}>
                <span className="material-symbols-outlined">{showNewPwd ? 'visibility_off' : 'visibility'}</span>
              </button>
            </div>
            <button type="button" className="primary" onClick={handleUpdatePassword}>
              修改密码
            </button>
          </div>
        </section>

        <section className="settings-card">
          <h3>配对码</h3>
          <p className="settings-hint">
            设置相同的配对码后，两人可以互相查看排便统计数据（不可编辑对方数据）。
          </p>
          <div className="settings-row">
            <input
              value={matchCodeInput}
              onChange={(e) => setMatchCodeInput(e.target.value)}
              placeholder="输入配对码"
            />
            <button type="button" className="primary" onClick={handleUpdateMatchCode}>
              保存
            </button>
          </div>
        </section>

        <button type="button" className="danger logout-btn" onClick={logout}>
          退出登录
        </button>
      </div>

      {message && <div className="toast">{message}</div>}
      {error && <div className="toast toast-error">{error}</div>}
    </main>
  )
}

export default SettingsPage
