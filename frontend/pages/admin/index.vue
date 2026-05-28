<template>
  <div class="dashboard-page">
    <h2>数据概览</h2>

    <div class="stats-grid">
      <el-card class="stat-card">
        <div class="stat-value">{{ stats.todayNewUsers || 0 }}</div>
        <div class="stat-label">今日新增用户</div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-value">{{ stats.onSaleProducts || 0 }}</div>
        <div class="stat-label">在售商品</div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-value">{{ stats.todayTransactions || 0 }}</div>
        <div class="stat-label">今日成交</div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-value">{{ (stats.pendingReports || 0) + (stats.pendingVerifications || 0) }}</div>
        <div class="stat-label">待审核</div>
      </el-card>
    </div>

    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待审核认证</span>
              <el-button text @click="navigateTo('/admin/review')">查看全部</el-button>
            </div>
          </template>
          <div v-if="verifications.length === 0" class="empty">暂无待审核</div>
          <div v-else class="pending-list">
            <div v-for="v in verifications" :key="v.id" class="pending-item">
              <span>{{ v.nickname }} ({{ v.realName }})</span>
              <div>
                <el-button size="small" type="success" @click="reviewVerify(v.id, true)">通过</el-button>
                <el-button size="small" type="danger" @click="reviewVerify(v.id, false)">拒绝</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待处理举报</span>
              <el-button text @click="navigateTo('/admin/review')">查看全部</el-button>
            </div>
          </template>
          <div v-if="reports.length === 0" class="empty">暂无待处理</div>
          <div v-else class="pending-list">
            <div v-for="r in reports" :key="r.id" class="pending-item">
              <span>{{ r.reason?.substring(0, 30) }}...</span>
              <div>
                <el-button size="small" type="success" @click="reviewReport(r.id, true)">处理</el-button>
                <el-button size="small" type="danger" @click="reviewReport(r.id, false)">驳回</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
definePageMeta({ layout: 'admin', middleware: 'admin' })

const { $api } = useNuxtApp()

const stats = ref({})
const verifications = ref([])
const reports = ref([])

const fetchDashboard = async () => {
  try {
    const res = await $api.get('/api/admin/dashboard')
    stats.value = res.data || {}
  } catch (e) {}
}

const fetchVerifications = async () => {
  try {
    const res = await $api.get('/api/admin/verifications', { params: { size: 5 } })
    verifications.value = res.data?.records || []
  } catch (e) {}
}

const fetchReports = async () => {
  try {
    const res = await $api.get('/api/admin/reports', { params: { status: 0, size: 5 } })
    reports.value = res.data?.records || []
  } catch (e) {}
}

const reviewVerify = async (id, approved) => {
  try {
    await $api.put(`/api/admin/verifications/${id}`, { approved })
    fetchVerifications()
    fetchDashboard()
  } catch (e) {}
}

const reviewReport = async (id, approved) => {
  try {
    await $api.put(`/api/admin/reports/${id}`, { approved })
    fetchReports()
    fetchDashboard()
  } catch (e) {}
}

onMounted(() => {
  fetchDashboard()
  fetchVerifications()
  fetchReports()
})
</script>

<style scoped>
.dashboard-page h2 {
  margin: 0 0 20px;
  font-size: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #409eff;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty {
  text-align: center;
  color: #909399;
  padding: 20px 0;
}

.pending-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.pending-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #ebeef5;
  font-size: 14px;
}

.pending-item:last-child {
  border-bottom: none;
}
</style>
