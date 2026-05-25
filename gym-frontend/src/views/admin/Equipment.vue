<template>
  <div class="equipment-page admin-page" v-loading="loading">
    <div class="page-header">
      <h2>器材管理</h2>
      <el-button type="primary" @click="handleAdd" plain>添加器材</el-button>
    </div>
    
    <el-table :data="equipmentList" style="width: 100%">
      <el-table-column type="index" label="序号" width="60" :index="indexMethod" />
      <el-table-column prop="name" label="器材名称" />
      <el-table-column prop="quantity" label="数量" width="100" />
      <el-table-column prop="location" label="位置" />
      <el-table-column label="操作" width="160">
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
        <el-button type="primary" :loading="submitting" @click="handleSubmit" plain>
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
const allEquipment = ref([])  // 保存所有器材数据
const equipmentList = ref([]) // 当前页的器材数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
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
      allEquipment.value = res.data || []
      total.value = allEquipment.value.length
      updateEquipmentList()
    }
  } catch (error) {
    console.error('加载器材列表失败:', error)
    ElMessage.error('加载器材列表失败')
  } finally {
    loading.value = false
  }
}

const updateEquipmentList = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  equipmentList.value = allEquipment.value.slice(start, end)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  updateEquipmentList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  updateEquipmentList()
}

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
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
