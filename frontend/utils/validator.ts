/**
 * 验证邮箱格式
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

/**
 * 验证密码强度
 */
export function isValidPassword(password: string): boolean {
  // 8-20位，包含字母和数字
  const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d).{8,20}$/
  return passwordRegex.test(password)
}

/**
 * 验证昵称
 */
export function isValidNickname(nickname: string): boolean {
  return nickname.length >= 2 && nickname.length <= 20
}
