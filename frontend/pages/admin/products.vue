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
          <el-button size="small" @click="navigateTo(`/product/${row.id}`)">查看</el-button>
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
  </div>
</template>

<script setup>
definePageMeta({ layout: 'admin', middleware: 'admin' })

const { $api } = useNuxtApp()

const products = ref([])
const page = ref(1)
const total = ref(0)

const fetchProducts = async () => {
  try {
    const res = await $api.get('/api/admin/products', { params: { page: page.value, size: 20 } })
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {}
}

const updateStatus = async (id, status) => {
  try {
    await $api.put(`/api/admin/products/${id}/status`, { status })
    fetchProducts()
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
</style>
