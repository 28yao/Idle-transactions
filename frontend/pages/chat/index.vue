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
          @delete="deleteConversation"
        />
      </div>

      <!-- 右侧：聊天窗口 -->
      <div class="chat-panel">
        <ChatWindow
          v-if="activeConversationId"
          ref="chatWindowRef"
          :conversation-id="activeConversationId"
          :conversation="activeConversation"
          @send="sendMessage"
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

const route = useRoute()
const { $api } = useNuxtApp()
const token = useCookie('token')

const conversations = ref([])
const activeConversationId = ref(null)
const ws = ref(null)
const chatWindowRef = ref(null)

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

const selectConversation = async (id) => {
  activeConversationId.value = id
  // 标记已读
  try {
    await $api.put(`/api/conversations/${id}/read`)
    // 更新本地未读数
    const conv = conversations.value.find(c => c.id === id)
    if (conv) conv.unreadCount = 0
  } catch (e) {}
}

const sendMessage = async (content) => {
  if (!activeConversationId.value) return
  try {
    await $api.post('/api/messages', {
      conversationId: activeConversationId.value,
      content,
    })
    await fetchConversations()
  } catch (e) {
    ElMessage.error(e.message || '发送失败')
  }
}

const deleteConversation = async (id) => {
  try {
    await $api.delete(`/api/conversations/${id}`)
    conversations.value = conversations.value.filter(c => c.id !== id)
    if (activeConversationId.value === id) {
      activeConversationId.value = null
    }
    ElMessage.success('已删除')
  } catch (e) {
    ElMessage.error(e.message || '删除失败')
  }
}

const connectWebSocket = () => {
  if (!token.value) return

  const wsUrl = `ws://localhost:8080/ws/chat?token=${token.value}`
  console.log('Connecting WebSocket:', wsUrl)
  ws.value = new WebSocket(wsUrl)

  ws.value.onopen = () => {
    console.log('WebSocket connected')
  }

  ws.value.onmessage = (event) => {
    console.log('WebSocket message received:', event.data)
    try {
      const data = JSON.parse(event.data)
      if (data.type === 'new_message') {
        fetchConversations()
        if (data.data && data.data.conversationId === activeConversationId.value && chatWindowRef.value) {
          chatWindowRef.value.addMessage(data.data)
        }
      }
    } catch (e) {
      console.error('WebSocket message parse error:', e)
    }
  }

  ws.value.onerror = (error) => {
    console.error('WebSocket error:', error)
  }

  ws.value.onclose = () => {
    console.log('WebSocket closed, reconnecting in 3s...')
    // 断线重连
    setTimeout(connectWebSocket, 3000)
  }
}

onMounted(async () => {
  if (!token.value) {
    navigateTo('/login')
    return
  }
  await fetchConversations()
  // 从 URL 参数自动选中会话
  const queryId = Number(route.query.id)
  if (queryId && conversations.value.some(c => c.id === queryId)) {
    activeConversationId.value = queryId
  }
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
  min-height: 0;
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
