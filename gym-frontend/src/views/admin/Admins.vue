<template>
  <div class="admins-page admin-page" v-loading="loading">
    <div class="page-header">
      <h2>管理员管理</h2>
      <el-button type="primary" @click="handleAdd" plain v-if="userStore.user?.isAdmin">添加管理员</el-button>
      <el-alert 
        v-else 
        title="权限提示" 
        description="普通管理员无权限操作管理员管理功能" 
        type="warning" 
        :closable="false"
        style="width: auto;"
      />
    </div>
    
    <el-table :data="adminList" style="width: 100%">
      <el-table-column type="index" label="序号" width="60" :index="indexMethod" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column label="角色" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.role === 2" type="danger">超级管理员</el-tag>
          <el-tag v-else type="primary">普通管理员</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" v-if="userStore.user?.isAdmin">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button type="primary" size="small" @click="handleEdit(row)" plain>编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)" plain>删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div v-if="total > 10" class="pagination-container" style="margin-top: 20px; display: flex; justify-content: flex-end;">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminList, deleteAdmin } from '@/api/admin'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const allAdmins = ref([])  // 保存所有管理员数据
const adminList = ref([])  // 当前页的管理员数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadAdmins = async () => {
  loading.value = true
  try {
    const res = await getAdminList()
    if (res.code === 200) {
      allAdmins.value = res.data || []
      total.value = allAdmins.value.length
      updateAdminList()
    }
  } catch (error) {
    console.error('加载管理员列表失败:', error)
    ElMessage.error('加载管理员列表失败')
  } finally {
    loading.value = false
  }
}

const updateAdminList = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  adminList.value = allAdmins.value.slice(start, end)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  updateAdminList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  updateAdminList()
}

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

const handleAdd = () => {
  ElMessage.info('添加管理员功能开发中')
}

const handleEdit = (admin) => {
  ElMessage.info('编辑管理员功能开发中')
}

const handleDelete = async (admin) => {
  try {
    await ElMessageBox.confirm(`确定要删除管理员 ${admin.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const adminId = userStore.userId
    const res = await deleteAdmin(adminId, admin.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadAdmins()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

onMounted(() => {
  loadAdmins()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  color: var(--text-primary);
}
</style>
