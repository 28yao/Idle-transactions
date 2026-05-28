import { useAuthStore } from '~/stores/auth'

export default defineNuxtRouteMiddleware((to) => {
  const authStore = useAuthStore()

  if (!authStore.isLoggedIn) {
    return navigateTo('/login')
  }

  // Check admin role from JWT token (sync, avoids async authStore.user issue)
  const token = useCookie('token')
  if (!token.value) {
    return navigateTo('/login')
  }

  try {
    const payload = JSON.parse(atob(token.value.split('.')[1]))
    if (payload.role !== 1) {
      return navigateTo('/')
    }
  } catch {
    return navigateTo('/login')
  }
})
