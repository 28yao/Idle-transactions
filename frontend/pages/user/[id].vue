<template>
  <div class="profile-page">
    <el-card class="profile-card">
      <div class="profile-header">
        <el-avatar :size="80" :src="profile.avatar">
          {{ profile.nickname?.charAt(0) }}
        </el-avatar>
        <div class="profile-info">
          <h2 class="nickname">{{ profile.nickname }}</h2>
          <div class="meta">
            <el-tag v-if="profile.verifyStatus === 2" type="success" size="small">已认证</el-tag>
            <span class="campus">{{ profile.campus }}</span>
          </div>
        </div>
      </div>

      <div class="profile-stats">
        <div class="stat-item">
          <span class="stat-value">{{ profile.publishCount || 0 }}</span>
          <span class="stat-label">发布</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ profile.transactionCount || 0 }}</span>
          <span class="stat-label">成交</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ profile.goodRate != null ? profile.goodRate + '%' : '暂无' }}</span>
          <span class="stat-label">好评率</span>
        </div>
      </div>
    </el-card>

    <el-card class="section-card">
      <template #header>
        <span>收到的评价</span>
      </template>
      <div v-if="!profile.recentReviews || profile.recentReviews.length === 0" class="empty">暂无评价</div>
      <div v-else class="review-list">
        <div v-for="r in profile.recentReviews" :key="r.id" class="review-item">
          <div class="review-header">
            <el-avatar :size="32" :src="r.reviewerAvatar">{{ r.reviewerNickname?.charAt(0) }}</el-avatar>
            <span class="reviewer-name">{{ r.reviewerNickname }}</span>
            <el-rate :model-value="r.rating" disabled size="small" />
          </div>
          <p class="review-content">{{ r.content }}</p>
          <div v-if="r.tags" class="review-tags">
            <el-tag v-for="tag in r.tags.split(',')" :key="tag" size="small" type="info">{{ tag }}</el-tag>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
const { $api } = useNuxtApp()
const route = useRoute()

const profile = ref({})

const fetchProfile = async () => {
  try {
    const res = await $api.get(`/api/users/${route.params.id}`)
    profile.value = res.data || {}
  } catch (e) {
    profile.value = {}
  }
}

onMounted(fetchProfile)
</script>

<style scoped>
.profile-page {
  padding: 24px 32px;
  max-width: 800px;
  margin: 0 auto;
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

.empty {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-item {
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.review-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.reviewer-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.review-content {
  font-size: 14px;
  color: #606266;
  margin: 0 0 8px;
  line-height: 1.5;
}

.review-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}
</style>
