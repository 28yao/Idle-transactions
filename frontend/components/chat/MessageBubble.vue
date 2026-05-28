<template>
  <div class="message-bubble" :class="{ 'is-self': isSelf }">
    <el-avatar v-if="!isSelf" :size="32" :src="message.senderAvatar">
      {{ message.senderNickname?.charAt(0) }}
    </el-avatar>

    <div class="bubble-content">
      <!-- 出价/还价消息 -->
      <div v-if="message.type === 3" class="offer-card">
        <div class="offer-header">
          <span class="offer-label">出价</span>
          <span class="offer-price">¥{{ message.priceOffer }}</span>
        </div>
        <div class="offer-body">{{ message.content }}</div>
        <div v-if="message.offerStatus === 0 && !isSelf" class="offer-actions">
          <el-button size="small" type="success" @click="$emit('accept-offer')">接受</el-button>
          <el-button size="small" type="danger" @click="$emit('reject-offer')">拒绝</el-button>
        </div>
        <div v-if="message.offerStatus === 1" class="offer-status accepted">已接受</div>
        <div v-if="message.offerStatus === 2" class="offer-status rejected">已拒绝</div>
      </div>

      <!-- 普通文字消息 -->
      <div v-else class="text-bubble">
        {{ message.content }}
      </div>

      <div class="bubble-time">{{ formatTime(message.createdAt) }}</div>
    </div>

    <el-avatar v-if="isSelf" :size="32" :src="message.senderAvatar" class="self-avatar">
      {{ message.senderNickname?.charAt(0) }}
    </el-avatar>
  </div>
</template>

<script setup>
defineProps({
  message: { type: Object, required: true },
  isSelf: { type: Boolean, default: false },
})

defineEmits(['accept-offer', 'reject-offer'])

const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>

<style scoped>
.message-bubble {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}

.message-bubble.is-self {
  justify-content: flex-end;
}

.bubble-content {
  max-width: 60%;
}

.text-bubble {
  background: #f4f4f5;
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 14px;
  color: #303133;
  line-height: 1.5;
  word-break: break-word;
}

.is-self .text-bubble {
  background: #ecf5ff;
  color: #409eff;
}

.bubble-time {
  font-size: 11px;
  color: #c0c4cc;
  margin-top: 4px;
}

.is-self .bubble-time {
  text-align: right;
}

.offer-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 12px;
  min-width: 200px;
}

.offer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.offer-label {
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  padding: 2px 8px;
  border-radius: 4px;
}

.offer-price {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

.offer-body {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.offer-actions {
  display: flex;
  gap: 8px;
}

.offer-status {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  text-align: center;
}

.offer-status.accepted {
  background: #f0f9eb;
  color: #67c23a;
}

.offer-status.rejected {
  background: #fef0f0;
  color: #f56c6c;
}
</style>
