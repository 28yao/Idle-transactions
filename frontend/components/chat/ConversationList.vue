<template>
  <div class="conversation-list">
    <div v-if="conversations.length === 0" class="empty">
      <p>暂无消息</p>
    </div>
    <div
      v-for="conv in conversations"
      :key="conv.id"
      class="conversation-item"
      :class="{ active: conv.id === activeId }"
      @click="$emit('select', conv.id)"
    >
      <div class="conv-avatar">
        <el-avatar :size="40" :src="conv.otherAvatar">
          {{ conv.otherNickname?.charAt(0) }}
        </el-avatar>
      </div>
      <div class="conv-info">
        <div class="conv-header">
          <span class="conv-name">{{ conv.otherNickname }}</span>
          <span class="conv-time">{{ formatTime(conv.lastMessageAt) }}</span>
        </div>
        <div class="conv-preview">
          <span class="conv-product">{{ conv.productTitle }}</span>
          <span class="conv-message">{{ conv.lastMessage }}</span>
        </div>
      </div>
      <el-button
        class="delete-btn"
        type="danger"
        text
        size="small"
        @click.stop="$emit('delete', conv.id)"
      >
        删除
      </el-button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  conversations: { type: Array, default: () => [] },
  activeId: { type: Number, default: null },
})

defineEmits(['select', 'delete'])

const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  const now = new Date()
  const diff = now - d
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
  return `${d.getMonth() + 1}/${d.getDate()}`
}
</script>

<style scoped>
.conversation-list {
  flex: 1;
  overflow-y: auto;
}

.empty {
  padding: 40px 20px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.conversation-item {
  display: flex;
  gap: 12px;
  padding: 12px 20px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.conversation-item:hover {
  background-color: #f5f7fa;
}

.conversation-item.active {
  background-color: #ecf5ff;
}

.conv-avatar {
  flex-shrink: 0;
}

.conv-info {
  flex: 1;
  min-width: 0;
}

.conv-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.conv-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.conv-time {
  font-size: 12px;
  color: #c0c4cc;
}

.conv-preview {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.conv-product {
  font-size: 12px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.conv-message {
  font-size: 13px;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.delete-btn {
  opacity: 0;
  transition: opacity 0.2s;
}

.conversation-item:hover .delete-btn {
  opacity: 1;
}
</style>
