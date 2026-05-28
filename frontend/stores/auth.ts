import { defineStore } from 'pinia'

interface User {
  id: number
  email: string
  nickname: string
  avatar: string | null
  campus: string
  role: number
  verifyStatus: number
}

interface AuthState {
  user: User | null
  token: string | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    token: null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 1,
  },

  actions: {
    setToken(token: string) {
      this.token = token
      const tokenCookie = useCookie('token', { maxAge: 60 * 60 * 24 * 7 })
      tokenCookie.value = token
    },

    setUser(user: User) {
      this.user = user ? JSON.parse(JSON.stringify(user)) : null
    },

    async login(email: string, password: string) {
      const { $api } = useNuxtApp()
      const res = await $api.post('/api/auth/login', { email, password })
      this.setToken(res.data.token)
      this.setUser(res.data)
      return res
    },

    async register(data: { email: string; nickname: string; password: string; campus: string }) {
      const { $api } = useNuxtApp()
      const res = await $api.post('/api/auth/register', data)
      this.setToken(res.data.token)
      this.setUser(res.data)
      return res
    },

    async fetchUser() {
      if (!this.token) return

      const { $api } = useNuxtApp()
      try {
        const res = await $api.get('/api/auth/me')
        this.setUser(res.data)
      } catch (e: any) {
        // 只有 401 才登出，其他错误保留 token
        if (e?.code === 401 || e?.status === 401) {
          this.logout()
        }
      }
    },

    logout() {
      this.user = null
      this.token = null
      const tokenCookie = useCookie('token', { maxAge: 60 * 60 * 24 * 7 })
      tokenCookie.value = null
    },

    init() {
      const tokenCookie = useCookie('token', { maxAge: 60 * 60 * 24 * 7 })
      if (tokenCookie.value) {
        this.token = tokenCookie.value
        this.fetchUser()
      }
    },
  },
})
