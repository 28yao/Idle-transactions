<template>
  <div class="products-page">
    <h2>商品管理</h2>

    <el-table :data="products" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">{{ getStatusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" size="small" type="warning" @click="updateStatus(row.id, 5)">违规下架</el-button>
          <el-button v-if="row.status === 5" size="small" type="success" @click="updateStatus(row.id, 1)">恢复上架</el-button>
          <el-button size="small" @click="showDetail(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        :page-size="20"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="fetchProducts"
      />
    </div>

    <!-- 商品详情弹窗 -->
    <el-dialog v-model="dialogVisible" title="商品详情" width="680px">
      <div v-if="currentProduct" class="detail-content">
        <div class="detail-images" v-if="currentProduct.images?.length">
          <el-image
            v-for="(img, idx) in currentProduct.images"
            :key="idx"
            :src="resolveUrl(img.url)"
            :preview-src-list="currentProduct.images.map(i => resolveUrl(i.url))"
            :initial-index="idx"
            fit="cover"
            class="preview-img"
          />
        </div>
        <div class="detail-row">
          <span class="detail-label">商品ID</span>
          <span class="detail-value">{{ currentProduct.id }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">标题</span>
          <span class="detail-value">{{ currentProduct.title }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">价格</span>
          <span class="detail-value price">¥{{ currentProduct.price }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">原价</span>
          <span class="detail-value">¥{{ currentProduct.originalPrice || '-' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">分类</span>
          <span class="detail-value">{{ currentProduct.category }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">成色</span>
          <span class="detail-value">{{ currentProduct.condition || '-' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <span class="detail-value">
            <el-tag :type="getStatusType(currentProduct.status)" size="small">{{ getStatusLabel(currentProduct.status) }}</el-tag>
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">卖家</span>
          <span class="detail-value">{{ currentProduct.sellerName || '-' }} (ID: {{ currentProduct.sellerId }})</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">发布时间</span>
          <span class="detail-value">{{ currentProduct.createdAt }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">描述</span>
          <span class="detail-value desc-text">{{ currentProduct.description || '暂无描述' }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="navigateTo(`/product/${currentProduct.id}`); dialogVisible = false">前台查看</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
definePageMeta({ layout: 'admin', middleware: 'admin' })

const config = useRuntimeConfig()
const { $api } = useNuxtApp()

const products = ref([])
const page = ref(1)
const total = ref(0)

const dialogVisible = ref(false)
const currentProduct = ref(null)

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}

const fetchProducts = async () => {
  try {
    const res = await $api.get('/api/admin/products', { params: { page: page.value, size: 20 } })
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {}
}

const fetchProductDetail = async (id) => {
  try {
    const res = await $api.get(`/api/products/${id}`)
    currentProduct.value = res.data
  } catch (e) {}
}

const showDetail = async (row) => {
  await fetchProductDetail(row.id)
  dialogVisible.value = true
}

const updateStatus = async (id, status) => {
  try {
    await $api.put(`/api/admin/products/${id}/status`, { status })
    fetchProducts()
    if (currentProduct.value?.id === id) {
      currentProduct.value.status = status
    }
  } catch (e) {}
}

const getStatusType = (status) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning', 3: 'info', 5: 'danger' }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = { 0: '草稿', 1: '在售', 2: '已售', 3: '已下架', 5: '违规下架' }
  return map[status] || '未知'
}

onMounted(fetchProducts)
</script>

<style scoped>
.products-page h2 {
  margin: 0 0 20px;
  font-size: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.detail-images {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 8px;
}

.preview-img {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  cursor: pointer;
}

.detail-row {
  display: flex;
  gap: 12px;
}

.detail-label {
  flex-shrink: 0;
  width: 70px;
  color: #909399;
  font-size: 14px;
  text-align: right;
}

.detail-value {
  flex: 1;
  font-size: 14px;
  color: #303133;
  word-break: break-all;
}

.price {
  color: #f56c6c;
  font-weight: 600;
}

.desc-text {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  line-height: 1.6;
  white-space: pre-wrap;
}
</style>
