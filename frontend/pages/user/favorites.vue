<template>
  <div class="favorites-page">
    <h2 class="page-title">我的收藏</h2>

    <div v-if="favorites.length === 0" class="empty">
      <p>暂无收藏</p>
      <el-button type="primary" @click="navigateTo('/')">去逛逛</el-button>
    </div>

    <div v-else class="product-grid">
      <div v-for="p in favorites" :key="p.id" class="product-card" @click="navigateTo(`/product/${p.id}`)">
        <div class="card-cover" :style="{ backgroundImage: `url(${p.coverImage})` }">
          <el-tag v-if="p.status === 2" type="danger" size="small" class="sold-tag">已售</el-tag>
        </div>
        <div class="card-body">
          <h3 class="card-title">{{ p.title }}</h3>
          <div class="card-price">¥{{ p.price }}</div>
          <div class="card-meta">
            <span>{{ p.campus }}</span>
            <span>{{ p.condition }}</span>
          </div>
        </div>
        <el-button
          class="unfav-btn"
          type="danger"
          text
          size="small"
          @click.stop="removeFavorite(p.id)"
        >
          取消收藏
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'

const { $api } = useNuxtApp()
const token = useCookie('token')

const favorites = ref([])

const fetchFavorites = async () => {
  try {
    const res = await $api.get('/api/favorites')
    favorites.value = res.data || []
  } catch (e) {
    favorites.value = []
  }
}

const removeFavorite = async (productId) => {
  try {
    await ElMessageBox.confirm('确定取消收藏？', '提示')
    await $api.delete(`/api/favorites/${productId}`)
    favorites.value = favorites.value.filter(p => p.id !== productId)
    ElMessage.success('已取消收藏')
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

onMounted(() => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  fetchFavorites()
})
</script>

<style scoped>
.favorites-page {
  padding: 24px 32px;
}

.page-title {
  font-size: 20px;
  margin: 0 0 20px;
}

.empty {
  text-align: center;
  color: #909399;
  padding: 60px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #ebeef5;
  cursor: pointer;
  transition: box-shadow 0.2s;
  position: relative;
}

.product-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-cover {
  height: 160px;
  background-color: #f5f7fa;
  background-size: cover;
  background-position: center;
  position: relative;
}

.sold-tag {
  position: absolute;
  top: 8px;
  right: 8px;
}

.card-body {
  padding: 12px;
}

.card-title {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 8px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
  margin-bottom: 8px;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.unfav-btn {
  position: absolute;
  bottom: 8px;
  right: 8px;
}
</style>
