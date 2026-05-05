import { HashRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext.jsx'
import { DumpDiaryProvider } from './context/DumpDiaryContext.jsx'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import NavRail from './components/NavRail.jsx'
import LoginPage from './pages/LoginPage.jsx'
import RecordPage from './pages/RecordPage.jsx'
import StatsPage from './pages/StatsPage.jsx'
import SettingsPage from './pages/SettingsPage.jsx'
import AdminPage from './pages/AdminPage.jsx'
import './App.css'

function AppShell() {
  return (
    <div className="app-shell">
      <NavRail />
      <main className="app-main">
        <Routes>
          <Route path="/" element={<Navigate to="/record" replace />} />
          <Route path="/record" element={<RecordPage />} />
          <Route path="/stats" element={<StatsPage />} />
          <Route path="/settings" element={<SettingsPage />} />
          <Route
            path="/admin"
            element={
              <ProtectedRoute requireAdmin>
                <AdminPage />
              </ProtectedRoute>
            }
          />
          <Route path="*" element={<Navigate to="/record" replace />} />
        </Routes>
      </main>
    </div>
  )
}

function App() {
  return (
    <HashRouter>
      <AuthProvider>
        <DumpDiaryProvider>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route
              path="/*"
              element={
                <ProtectedRoute>
                  <AppShell />
                </ProtectedRoute>
              }
            />
          </Routes>
        </DumpDiaryProvider>
      </AuthProvider>
    </HashRouter>
  )
}

export default App
