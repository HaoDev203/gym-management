<template>
  <div class="orders-page" v-loading="loading">
    <div class="page-header">
      <h2>订单管理</h2>
      <el-button type="primary" @click="loadOrders">刷新</el-button>
    </div>

    <el-table :data="filteredOrders" style="width: 100%">
      <el-table-column type="index" label="序号" width="60" :index="indexMethod" />
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="memberName" label="会员" width="100" />
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="coachName" label="教练" width="100" />
      <el-table-column label="金额" width="220">
        <template #default="{ row }">
          <div style="display: flex; align-items: center; gap: 8px;">
            <span :style="row.paidAmount > 0 ? 'color: #67C23A; font-weight: 500;' : 'color: #909399;'">
              ¥{{ row.amount }}
            </span>
            <el-tag 
              v-if="row.paidAmount > 0" 
              type="success" 
              size="small"
              effect="plain"
            >
              已付
            </el-tag>
            <el-tag 
              v-else-if="row.amount === 0" 
              type="info" 
              size="small"
              effect="plain"
            >
              免费
            </el-tag>
            <el-tag 
              v-else-if="row.status === 1 || row.status === 2" 
              type="warning" 
              size="small"
              effect="plain"
            >
              未付
            </el-tag>
            <!-- 已预约状态且有金额的订单显示修改支付按钮 -->
            <el-dropdown v-if="row.status === 2 && row.amount > 0 && userStore.user?.isAdmin" trigger="click" size="small">
              <el-button circle size="small" type="info" plain>
                <el-icon><edit /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleMarkAsPaid(row)" :disabled="row.paidAmount > 0">
                    <el-icon><circle-check /></el-icon> 标记为已付
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleMarkAsUnpaid(row)" :disabled="row.paidAmount === 0">
                    <el-icon><circle-close /></el-icon> 标记为未付
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="warning" effect="dark">待支付</el-tag>
          <el-tag v-else-if="row.status === 2" type="success" effect="dark">已预约</el-tag>
          <el-tag v-else-if="row.status === 3" type="info">已取消</el-tag>
          <el-tag v-else-if="row.status === 4" type="success" effect="dark">已完成</el-tag>
          <el-tag v-else-if="row.status === 5" type="danger" effect="dark">已爽约</el-tag>
          <el-tag v-else-if="row.status === 6" type="info">已退款</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="签到" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.checkIn === 1" type="success">已签到</el-tag>
          <el-tag v-else type="info">未签到</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right" v-if="userStore.user?.isAdmin">
        <template #default="{ row }">
          <!-- 待支付状态：确认支付 -->
          <el-button
            v-if="row.status === 1"
            type="success"
            size="small"
            @click="handleConfirmPayment(row)"
            plain
          >
            确认支付
          </el-button>
          
          <!-- 已预约状态：签到、取消、爽约 -->
          <div v-if="row.status === 2" style="display: flex; gap: 6px;">
            <el-button
              type="primary"
              size="small"
              @click="handleCheckIn(row)"
              plain
            >
              签到
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleCancel(row)"
              plain
            >
              取消
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleNoShow(row)"
              plain
            >
              爽约
            </el-button>
          </div>
          
          <!-- 其他状态：显示状态说明，不提供操作按钮 -->
          <span v-if="row.status === 3" style="color: #909399; font-size: 12px;">
            已取消
          </span>
          <span v-if="row.status === 4" style="color: #909399; font-size: 12px;">
            已完成
          </span>
          <span v-if="row.status === 5" style="color: #909399; font-size: 12px;">
            已爽约
          </span>
          <span v-if="row.status === 6" style="color: #909399; font-size: 12px;">
            已退款
          </span>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div class="pagination-container" style="margin-top: 20px; display: flex; justify-content: flex-end;">
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
import { User, Clock, Timer, WarningFilled, CircleCloseFilled, Delete, ArrowDown, CircleCheck, CircleClose, Edit } from '@element-plus/icons-vue'
import { getAllOrders, confirmPayment, cancelOrder, checkInOrder, adminCheckInOrder, markNoShow, markAsPaid, markAsUnpaid } from '@/api/order'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const loading = ref(false)
const allOrders = ref([])  // 保存所有订单数据
const filteredOrders = ref([])  // 当前页的订单数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getAllOrders()
    if (res.code === 200) {
      allOrders.value = res.data || []
      total.value = allOrders.value.length
      updateOrderList()
    }
  } catch (error) {
    console.error('加载订单列表失败:', error)
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const updateOrderList = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  filteredOrders.value = allOrders.value.slice(start, end)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  updateOrderList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  updateOrderList()
}

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

const refreshList = () => {
  filteredOrders.value = allOrders.value
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

    const res = await adminCheckInOrder(order.id)
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

const handleMarkAsPaid = async (order) => {
  try {
    await ElMessageBox.confirm(`确定将订单 ${order.orderNo} 标记为已支付吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('标记为已支付 - 订单 ID:', order.id)
    const res = await markAsPaid(order.id)
    console.log('标记为已支付 - 响应:', res)
    if (res.code === 200) {
      ElMessage.success('已标记为已支付')
      loadOrders()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('标记为已支付 - 错误:', error)
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '操作失败'
      ElMessage.error(msg)
    }
  }
}

const handleMarkAsUnpaid = async (order) => {
  try {
    await ElMessageBox.confirm(`确定将订单 ${order.orderNo} 标记为未支付吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('标记为未支付 - 订单 ID:', order.id)
    const res = await markAsUnpaid(order.id)
    console.log('标记为未支付 - 响应:', res)
    if (res.code === 200) {
      ElMessage.success('已标记为未支付')
      loadOrders()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('标记为未支付 - 错误:', error)
    if (error !== 'cancel') {
      const msg = error.response?.data?.message || error.message || '操作失败'
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

:deep(.el-button--small) {
  padding: 4px 8px;
  font-size: 12px;
}

:deep(.el-button--primary.is-plain) {
  background-color: #ecf5ff;
  border-color: #b3d8ff;
  color: #409eff;
}

:deep(.el-button--primary.is-plain:hover) {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
}

:deep(.el-button--warning.is-plain) {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}

:deep(.el-button--warning.is-plain:hover) {
  background-color: #e6a23c;
  border-color: #e6a23c;
  color: #fff;
}

:deep(.el-button--danger.is-plain) {
  background-color: #fef0f0;
  border-color: #fde2e2;
  color: #f56c6c;
}

:deep(.el-button--danger.is-plain:hover) {
  background-color: #f56c6c;
  border-color: #f56c6c;
  color: #fff;
}

:deep(.el-button--success.is-plain) {
  background-color: #f0f9eb;
  border-color: #e1f3d8;
  color: #67c23a;
}

:deep(.el-button--success.is-plain:hover) {
  background-color: #67c23a;
  border-color: #67c23a;
  color: #fff;
}
</style>
