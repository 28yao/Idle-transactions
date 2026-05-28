<template>
  <el-dialog v-model="visible" title="举报" width="480px" @close="reset">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="举报类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio :value="1">虚假信息</el-radio>
          <el-radio :value="2">违禁物品</el-radio>
          <el-radio :value="3">欺诈</el-radio>
          <el-radio :value="4">其他</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="举报原因" prop="reason">
        <el-input
          v-model="form.reason"
          type="textarea"
          :rows="4"
          placeholder="请详细描述举报原因（5-500字）"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="submit">提交举报</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const props = defineProps({
  targetType: { type: Number, required: true },
  targetId: { type: Number, required: true },
})

const emit = defineEmits(['success'])

const { $api } = useNuxtApp()
const visible = ref(false)
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  type: null,
  reason: '',
})

const rules = {
  type: [{ required: true, message: '请选择举报类型', trigger: 'change' }],
  reason: [
    { required: true, message: '请填写举报原因', trigger: 'blur' },
    { min: 5, max: 500, message: '长度为5-500字', trigger: 'blur' },
  ],
}

const open = () => {
  visible.value = true
}

const reset = () => {
  form.type = null
  form.reason = ''
}

const submit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await $api.post('/api/reports', {
      targetType: props.targetType,
      targetId: props.targetId,
      type: form.type,
      reason: form.reason,
    })
    ElMessage.success('举报成功，我们将尽快处理')
    visible.value = false
    emit('success')
  } catch (e) {
    ElMessage.error(e.message || '举报失败')
  } finally {
    loading.value = false
  }
}

defineExpose({ open })
</script>
