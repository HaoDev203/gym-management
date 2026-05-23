<template>
  <div class="orders-page" v-loading="loading">
    <div class="page-header">
      <h2>订单管理</h2>
      <el-button type="primary" @click="loadOrders">刷新</el-button>
    </div>

    <el-table :data="filteredOrders" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="memberName" label="会员" width="100" />
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="coachName" label="教练" width="100" />
      <el-table-column label="金额" width="100">
        <template #default="{ row }">
          ¥{{ row.amount }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="warning">待支付</el-tag>
          <el-tag v-else-if="row.status === 2" type="success">已支付</el-tag>
          <el-tag v-else-if="row.status === 3" type="info">已取消</el-tag>
          <el-tag v-else-if="row.status === 4" type="primary">已完成</el-tag>
          <el-tag v-else-if="row.status === 5" type="danger">已爽约</el-tag>
          <el-tag v-else-if="row.status === 6" type="info">已退款</el-tag>
          <el-tag v-else>未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="签到" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.checkIn === 1" type="success">已签到</el-tag>
          <el-tag v-else type="info">未签到</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="170" />
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 1"
            type="success"
            size="small"
            @click="handleConfirmPayment(row)"
          >
            确认支付
          </el-button>
          <el-button
            v-if="row.status === 2"
            type="primary"
            size="small"
            @click="handleCheckIn(row)"
          >
            签到
          </el-button>
          <el-button
            v-if="row.status === 2"
            type="warning"
            size="small"
            @click="handleCancel(row)"
          >
            取消
          </el-button>
          <el-button
            v-if="row.status === 2"
            type="danger"
            size="small"
            @click="handleNoShow(row)"
          >
            爽约
          </el-button>
          <el-button
            v-if="row.status === 3 || row.status === 5"
            type="danger"
            size="small"
            plain
            @click="handleDelete(row)"
          >
            删除
          </el-button>
          <span v-if="row.status === 4 || row.status === 6" style="color: #909399;">--</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllOrders, confirmPayment, cancelOrder, checkInOrder, markNoShow, deleteOrder } from '@/api/order'

const loading = ref(false)
const orderList = ref([])

const filteredOrders = ref([])

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getAllOrders()
    if (res.code === 200) {
      orderList.value = res.data || []
      filteredOrders.value = orderList.value
    }
  } catch (error) {
    console.error('加载订单列表失败:', error)
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const refreshList = () => {
  filteredOrders.value = orderList.value
}

const handleConfirmPayment = async (order) => {
  try {
    await ElMessageBox.confirm(`确定要确认支付订单 ${order.orderNo} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await confirmPayment(order.id)
    if (res.code === 200) {
      ElMessage.success('确认支付成功')
      loadOrders()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '操作失败'
      ElMessage.error(msg)
    }
  }
}

const handleCheckIn = async (order) => {
  try {
    await ElMessageBox.confirm(`确定要为订单 ${order.orderNo} 签到的吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await checkInOrder(order.id)
    if (res.code === 200) {
      ElMessage.success('签到成功')
      loadOrders()
    } else {
      ElMessage.error(res.message || '签到失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '签到失败'
      ElMessage.error(msg)
    }
  }
}

const handleCancel = async (order) => {
  try {
    await ElMessageBox.confirm(`确定要取消订单 ${order.orderNo} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await cancelOrder(order.id)
    if (res.code === 200) {
      ElMessage.success('取消成功')
      loadOrders()
    } else {
      ElMessage.error(res.message || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '取消失败'
      ElMessage.error(msg)
    }
  }
}

const handleNoShow = async (order) => {
  try {
    await ElMessageBox.confirm(`确定要将订单 ${order.orderNo} 标记为爽约吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await markNoShow(order.id)
    if (res.code === 200) {
      ElMessage.success('已标记为爽约')
      loadOrders()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '操作失败'
      ElMessage.error(msg)
    }
  }
}

const handleDelete = async (order) => {
  try {
    await ElMessageBox.confirm(`确定要永久删除订单 ${order.orderNo} 吗？此操作不可恢复！`, '警告', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'error'
    })

    const res = await deleteOrder(order.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadOrders()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '删除失败'
      ElMessage.error(msg)
    }
  }
}

onMounted(() => {
  loadOrders()
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
