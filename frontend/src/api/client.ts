import axios from 'axios'

let accessToken: string | null = null
export const setAccessToken = (token: string | null) => { accessToken = token }

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`
  return config
})

api.interceptors.response.use((response) => response, (error: unknown) => {
  if (axios.isAxiosError(error) && error.response?.status === 401 && accessToken) {
    accessToken = null
    window.dispatchEvent(new Event('session-expired'))
  }
  return Promise.reject(error)
})

export function errorMessage(error: unknown, fallback = 'Não foi possível concluir a operação.') {
  if (axios.isAxiosError(error)) {
    const message = error.response?.data?.message
    if (typeof message === 'string' && message.trim()) return message
    if (!error.response) return 'Não foi possível conectar à API. Verifique se o backend está em execução.'
    if (error.response.status === 403) return 'Você não tem permissão para realizar esta ação.'
  }
  return fallback
}
