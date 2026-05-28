<template>
  <div class="image-carousel">
    <div class="main-image">
      <img v-if="images.length" :src="resolveUrl(images[currentIndex])" class="preview" />
      <div v-else class="placeholder">
        <el-icon size="48" color="#c0c4cc"><Picture /></el-icon>
      </div>
      <div v-if="images.length > 1" class="nav-arrows">
        <el-icon class="arrow left" @click="prev"><ArrowLeft /></el-icon>
        <el-icon class="arrow right" @click="next"><ArrowRight /></el-icon>
      </div>
      <div v-if="images.length" class="counter">{{ currentIndex + 1 }} / {{ images.length }}</div>
    </div>
    <div v-if="images.length > 1" class="thumb-list">
      <div
        v-for="(img, i) in images"
        :key="i"
        class="thumb"
        :class="{ active: i === currentIndex }"
        @click="currentIndex = i"
      >
        <img :src="resolveUrl(img)" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { Picture, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

const props = defineProps({
  images: { type: Array, default: () => [] },
})

const config = useRuntimeConfig()
const currentIndex = ref(0)

const resolveUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${config.public.apiBase}${url}`
}

const prev = () => {
  currentIndex.value = currentIndex.value > 0 ? currentIndex.value - 1 : props.images.length - 1
}

const next = () => {
  currentIndex.value = currentIndex.value < props.images.length - 1 ? currentIndex.value + 1 : 0
}

watch(() => props.images, () => { currentIndex.value = 0 })
</script>

<style scoped>
.image-carousel {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.main-image {
  position: relative;
  width: 100%;
  aspect-ratio: 1 / 1;
  background-color: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-arrows {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 8px;
  pointer-events: none;
}

.arrow {
  width: 32px;
  height: 32px;
  background: rgba(0, 0, 0, 0.4);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  pointer-events: auto;
  transition: background 0.2s;
}

.arrow:hover {
  background: rgba(0, 0, 0, 0.6);
}

.counter {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.thumb-list {
  display: flex;
  gap: 8px;
  overflow-x: auto;
}

.thumb {
  width: 64px;
  height: 64px;
  border-radius: 4px;
  overflow: hidden;
  border: 2px solid transparent;
  cursor: pointer;
  flex-shrink: 0;
  transition: border-color 0.2s;
}

.thumb.active {
  border-color: #409eff;
}

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
