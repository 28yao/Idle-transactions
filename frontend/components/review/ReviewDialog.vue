<template>
  <el-dialog v-model="visible" title="评价交易" width="500px" @close="handleClose">
    <el-form v-if="!submitted" :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="评分" prop="rating">
        <el-rate v-model="form.rating" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" show-text />
      </el-form-item>

      <el-form-item label="评价内容">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="3"
          placeholder="请输入评价内容（选填）"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="评价标签">
        <div class="tag-list">
          <el-check-tag
            v-for="tag in tagOptions"
            :key="tag"
            :checked="selectedTags.includes(tag)"
            @change="toggleTag(tag)"
          >
            {{ tag }}
          </el-check-tag>
        </div>
      </el-form-item>
    </el-form>

    <div v-else class="success-message">
      <el-icon :size="48" color="#67C23A"><CircleCheck /></el-icon>
      <p>评价提交成功！</p>
    </div>

    <template #footer>
      <template v-if="!submitted">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">提交评价</el-button>
      </template>
      <template v-else>
        <el-button type="primary" @click="visible = false">完成</el-button>
      </template>
    </template>
  </el-dialog>
</template>

<script setup>
import { CircleCheck } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  transactionId: { type: Number, required: true },
})

const emit = defineEmits(['submitted'])

const { $api } = useNuxtApp()
const visible = ref(true)
const loading = ref(false)
const submitted = ref(false)
const formRef = ref(null)

const form = ref({
  rating: null,
  content: '',
})

const selectedTags = ref([])

const tagOptions = ['描述准确', '沟通顺畅', '交易守时', '态度友好', '物品完好']

const rules = {
  rating: [{ required: true, message: '请选择评分', trigger: 'change' }],
}

const toggleTag = (tag) => {
  const index = selectedTags.value.indexOf(tag)
  if (index === -1) {
    selectedTags.value.push(tag)
  } else {
    selectedTags.value.splice(index, 1)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      await $api.post('/api/reviews', {
        transactionId: props.transactionId,
        rating: form.value.rating,
        content: form.value.content,
        tags: selectedTags.value.length > 0 ? JSON.stringify(selectedTags.value) : null,
      })
      submitted.value = true
      emit('submitted')
    } catch (e) {
      ElMessage.error(e.message || '评价失败')
    } finally {
      loading.value = false
    }
  })
}

const handleClose = () => {
  if (submitted.value) {
    emit('submitted')
  }
}
</script>

<style scoped>
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.success-message {
  text-align: center;
  padding: 20px;
}

.success-message p {
  margin-top: 12px;
  font-size: 16px;
  color: #67C23A;
}
</style>
