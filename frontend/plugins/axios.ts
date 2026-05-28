import axios from 'axios'

export default defineNuxtPlugin(() => {
  const config = useRuntimeConfig()
  const token = useCookie('token')

  const api = axios.create({
    baseURL: config.public.apiBase,
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
    },
  })

  // 请求拦截器
  api.interceptors.request.use(
    (config) => {
      if (token.value) {
        config.headers.Authorization = `Bearer ${token.value}`
      }
      return config
    },
    (error) => {
      return Promise.reject(error)
    }
  )

  // 响应拦截器
  api.interceptors.response.use(
    (response) => {
      const data = response.data
      // 业务错误码非 200 时，按错误处理
      if (data && typeof data.code === 'number' && data.code !== 200) {
        if (data.code === 401) {
          token.value = null
          navigateTo('/login')
        }
        return Promise.reject(data)
      }
      return data
    },
    (error) => {
      if (error.response) {
        const { status, data } = error.response

        if (status === 401) {
          token.value = null
          navigateTo('/login')
        }

        return Promise.reject(data || error.response)
      }
      return Promise.reject(error)
    }
  )

  return {
    provide: {
      api,
    },
  }
})
