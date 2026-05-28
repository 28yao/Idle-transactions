/**
 * 格式化价格
 */
export function formatPrice(price: number): string {
  return `¥${price.toFixed(2)}`
}

/**
 * 格式化日期
 */
export function formatDate(date: string | Date): string {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

/**
 * 格式化相对时间
 */
export function formatRelativeTime(date: string | Date): string {
  const now = new Date()
  const d = new Date(date)
  const diff = now.getTime() - d.getTime()

  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return formatDate(date)
}

/**
 * 格式化商品状态
 */
export function formatProductStatus(status: number): string {
  const statusMap: Record<number, string> = {
    0: '草稿',
    1: '在售',
    2: '已售',
    3: '已下架',
    4: '被举报',
    5: '违规下架',
  }
  return statusMap[status] || '未知'
}

/**
 * 格式化商品成色
 */
export function formatCondition(condition: string): string {
  const conditionMap: Record<string, string> = {
    new: '全新',
    like_new: '几乎全新',
    used: '轻微使用',
    minor_damage: '有明显使用痕迹',
  }
  return conditionMap[condition] || condition
}

/**
 * 格式化商品分类
 */
export function formatCategory(category: string): string {
  const categoryMap: Record<string, string> = {
    books: '书籍教材',
    electronics: '电子数码',
    daily: '生活用品',
    clothing: '服饰鞋帽',
  }
  return categoryMap[category] || category
}
