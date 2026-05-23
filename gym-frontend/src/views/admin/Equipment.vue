<template>
  <div class="equipment-page" v-loading="loading">
    <div class="page-header">
      <h2>器材管理</h2>
      <el-button type="primary" @click="handleAdd">添加器材</el-button>
    </div>
    
    <el-table :data="equipmentList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="器材名称" />
      <el-table-column prop="quantity" label="数量" width="100" />
      <el-table-column prop="location" label="位置" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑器材弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="equipmentForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="器材名称" prop="name">
          <el-input v-model="equipmentForm.name" placeholder="请输入器材名称" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="equipmentForm.quantity" :min="1" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="equipmentForm.location" placeholder="请输入器材位置" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEquipmentList, createEquipment, updateEquipment, deleteEquipment } from '@/api/admin'

const loading = ref(false)
const equipmentList = ref([])
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const equipmentForm = reactive({
  id: null,
  name: '',
  quantity: 1,
  location: ''
})

const rules = {
  name: [
    { required: true, message: '请输入器材名称', trigger: 'blur' }
  ],
  quantity: [
    { required: true, message: '请输入器材数量', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入器材位置', trigger: 'blur' }
  ]
}

const dialogTitle = computed(() => isEdit.value ? '编辑器材' : '添加器材')

const loadEquipment = async () => {
  loading.value = true
  try {
    const res = await getEquipmentList()
    if (res.code === 200) {
      equipmentList.value = res.data || []
    }
  } catch (error) {
    console.error('加载器材列表失败:', error)
    ElMessage.error('加载器材列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  equipmentForm.id = null
  equipmentForm.name = ''
  equipmentForm.quantity = 1
  equipmentForm.location = ''
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (equipment) => {
  equipmentForm.id = equipment.id
  equipmentForm.name = equipment.name
  equipmentForm.quantity = equipment.quantity
  equipmentForm.location = equipment.location
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
          name: equipmentForm.name,
          quantity: equipmentForm.quantity,
          location: equipmentForm.location
        }
        
        let res
        if (isEdit.value) {
          res = await updateEquipment(equipmentForm.id, data)
        } else {
          res = await createEquipment(data)
        }
        
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
          dialogVisible.value = false
          loadEquipment()
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

const handleDelete = async (equipment) => {
  try {
    await ElMessageBox.confirm(`确定要删除器材 ${equipment.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteEquipment(equipment.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadEquipment()
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
  loadEquipment()
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
