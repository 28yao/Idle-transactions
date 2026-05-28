<template>
  <div class="chat-window">
    <!-- 头部：对方信息 + 商品卡片 -->
    <div class="chat-header">
      <div class="header-user">
        <el-avatar :size="32" :src="conversation?.otherAvatar">
          {{ conversation?.otherNickname?.charAt(0) }}
        </el-avatar>
        <span class="header-name">{{ conversation?.otherNickname }}</span>
      </div>
      <div v-if="conversation?.productTitle" class="header-product" @click="goProduct">
        <span class="product-title">{{ conversation.productTitle }}</span>
        <span v-if="conversation.productPrice" class="product-price">
          ¥{{ conversation.productPrice }}
        </span>
      </div>
    </div>

    <!-- 消息列表 -->
    <div ref="messageListRef" class="message-list">
      <div v-if="loading" class="loading">
        <el-icon class="is-loading"><Loading /></el-icon>
      </div>
      <MessageBubble
        v-for="msg in messages"
        :key="msg.id"
        :message="msg"
        :is-self="msg.senderId === currentUserId"
        @accept-offer="$emit('offer', msg.id, 1)"
        @reject-offer="$emit('offer', msg.id, 2)"
      />
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <div class="input-toolbar">
        <el-tooltip content="出价">
          <el-button :icon="Money" circle size="small" @click="showOfferDialog = true" />
        </el-tooltip>
      </div>
      <div class="input-row">
        <el-input
          v-model="inputText"
          placeholder="输入消息..."
          :rows="1"
          resize="none"
          @keydown.enter.prevent="sendText"
        />
        <el-button type="primary" @click="sendText" :disabled="!inputText.trim()">
          发送
        </el-button>
      </div>
    </div>

    <!-- 出价弹窗 -->
    <el-dialog v-model="showOfferDialog" title="出价" width="400px">
      <el-form>
        <el-form-item label="期望价格">
          <el-input-number v-model="offerPrice" :min="0" :precision="2" :step="10" />
        </el-form-item>
        <el-form-item label="留言">
          <el-input v-model="offerMessage" type="textarea" placeholder="说点什么..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showOfferDialog = false">取消</el-button>
        <el-button type="primary" @click="sendOffer">发送出价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { Loading, Money } from '@element-plus/icons-vue'
import MessageBubble from './MessageBubble.vue'

const props = defineProps({
  conversationId: { type: Number, required: true },
  conversation: { type: Object, default: null },
})

const emit = defineEmits(['send', 'offer'])

const { $api } = useNuxtApp()
const router = useRouter()

const messages = ref([])
const loading = ref(false)
const inputText = ref('')
const messageListRef = ref(null)
const showOfferDialog = ref(false)
const offerPrice = ref(0)
const offerMessage = ref('')

// 从 token 获取当前用户 ID
const currentUserId = computed(() => {
  const token = useCookie('token')
  if (!token.value) return null
  try {
    const payload = JSON.parse(atob(token.value.split('.')[1]))
    return Number(payload.sub)
  } catch {
    return null
  }
})

const fetchMessages = async () => {
  if (!props.conversationId) return
  loading.value = true
  try {
    const res = await $api.get(`/api/messages/conversation/${props.conversationId}`)
    messages.value = res.data || []
    nextTick(scrollToBottom)
  } catch (e) {
    console.error('Failed to fetch messages:', e)
  } finally {
    loading.value = false
  }
}

const sendText = () => {
  const text = inputText.value.trim()
  if (!text) return
  emit('send', text, 1)
  // 乐观更新
  messages.value.push({
    id: Date.now(),
    senderId: currentUserId.value,
    content: text,
    type: 1,
    createdAt: new Date().toISOString(),
  })
  inputText.value = ''
  nextTick(scrollToBottom)
}

const sendOffer = () => {
  if (offerPrice.value <= 0) return
  const msg = offerMessage.value.trim() || `我出价 ¥${offerPrice.value}`
  emit('send', msg, 3, offerPrice.value)
  messages.value.push({
    id: Date.now(),
    senderId: currentUserId.value,
    content: msg,
    type: 3,
    priceOffer: offerPrice.value,
    offerStatus: 0,
    createdAt: new Date().toISOString(),
  })
  showOfferDialog.value = false
  offerPrice.value = 0
  offerMessage.value = ''
  nextTick(scrollToBottom)
}

const goProduct = () => {
  if (props.conversation?.productId) {
    router.push(`/product/${props.conversation.productId}`)
  }
}

const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

// 添加新消息（供父组件调用）
const addMessage = (msg) => {
  // 避免重复添加
  if (!messages.value.some(m => m.id === msg.id)) {
    messages.value.push(msg)
    nextTick(scrollToBottom)
  }
}

// 暴露方法给父组件
defineExpose({ addMessage })

watch(() => props.conversationId, fetchMessages)

onMounted(fetchMessages)
</script>

<style scoped>
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.chat-header {
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.header-product {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px;
  background: #f5f7fa;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.header-product:hover {
  background: #ecf5ff;
}

.product-title {
  color: #606266;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #f56c6c;
  font-weight: 500;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 16px 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-height: 0;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #909399;
}

.input-area {
  border-top: 1px solid #ebeef5;
  padding: 12px 20px;
  flex-shrink: 0;
}

.input-toolbar {
  margin-bottom: 8px;
}

.input-row {
  display: flex;
  gap: 8px;
}

.input-row .el-input {
  flex: 1;
}
</style>
