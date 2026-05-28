<template>
  <div class="seller-info">
    <div class="seller-header">
      <el-avatar :size="48" :src="resolveUrl(seller.avatar)">
        <el-icon size="24"><User /></el-icon>
      </el-avatar>
      <div class="seller-detail">
        <div class="seller-name">
          {{ seller.nickname || '匿名用户' }}
          <el-tag v-if="seller.verifyStatus === 2" type="success" size="small" effect="plain">
            已认证
          </el-tag>
        </div>
        <div class="seller-campus">{{ seller.campus || '' }}</div>
      </div>
    </div>
    <div class="seller-stats">
      <div class="stat-item">
        <span class="stat-value">{{ seller.productCount || 0 }}</span>
        <span class="stat-label">在售</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ seller.favCount || 0 }}</span>
        <span class="stat-label">被收藏</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { User } from '@element-plus/icons-vue'

defineProps({
  seller: { type: Object, required: true },
})

const config = useRuntimeConfig()

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}
</script>

<style scoped>
.seller-info {
  padding: 16px;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.seller-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.seller-detail {
  flex: 1;
}

.seller-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.seller-campus {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.seller-stats {
  display: flex;
  gap: 24px;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}
</style>
