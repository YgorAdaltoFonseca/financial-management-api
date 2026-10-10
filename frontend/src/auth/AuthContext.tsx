import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { useNavigate } from 'react-router-dom'
import { api, setAccessToken } from '../api/client'
import type { LoginResponse, User } from '../api/types'

type AuthValue = { user: User | null; loading: boolean; signIn: (email: string, password: string) => Promise<void>; signUp: (name: string, email: string, password: string) => Promise<void>; refreshUser: () => Promise<void>; signOut: () => void }
const AuthContext = createContext<AuthValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const loading = false
  useEffect(() => {
    const expire = () => { setAccessToken(null); setUser(null) }
    window.addEventListener('session-expired', expire)
    return () => window.removeEventListener('session-expired', expire)
  }, [])

  const signIn = async (email: string, password: string) => {
    const { data } = await api.post<LoginResponse>('/api/user/login', { email, senha: password })
    setAccessToken(data.token)
    try {
      const profile = await api.get<User>('/api/user/me')
      setUser(profile.data)
    } catch (error) {
      setAccessToken(null)
      throw error
    }
  }
  const signUp = async (name: string, email: string, password: string) => {
    await api.post('/api/user/createUser', { name, email, senhaHash: password })
    await signIn(email, password)
  }
  const refreshUser = async () => { const profile = await api.get<User>('/api/user/me'); setUser(profile.data) }
  const signOut = () => { setAccessToken(null); setUser(null) }
  const value = useMemo(() => ({ user, loading, signIn, signUp, refreshUser, signOut }), [user, loading])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth precisa estar dentro de AuthProvider')
  return context
}

export function ProtectedRoute({ children }: { children: ReactNode }) {
  const { user, loading } = useAuth()
  const navigate = useNavigate()
  useEffect(() => { if (!loading && !user) navigate('/login', { replace: true }) }, [loading, user, navigate])
  if (loading || !user) return <div className="screen-loader" role="status">Carregando sua sessão…</div>
  return children
}
