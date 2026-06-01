<template>
  <div class="admins-page admin-page" v-loading="loading">
    <div class="page-header">
      <h2>管理员管理</h2>
      <el-button type="primary" @click="handleAdd" plain v-if="userStore.user?.isAdmin">添加管理员</el-button>
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
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
              plain
              :disabled="row.role === 2"
            >编辑</el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row)"
              plain
              :disabled="row.role === 2"
            >删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="480px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="adminForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="账号" prop="username">
          <el-input
            v-model="adminForm.username"
            placeholder="请输入管理员账号"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="adminForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item :label="isEdit ? '新密码' : '密码'" prop="password">
          <el-input
            v-model="adminForm.password"
            type="password"
            :placeholder="isEdit ? '留空则不修改密码' : '请输入密码'"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit" plain>确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminList, createAdmin, updateAdmin, deleteAdmin } from '@/api/admin'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const submitting = ref(false)
const allAdmins = ref([])
const adminList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const isEdit = ref(false)
const formRef = ref(null)
const dialogVisible = ref(false)

const adminForm = reactive({
  id: null,
  username: '',
  name: '',
  password: ''
})

const dialogTitle = computed(() => isEdit.value ? '编辑管理员' : '添加管理员')

const validatePassword = (rule, value, callback) => {
  if (!isEdit.value && !value) {
    callback(new Error('请输入密码'))
  } else if (value && value.length < 6) {
    callback(new Error('密码长度不能少于 6 位'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  password: [
    { validator: validatePassword, trigger: 'blur' }
  ]
}

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
  adminForm.id = null
  adminForm.username = ''
  adminForm.name = ''
  adminForm.password = ''
  isEdit.value = false
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

const handleEdit = (admin) => {
  adminForm.id = admin.id
  adminForm.username = admin.username
  adminForm.name = admin.name
  adminForm.password = ''
  isEdit.value = true
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEdit.value) {
          const data = { id: adminForm.id, name: adminForm.name }
          if (adminForm.password) {
            data.password = adminForm.password
          }
          const res = await updateAdmin(userStore.userId, data)
          if (res.code === 200) {
            ElMessage.success('编辑成功')
            dialogVisible.value = false
            loadAdmins()
          } else {
            ElMessage.error(res.message || '编辑失败')
          }
        } else {
          const data = {
            username: adminForm.username,
            name: adminForm.name,
            password: adminForm.password
          }
          const res = await createAdmin(userStore.userId, data)
          if (res.code === 200) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            loadAdmins()
          } else {
            ElMessage.error(res.message || '添加失败')
          }
        }
      } catch (error) {
        const msg = error.response?.data?.message || error.message
        ElMessage.error(msg || '操作失败，请稍后重试')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleDelete = async (admin) => {
  if (admin.role === 2) {
    ElMessage.warning('超级管理员不可删除')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除管理员「${admin.name}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await deleteAdmin(userStore.userId, admin.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadAdmins()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message
      ElMessage.error(msg || '删除失败，请稍后重试')
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
