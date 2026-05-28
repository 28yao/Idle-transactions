export interface Product {
  id: number
  sellerId: number
  title: string
  description: string
  category: string
  price: number
  originalPrice: number | null
  condition: string
  location: string
  campus: string
  status: number
  viewCount: number
  favCount: number
  createdAt: string
  updatedAt: string
}

export interface ProductImage {
  id: number
  productId: number
  url: string
  sortOrder: number
  isCover: number
}

export interface ProductCreateRequest {
  title: string
  description: string
  category: string
  price: number
  originalPrice?: number
  condition: string
  location: string
  campus: string
  images: string[]
}
