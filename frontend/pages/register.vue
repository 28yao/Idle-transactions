<template>
  <div class="register-page">
    <!-- Header -->
    <header class="app-header">
      <div class="header-content">
        <NuxtLink to="/" class="logo">
          <span class="logo-icon"></span>
          <span class="logo-text">校园二手</span>
        </NuxtLink>
      </div>
    </header>

    <!-- 注册表单 -->
    <div class="register-body">
      <div class="register-card">
        <h2 class="card-title">创建账号</h2>
        <p class="card-subtitle">填写邮箱、昵称、密码与校区完成注册</p>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-position="top"
          @submit.prevent="handleRegister"
        >
          <el-form-item label="邮箱 *" prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入邮箱地址"
              type="email"
            />
          </el-form-item>

          <el-form-item label="昵称 *" prop="nickname">
            <el-input
              v-model="form.nickname"
              placeholder="2-20位，可含中文、字母、数字"
            />
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              placeholder="8-20位，包含字母和数字"
              type="password"
              show-password
            />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input
              v-model="form.confirmPassword"
              placeholder="请再次输入密码"
              type="password"
              show-password
            />
          </el-form-item>

          <el-form-item label="所属校区" prop="campus">
            <el-select
              v-model="form.campus"
              placeholder="请选择校区"
              style="width: 100%;"
            >
              <el-option label="东校区" value="东校区" />
              <el-option label="西校区" value="西校区" />
              <el-option label="南校区" value="南校区" />
              <el-option label="北校区" value="北校区" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              native-type="submit"
              :loading="loading"
              class="register-btn"
            >
              注 册
            </el-button>
          </el-form-item>

          <div class="login-link">
            已有账号？
            <NuxtLink to="/login">立即登录</NuxtLink>
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
  nickname: '',
  password: '',
  confirmPassword: '',
  campus: '',
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度为2-20位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度为8-20位', trigger: 'blur' },
    {
      pattern: /^(?=.*[a-zA-Z])(?=.*\d)/,
      message: '密码必须包含字母和数字',
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' },
  ],
  campus: [
    { required: true, message: '请选择校区', trigger: 'change' },
  ],
}

const handleRegister = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await authStore.register({
      email: form.email,
      nickname: form.nickname,
      password: form.password,
      campus: form.campus,
    })
    ElMessage.success('注册成功')
    navigateTo('/')
  } catch (error) {
    ElMessage.error(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.app-header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 32px;
  height: 64px;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background-color: #409eff;
  border-radius: 8px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.register-body {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
}

.register-card {
  width: 480px;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 12px;
  padding: 40px;
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.card-subtitle {
  font-size: 14px;
  color: #909399;
  margin-bottom: 24px;
}

.register-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
  letter-spacing: 4px;
}

.login-link {
  text-align: center;
  font-size: 13px;
  color: #909399;
}

.login-link a {
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
  padding: 0 12px;
  height: 40px;
}

:deep(.el-input__inner) {
  font-size: 14px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  padding: 0 12px;
  height: 40px;
}
</style>
