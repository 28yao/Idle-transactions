<template>
  <NuxtLink :to="`/product/${product.id}`" class="product-card">
    <div class="product-image">
      <img v-if="product.coverImage" :src="resolveUrl(product.coverImage)" class="cover" />
      <div v-else class="placeholder">
        <el-icon size="36" color="#c0c4cc"><Picture /></el-icon>
      </div>
    </div>
    <div class="product-info">
      <div class="product-title">{{ product.title }}</div>
      <div class="product-price">¥{{ formatPrice(product.price) }}</div>
      <div class="product-meta">
        <span>{{ product.campus || '' }}</span>
        <span v-if="product.condition">· {{ product.condition }}</span>
      </div>
    </div>
  </NuxtLink>
</template>

<script setup>
import { Picture } from '@element-plus/icons-vue'

defineProps({
  product: { type: Object, required: true },
})

const config = useRuntimeConfig()

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}

const formatPrice = (price) => {
  if (price == null) return '0'
  const num = Number(price)
  return Number.isInteger(num) ? String(num) : num.toFixed(2)
}
</script>

<style scoped>
.product-card {
  display: block;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  text-decoration: none;
  color: inherit;
  cursor: pointer;
  transition: all 0.2s;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.product-image {
  width: 100%;
  aspect-ratio: 1 / 1;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-info {
  padding: 12px;
}

.product-title {
  font-size: 13px;
  color: #303133;
  font-weight: 500;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  font-size: 16px;
  color: #f56c6c;
  font-weight: 600;
  margin-bottom: 4px;
}

.product-meta {
  font-size: 12px;
  color: #909399;
  display: flex;
  gap: 4px;
}
</style>
