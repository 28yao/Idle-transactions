<template>
  <div class="product-detail-page">
    <div class="detail-container">
      <!-- 左侧：图片轮播 -->
      <div class="detail-left">
        <ProductImageCarousel :images="product.images || []" />
      </div>

      <!-- 右侧：商品信息 -->
      <div class="detail-right">
        <div class="product-header">
          <h1 class="product-title">{{ product.title }}</h1>
          <div class="product-price">
            <span class="price-symbol">¥</span>
            <span class="price-value">{{ formatPrice(product.price) }}</span>
            <span v-if="product.originalPrice" class="original-price">
              ¥{{ formatPrice(product.originalPrice) }}
            </span>
          </div>
        </div>

        <div class="product-meta">
          <div class="meta-item">
            <span class="meta-label">分类</span>
            <span class="meta-value">{{ product.category || '-' }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">成色</span>
            <span class="meta-value">{{ product.condition || '-' }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">校区</span>
            <span class="meta-value">{{ product.campus || '-' }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">位置</span>
            <span class="meta-value">{{ product.location || '-' }}</span>
          </div>
        </div>

        <div class="product-stats">
          <span><el-icon><View /></el-icon> {{ product.viewCount || 0 }} 浏览</span>
          <span><el-icon><Star /></el-icon> {{ product.favCount || 0 }} 收藏</span>
          <span>{{ formatTime(product.createdAt) }}</span>
        </div>

        <div class="product-actions">
          <el-button
            v-if="isLoggedIn && !isOwner && product.status === 1"
            type="primary"
            size="large"
            @click="contactSeller"
          >
            <el-icon><ChatDotRound /></el-icon>
            联系卖家
          </el-button>
          <el-button
            v-if="isLoggedIn && !isOwner"
            :type="product.favorited ? 'danger' : 'primary'"
            :icon="product.favorited ? StarFilled : Star"
            size="large"
            @click="toggleFavorite"
          >
            {{ product.favorited ? '取消收藏' : '收藏' }}
          </el-button>
          <el-button
            v-if="isLoggedIn && !isOwner"
            size="large"
            @click="openReport"
          >
            <el-icon><Warning /></el-icon>
            举报
          </el-button>
          <el-button
            v-if="isOwner"
            type="primary"
            size="large"
            @click="editProduct"
          >
            编辑商品
          </el-button>
        </div>

        <!-- 卖家信息 -->
        <SellerInfo
          :seller="{
            nickname: product.sellerNickname,
            avatar: product.sellerAvatar,
            verifyStatus: product.sellerVerifyStatus,
            campus: product.campus,
          }"
        />
      </div>
    </div>

    <!-- 商品描述 -->
    <div class="description-section">
      <h3 class="section-title">商品描述</h3>
      <div class="description-content">
        {{ product.description || '暂无描述' }}
      </div>
    </div>

    <!-- 举报弹窗 -->
    <ReportDialog
      ref="reportDialogRef"
      :target-type="1"
      :target-id="productId"
    />
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { View, Star, StarFilled, Warning, ChatDotRound } from '@element-plus/icons-vue'
import ProductImageCarousel from '~/components/product/ImageCarousel.vue'
import SellerInfo from '~/components/user/SellerInfo.vue'
import ReportDialog from '~/components/common/ReportDialog.vue'

const route = useRoute()
const router = useRouter()
const { $api } = useNuxtApp()
const token = useCookie('token')

const productId = Number(route.params.id)
const product = ref({})
const reportDialogRef = ref(null)

const isLoggedIn = computed(() => !!token.value)
const isOwner = computed(() => {
  // 需要从用户信息判断，暂时用 sellerId 粗略判断
  return false
})

const fetchProduct = async () => {
  try {
    const res = await $api.get(`/api/products/${productId}`)
    product.value = res.data || {}
  } catch (e) {
    ElMessage.error('商品不存在或已下架')
    router.push('/')
  }
}

const toggleFavorite = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    if (product.value.favorited) {
      await $api.delete(`/api/favorites/${productId}`)
      product.value.favorited = false
      product.value.favCount = Math.max((product.value.favCount || 1) - 1, 0)
      ElMessage.success('已取消收藏')
    } else {
      await $api.post(`/api/favorites/${productId}`)
      product.value.favorited = true
      product.value.favCount = (product.value.favCount || 0) + 1
      ElMessage.success('收藏成功')
    }
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

const openReport = () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  reportDialogRef.value?.open()
}

const editProduct = () => {
  router.push(`/product/publish?edit=${productId}`)
}

const contactSeller = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    const res = await $api.post(`/api/conversations?productId=${productId}`)
    const conv = res.data
    router.push(`/chat?id=${conv.id}`)
  } catch (e) {
    ElMessage.error(e.message || '无法发起对话')
  }
}

const formatPrice = (price) => {
  if (price == null) return '0'
  const num = Number(price)
  return Number.isInteger(num) ? String(num) : num.toFixed(2)
}

const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  const now = new Date()
  const diff = now - d
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

onMounted(fetchProduct)
</script>

<style scoped>
.product-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 32px;
}

.detail-container {
  display: flex;
  gap: 32px;
}

.detail-left {
  width: 480px;
  flex-shrink: 0;
}

.detail-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.product-header {
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 16px;
}

.product-title {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px;
  line-height: 1.4;
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.price-symbol {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 500;
}

.price-value {
  font-size: 28px;
  color: #f56c6c;
  font-weight: 700;
}

.original-price {
  font-size: 14px;
  color: #c0c4cc;
  text-decoration: line-through;
}

.product-meta {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.meta-item {
  display: flex;
  gap: 8px;
}

.meta-label {
  font-size: 14px;
  color: #909399;
  min-width: 40px;
}

.meta-value {
  font-size: 14px;
  color: #303133;
}

.product-stats {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #909399;
}

.product-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.product-actions {
  display: flex;
  gap: 12px;
}

.description-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px;
}

.description-content {
  font-size: 15px;
  color: #606266;
  line-height: 1.8;
  white-space: pre-wrap;
}

@media (max-width: 768px) {
  .detail-container {
    flex-direction: column;
  }

  .detail-left {
    width: 100%;
  }
}
</style>
