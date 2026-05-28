<template>
  <div class="review-list">
    <div v-if="loading" class="loading">
      <el-skeleton :rows="2" animated />
    </div>

    <div v-else-if="reviews.length === 0" class="empty">
      <p>暂无评价</p>
    </div>

    <div v-else class="reviews">
      <div v-for="review in reviews" :key="review.id" class="review-item">
        <div class="review-header">
          <el-avatar :size="32" :src="review.reviewerAvatar">
            {{ review.reviewerNickname?.charAt(0) }}
          </el-avatar>
          <div class="review-meta">
            <span class="reviewer-name">{{ review.reviewerNickname }}</span>
            <el-rate :model-value="review.rating" disabled size="small" />
          </div>
          <span class="review-time">{{ formatTime(review.createdAt) }}</span>
        </div>

        <div v-if="review.content" class="review-content">
          {{ review.content }}
        </div>

        <div v-if="review.tags" class="review-tags">
          <el-tag v-for="tag in parseTags(review.tags)" :key="tag" size="small" type="info">
            {{ tag }}
          </el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  userId: { type: Number, required: true },
  limit: { type: Number, default: 10 },
})

const { $api } = useNuxtApp()

const reviews = ref([])
const loading = ref(false)

const fetchReviews = async () => {
  loading.value = true
  try {
    const res = await $api.get(`/api/reviews/user/${props.userId}`, {
      params: { limit: props.limit },
    })
    reviews.value = res.data || []
  } catch (e) {
    console.error('Failed to fetch reviews:', e)
  } finally {
    loading.value = false
  }
}

const parseTags = (tags) => {
  try {
    return JSON.parse(tags)
  } catch {
    return []
  }
}

const formatTime = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const now = new Date()
  const diff = now - d
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60))
    if (hours === 0) {
      const minutes = Math.floor(diff / (1000 * 60))
      return minutes <= 1 ? '刚刚' : `${minutes}分钟前`
    }
    return `${hours}小时前`
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return `${d.getMonth() + 1}月${d.getDate()}日`
  }
}

onMounted(fetchReviews)

watch(() => props.userId, fetchReviews)
</script>

<style scoped>
.review-list {
  min-height: 100px;
}

.loading {
  padding: 16px;
}

.empty {
  text-align: center;
  padding: 24px;
  color: #909399;
}

.reviews {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-item {
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.review-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.reviewer-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.review-time {
  font-size: 12px;
  color: #909399;
}

.review-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}

.review-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
</style>
