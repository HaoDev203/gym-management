<template>
  <div class="courses-manage-page admin-page" v-loading="loading">
    <div class="page-header">
      <h2>课程管理</h2>
      <el-button type="primary" @click="handleAdd" plain>添加课程</el-button>
    </div>
    
    <el-table :data="courseList" style="width: 100%">
      <el-table-column type="index" label="序号" width="60" :index="indexMethod" />
      <el-table-column prop="name" label="课程名称" />
      <el-table-column prop="coachName" label="教练" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'primary' : 'success'">
            {{ row.type === 1 ? '团课' : '私教课' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="danger">已取消</el-tag>
          <el-tag v-else type="success">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预约人数" width="100">
        <template #default="{ row }">
          {{ row.bookedCount }}/{{ row.capacity }}
        </template>
      </el-table-column>
      <el-table-column label="价格" width="100">
        <template #default="{ row }">
          ¥{{ row.price }}
        </template>
      </el-table-column>
      <el-table-column label="时间" width="200">
        <template #default="{ row }">
          {{ formatDateTime(row.startTime) }}<br>至 {{ formatDateTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button type="primary" size="small" @click="handleEdit(row)" plain>编辑</el-button>
            <el-button
              v-if="row.status === 1"
              type="warning"
              size="small"
              @click="handleCancel(row)"
              plain
            >
              取消
            </el-button>
            <el-button
              v-if="row.status === 0"
              type="success"
              size="small"
              @click="handleRestore(row)"
              plain
            >
              恢复
            </el-button>
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

    <!-- 添加/编辑课程弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="courseForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="courseForm.name" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="教练" prop="coachId">
          <el-select v-model="courseForm.coachId" placeholder="请选择教练" style="width: 100%">
            <el-option
              v-for="coach in coachList"
              :key="coach.id"
              :label="coach.name"
              :value="coach.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程类型" prop="type">
          <el-radio-group v-model="courseForm.type">
            <el-radio :label="1">团课</el-radio>
            <el-radio :label="0">私教课</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="courseForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="courseForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="课程容量" prop="capacity">
          <el-input-number v-model="courseForm.capacity" :min="1" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="课程价格" prop="price">
          <el-input-number v-model="courseForm.price" :min="0" :precision="2" style="width: 100%" />
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
import { getAllCourses, createCourse, updateCourse, deleteCourse, cancelCourse, restoreCourse } from '@/api/course'
import { getCoachList } from '@/api/admin'

const loading = ref(false)
const allCourses = ref([])  // 保存所有课程数据
const courseList = ref([])  // 当前页的课程数据
const coachList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const courseForm = reactive({
  id: null,
  name: '',
  coachId: null,
  type: 1,
  startTime: '',
  endTime: '',
  capacity: 10,
  price: 0
})

const rules = {
  name: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  coachId: [
    { required: true, message: '请选择教练', trigger: 'change' }
  ],
  type: [
    { required: true, message: '请选择课程类型', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  capacity: [
    { required: true, message: '请输入课程容量', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入课程价格', trigger: 'blur' }
  ]
}

const dialogTitle = computed(() => isEdit.value ? '编辑课程' : '添加课程')

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', { 
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadCoaches = async () => {
  try {
    const res = await getCoachList()
    if (res.code === 200) {
      coachList.value = res.data || []
    }
  } catch (error) {
    console.error('加载教练列表失败:', error)
  }
}

const loadCourses = async () => {
  loading.value = true
  try {
    const res = await getAllCourses()
    if (res.code === 200) {
      allCourses.value = res.data || []
      total.value = allCourses.value.length
      updateCourseList()
    }
  } catch (error) {
    console.error('加载课程列表失败:', error)
    ElMessage.error('加载课程列表失败')
  } finally {
    loading.value = false
  }
}

const updateCourseList = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  courseList.value = allCourses.value.slice(start, end)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  updateCourseList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  updateCourseList()
}

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

const handleAdd = () => {
  courseForm.id = null
  courseForm.name = ''
  courseForm.coachId = null
  courseForm.type = 1
  courseForm.startTime = ''
  courseForm.endTime = ''
  courseForm.capacity = 10
  courseForm.price = 0
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (course) => {
  courseForm.id = course.id
  courseForm.name = course.name
  courseForm.coachId = course.coachId
  courseForm.type = course.type
  courseForm.startTime = course.startTime
  courseForm.endTime = course.endTime
  courseForm.capacity = course.capacity
  courseForm.price = parseFloat(course.price) || 0
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
          name: courseForm.name,
          coachId: courseForm.coachId,
          type: courseForm.type,
          startTime: courseForm.startTime,
          endTime: courseForm.endTime,
          capacity: courseForm.capacity,
          price: courseForm.price
        }
        
        let res
        if (isEdit.value) {
          res = await updateCourse(courseForm.id, data)
        } else {
          res = await createCourse(data)
        }
        
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
          dialogVisible.value = false
          loadCourses()
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

const handleCancel = async (course) => {
  try {
    await ElMessageBox.confirm(`确定要取消课程 ${course.name} 吗？取消后可以恢复。`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await cancelCourse(course.id)
    if (res.code === 200) {
      ElMessage.success('取消成功')
      loadCourses()
    } else {
      ElMessage.error(res.message || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
      ElMessage.error('取消失败，请稍后重试')
    }
  }
}

const handleRestore = async (course) => {
  try {
    const res = await restoreCourse(course.id)
    if (res.code === 200) {
      ElMessage.success('恢复成功')
      loadCourses()
    } else {
      ElMessage.error(res.message || '恢复失败')
    }
  } catch (error) {
    console.error('恢复失败:', error)
    ElMessage.error('恢复失败，请稍后重试')
  }
}

const handleDelete = async (course) => {
  try {
    await ElMessageBox.confirm(`确定要删除课程 ${course.name} 吗？删除后无法恢复！`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })
    
    const res = await deleteCourse(course.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadCourses()
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
  loadCourses()
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
