<template>
  <div class="listings-page">
    <div class="page-header">
      <h2>我的发布</h2>
      <el-button type="primary" @click="navigateTo('/product/publish')">发布商品</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="fetchProducts">
      <el-tab-pane label="在售" name="1" />
      <el-tab-pane label="已售" name="2" />
      <el-tab-pane label="已下架" name="3" />
      <el-tab-pane label="草稿" name="0" />
    </el-tabs>

    <div v-if="products.length === 0" class="empty">
      <p>暂无商品</p>
    </div>

    <div v-else class="product-list">
      <div v-for="p in products" :key="p.id" class="product-item">
        <div class="product-cover" :style="{ backgroundImage: `url(${p.coverImage})` }" />
        <div class="product-info">
          <h3 class="product-title">{{ p.title }}</h3>
          <div class="product-price">¥{{ p.price }}</div>
          <div class="product-meta">
            <span>{{ p.category }}</span>
            <span>{{ p.viewCount }} 浏览</span>
            <span>{{ p.favCount }} 收藏</span>
          </div>
        </div>
        <div class="product-actions">
          <el-button size="small" @click="navigateTo(`/product/${p.id}`)">查看</el-button>
          <el-button v-if="p.status !== 2" size="small" @click="navigateTo(`/product/publish?edit=${p.id}`)">编辑</el-button>
          <el-button v-if="p.status === 1" size="small" @click="updateStatus(p.id, 3)">下架</el-button>
          <el-button v-if="p.status === 3" size="small" type="success" @click="updateStatus(p.id, 1)">上架</el-button>
          <el-button v-if="p.status === 1" size="small" type="warning" @click="updateStatus(p.id, 2)">已售</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'

const { $api } = useNuxtApp()
const token = useCookie('token')

const activeTab = ref('1')
const products = ref([])

const fetchProducts = async () => {
  try {
    const res = await $api.get('/api/products', { params: { status: activeTab.value, size: 50 } })
    products.value = res.data?.records || []
  } catch (e) {
    products.value = []
  }
}

const updateStatus = async (id, status) => {
  const label = status === 1 ? '上架' : status === 2 ? '标记已售' : '下架'
  try {
    await ElMessageBox.confirm(`确定要${label}吗？`, '提示')
    await $api.put(`/api/products/${id}/status`, { status })
    ElMessage.success(`${label}成功`)
    fetchProducts()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

onMounted(() => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  fetchProducts()
})
</script>

<style scoped>
.listings-page {
  padding: 24px 32px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
}

.empty {
  text-align: center;
  color: #909399;
  padding: 60px 0;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.product-cover {
  width: 120px;
  height: 90px;
  background-color: #f5f7fa;
  background-size: cover;
  background-position: center;
  border-radius: 6px;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  min-width: 0;
}

.product-title {
  font-size: 16px;
  font-weight: 500;
  margin: 0 0 8px;
  color: #303133;
}

.product-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
  margin-bottom: 8px;
}

.product-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #909399;
}

.product-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  justify-content: center;
}
</style>
