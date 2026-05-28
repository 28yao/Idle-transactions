export interface User {
  id: number
  email: string
  nickname: string
  avatar: string | null
  campus: string
  role: number
  verifyStatus: number
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  nickname: string
  password: string
  campus: string
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface PageResponse<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}
