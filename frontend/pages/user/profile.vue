<template>
  <div class="profile-page">
    <el-card class="profile-card">
      <div class="profile-header">
        <el-avatar :size="80" :src="user.avatar">
          {{ user.nickname?.charAt(0) }}
        </el-avatar>
        <div class="profile-info">
          <h2 class="nickname">{{ user.nickname }}</h2>
          <div class="meta">
            <el-tag v-if="user.verifyStatus === 2" type="success" size="small">已认证</el-tag>
            <el-tag v-else-if="user.verifyStatus === 1" type="warning" size="small">审核中</el-tag>
            <span class="campus">{{ user.campus }}</span>
          </div>
        </div>
        <el-button type="primary" plain @click="navigateTo('/user/verify')">
          {{ user.verifyStatus === 2 ? '已认证' : '实名认证' }}
        </el-button>
      </div>

      <div class="profile-stats">
        <div class="stat-item">
          <span class="stat-value">{{ stats.publishCount }}</span>
          <span class="stat-label">发布</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ stats.transactionCount }}</span>
          <span class="stat-label">成交</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ stats.goodRate || '暂无' }}</span>
          <span class="stat-label">好评率</span>
        </div>
      </div>
    </el-card>

    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <span>最近发布</span>
          <el-button text @click="navigateTo('/user/listings')">查看全部</el-button>
        </div>
      </template>
      <div v-if="recentProducts.length === 0" class="empty">暂无发布</div>
      <div v-else class="product-grid">
        <div v-for="p in recentProducts" :key="p.id" class="product-item" @click="navigateTo(`/product/${p.id}`)">
          <div class="product-cover" :style="{ backgroundImage: `url(${p.coverImage})` }" />
          <div class="product-info">
            <div class="product-title">{{ p.title }}</div>
            <div class="product-price">¥{{ p.price }}</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const { $api } = useNuxtApp()
const token = useCookie('token')

const user = ref({})
const stats = ref({ publishCount: 0, transactionCount: 0, goodRate: null })
const recentProducts = ref([])

const fetchProfile = async () => {
  try {
    const res = await $api.get('/api/auth/me')
    user.value = res.data || {}
  } catch (e) {
    ElMessage.error('获取用户信息失败')
  }
}

const fetchProducts = async () => {
  try {
    const res = await $api.get('/api/products', { params: { size: 6 } })
    recentProducts.value = res.data?.records || []
  } catch (e) {}
}

onMounted(() => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  fetchProfile()
  fetchProducts()
})
</script>

<style scoped>
.profile-page {
  padding: 24px 32px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.profile-card {
  border-radius: 12px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.profile-info {
  flex: 1;
}

.nickname {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 4px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.campus {
  font-size: 14px;
  color: #909399;
}

.profile-stats {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 13px;
  color: #909399;
}

.section-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.product-item {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #ebeef5;
  transition: box-shadow 0.2s;
}

.product-item:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.product-cover {
  height: 120px;
  background-color: #f5f7fa;
  background-size: cover;
  background-position: center;
}

.product-info {
  padding: 8px 12px;
}

.product-title {
  font-size: 14px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  font-size: 16px;
  color: #f56c6c;
  font-weight: 600;
  margin-top: 4px;
}
</style>
