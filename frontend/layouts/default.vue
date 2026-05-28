<template>
  <div class="app-container">
    <header class="app-header">
      <div class="header-content">
        <NuxtLink to="/" class="logo">
          <span class="logo-icon"></span>
          <span class="logo-text">校园二手</span>
        </NuxtLink>
        <nav class="nav-links">
          <NuxtLink to="/" class="nav-item" :class="{ active: route.path === '/' }">首页</NuxtLink>
          <NuxtLink to="/messages" class="nav-item" :class="{ active: route.path.startsWith('/messages') }">消息</NuxtLink>
          <NuxtLink to="/user/listings" class="nav-item" :class="{ active: route.path.startsWith('/user/listings') }">我的发布</NuxtLink>
          <NuxtLink to="/user/favorites" class="nav-item" :class="{ active: route.path.startsWith('/user/favorites') }">收藏</NuxtLink>
        </nav>
        <div class="header-actions">
          <template v-if="authStore.isLoggedIn">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" :src="authStore.user?.avatar" />
                <span class="nickname">{{ authStore.user?.nickname }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="navigateTo('/user/profile')">个人主页</el-dropdown-item>
                  <el-dropdown-item @click="navigateTo('/user/listings')">我的发布</el-dropdown-item>
                  <el-dropdown-item @click="navigateTo('/user/favorites')">我的收藏</el-dropdown-item>
                  <el-dropdown-item @click="navigateTo('/notifications')">通知中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" size="small" @click="navigateTo('/login')">登录</el-button>
            <el-button size="small" @click="navigateTo('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <main class="app-main">
      <slot />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from '~/stores/auth'

const route = useRoute()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  navigateTo('/login')
}
</script>

<style scoped>
.app-container {
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
  justify-content: space-between;
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

.nav-links {
  display: flex;
  align-items: center;
  gap: 48px;
}

.nav-item {
  font-size: 14px;
  color: #606266;
  text-decoration: none;
  transition: color 0.2s;
}

.nav-item:hover {
  color: #409eff;
}

.nav-item.active {
  color: #303133;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.nickname {
  font-size: 14px;
  color: #303133;
}

.app-main {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}
</style>
