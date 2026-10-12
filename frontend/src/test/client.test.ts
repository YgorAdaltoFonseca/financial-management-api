import { AxiosError, type AxiosAdapter } from 'axios'
import { afterEach, describe, expect, it, vi } from 'vitest'
import { api, errorMessage, setAccessToken } from '../api/client'

const originalAdapter = api.defaults.adapter
afterEach(() => { api.defaults.adapter = originalAdapter; setAccessToken(null); vi.restoreAllMocks() })

describe('HTTP authentication errors', () => {
  it('clears the in-memory token and emits a session-expired event on 401', async () => {
    setAccessToken('temporary-token')
    const expired = vi.fn()
    window.addEventListener('session-expired', expired)
    const responseError = Object.assign(new AxiosError('Unauthorized'), {
      response: { status: 401, data: {}, statusText: 'Unauthorized', headers: {}, config: {} },
    })
    api.defaults.adapter = (() => Promise.reject(responseError)) as AxiosAdapter

    await expect(api.get('/protected')).rejects.toBe(responseError)
    expect(expired).toHaveBeenCalledOnce()
    window.removeEventListener('session-expired', expired)
  })

  it('shows a permission message for 403 responses', () => {
    const responseError = Object.assign(new AxiosError('Forbidden'), {
      response: { status: 403, data: {}, statusText: 'Forbidden', headers: {}, config: {} },
    })
    expect(errorMessage(responseError)).toBe('Você não tem permissão para realizar esta ação.')
  })
})
