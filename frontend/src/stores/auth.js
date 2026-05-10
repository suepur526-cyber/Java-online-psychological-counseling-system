import { defineStore } from 'pinia'
import { authApi } from '../api/modules'

const STORAGE_KEY = 'counseling-user'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem(STORAGE_KEY) || 'null'),
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.user),
    role: (state) => state.user?.role || '',
  },
  actions: {
    async login(form) {
      const response = await authApi.login(form)
      this.user = response.data
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.user))
      return this.user
    },
    logout() {
      this.user = null
      localStorage.removeItem(STORAGE_KEY)
    },
  },
})
