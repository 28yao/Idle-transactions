<template>
  <div class="review-page">
    <h2>审核中心</h2>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="实名认证" name="verify" />
      <el-tab-pane label="举报处理" name="report" />
    </el-tabs>

    <!-- 实名认证审核 -->
    <template v-if="activeTab === 'verify'">
      <el-table :data="verifications" border style="width: 100%">
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column label="认证图片">
          <template #default="{ row }">
            <el-image v-if="row.verifyImage" :src="resolveUrl(row.verifyImage)" style="width: 80px; height: 60px" fit="cover" :preview-src-list="[resolveUrl(row.verifyImage)]" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260">
          <template #default="{ row }">
            <el-button size="small" @click="showVerifyDetail(row)">查看详情</el-button>
            <el-button size="small" type="success" @click="reviewVerify(row.id, true)">通过</el-button>
            <el-button size="small" type="danger" @click="reviewVerify(row.id, false)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="verifyPage" :page-size="20" :total="verifyTotal" layout="total, prev, pager, next" @current-change="fetchVerifications" />
      </div>
    </template>

    <!-- 举报处理 -->
    <template v-if="activeTab === 'report'">
      <el-table :data="reports" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column label="举报者" width="100">
          <template #default="{ row }">{{ row.reporterNickname || row.reporterId }}</template>
        </el-table-column>
        <el-table-column label="目标类型" width="80">
          <template #default="{ row }">{{ row.targetType === 1 ? '商品' : '用户' }}</template>
        </el-table-column>
        <el-table-column label="目标" width="120">
          <template #default="{ row }">{{ row.targetTitle || row.targetNickname || row.targetId }}</template>
        </el-table-column>
        <el-table-column label="举报类型" width="100">
          <template #default="{ row }">{{ getReportType(row.type) }}</template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning" size="small">待处理</el-tag>
            <el-tag v-else-if="row.status === 1" type="success" size="small">已处理</el-tag>
            <el-tag v-else type="info" size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260">
          <template #default="{ row }">
            <el-button size="small" @click="showReportDetail(row)">查看详情</el-button>
            <template v-if="row.status === 0">
              <el-button size="small" type="success" @click="reviewReportItem(row.id, true)">处理</el-button>
              <el-button size="small" type="danger" @click="reviewReportItem(row.id, false)">驳回</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="reportPage" :page-size="20" :total="reportTotal" layout="total, prev, pager, next" @current-change="fetchReports" />
      </div>
    </template>

    <!-- 实名认证详情弹窗 -->
    <el-dialog v-model="verifyDialogVisible" title="实名认证详情" width="520px">
      <div v-if="currentVerify" class="detail-content">
        <div class="detail-row">
          <span class="detail-label">用户ID</span>
          <span class="detail-value">{{ currentVerify.id }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">昵称</span>
          <span class="detail-value">{{ currentVerify.nickname }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">真实姓名</span>
          <span class="detail-value">{{ currentVerify.realName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">申请时间</span>
          <span class="detail-value">{{ currentVerify.createdAt }}</span>
        </div>
        <div class="detail-row" v-if="currentVerify.verifyImage">
          <span class="detail-label">认证材料</span>
          <span class="detail-value">
            <el-image :src="resolveUrl(currentVerify.verifyImage)" :preview-src-list="[resolveUrl(currentVerify.verifyImage)]" style="max-width: 400px; max-height: 300px" fit="contain" />
          </span>
        </div>
      </div>
      <template #footer>
        <el-button @click="verifyDialogVisible = false">关闭</el-button>
        <el-button type="success" @click="reviewVerify(currentVerify.id, true); verifyDialogVisible = false">通过</el-button>
        <el-button type="danger" @click="reviewVerify(currentVerify.id, false); verifyDialogVisible = false">拒绝</el-button>
      </template>
    </el-dialog>

    <!-- 举报详情弹窗 -->
    <el-dialog v-model="reportDialogVisible" title="举报详情" width="560px">
      <div v-if="currentReport" class="detail-content">
        <div class="detail-row">
          <span class="detail-label">举报ID</span>
          <span class="detail-value">{{ currentReport.id }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">举报者</span>
          <span class="detail-value">{{ currentReport.reporterNickname }} (ID: {{ currentReport.reporterId }})</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">举报类型</span>
          <span class="detail-value">{{ getReportType(currentReport.type) }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">目标类型</span>
          <span class="detail-value">{{ currentReport.targetType === 1 ? '商品' : '用户' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">举报目标</span>
          <span class="detail-value">
            <template v-if="currentReport.targetType === 1">
              {{ currentReport.targetTitle || '商品已删除' }} (ID: {{ currentReport.targetId }})
            </template>
            <template v-else>
              {{ currentReport.targetNickname || '用户已注销' }} (ID: {{ currentReport.targetId }})
            </template>
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">举报原因</span>
          <span class="detail-value reason-text">{{ currentReport.reason }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <span class="detail-value">
            <el-tag v-if="currentReport.status === 0" type="warning" size="small">待处理</el-tag>
            <el-tag v-else-if="currentReport.status === 1" type="success" size="small">已处理</el-tag>
            <el-tag v-else type="info" size="small">已驳回</el-tag>
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">举报时间</span>
          <span class="detail-value">{{ currentReport.createdAt }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="reportDialogVisible = false">关闭</el-button>
        <template v-if="currentReport?.status === 0">
          <el-button type="success" @click="reviewReportItem(currentReport.id, true); reportDialogVisible = false">处理</el-button>
          <el-button type="danger" @click="reviewReportItem(currentReport.id, false); reportDialogVisible = false">驳回</el-button>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
definePageMeta({ layout: 'admin', middleware: 'admin' })

const config = useRuntimeConfig()
const { $api } = useNuxtApp()

const activeTab = ref('verify')

const verifications = ref([])
const verifyPage = ref(1)
const verifyTotal = ref(0)

const reports = ref([])
const reportPage = ref(1)
const reportTotal = ref(0)

const verifyDialogVisible = ref(false)
const currentVerify = ref(null)
const reportDialogVisible = ref(false)
const currentReport = ref(null)

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}

const fetchVerifications = async () => {
  try {
    const res = await $api.get('/api/admin/verifications', { params: { page: verifyPage.value, size: 20 } })
    verifications.value = res.data?.records || []
    verifyTotal.value = res.data?.total || 0
  } catch (e) {}
}

const fetchReports = async () => {
  try {
    const res = await $api.get('/api/admin/reports', { params: { page: reportPage.value, size: 20 } })
    reports.value = res.data?.records || []
    reportTotal.value = res.data?.total || 0
  } catch (e) {}
}

const handleTabChange = () => {
  if (activeTab.value === 'verify') fetchVerifications()
  else fetchReports()
}

const showVerifyDetail = (row) => {
  currentVerify.value = row
  verifyDialogVisible.value = true
}

const showReportDetail = (row) => {
  currentReport.value = row
  reportDialogVisible.value = true
}

const reviewVerify = async (id, approved) => {
  try {
    await $api.put(`/api/admin/verifications/${id}`, { approved })
    fetchVerifications()
  } catch (e) {}
}

const reviewReportItem = async (id, approved) => {
  try {
    await $api.put(`/api/admin/reports/${id}`, { approved })
    fetchReports()
  } catch (e) {}
}

const getReportType = (type) => {
  const map = { 1: '虚假信息', 2: '违禁物品', 3: '欺诈', 4: '其他' }
  return map[type] || '未知'
}

onMounted(fetchVerifications)
</script>

<style scoped>
.review-page h2 {
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
  gap: 16px;
}

.detail-row {
  display: flex;
  gap: 12px;
}

.detail-label {
  flex-shrink: 0;
  width: 80px;
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

.reason-text {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  line-height: 1.6;
}
</style>
