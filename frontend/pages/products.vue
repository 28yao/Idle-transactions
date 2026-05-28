<template>
  <div class="products-page">
    <!-- 顶部搜索 + 面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <NuxtLink to="/">首页</NuxtLink>
        <span>/</span>
        <span>{{ pageTitle }}</span>
      </div>
      <div class="search-input">
        <el-icon class="search-icon"><Search /></el-icon>
        <input
          v-model="filters.keyword"
          class="search-text"
          placeholder="搜索商品标题、描述..."
          @keyup.enter="applyFilters"
        />
        <el-button type="primary" size="small" @click="applyFilters">搜索</el-button>
      </div>
    </div>

    <div class="content-layout">
      <!-- 左侧筛选 -->
      <aside class="filter-panel">
        <div class="filter-group">
          <div class="filter-label">分类</div>
          <div
            v-for="cat in categories"
            :key="cat.value"
            class="filter-item"
            :class="{ active: filters.category === cat.value }"
            @click="setCategory(cat.value)"
          >
            {{ cat.label }}
          </div>
        </div>

        <div class="filter-group">
          <div class="filter-label">成色</div>
          <div
            v-for="cond in conditions"
            :key="cond"
            class="filter-item"
            :class="{ active: filters.condition === cond }"
            @click="setCondition(cond)"
          >
            {{ cond || '不限' }}
          </div>
        </div>

        <div class="filter-group">
          <div class="filter-label">价格区间 (¥)</div>
          <div class="price-range">
            <el-input v-model="filters.minPrice" placeholder="最低" type="number" size="small" />
            <span>-</span>
            <el-input v-model="filters.maxPrice" placeholder="最高" type="number" size="small" />
          </div>
          <el-button size="small" style="width: 100%; margin-top: 8px" @click="applyFilters">应用</el-button>
        </div>
      </aside>

      <!-- 右侧商品列表 -->
      <div class="product-list">
        <div class="list-header">
          <div class="result-count">共 {{ total }} 件商品</div>
          <el-radio-group v-model="filters.sort" size="small" @change="applyFilters">
            <el-radio-button value="latest">最新</el-radio-button>
            <el-radio-button value="popular">最热</el-radio-button>
            <el-radio-button value="price_asc">价格↑</el-radio-button>
            <el-radio-button value="price_desc">价格↓</el-radio-button>
          </el-radio-group>
        </div>

        <div v-if="loading" class="loading-grid">
          <div v-for="i in 12" :key="i" class="skeleton-card"></div>
        </div>
        <div v-else-if="products.length === 0" class="empty">
          <p>暂无搜索结果</p>
          <NuxtLink to="/">返回首页</NuxtLink>
        </div>
        <div v-else class="product-grid">
          <ProductCard v-for="p in products" :key="p.id" :product="p" />
        </div>

        <div v-if="total > pageSize" class="pagination-wrapper">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            background
            layout="prev, pager, next, jumper"
            @current-change="loadList"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import ProductCard from '~/components/product/ProductCard.vue'

const route = useRoute()
const router = useRouter()
const { $api } = useNuxtApp()

const categories = [
  { label: '全部', value: '' },
  { label: '书籍教材', value: '书籍教材' },
  { label: '电子数码', value: '电子数码' },
  { label: '生活用品', value: '生活用品' },
  { label: '服饰美妆', value: '服饰美妆' },
]

const conditions = ['', '全新', '几乎全新', '轻微使用痕迹', '明显使用痕迹']

const filters = reactive({
  keyword: route.query.keyword || '',
  category: route.query.category || '',
  condition: route.query.condition || '',
  minPrice: route.query.minPrice || '',
  maxPrice: route.query.maxPrice || '',
  sort: route.query.sort || 'latest',
})

const currentPage = ref(Number(route.query.page) || 1)
const pageSize = 20
const total = ref(0)
const products = ref([])
const loading = ref(true)

const pageTitle = computed(() => {
  if (filters.keyword) return `搜索"${filters.keyword}"`
  if (filters.category) return filters.category
  return '全部商品'
})

const loadList = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize,
      sort: filters.sort,
    }
    if (filters.keyword) params.keyword = filters.keyword
    if (filters.category) params.category = filters.category
    if (filters.condition) params.condition = filters.condition
    if (filters.minPrice) params.minPrice = filters.minPrice
    if (filters.maxPrice) params.maxPrice = filters.maxPrice

    const res = await $api.get('/api/products', { params })
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    products.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const applyFilters = () => {
  currentPage.value = 1
  syncQueryAndLoad()
}

const setCategory = (v) => {
  filters.category = v
  applyFilters()
}

const setCondition = (v) => {
  filters.condition = v
  applyFilters()
}

const syncQueryAndLoad = () => {
  const q = {}
  if (filters.keyword) q.keyword = filters.keyword
  if (filters.category) q.category = filters.category
  if (filters.condition) q.condition = filters.condition
  if (filters.minPrice) q.minPrice = filters.minPrice
  if (filters.maxPrice) q.maxPrice = filters.maxPrice
  if (filters.sort && filters.sort !== 'latest') q.sort = filters.sort
  if (currentPage.value > 1) q.page = currentPage.value
  router.replace({ path: '/products', query: q })
  loadList()
}

onMounted(loadList)
watch(() => route.query, () => {
  // URL 直接变化时同步 filters
  filters.keyword = route.query.keyword || ''
  filters.category = route.query.category || ''
  filters.condition = route.query.condition || ''
  filters.minPrice = route.query.minPrice || ''
  filters.maxPrice = route.query.maxPrice || ''
  filters.sort = route.query.sort || 'latest'
  currentPage.value = Number(route.query.page) || 1
})
</script>

<style scoped>
.products-page {
  padding: 24px 32px;
}

.page-header {
  margin-bottom: 24px;
}

.breadcrumb {
  font-size: 13px;
  color: #909399;
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}

.breadcrumb a {
  color: #409eff;
  text-decoration: none;
}

.search-input {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 44px;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 0 8px 0 16px;
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
}

.content-layout {
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 24px;
}

.filter-panel {
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  height: fit-content;
}

.filter-group {
  margin-bottom: 20px;
}

.filter-label {
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
}

.filter-item {
  padding: 6px 8px;
  font-size: 13px;
  color: #606266;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.15s;
}

.filter-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
}

.filter-item.active {
  background-color: #ecf5ff;
  color: #409eff;
  font-weight: 500;
}

.price-range {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #909399;
}

.product-list {
  min-width: 0;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 12px 16px;
  background-color: #fff;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.result-count {
  font-size: 13px;
  color: #606266;
}

.product-grid,
.loading-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
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
  padding: 60px 0;
  background-color: #fff;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.empty p {
  color: #909399;
  margin-bottom: 8px;
}

.empty a {
  color: #409eff;
  font-size: 13px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
