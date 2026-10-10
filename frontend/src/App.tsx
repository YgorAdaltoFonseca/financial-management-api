import { lazy, Suspense, useState } from 'react'
import { Link, Navigate, Outlet, Route, Routes, useLocation, useNavigate } from 'react-router-dom'
import { ArrowLeftRight, ChartNoAxesCombined, CircleUserRound, CreditCard, LayoutDashboard, LogOut, Menu, Tags, Wallet, X } from 'lucide-react'
import { useAuth, ProtectedRoute } from './auth/AuthContext'

const DashboardPage = lazy(() => import('./pages/FinancePages').then((module) => ({ default: module.DashboardPage })))
const TransactionsPage = lazy(() => import('./pages/FinancePages').then((module) => ({ default: module.TransactionsPage })))
const CategoriesPage = lazy(() => import('./pages/ManagementPages').then((module) => ({ default: module.CategoriesPage })))
const SubscriptionsPage = lazy(() => import('./pages/ManagementPages').then((module) => ({ default: module.SubscriptionsPage })))
const ProfilePage = lazy(() => import('./pages/ManagementPages').then((module) => ({ default: module.ProfilePage })))
const LoginPage = lazy(() => import('./pages/AuthPages').then((module) => ({ default: module.LoginPage })))
const RegisterPage = lazy(() => import('./pages/AuthPages').then((module) => ({ default: module.RegisterPage })))

const links = [
  { to: '/', label: 'Visão geral', icon: LayoutDashboard },
  { to: '/transactions', label: 'Transações', icon: ArrowLeftRight },
  { to: '/categories', label: 'Categorias', icon: Tags },
  { to: '/subscriptions', label: 'Assinaturas', icon: CreditCard },
]

function Workspace() {
  const { user, signOut } = useAuth()
  const [menuOpen, setMenuOpen] = useState(false)
  const location = useLocation()
  const navigate = useNavigate()
  const current = links.find((link) => link.to === location.pathname)?.label ?? 'Configurações'
  const exit = () => { signOut(); navigate('/login', { replace: true }) }
  return <div className="workspace">
    <aside className={`sidebar ${menuOpen ? 'sidebar-open' : ''}`}>
      <Link to="/" className="brand" onClick={() => setMenuOpen(false)}><span className="brand-mark"><Wallet size={19} /></span><span>saldo<span className="brand-dot">.</span></span></Link>
      <div className="workspace-label">ESPAÇO PESSOAL</div>
      <nav aria-label="Navegação principal" className="side-nav">
        {links.map(({ to, label, icon: Icon }) => <Link key={to} to={to} onClick={() => setMenuOpen(false)} className={`nav-link ${location.pathname === to ? 'nav-active' : ''}`}><Icon size={18} strokeWidth={1.8} />{label}</Link>)}
      </nav>
      <div className="sidebar-bottom">
        <div className="help-card"><span className="help-icon"><ChartNoAxesCombined size={17} /></span><strong>Um passo de cada vez</strong><span>Pequenas escolhas também constroem grandes planos.</span></div>
        <button className="profile-link" onClick={() => navigate('/profile')}><span className="avatar">{user?.name?.slice(0, 1).toUpperCase()}</span><span className="profile-copy"><strong>{user?.name}</strong><small>{user?.email}</small></span><CircleUserRound size={18} /></button>
        <button className="nav-link signout-link" onClick={exit}><LogOut size={18} />Sair da conta</button>
      </div>
    </aside>
    {menuOpen && <button className="mobile-scrim" aria-label="Fechar menu" onClick={() => setMenuOpen(false)} />}
    <main className="main-area">
      <header className="topbar"><button className="icon-button mobile-menu" aria-label={menuOpen ? 'Fechar menu' : 'Abrir menu'} onClick={() => setMenuOpen(!menuOpen)}>{menuOpen ? <X size={20} /> : <Menu size={20} />}</button><div className="breadcrumb">Seu espaço <span>/</span> <strong>{current}</strong></div><div className="topbar-user"><span>Bem-vindo(a), {user?.name?.split(' ')[0]}</span><span className="avatar avatar-small">{user?.name?.slice(0, 1).toUpperCase()}</span></div></header>
      <div className="page-content"><Suspense fallback={<div className="loading-state" role="status">Carregando tela…</div>}><Outlet /></Suspense></div>
    </main>
  </div>
}

export function App() {
  return <Routes>
    <Route path="/login" element={<Suspense fallback={<div className="screen-loader" role="status">Carregando…</div>}><LoginPage /></Suspense>} />
    <Route path="/register" element={<Suspense fallback={<div className="screen-loader" role="status">Carregando…</div>}><RegisterPage /></Suspense>} />
    <Route path="/" element={<ProtectedRoute><Workspace /></ProtectedRoute>}>
      <Route index element={<DashboardPage />} />
      <Route path="transactions" element={<TransactionsPage />} />
      <Route path="categories" element={<CategoriesPage />} />
      <Route path="subscriptions" element={<SubscriptionsPage />} />
      <Route path="profile" element={<ProfilePage />} />
    </Route>
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes>
}
