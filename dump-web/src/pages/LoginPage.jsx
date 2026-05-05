import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

function readAsDataUrl(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(String(reader.result || ''))
    reader.onerror = () => reject(new Error('文件读取失败'))
    reader.readAsDataURL(file)
  })
}

function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { login, register, isAuthenticated, storageMode, syncError } = useAuth()

  const [mode, setMode] = useState('login')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [matchCode, setMatchCode] = useState('')
  const [regUsername, setRegUsername] = useState('')
  const [regPassword, setRegPassword] = useState('')
  const [regNickname, setRegNickname] = useState('')
  const [regMatchCode, setRegMatchCode] = useState('')
  const [regAvatar, setRegAvatar] = useState('')
  const [error, setError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const from = location.state?.from?.pathname || '/record'

  if (isAuthenticated) {
    return <Navigate to={from} replace />
  }

  const handleLogin = async (e) => {
    e.preventDefault()
    setIsSubmitting(true)
    const result = await login(username, password, matchCode)
    setIsSubmitting(false)
    if (!result.ok) {
      setError(result.message || '登录失败')
      return
    }
    setError('')
    navigate(from, { replace: true })
  }

  const handleAvatarChange = async (e) => {
    const file = e.target.files?.[0]
    if (!file) return
    try {
      const avatar = await readAsDataUrl(file)
      setRegAvatar(avatar)
    } catch {
      setError('头像读取失败，请重试。')
    }
  }

  const handleRegister = async (e) => {
    e.preventDefault()
    setIsSubmitting(true)
    const result = await register({
      username: regUsername,
      password: regPassword,
      nickname: regNickname,
      avatar: regAvatar,
      matchCode: regMatchCode,
    })
    setIsSubmitting(false)

    if (!result.ok) {
      setError(result.message || '注册失败')
      setSuccessMessage('')
      return
    }

    setError('')
    setSuccessMessage(result.message || '注册成功，请等待审核。')
    setRegUsername('')
    setRegPassword('')
    setRegNickname('')
    setRegMatchCode('')
    setRegAvatar('')
    setMode('login')
  }

  return (
    <main className="auth-page">
      <div className="auth-hero">
        <span className="auth-emoji">💩</span>
        <h1>DumpDiary</h1>
        <p>记录每一天的健康小习惯</p>
      </div>

      <section className="auth-card">
        <div className="auth-switch" role="tablist">
          <button
            type="button"
            role="tab"
            className={mode === 'login' ? 'primary' : 'ghost'}
            aria-selected={mode === 'login'}
            onClick={() => { setMode('login'); setError(''); setSuccessMessage('') }}
          >
            登录
          </button>
          <button
            type="button"
            role="tab"
            className={mode === 'register' ? 'primary' : 'ghost'}
            aria-selected={mode === 'register'}
            onClick={() => { setMode('register'); setError(''); setSuccessMessage('') }}
          >
            注册
          </button>
        </div>

        {mode === 'login' ? (
          <form className="auth-form" onSubmit={handleLogin}>
            <input
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="用户名"
              autoComplete="username"
              disabled={isSubmitting}
            />
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="密码"
              autoComplete="current-password"
              disabled={isSubmitting}
            />
            <input
              value={matchCode}
              onChange={(e) => setMatchCode(e.target.value)}
              placeholder="配对码（可选，同码可看对方数据）"
              disabled={isSubmitting}
            />
            <button className="primary" type="submit" disabled={isSubmitting}>
              {isSubmitting ? '登录中...' : '登录'}
            </button>
          </form>
        ) : (
          <form className="auth-form" onSubmit={handleRegister}>
            <input
              value={regUsername}
              onChange={(e) => setRegUsername(e.target.value)}
              placeholder="用户名"
              autoComplete="username"
              disabled={isSubmitting}
            />
            <input
              value={regNickname}
              onChange={(e) => setRegNickname(e.target.value)}
              placeholder="昵称"
              autoComplete="nickname"
              disabled={isSubmitting}
            />
            <input
              type="password"
              value={regPassword}
              onChange={(e) => setRegPassword(e.target.value)}
              placeholder="密码（至少6位）"
              autoComplete="new-password"
              disabled={isSubmitting}
            />
            <input
              value={regMatchCode}
              onChange={(e) => setRegMatchCode(e.target.value)}
              placeholder="配对码（可选，同码可看对方数据）"
              disabled={isSubmitting}
            />
            <label className="upload-dropzone">
              <span>上传头像（可选）</span>
              <input
                type="file"
                accept="image/*"
                className="sr-only"
                onChange={handleAvatarChange}
                disabled={isSubmitting}
              />
              <span>点击这里选择头像</span>
            </label>
            {regAvatar && (
              <div className="auth-avatar-preview">
                <img src={regAvatar} alt="头像预览" />
              </div>
            )}
            <button className="primary" type="submit" disabled={isSubmitting}>
              {isSubmitting ? '提交中...' : '提交注册'}
            </button>
          </form>
        )}

        {storageMode === 'local' && (
          <p className="form-error">当前为本地模式，注册审核需管理员在本地操作。配置 Supabase 后可支持云端同步。</p>
        )}
        {syncError && <p className="form-error">{syncError}</p>}
        {successMessage && <p className="auth-success">{successMessage}</p>}
        {error && <p className="form-error">{error}</p>}
      </section>
    </main>
  )
}

export default LoginPage
