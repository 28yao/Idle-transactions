<template>
  <div class="login-page">
    <!-- 左侧品牌面板 -->
    <div class="brand-panel">
      <div class="brand-content">
        <h1 class="brand-title">校园二手平台</h1>
        <p class="brand-desc">连接校园内的买卖双方<br />安全、便捷的二手物品交易平台</p>
      </div>
    </div>

    <!-- 右侧表单面板 -->
    <div class="form-panel">
      <div class="form-container">
        <h2 class="form-title">欢迎回来</h2>
        <p class="form-subtitle">使用邮箱和密码登录</p>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-position="top"
          @submit.prevent="handleLogin"
        >
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入邮箱地址"
              type="email"
            />
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              placeholder="请输入密码"
              type="password"
              show-password
            />
          </el-form-item>

          <div class="form-actions">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
            <NuxtLink to="/forgot-password" class="forgot-link">
              忘记密码？
            </NuxtLink>
          </div>

          <el-form-item>
            <el-button
              type="primary"
              native-type="submit"
              :loading="loading"
              class="login-btn"
            >
              登 录
            </el-button>
          </el-form-item>

          <div class="register-link">
            还没有账号？
            <NuxtLink to="/register">立即注册</NuxtLink>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '~/stores/auth'

definePageMeta({
  layout: false,
})

const authStore = useAuthStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  email: '',
  password: '',
  remember: false,
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度为8-20位', trigger: 'blur' },
  ],
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await authStore.login(form.email, form.password)
    ElMessage.success('登录成功')
    navigateTo('/')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  overflow: hidden;
}

.brand-panel {
  width: 720px;
  min-width: 720px;
  background: linear-gradient(135deg, #409eff 0%, #337ecc 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 64px;
}

.brand-content {
  color: white;
}

.brand-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 24px;
}

.brand-desc {
  font-size: 16px;
  width: 400px;
  opacity: 0.8;
  line-height: 1.8;
}

.form-panel {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  padding: 64px;
}

.form-container {
  width: 400px;
}

.form-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 14px;
  color: #909399;
  margin-bottom: 32px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.forgot-link {
  font-size: 13px;
  color: #409eff;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
  letter-spacing: 4px;
}

.register-link {
  text-align: center;
  font-size: 13px;
  color: #909399;
}

.register-link a {
  color: #409eff;
}

:deep(.el-form-item__label) {
  font-size: 13px;
  font-weight: 500;
  color: #303133;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  padding: 0 16px;
  height: 44px;
}

:deep(.el-input__inner) {
  font-size: 14px;
}
</style>
