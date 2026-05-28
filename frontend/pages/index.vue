<template>
  <div class="home-page">
    <!-- 搜索栏 + 发布按钮 -->
    <section class="search-section">
      <div class="search-input">
        <el-icon class="search-icon"><Search /></el-icon>
        <input
          v-model="searchQuery"
          class="search-text"
          placeholder="搜索商品标题、描述..."
          @keyup.enter="handleSearch"
        />
      </div>
      <el-button type="primary" class="publish-btn" @click="navigateTo('/product/publish')">
        发布商品
      </el-button>
    </section>

    <!-- 分类标签 -->
    <section class="categories">
      <div
        v-for="cat in categories"
        :key="cat.value"
        class="category-tag"
        :class="{ active: activeCategory === cat.value }"
        @click="selectCategory(cat.value)"
      >
        {{ cat.label }}
      </div>
    </section>

    <!-- 推荐商品 -->
    <section class="product-section">
      <div class="section-header">
        <h2 class="section-title">推荐商品</h2>
        <NuxtLink to="/products?sort=popular" class="more-link">查看更多 →</NuxtLink>
      </div>
      <div v-if="loadingRecommended" class="loading-grid">
        <div v-for="i in 5" :key="i" class="skeleton-card"></div>
      </div>
      <div v-else-if="recommendedProducts.length === 0" class="empty">暂无推荐商品</div>
      <div v-else class="product-grid">
        <ProductCard v-for="p in recommendedProducts" :key="p.id" :product="p" />
      </div>
    </section>

    <!-- 最新发布 -->
    <section class="product-section">
      <div class="section-header">
        <h2 class="section-title">最新发布</h2>
        <NuxtLink to="/products" class="more-link">查看更多 →</NuxtLink>
      </div>
      <div v-if="loadingLatest" class="loading-grid">
        <div v-for="i in 5" :key="i" class="skeleton-card"></div>
      </div>
      <div v-else-if="latestProducts.length === 0" class="empty">暂无商品，<NuxtLink to="/product/publish">发布第一件</NuxtLink></div>
      <div v-else class="product-grid">
        <ProductCard v-for="p in latestProducts" :key="p.id" :product="p" />
      </div>
    </section>
  </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import ProductCard from '~/components/product/ProductCard.vue'

const searchQuery = ref('')
const activeCategory = ref('')

const categories = [
  { label: '全部', value: '' },
  { label: '书籍教材', value: '书籍教材' },
  { label: '电子数码', value: '电子数码' },
  { label: '生活用品', value: '生活用品' },
  { label: '服饰美妆', value: '服饰美妆' },
]

const recommendedProducts = ref([])
const latestProducts = ref([])
const loadingRecommended = ref(true)
const loadingLatest = ref(true)

const { $api } = useNuxtApp()

const loadRecommended = async () => {
  loadingRecommended.value = true
  try {
    const res = await $api.get('/api/products/recommended', { params: { limit: 5 } })
    recommendedProducts.value = res.data || []
  } catch (e) {
    recommendedProducts.value = []
  } finally {
    loadingRecommended.value = false
  }
}

const loadLatest = async () => {
  loadingLatest.value = true
  try {
    const params = { limit: 10 }
    if (activeCategory.value) params.category = activeCategory.value
    // 分类筛选时走列表接口
    if (activeCategory.value) {
      const res = await $api.get('/api/products', {
        params: { category: activeCategory.value, size: 10, page: 1 },
      })
      latestProducts.value = res.data?.records || []
    } else {
      const res = await $api.get('/api/products/latest', { params: { limit: 10 } })
      latestProducts.value = res.data || []
    }
  } catch (e) {
    latestProducts.value = []
  } finally {
    loadingLatest.value = false
  }
}

const selectCategory = (cat) => {
  activeCategory.value = cat
  loadLatest()
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    navigateTo(`/products?keyword=${encodeURIComponent(searchQuery.value.trim())}`)
  }
}

onMounted(() => {
  loadRecommended()
  loadLatest()
})
</script>

<style scoped>
.home-page {
  padding: 0;
}

.search-section {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px 32px;
}

.search-input {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  height: 44px;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 0 16px;
}

.search-icon {
  font-size: 18px;
  color: #909399;
}

.search-text {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  color: #303133;
}

.search-text::placeholder {
  color: #c0c4cc;
}

.publish-btn {
  height: 44px;
  padding: 0 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
}

.categories {
  display: flex;
  gap: 12px;
  padding: 0 32px;
  flex-wrap: wrap;
}

.category-tag {
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  color: #606266;
  cursor: pointer;
  transition: all 0.2s;
}

.category-tag:hover {
  border-color: #409eff;
  color: #409eff;
}

.category-tag.active {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
  font-weight: 500;
}

.product-section {
  padding: 16px 32px;
}

.product-section:last-child {
  padding-bottom: 32px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.more-link {
  font-size: 13px;
  color: #409eff;
  text-decoration: none;
}

.product-grid,
.loading-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.skeleton-card {
  background-color: #f5f7fa;
  border-radius: 8px;
  aspect-ratio: 1 / 1.4;
  animation: skeleton 1.5s ease-in-out infinite;
}

@keyframes skeleton {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.empty {
  text-align: center;
  font-size: 14px;
  color: #909399;
  padding: 60px 0;
  background-color: #fff;
  border-radius: 8px;
}

.empty a {
  color: #409eff;
}
</style>
