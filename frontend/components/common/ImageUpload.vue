<template>
  <div class="image-upload">
    <el-upload
      :action="uploadUrl"
      :headers="headers"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      name="file"
      accept="image/jpeg,image/png,image/webp"
    >
      <div v-if="modelValue" class="preview-wrapper">
        <img :src="resolveUrl(modelValue)" class="preview-image" />
        <div class="preview-mask">
          <el-icon size="24"><EditPen /></el-icon>
          <span>点击更换</span>
        </div>
      </div>
      <div v-else class="upload-placeholder">
        <el-icon size="32" color="#909399"><Plus /></el-icon>
        <div class="upload-text">{{ placeholder }}</div>
        <div class="upload-hint">支持 JPG / PNG / WebP，不超过 5MB</div>
      </div>
    </el-upload>
  </div>
</template>

<script setup>
import { Plus, EditPen } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  uploadPath: { type: String, default: '/api/users/verify/image' },
  placeholder: { type: String, default: '点击上传图片' },
  maxSize: { type: Number, default: 5 },
})

const emit = defineEmits(['update:modelValue'])

const config = useRuntimeConfig()
const token = useCookie('token')

const uploadUrl = computed(() => `${config.public.apiBase}${props.uploadPath}`)
const headers = computed(() => ({ Authorization: token.value ? `Bearer ${token.value}` : '' }))

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}

const beforeUpload = (file) => {
  const allowed = ['image/jpeg', 'image/png', 'image/webp']
  if (!allowed.includes(file.type)) {
    ElMessage.error('仅支持 JPG / PNG / WebP 格式')
    return false
  }
  if (file.size > props.maxSize * 1024 * 1024) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}MB`)
    return false
  }
  return true
}

const handleSuccess = (res) => {
  if (res && res.code === 200 && res.data?.url) {
    emit('update:modelValue', res.data.url)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(res?.message || '上传失败')
  }
}

const handleError = () => {
  ElMessage.error('上传失败，请重试')
}
</script>

<style scoped>
.image-upload :deep(.el-upload) {
  width: 100%;
}

.preview-wrapper {
  position: relative;
  width: 240px;
  height: 240px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-mask {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 13px;
  opacity: 0;
  transition: opacity 0.2s;
}

.preview-wrapper:hover .preview-mask {
  opacity: 1;
}

.upload-placeholder {
  width: 240px;
  height: 240px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  background-color: #fafafa;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.upload-placeholder:hover {
  border-color: #409eff;
}

.upload-text {
  font-size: 14px;
  color: #606266;
}

.upload-hint {
  font-size: 12px;
  color: #909399;
}
</style>
