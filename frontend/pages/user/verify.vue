<template>
  <div class="verify-page">
    <div class="verify-card">
      <h2 class="card-title">实名认证</h2>
      <p class="card-subtitle">完成实名认证后将显示认证标识，增加买家信任度</p>

      <!-- 当前状态 -->
      <div class="status-block" :class="statusClass">
        <div class="status-label">当前状态</div>
        <div class="status-value">{{ statusText }}</div>
        <div v-if="status === 3" class="status-reason">认证失败，请重新提交清晰可辨认的证件照片</div>
      </div>

      <!-- 认证好处 -->
      <div class="benefits">
        <div class="benefit-item">
          <el-icon color="#409eff"><CircleCheck /></el-icon>
          <span>个人主页显示"已认证"标识</span>
        </div>
        <div class="benefit-item">
          <el-icon color="#409eff"><CircleCheck /></el-icon>
          <span>商品详情页展示认证身份</span>
        </div>
        <div class="benefit-item">
          <el-icon color="#409eff"><CircleCheck /></el-icon>
          <span>提升买家信任度，更容易卖出</span>
        </div>
      </div>

      <!-- 表单（未认证 / 认证失败时显示） -->
      <el-form
        v-if="canSubmit"
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        class="verify-form"
      >
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入身份证上的姓名" />
        </el-form-item>

        <el-form-item label="学生证 / 身份证照片" prop="verifyImage">
          <ImageUpload v-model="form.verifyImage" placeholder="上传学生证或身份证照片" />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="submit-btn"
            @click="handleSubmit"
          >
            提交认证
          </el-button>
        </el-form-item>
      </el-form>

      <div v-else-if="status === 1" class="info-tip">
        <el-icon color="#e6a23c"><Clock /></el-icon>
        <span>您的认证申请已提交，预计 24 小时内完成审核</span>
      </div>

      <div v-else-if="status === 2" class="info-tip success">
        <el-icon color="#67c23a"><CircleCheck /></el-icon>
        <span>恭喜，您已通过实名认证</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { CircleCheck, Clock } from '@element-plus/icons-vue'
import { useAuthStore } from '~/stores/auth'
import ImageUpload from '~/components/common/ImageUpload.vue'

definePageMeta({ middleware: 'auth' })

const authStore = useAuthStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  realName: '',
  verifyImage: '',
})

const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20位', trigger: 'blur' },
  ],
  verifyImage: [
    { required: true, message: '请上传认证图片', trigger: 'change' },
  ],
}

const status = computed(() => authStore.user?.verifyStatus ?? 0)

const statusText = computed(() => ({
  0: '未认证',
  1: '审核中',
  2: '已认证',
  3: '认证失败',
}[status.value] || '未认证'))

const statusClass = computed(() => ({
  0: 'status-default',
  1: 'status-pending',
  2: 'status-success',
  3: 'status-fail',
}[status.value]))

const canSubmit = computed(() => status.value === 0 || status.value === 3)

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const { $api } = useNuxtApp()
    await $api.post('/api/users/verify', form)
    ElMessage.success('提交成功，等待审核')
    await authStore.fetchUser()
  } catch (error) {
    ElMessage.error(error?.message || '提交失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.verify-page {
  padding: 32px;
  display: flex;
  justify-content: center;
}

.verify-card {
  width: 100%;
  max-width: 640px;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 12px;
  padding: 40px;
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.card-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0 0 24px 0;
}

.status-block {
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 24px;
}

.status-default {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
}

.status-pending {
  background-color: #fdf6ec;
  border: 1px solid #faecd8;
}

.status-success {
  background-color: #f0f9eb;
  border: 1px solid #e1f3d8;
}

.status-fail {
  background-color: #fef0f0;
  border: 1px solid #fde2e2;
}

.status-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.status-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.status-reason {
  font-size: 13px;
  color: #f56c6c;
  margin-top: 8px;
}

.benefits {
  background-color: #f5f9ff;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 24px;
}

.benefit-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #606266;
  padding: 4px 0;
}

.verify-form {
  margin-top: 8px;
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
}

.info-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #606266;
  padding: 12px 16px;
  background-color: #fdf6ec;
  border-radius: 8px;
}

.info-tip.success {
  background-color: #f0f9eb;
  color: #67c23a;
}

:deep(.el-form-item__label) {
  font-size: 13px;
  font-weight: 500;
  color: #303133;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  height: 40px;
}
</style>
