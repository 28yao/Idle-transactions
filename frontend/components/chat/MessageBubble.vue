<template>
  <div class="message-bubble" :class="{ 'is-self': isSelf }">
    <el-avatar v-if="!isSelf" :size="32" :src="message.senderAvatar">
      {{ message.senderNickname?.charAt(0) }}
    </el-avatar>

    <div class="bubble-content">
      <div class="text-bubble">
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
</style>
