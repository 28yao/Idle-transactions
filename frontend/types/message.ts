export interface Conversation {
  id: number
  user1Id: number
  user2Id: number
  productId: number
  lastMessage: string | null
  lastMessageAt: string | null
  createdAt: string
}

export interface Message {
  id: number
  conversationId: number
  senderId: number
  content: string
  type: number
  priceOffer: number | null
  offerStatus: number | null
  createdAt: string
}

export interface MessageSendRequest {
  conversationId: number
  content: string
  type?: number
  priceOffer?: number
}
