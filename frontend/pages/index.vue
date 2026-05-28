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
        @click="activeCategory = cat.value"
      >
        {{ cat.label }}
      </div>
    </section>

    <!-- 推荐商品 -->
    <section class="product-section">
      <div class="section-header">
        <h2 class="section-title">推荐商品</h2>
        <NuxtLink to="/products?type=recommended" class="more-link">查看更多 →</NuxtLink>
      </div>
      <div class="product-grid">
        <div v-for="item in recommendedProducts" :key="item.id" class="product-card">
          <div class="product-image"></div>
          <div class="product-info">
            <div class="product-title">{{ item.title }}</div>
            <div class="product-price">¥{{ item.price }}</div>
            <div class="product-meta">
              <span>{{ item.campus }}</span>
              <span>· {{ item.condition }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新发布 -->
    <section class="product-section">
      <h2 class="section-title">最新发布</h2>
      <div class="product-grid">
        <div v-for="item in latestProducts" :key="item.id" class="product-card">
          <div class="product-image"></div>
          <div class="product-info">
            <div class="product-title">{{ item.title }}</div>
            <div class="product-price">¥{{ item.price }}</div>
            <div class="product-meta">
              <span>{{ item.campus }}</span>
              <span>· {{ item.condition }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'

const searchQuery = ref('')
const activeCategory = ref('books')

const categories = [
  { label: '书籍教材', value: 'books' },
  { label: '电子数码', value: 'electronics' },
  { label: '生活用品', value: 'daily' },
  { label: '服饰美妆', value: 'clothing' },
  { label: '全部', value: 'all' },
]

const recommendedProducts = [
  { id: 1, title: '高等数学教材 第7版', price: 25, campus: '东校区', condition: '9成新' },
  { id: 2, title: 'iPad 2024款 64G', price: 2800, campus: '西校区', condition: '95新' },
  { id: 3, title: '宿舍台灯 LED', price: 35, campus: '南校区', condition: '8成新' },
  { id: 4, title: 'Nike运动鞋 42码', price: 280, campus: '北校区', condition: '9成新' },
  { id: 5, title: '蓝牙耳机 AirPods', price: 680, campus: '东校区', condition: '95新' },
]

const latestProducts = [
  { id: 6, title: '数据结构教材', price: 18, campus: '东校区', condition: '8成新' },
  { id: 7, title: '机械键盘 青轴', price: 220, campus: '西校区', condition: '9成新' },
  { id: 8, title: '收纳箱 大号', price: 25, campus: '南校区', condition: '9成新' },
  { id: 9, title: '冬季羽绒服 M', price: 150, campus: '北校区', condition: '9成新' },
  { id: 10, title: '考研英语词汇', price: 22, campus: '东校区', condition: '95新' },
]

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    navigateTo(`/search?q=${encodeURIComponent(searchQuery.value)}`)
  }
}
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
  margin: 0 0 16px 0;
}

.section-header .section-title {
  margin: 0;
}

.more-link {
  font-size: 13px;
  color: #409eff;
  text-decoration: none;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.product-card {
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
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
  background: linear-gradient(135deg, #e4e7ed 0%, #c8cdd6 100%);
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
