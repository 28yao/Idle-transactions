export interface Transaction {
  id: number
  productId: number
  productTitle?: string
  productImage?: string
  buyerId: number
  buyerNickname?: string
  buyerAvatar?: string
  sellerId: number
  sellerNickname?: string
  sellerAvatar?: string
  price: number
  status: number
  cancelReason?: string
  cancelBy?: number
  deliveredAt?: string
  completedAt?: string
  createdAt: string
}

/** 交易状态：0进行中 1卖家已交付 2已完成 3已取消 */
export const TransactionStatus = {
  ONGOING: 0,
  DELIVERED: 1,
  COMPLETED: 2,
  CANCELLED: 3,
} as const

export const TransactionStatusLabel: Record<number, string> = {
  0: '进行中',
  1: '卖家已交付',
  2: '已完成',
  3: '已取消',
}

export const TransactionStatusType: Record<number, string> = {
  0: 'warning',
  1: 'primary',
  2: 'success',
  3: 'info',
}
