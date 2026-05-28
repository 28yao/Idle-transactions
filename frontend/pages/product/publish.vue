<template>
  <div class="publish-page">
    <div class="publish-card">
      <h2 class="card-title">{{ isEdit ? '编辑商品' : '发布商品' }}</h2>
      <p class="card-subtitle">填写商品信息，第一张图片将作为封面</p>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
      >
        <el-form-item label="商品标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入商品标题，最多50字"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>

        <div class="form-row">
          <el-form-item label="商品分类" prop="category" class="form-col">
            <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
              <el-option label="书籍教材" value="书籍教材" />
              <el-option label="电子数码" value="电子数码" />
              <el-option label="生活用品" value="生活用品" />
              <el-option label="服饰美妆" value="服饰美妆" />
            </el-select>
          </el-form-item>

          <el-form-item label="成色" prop="condition" class="form-col">
            <el-select v-model="form.condition" placeholder="请选择成色" style="width: 100%">
              <el-option label="全新" value="全新" />
              <el-option label="几乎全新" value="几乎全新" />
              <el-option label="轻微使用痕迹" value="轻微使用痕迹" />
              <el-option label="明显使用痕迹" value="明显使用痕迹" />
            </el-select>
          </el-form-item>
        </div>

        <div class="form-row">
          <el-form-item label="转让价格 (¥)" prop="price" class="form-col">
            <el-input-number
              v-model="form.price"
              :min="0.01"
              :precision="2"
              :step="1"
              style="width: 100%"
              controls-position="right"
            />
          </el-form-item>

          <el-form-item label="原价 (¥，选填)" prop="originalPrice" class="form-col">
            <el-input-number
              v-model="form.originalPrice"
              :min="0"
              :precision="2"
              :step="1"
              style="width: 100%"
              controls-position="right"
            />
          </el-form-item>
        </div>

        <el-form-item label="交易地点" prop="location">
          <el-input v-model="form.location" placeholder="例如：东校区图书馆门口" />
        </el-form-item>

        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="详细描述商品的成色、使用情况、附件等"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="商品图片（最多9张，可不传）" prop="images">
          <ProductImageUpload v-model="form.images" />
        </el-form-item>

        <div class="form-actions">
          <el-button @click="handleCancel">取消</el-button>
          <el-button :loading="loading && submitStatus === 0" @click="handleSubmit(0)">
            存为草稿
          </el-button>
          <el-button
            type="primary"
            :loading="loading && submitStatus === 1"
            @click="handleSubmit(1)"
          >
            立即发布
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import ProductImageUpload from '~/components/product/ProductImageUpload.vue'

definePageMeta({ middleware: 'auth' })

const route = useRoute()
const router = useRouter()
const { $api } = useNuxtApp()

const editId = computed(() => route.query.edit ? Number(route.query.edit) : null)
const isEdit = computed(() => !!editId.value)

const formRef = ref()
const loading = ref(false)
const submitStatus = ref(null)

const form = reactive({
  title: '',
  category: '',
  condition: '',
  price: null,
  originalPrice: null,
  location: '',
  description: '',
  images: [],
})

const rules = {
  title: [
    { required: true, message: '请输入商品标题', trigger: 'blur' },
    { max: 50, message: '标题不能超过50个字', trigger: 'blur' },
  ],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  condition: [{ required: true, message: '请选择成色', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  location: [{ required: true, message: '请填写交易地点', trigger: 'blur' }],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' },
    { max: 1000, message: '描述不能超过1000个字', trigger: 'blur' },
  ],
}

// 编辑模式：加载已有数据
const loadProduct = async (id) => {
  try {
    const res = await $api.get(`/api/products/${id}`)
    const p = res.data
    form.title = p.title || ''
    form.category = p.category || ''
    form.condition = p.condition || ''
    form.price = p.price || null
    form.originalPrice = p.originalPrice || null
    form.location = p.location || ''
    form.description = p.description || ''
    form.images = Array.isArray(p.images) ? p.images : []
  } catch (error) {
    ElMessage.error(error?.message || '加载商品失败')
    router.replace('/')
  }
}

onMounted(() => {
  if (editId.value) loadProduct(editId.value)
})

const handleSubmit = async (status) => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  submitStatus.value = status
  try {
    const payload = { ...form, status }
    let res
    if (isEdit.value) {
      res = await $api.put(`/api/products/${editId.value}`, payload)
    } else {
      res = await $api.post('/api/products', payload)
    }
    ElMessage.success(status === 1 ? '发布成功' : '已存为草稿')
    const productId = res.data?.id
    if (status === 1 && productId) {
      navigateTo(`/product/${productId}`)
    } else {
      navigateTo('/user/listings')
    }
  } catch (error) {
    ElMessage.error(error?.message || '操作失败')
  } finally {
    loading.value = false
    submitStatus.value = null
  }
}

const handleCancel = () => {
  router.back()
}
</script>

<style scoped>
.publish-page {
  padding: 24px 32px;
  display: flex;
  justify-content: center;
}

.publish-card {
  width: 100%;
  max-width: 800px;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 12px;
  padding: 40px;
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.card-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0 0 24px 0;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-col {
  margin-bottom: 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

:deep(.el-form-item__label) {
  font-size: 13px;
  font-weight: 500;
  color: #303133;
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
  border-radius: 8px;
}
</style>
