<template>
  <div class="transactions-page">
    <h2 class="page-title">交易记录</h2>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="进行中" name="0" />
      <el-tab-pane label="已交付" name="1" />
      <el-tab-pane label="已完成" name="2" />
      <el-tab-pane label="待确认取消" name="4" />
      <el-tab-pane label="已取消" name="3" />
    </el-tabs>

    <div v-if="loading" class="loading">
      <el-skeleton :rows="3" animated />
    </div>

    <div v-else-if="transactions.length === 0" class="empty">
      <el-empty description="暂无交易记录">
        <el-button type="primary" @click="navigateTo('/')">去逛逛</el-button>
      </el-empty>
    </div>

    <div v-else class="transaction-list">
      <div v-for="t in transactions" :key="t.id" class="transaction-card" @click="showDetail(t)">
        <div class="card-header">
          <span class="transaction-id">交易号：{{ t.id }}</span>
          <el-tag :type="getStatusType(t.status)" size="small">
            {{ getStatusLabel(t.status) }}
          </el-tag>
        </div>

        <div class="card-body">
          <div class="product-info">
            <div class="product-image" :style="t.productImage ? { backgroundImage: `url(${resolveUrl(t.productImage)})` } : {}">
              <el-icon v-if="!t.productImage" class="image-placeholder"><Picture /></el-icon>
            </div>
            <div class="product-detail">
              <h4 class="product-title">{{ t.productTitle || '商品已删除' }}</h4>
              <div class="product-price">¥{{ t.price?.toFixed(2) }}</div>
            </div>
          </div>

          <div class="trade-info">
            <div class="trade-user">
              <span class="label">买家：</span>
              <span>{{ t.buyerNickname || '未知用户' }}</span>
            </div>
            <div class="trade-user">
              <span class="label">卖家：</span>
              <span>{{ t.sellerNickname || '未知用户' }}</span>
            </div>
            <div class="trade-time">
              <span class="label">时间：</span>
              <span>{{ formatDate(t.createdAt) }}</span>
            </div>
          </div>
        </div>

        <div class="card-footer">
          <template v-if="t.status === 0 && isSeller(t)">
            <el-button type="primary" size="small" @click.stop="markDelivered(t)">
              标记已交付
            </el-button>
          </template>
          <template v-if="t.status === 1 && isBuyer(t)">
            <el-button type="success" size="small" @click.stop="confirmReceive(t)">
              确认收货
            </el-button>
          </template>
          <template v-if="t.status === 2 && !t.reviewed">
            <el-button type="warning" size="small" @click.stop="openReview(t)">
              去评价
            </el-button>
          </template>
          <template v-if="t.status === 2 && t.reviewed">
            <el-tag type="success" size="small">已评价</el-tag>
          </template>
          <template v-if="(t.status === 0 || t.status === 1) && !isCancelRequester(t)">
            <el-button type="danger" text size="small" @click.stop="cancelTransaction(t)">
              申请取消
            </el-button>
          </template>
          <template v-if="t.status === 4 && isCancelRequester(t)">
            <el-tag type="warning" size="small">等待对方确认取消</el-tag>
          </template>
          <template v-if="t.status === 4 && !isCancelRequester(t)">
            <el-button type="danger" size="small" @click.stop="confirmCancel(t)">
              确认取消
            </el-button>
            <el-button type="info" size="small" @click.stop="rejectCancel(t)">
              拒绝取消
            </el-button>
          </template>
        </div>
      </div>

      <div v-if="total > pageSize" class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="fetchTransactions"
        />
      </div>
    </div>

    <!-- 交易详情弹窗 -->
    <el-dialog v-model="detailVisible" title="交易详情" width="500px">
      <template v-if="currentTransaction">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="交易号">{{ currentTransaction.id }}</el-descriptions-item>
          <el-descriptions-item label="商品">{{ currentTransaction.productTitle || '已删除' }}</el-descriptions-item>
          <el-descriptions-item label="价格">¥{{ currentTransaction.price?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentTransaction.status)">
              {{ getStatusLabel(currentTransaction.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="买家">{{ currentTransaction.buyerNickname }}</el-descriptions-item>
          <el-descriptions-item label="卖家">{{ currentTransaction.sellerNickname }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentTransaction.createdAt) }}</el-descriptions-item>
          <el-descriptions-item v-if="currentTransaction.deliveredAt" label="交付时间">
            {{ formatDate(currentTransaction.deliveredAt) }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentTransaction.completedAt" label="完成时间">
            {{ formatDate(currentTransaction.completedAt) }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentTransaction.cancelReason" label="取消原因">
            {{ currentTransaction.cancelReason }}
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>

    <!-- 取消原因弹窗 -->
    <el-dialog v-model="cancelDialogVisible" title="申请取消交易" width="400px">
      <el-form>
        <el-form-item label="取消原因">
          <el-input
            v-model="cancelReason"
            type="textarea"
            :rows="3"
            placeholder="请输入取消原因（可选）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancelDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCancelRequest">确定</el-button>
      </template>
    </el-dialog>

    <!-- 评价弹窗 -->
    <ReviewDialog
      v-if="reviewVisible"
      :transaction-id="reviewTransactionId"
      @submitted="onReviewSubmitted"
    />
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import { TransactionStatusLabel, TransactionStatusType } from '~/types/transaction'
import ReviewDialog from '~/components/review/ReviewDialog.vue'

const { $api } = useNuxtApp()
const config = useRuntimeConfig()
const token = useCookie('token')
const authStore = useAuthStore()

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}

// 从 token 获取当前用户 ID（同步）
const currentUserId = computed(() => {
  if (!token.value) return null
  try {
    const payload = JSON.parse(atob(token.value.split('.')[1]))
    return Number(payload.sub)
  } catch {
    return null
  }
})

const activeTab = ref('all')

const reviewVisible = ref(false)
const reviewTransactionId = ref(null)
const transactions = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const detailVisible = ref(false)
const currentTransaction = ref(null)

const cancelDialogVisible = ref(false)
const cancelReason = ref('')
const cancelTarget = ref(null)

const fetchTransactions = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
    }
    if (activeTab.value !== 'all') {
      params.status = parseInt(activeTab.value)
    }
    const res = await $api.get('/api/transactions', { params })
    transactions.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    transactions.value = []
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  currentPage.value = 1
  fetchTransactions()
}

const isBuyer = (t) => currentUserId.value && currentUserId.value === t.buyerId
const isSeller = (t) => currentUserId.value && currentUserId.value === t.sellerId
const isCancelRequester = (t) => currentUserId.value && currentUserId.value === t.cancelBy

const getStatusLabel = (status) => TransactionStatusLabel[status] || '未知'
const getStatusType = (status) => TransactionStatusType[status] || 'info'

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const showDetail = (t) => {
  currentTransaction.value = t
  detailVisible.value = true
}

const markDelivered = async (t) => {
  try {
    await ElMessageBox.confirm('确认已将商品交付给买家？', '标记交付')
    await $api.put(`/api/transactions/${t.id}/deliver`)
    ElMessage.success('已标记交付')
    fetchTransactions()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

const confirmReceive = async (t) => {
  try {
    await ElMessageBox.confirm('确认已收到商品？', '确认收货')
    await $api.put(`/api/transactions/${t.id}/confirm`)
    ElMessage.success('已确认收货')
    fetchTransactions()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

const cancelTransaction = (t) => {
  cancelTarget.value = t
  cancelReason.value = ''
  cancelDialogVisible.value = true
}

const submitCancelRequest = async () => {
  try {
    const params = cancelReason.value ? { reason: cancelReason.value } : {}
    await $api.post(`/api/transactions/${cancelTarget.value.id}/cancel`, null, { params })
    ElMessage.success('已申请取消，等待对方确认')
    cancelDialogVisible.value = false
    fetchTransactions()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

const confirmCancel = async (t) => {
  try {
    await ElMessageBox.confirm('确认取消此交易？商品将重新上架。', '确认取消')
    await $api.post(`/api/transactions/${t.id}/confirm-cancel`)
    ElMessage.success('已确认取消')
    fetchTransactions()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

const rejectCancel = async (t) => {
  try {
    await ElMessageBox.confirm('拒绝取消请求？交易将继续进行。', '拒绝取消')
    await $api.post(`/api/transactions/${t.id}/reject-cancel`)
    ElMessage.success('已拒绝取消')
    fetchTransactions()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

const openReview = (t) => {
  reviewTransactionId.value = t.id
  reviewVisible.value = true
}

const onReviewSubmitted = () => {
  reviewVisible.value = false
  reviewTransactionId.value = null
  fetchTransactions()
}

onMounted(() => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  fetchTransactions()
})
</script>

<style scoped>
.transactions-page {
  padding: 24px 32px;
  max-width: 900px;
  margin: 0 auto;
}

.page-title {
  font-size: 20px;
  margin: 0 0 20px;
}

.loading {
  padding: 20px;
}

.empty {
  padding: 60px 0;
}

.transaction-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.transaction-card {
  background: #fff;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.transaction-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fafafa;
  border-bottom: 1px solid #ebeef5;
}

.transaction-id {
  font-size: 13px;
  color: #909399;
}

.card-body {
  padding: 16px;
  display: flex;
  gap: 20px;
}

.product-info {
  display: flex;
  gap: 12px;
  flex: 1;
}

.product-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  background-color: #f5f7fa;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.image-placeholder {
  font-size: 24px;
  color: #c0c4cc;
}

.product-detail {
  flex: 1;
  min-width: 0;
}

.product-title {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 8px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
}

.trade-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 13px;
  color: #606266;
  min-width: 200px;
}

.trade-user, .trade-time {
  display: flex;
  gap: 4px;
}

.label {
  color: #909399;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}
</style>
