<template>
  <div class="product-image-upload">
    <div class="image-list">
      <div
        v-for="(url, idx) in modelValue"
        :key="url + idx"
        class="image-item"
      >
        <img :src="resolveUrl(url)" class="thumb" />
        <div v-if="idx === 0" class="cover-badge">封面</div>
        <div class="image-mask">
          <el-icon v-if="idx > 0" class="action-icon" @click="moveUp(idx)"><Top /></el-icon>
          <el-icon class="action-icon" @click="remove(idx)"><Delete /></el-icon>
        </div>
      </div>

      <el-upload
        v-if="modelValue.length < max"
        :action="uploadUrl"
        :headers="headers"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError"
        :multiple="true"
        name="files"
        accept="image/jpeg,image/png,image/webp"
        class="upload-tile"
      >
        <div class="upload-placeholder">
          <el-icon size="24" color="#909399"><Plus /></el-icon>
          <div class="upload-text">添加图片</div>
          <div class="upload-hint">{{ modelValue.length }} / {{ max }}</div>
        </div>
      </el-upload>
    </div>
    <div class="upload-tips">第一张图为封面，建议尺寸 1:1，单张不超过 5MB</div>
  </div>
</template>

<script setup>
import { Plus, Delete, Top } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: { type: Array, default: () => [] },
  max: { type: Number, default: 9 },
})

const emit = defineEmits(['update:modelValue'])

const config = useRuntimeConfig()
const token = useCookie('token')

const uploadUrl = computed(() => `${config.public.apiBase}/api/products/images`)
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
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  if (props.modelValue.length >= props.max) {
    ElMessage.error(`最多上传 ${props.max} 张图片`)
    return false
  }
  return true
}

const handleSuccess = (res) => {
  if (res && res.code === 200 && Array.isArray(res.data?.urls)) {
    const merged = [...props.modelValue, ...res.data.urls].slice(0, props.max)
    emit('update:modelValue', merged)
  } else {
    ElMessage.error(res?.message || '上传失败')
  }
}

const handleError = () => {
  ElMessage.error('上传失败，请重试')
}

const remove = (idx) => {
  const next = props.modelValue.slice()
  next.splice(idx, 1)
  emit('update:modelValue', next)
}

const moveUp = (idx) => {
  if (idx <= 0) return
  const next = props.modelValue.slice()
  ;[next[idx - 1], next[idx]] = [next[idx], next[idx - 1]]
  emit('update:modelValue', next)
}
</script>

<style scoped>
.image-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
}

.image-item, .upload-tile :deep(.el-upload), .upload-placeholder {
  width: 100%;
  aspect-ratio: 1 / 1;
}

.image-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f5f7fa;
}

.thumb {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.cover-badge {
  position: absolute;
  left: 6px;
  top: 6px;
  background-color: rgba(64, 158, 255, 0.9);
  color: #fff;
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
}

.image-mask {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  opacity: 0;
  transition: opacity 0.2s;
}

.image-item:hover .image-mask {
  opacity: 1;
}

.action-icon {
  font-size: 20px;
  color: #fff;
  cursor: pointer;
}

.upload-placeholder {
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  background-color: #fafafa;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.upload-placeholder:hover {
  border-color: #409eff;
}

.upload-text {
  font-size: 13px;
  color: #606266;
}

.upload-hint {
  font-size: 12px;
  color: #909399;
}

.upload-tips {
  margin-top: 12px;
  font-size: 12px;
  color: #909399;
}
</style>
