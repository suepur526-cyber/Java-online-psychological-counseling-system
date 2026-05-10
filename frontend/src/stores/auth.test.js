import { beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useAuthStore } from './auth'

vi.mock('../api/modules', () => ({
  authApi: {
    login: vi.fn(async () => ({ data: { id: 1, account: 'admin', name: '系统管理员', role: 'ADMIN', status: 'ACTIVE' } })),
  },
}))

describe('auth store', () => {
  beforeEach(() => {
    localStorage.clear()
    setActivePinia(createPinia())
  })

  it('stores logged in user and clears on logout', async () => {
    const store = useAuthStore()
    await store.login({ account: 'admin', password: 'admin123', role: 'ADMIN' })
    expect(store.role).toBe('ADMIN')
    expect(store.isLoggedIn).toBe(true)
    store.logout()
    expect(store.isLoggedIn).toBe(false)
  })
})
