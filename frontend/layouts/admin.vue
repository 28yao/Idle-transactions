<template>
  <div class="admin-container">
    <aside class="admin-sidebar">
      <div class="sidebar-header">
        <NuxtLink to="/admin" class="logo">管理后台</NuxtLink>
      </div>
      <nav class="sidebar-nav">
        <NuxtLink to="/admin" class="nav-item" :class="{ active: route.path === '/admin' }">
          数据概览
        </NuxtLink>
        <NuxtLink to="/admin/users" class="nav-item" :class="{ active: route.path === '/admin/users' }">
          用户管理
        </NuxtLink>
        <NuxtLink to="/admin/products" class="nav-item" :class="{ active: route.path === '/admin/products' }">
          商品管理
        </NuxtLink>
        <NuxtLink to="/admin/review" class="nav-item" :class="{ active: route.path === '/admin/review' }">
          审核中心
        </NuxtLink>
      </nav>
      <div class="sidebar-footer">
        <NuxtLink to="/" class="back-link">返回前台</NuxtLink>
      </div>
    </aside>
    <main class="admin-main">
      <slot />
    </main>
  </div>
</template>

<script setup>
const route = useRoute()
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
}

.admin-sidebar {
  width: 220px;
  background: #304156;
  color: #bfcbd9;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  text-decoration: none;
}

.sidebar-nav {
  flex: 1;
  padding: 12px 0;
}

.nav-item {
  display: block;
  padding: 12px 24px;
  color: #bfcbd9;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
}

.nav-item.active {
  background: #409eff;
  color: #fff;
}

.sidebar-footer {
  padding: 16px 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.back-link {
  color: #bfcbd9;
  text-decoration: none;
  font-size: 13px;
}

.back-link:hover {
  color: #fff;
}

.admin-main {
  flex: 1;
  background: #f5f7fa;
  padding: 24px;
  overflow-y: auto;
}
</style>
