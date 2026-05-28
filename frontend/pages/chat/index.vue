<template>
  <div class="chat-page">
    <div class="chat-container">
      <!-- 左侧：会话列表 -->
      <div class="conversation-panel">
        <div class="panel-header">
          <h3>消息</h3>
        </div>
        <ConversationList
          :conversations="conversations"
          :active-id="activeConversationId"
          @select="selectConversation"
        />
      </div>

      <!-- 右侧：聊天窗口 -->
      <div class="chat-panel">
        <ChatWindow
          v-if="activeConversationId"
          :conversation-id="activeConversationId"
          :conversation="activeConversation"
          @send="sendMessage"
          @offer="handleOffer"
        />
        <div v-else class="empty-chat">
          <el-icon :size="64" color="#c0c4cc"><ChatDotRound /></el-icon>
          <p>选择一个会话开始聊天</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ChatDotRound } from '@element-plus/icons-vue'
import ConversationList from '~/components/chat/ConversationList.vue'
import ChatWindow from '~/components/chat/ChatWindow.vue'

const { $api } = useNuxtApp()
const token = useCookie('token')

const conversations = ref([])
const activeConversationId = ref(null)
const ws = ref(null)

const activeConversation = computed(() => {
  return conversations.value.find(c => c.id === activeConversationId.value)
})

const fetchConversations = async () => {
  try {
    const res = await $api.get('/api/conversations')
    conversations.value = res.data || []
  } catch (e) {
    console.error('Failed to fetch conversations:', e)
  }
}

const selectConversation = (id) => {
  activeConversationId.value = id
}

const sendMessage = async (content, type = 1, priceOffer = null) => {
  if (!activeConversationId.value) return
  try {
    await $api.post('/api/messages', {
      conversationId: activeConversationId.value,
      content,
      type,
      priceOffer,
    })
    // 刷新会话列表
    await fetchConversations()
  } catch (e) {
    ElMessage.error(e.message || '发送失败')
  }
}

const handleOffer = async (messageId, action) => {
  try {
    await $api.post(`/api/messages/${messageId}/offer`, { action })
    ElMessage.success(action === 1 ? '已接受' : '已拒绝')
    await fetchConversations()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

const connectWebSocket = () => {
  if (!token.value) return

  const wsUrl = `ws://localhost:8080/ws/chat?token=${token.value}`
  ws.value = new WebSocket(wsUrl)

  ws.value.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data)
      if (data.type === 'new_message') {
        // 新消息到达，刷新会话列表
        fetchConversations()
      }
    } catch (e) {
      console.error('WebSocket message parse error:', e)
    }
  }

  ws.value.onclose = () => {
    // 断线重连
    setTimeout(connectWebSocket, 3000)
  }
}

onMounted(() => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  fetchConversations()
  connectWebSocket()
})

onUnmounted(() => {
  if (ws.value) {
    ws.value.close()
  }
})
</script>

<style scoped>
.chat-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 32px;
  height: calc(100vh - 64px - 48px);
}

.chat-container {
  display: flex;
  height: 100%;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
}

.conversation-panel {
  width: 320px;
  border-right: 1px solid #ebeef5;
  display: flex;
  flex-direction: column;
}

.panel-header {
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.empty-chat {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.empty-chat p {
  margin-top: 16px;
  font-size: 14px;
}
</style>
