<template>
  <div class="orders-page" v-loading="loading">
    <div class="page-hero">
      <div class="hero-left">
        <h2>我的订单</h2>
        <p>查看课程支付与消费记录</p>
      </div>
      <div class="hero-total">
        <span class="total-label">消费合计</span>
        <span class="total-amount">¥{{ totalAmount }}</span>
      </div>
    </div>

    <div v-if="!loading && orderList.length === 0" class="empty-state">
      <el-empty description="暂无订单记录" />
    </div>

    <div v-else class="order-list">
      <div v-for="order in orderList" :key="order.id" class="order-card">
        <div class="order-icon-wrap">
          <div class="order-icon" :class="iconClass(order.status)">
            <el-icon :size="20"><component :is="iconName(order.status)" /></el-icon>
          </div>
        </div>
        <div class="order-body">
          <div class="order-top">
              <h4>{{ order.courseName }}</h4>
              <div class="order-amounts">
                <span class="order-price">¥{{ displayPaidAmount(order) }}</span>
                <span v-if="order.paidAmount && order.paidAmount !== order.amount" class="order-original">
                  <del>¥{{ order.amount }}</del>
                </span>
              </div>
            </div>
          <div class="order-bottom">
            <el-tag :type="statusTagType(order.status)" effect="plain" round size="small">
              {{ statusLabel(order.status) }}
            </el-tag>
            <span class="order-time">{{ formatDateTime(order.createTime) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Wallet, Check, CircleClose, Finished, CircleCheck } from '@element-plus/icons-vue'
import { getMemberOrders } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { formatDateTime } from '@/utils/date'

const userStore = useUserStore()
const loading = ref(false)
const orderList = ref([])

const totalAmount = computed(() =>
  orderList.value.reduce((sum, o) => sum + displayPaidAmountValue(o), 0).toFixed(0)
)

const displayPaidAmountValue = (o) => o.paidAmount || o.amount || 0
const displayPaidAmount = (o) => {
  const val = o.paidAmount || o.amount || 0
  return Number(val).toFixed(0)
}

const statusMap = { 1: '待支付', 2: '已预约', 3: '已取消', 4: '已完成', 5: '已爽约' }
const statusTagMap = { 1: 'warning', 2: 'success', 3: 'info', 4: '', 5: 'danger' }

const statusLabel = (s) => statusMap[s] || '未知'
const statusTagType = (s) => statusTagMap[s] || 'info'

const iconClass = (s) => {
  const map = { 1: 'icon-pending', 2: 'icon-active', 3: 'icon-cancel', 4: 'icon-done', 5: 'icon-noshow' }
  return map[s] || 'icon-default'
}

const iconName = (s) => {
  const map = { 1: Wallet, 2: Check, 3: CircleClose, 4: Finished, 5: CircleCheck }
  return map[s] || Wallet
}

const loadOrders = async () => {
  loading.value = true
  try {
    const memberId = userStore.user?.id
    if (!memberId) { ElMessage.error('请先登录'); return }
    const res = await getMemberOrders(memberId)
    if (res.code === 200) orderList.value = res.data || []
  } catch (error) {
    console.error('加载订单列表失败:', error)
    ElMessage.error('加载订单列表失败')
  } finally { loading.value = false }
}

onMounted(() => { loadOrders() })
</script>

<style scoped>
.orders-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.page-hero {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: var(--radius-xl);
  padding: 24px 28px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.hero-left h2 {
  color: #fff;
  font-size: 22px;
  font-weight: 700;
  margin: 0 0 4px 0;
}

.hero-left p {
  color: rgba(255,255,255,0.8);
  margin: 0;
  font-size: 13px;
}

.hero-total {
  text-align: right;
}

.total-label {
  font-size: 12px;
  color: rgba(255,255,255,0.7);
  display: block;
}

.total-amount {
  font-size: 28px;
  font-weight: 800;
  color: #fff;
}

.empty-state {
  padding: 40px 0;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.order-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 16px 18px;
  transition: all var(--transition-fast);
}

.order-card:hover {
  border-color: var(--color-primary-light);
  box-shadow: var(--shadow-sm);
}

.order-icon-wrap {
  flex-shrink: 0;
}

.order-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.icon-active { background: #22c55e; }
.icon-pending { background: #f59e0b; }
.icon-cancel { background: #9ca3af; }
.icon-done { background: #6366f1; }
.icon-noshow { background: #ef4444; }
.icon-default { background: #6366f1; }

.order-body {
  flex: 1;
  min-width: 0;
}

.order-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.order-top h4 {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.order-amounts {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.order-price {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-danger);
}

.order-original {
  font-size: 12px;
  color: var(--text-muted);
}

.order-bottom {
  display: flex;
  align-items: center;
  gap: 12px;
}

.order-time {
  font-size: 12px;
  color: var(--text-muted);
}

@media (max-width: 640px) {
  .page-hero { flex-direction: column; align-items: flex-start; }
  .hero-total { text-align: left; }
}
</style>
