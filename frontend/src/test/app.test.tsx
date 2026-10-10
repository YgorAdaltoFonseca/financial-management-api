import { afterEach, describe, expect, it, vi } from 'vitest'
import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import { api } from '../api/client'
import { AuthProvider, ProtectedRoute } from '../auth/AuthContext'
import { LoginPage } from '../pages/AuthPages'
import { DashboardPage } from '../pages/FinancePages'

vi.mock('../api/client', () => ({
  api: { get: vi.fn(), post: vi.fn() },
  setAccessToken: vi.fn(),
  errorMessage: (_error: unknown, fallback: string) => fallback,
}))

afterEach(() => { cleanup(); vi.clearAllMocks() })

describe('authentication flow', () => {
  it('logs in with the API token and opens the protected page', async () => {
    vi.mocked(api.post).mockResolvedValueOnce({ data: { token: 'signed-token' } } as never)
    vi.mocked(api.get).mockResolvedValueOnce({ data: { id: 1, name: 'Ana', email: 'ana@example.test' } } as never)
    render(<MemoryRouter initialEntries={['/login']}><AuthProvider><Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<ProtectedRoute><span>Painel protegido</span></ProtectedRoute>} />
    </Routes></AuthProvider></MemoryRouter>)

    fireEvent.change(screen.getByLabelText('E-mail'), { target: { value: 'ana@example.test' } })
    fireEvent.change(screen.getByLabelText('Senha'), { target: { value: 'correct-horse-battery' } })
    fireEvent.click(screen.getByRole('button', { name: /entrar/i }))

    expect(await screen.findByText('Painel protegido')).toBeInTheDocument()
    expect(api.post).toHaveBeenCalledWith('/api/user/login', { email: 'ana@example.test', senha: 'correct-horse-battery' })
    expect(api.get).toHaveBeenCalledWith('/api/user/me')
  })

  it('redirects unauthenticated visitors to login', async () => {
    render(<MemoryRouter initialEntries={['/private']}><AuthProvider><Routes>
      <Route path="/private" element={<ProtectedRoute><span>Conteúdo privado</span></ProtectedRoute>} />
      <Route path="/login" element={<span>Entrar na conta</span>} />
    </Routes></AuthProvider></MemoryRouter>)
    expect(await screen.findByText('Entrar na conta')).toBeInTheDocument()
    expect(screen.queryByText('Conteúdo privado')).not.toBeInTheDocument()
  })
})

describe('dashboard data states', () => {
  it('renders actual zero totals and an empty-state message without sample values', async () => {
    vi.mocked(api.get).mockImplementation(async (url) => {
      if (url === '/api/dashboard/total') return { data: { totalEntries: 0, totalExit: 0, totalSubscriptions: 0 } } as never
      if (url === '/api/dashboard/monthly') return { data: [] } as never
      return { data: [] } as never
    })
    render(<MemoryRouter><DashboardPage /></MemoryRouter>)
    expect(await screen.findByText('Saldo acumulado')).toBeInTheDocument()
    expect(screen.getByText('Saldo acumulado').closest('.stat-card')?.querySelector('.stat-value')).toHaveTextContent('0,00')
    expect(screen.getByText('Ainda sem movimentações')).toBeInTheDocument()
    expect(screen.getByText('Sua lista está vazia')).toBeInTheDocument()
    await waitFor(() => expect(api.get).toHaveBeenCalledTimes(4))
  })
})
