<template>
  <div class="users-page">
    <h2>用户管理</h2>

    <el-table :data="users" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="campus" label="校区" width="120" />
      <el-table-column label="认证状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.verifyStatus === 2" type="success" size="small">已认证</el-tag>
          <el-tag v-else-if="row.verifyStatus === 1" type="warning" size="small">审核中</el-tag>
          <el-tag v-else size="small">未认证</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.role === 1" type="primary" size="small">管理员</el-tag>
          <el-tag v-else-if="row.status === 0" type="danger" size="small">已封禁</el-tag>
          <el-tag v-else type="success" size="small">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <template v-if="row.role !== 1">
            <el-button v-if="row.status !== 0" size="small" type="danger" @click="updateStatus(row.id, 0)">封禁</el-button>
            <el-button v-else size="small" type="success" @click="updateStatus(row.id, 1)">解封</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        :page-size="20"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="fetchUsers"
      />
    </div>
  </div>
</template>

<script setup>
definePageMeta({ layout: 'admin', middleware: 'admin' })

const { $api } = useNuxtApp()

const users = ref([])
const page = ref(1)
const total = ref(0)

const fetchUsers = async () => {
  try {
    const res = await $api.get('/api/admin/users', { params: { page: page.value, size: 20 } })
    users.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {}
}

const updateStatus = async (id, status) => {
  try {
    await $api.put(`/api/admin/users/${id}/status`, { status })
    fetchUsers()
  } catch (e) {}
}

onMounted(fetchUsers)
</script>

<style scoped>
.users-page h2 {
  margin: 0 0 20px;
  font-size: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
