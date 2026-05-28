<template>
  <div class="notifications-page">
    <div class="page-header">
      <h2>通知中心</h2>
      <el-button type="primary" text @click="markAllAsRead" :disabled="unreadCount === 0">
        全部已读
      </el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="私信" name="1" />
      <el-tab-pane label="互动" name="2" />
      <el-tab-pane label="系统" name="3" />
      <el-tab-pane label="交易" name="4" />
    </el-tabs>

    <div v-if="loading" class="loading">
      <el-skeleton :rows="3" animated />
    </div>

    <div v-else-if="notifications.length === 0" class="empty">
      <el-empty description="暂无通知" />
    </div>

    <div v-else class="notification-list">
      <div
        v-for="notification in notifications"
        :key="notification.id"
        class="notification-item"
        :class="{ unread: notification.isRead === 0 }"
        @click="handleClick(notification)"
      >
        <div class="notification-icon">
          <el-icon :size="20" :color="getIconColor(notification.type)">
            <component :is="getIcon(notification.type)" />
          </el-icon>
        </div>
        <div class="notification-content">
          <div class="notification-title">{{ notification.title }}</div>
          <div class="notification-text">{{ notification.content }}</div>
          <div class="notification-time">{{ formatTime(notification.createdAt) }}</div>
        </div>
        <div v-if="notification.isRead === 0" class="unread-dot" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ChatDotRound, Star, Bell, Goods } from '@element-plus/icons-vue'

const { $api } = useNuxtApp()
const token = useCookie('token')

const activeTab = ref('all')
const notifications = ref([])
const loading = ref(false)
const unreadCount = ref(0)

const fetchNotifications = async () => {
  loading.value = true
  try {
    const params = { page: 1, size: 50 }
    if (activeTab.value !== 'all') {
      params.type = parseInt(activeTab.value)
    }
    const res = await $api.get('/api/notifications', { params })
    notifications.value = res.data || []
  } catch (e) {
    console.error('Failed to fetch notifications:', e)
  } finally {
    loading.value = false
  }
}

const fetchUnreadCount = async () => {
  try {
    const res = await $api.get('/api/notifications/unread-count')
    unreadCount.value = res.data || 0
  } catch (e) {
    console.error('Failed to fetch unread count:', e)
  }
}

const handleTabChange = () => {
  fetchNotifications()
}

const markAllAsRead = async () => {
  try {
    await $api.put('/api/notifications/read-all')
    unreadCount.value = 0
    notifications.value.forEach(n => n.isRead = 1)
  } catch (e) {
    console.error('Failed to mark all as read:', e)
  }
}

const handleClick = async (notification) => {
  // 标记为已读
  if (notification.isRead === 0) {
    try {
      await $api.put(`/api/notifications/${notification.id}/read`)
      notification.isRead = 1
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    } catch (e) {
      console.error('Failed to mark as read:', e)
    }
  }

  // 跳转到相关页面
  if (notification.relatedId) {
    switch (notification.type) {
      case 1: // 私信
        navigateTo('/chat')
        break
      case 4: // 交易
        navigateTo('/user/transactions')
        break
    }
  }
}

const getIcon = (type) => {
  switch (type) {
    case 1: return ChatDotRound
    case 2: return Star
    case 3: return Bell
    case 4: return Goods
    default: return Bell
  }
}

const getIconColor = (type) => {
  switch (type) {
    case 1: return '#409eff'
    case 2: return '#e6a23c'
    case 3: return '#909399'
    case 4: return '#67c23a'
    default: return '#909399'
  }
}

const formatTime = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const now = new Date()
  const diff = now - d
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60))
    if (hours === 0) {
      const minutes = Math.floor(diff / (1000 * 60))
      return minutes <= 1 ? '刚刚' : `${minutes}分钟前`
    }
    return `${hours}小时前`
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return `${d.getMonth() + 1}月${d.getDate()}日`
  }
}

onMounted(() => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  fetchNotifications()
  fetchUnreadCount()
})
</script>

<style scoped>
.notifications-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px 32px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
}

.loading {
  padding: 20px;
}

.empty {
  padding: 60px 0;
}

.notification-list {
  display: flex;
  flex-direction: column;
  gap: 1px;
  background: #ebeef5;
  border-radius: 8px;
  overflow: hidden;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: #fff;
  cursor: pointer;
  transition: background-color 0.2s;
}

.notification-item:hover {
  background: #f5f7fa;
}

.notification-item.unread {
  background: #ecf5ff;
}

.notification-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.notification-text {
  font-size: 13px;
  color: #606266;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.unread-dot {
  flex-shrink: 0;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #f56c6c;
  margin-top: 6px;
}
</style>
