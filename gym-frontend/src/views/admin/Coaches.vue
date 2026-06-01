<template>
  <div class="coaches-page admin-page" v-loading="loading">
    <div class="page-header">
      <h2>教练管理</h2>
      <el-button type="primary" @click="handleAdd" plain v-if="userStore.user?.isAdmin">添加教练</el-button>
    </div>
    
    <el-table :data="coachList" style="width: 100%">
      <el-table-column type="index" label="序号" width="60" :index="indexMethod" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column label="性别" width="80">
        <template #default="{ row }">
          {{ row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="email" label="邮箱" />
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

    <!-- 添加/编辑教练弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="coachForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="coachForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="coachForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="coachForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="coachForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="简介" prop="expertise">
          <el-input
            v-model="coachForm.expertise"
            type="textarea"
            :rows="3"
            placeholder="请输入教练简介"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit" plain>
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCoachList, createCoach, updateCoach, deleteCoach } from '@/api/admin'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const loading = ref(false)
const allCoaches = ref([])  // 保存所有教练数据
const coachList = ref([])   // 当前页的教练数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const dialogVisible = ref(false)

const coachForm = reactive({
  id: null,
  name: '',
  gender: 1,
  phone: '',
  email: '',
  expertise: ''
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const dialogTitle = computed(() => isEdit.value ? '编辑教练' : '添加教练')

const loadCoaches = async () => {
  loading.value = true
  try {
    const res = await getCoachList()
    if (res.code === 200) {
      allCoaches.value = res.data || []
      total.value = allCoaches.value.length
      updateCoachList()
      await nextTick()
    }
  } catch (error) {
    console.error('加载教练列表失败:', error)
    ElMessage.error('加载教练列表失败')
  } finally {
    loading.value = false
  }
}

const updateCoachList = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  coachList.value = allCoaches.value.slice(start, end)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  updateCoachList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  updateCoachList()
}

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

const handleAdd = () => {
  // 重置表单
  coachForm.id = null
  coachForm.name = ''
  coachForm.gender = 1
  coachForm.phone = ''
  coachForm.email = ''
  coachForm.expertise = ''
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (coach) => {
  // 填充表单
  coachForm.id = coach.id
  coachForm.name = coach.name
  coachForm.gender = coach.gender
  coachForm.phone = coach.phone
  coachForm.email = coach.email
  coachForm.expertise = coach.expertise || ''
  isEdit.value = true
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const data = {
          name: coachForm.name,
          gender: coachForm.gender,
          phone: coachForm.phone,
          email: coachForm.email,
          expertise: coachForm.expertise
        }
        
        let res
        if (isEdit.value) {
          res = await updateCoach(coachForm.id, data)
        } else {
          res = await createCoach(data)
        }
        
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
          dialogVisible.value = false
          loadCoaches()
        } else {
          ElMessage.error(res.message || (isEdit.value ? '修改失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(error.response?.data?.message || (isEdit.value ? '修改失败' : '添加失败'))
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleDelete = async (coach) => {
  try {
    await ElMessageBox.confirm(`确定要删除教练 ${coach.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteCoach(coach.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadCoaches()
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
  loadCoaches()
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
