import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

const NAV_ITEMS = [
  { to: '/record', label: '记录', icon: 'edit_note' },
  { to: '/stats', label: '统计', icon: 'bar_chart' },
  { to: '/settings', label: '设置', icon: 'settings' },
]

function NavRail() {
  const { isAdmin } = useAuth()

  return (
    <nav className="nav-rail">
      <div className="nav-brand">
        <span className="nav-logo">💩</span>
      </div>

      <div className="nav-links">
        {NAV_ITEMS.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) => `nav-item${isActive ? ' active' : ''}`}
          >
            <span className="material-symbols-outlined nav-icon">{item.icon}</span>
            <span className="nav-label">{item.label}</span>
          </NavLink>
        ))}

        {isAdmin && (
          <NavLink
            to="/admin"
            className={({ isActive }) => `nav-item nav-admin${isActive ? ' active' : ''}`}
          >
            <span className="material-symbols-outlined nav-icon">admin_panel_settings</span>
            <span className="nav-label">管理</span>
          </NavLink>
        )}
      </div>
    </nav>
  )
}

export default NavRail
