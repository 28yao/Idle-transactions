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
            <el-image v-if="row.verifyImage" :src="row.verifyImage" style="width: 80px; height: 60px" fit="cover" :preview-src-list="[row.verifyImage]" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
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
        <el-table-column prop="reason" label="原因" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning" size="small">待处理</el-tag>
            <el-tag v-else-if="row.status === 1" type="success" size="small">已处理</el-tag>
            <el-tag v-else type="info" size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
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
  </div>
</template>

<script setup>
definePageMeta({ layout: 'admin', middleware: 'admin' })

const { $api } = useNuxtApp()

const activeTab = ref('verify')

const verifications = ref([])
const verifyPage = ref(1)
const verifyTotal = ref(0)

const reports = ref([])
const reportPage = ref(1)
const reportTotal = ref(0)

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
</style>
