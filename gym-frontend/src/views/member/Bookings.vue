<template>
  <div class="bookings-page" v-loading="loading">
    <div class="page-hero">
      <div class="hero-left">
        <h2>我的预约</h2>
        <p>管理你的课程预约，保持训练节奏</p>
      </div>
      <div class="hero-stats">
        <div class="mini-stat">
          <span class="mini-num">{{ bookingList.length }}</span>
          <span class="mini-label">总预约</span>
        </div>
        <div class="mini-stat">
          <span class="mini-num accent">{{ activeCount }}</span>
          <span class="mini-label">进行中</span>
        </div>
      </div>
    </div>

    <div v-if="noShowWarning" class="ban-warning">
      <el-icon :size="20"><WarningFilled /></el-icon>
      <span>已累计爽约 {{ nsc }} 次，累计 5 次将被限制预约 7 天</span>
    </div>

    <div v-if="isBannedWarning" class="ban-warning ban-critical">
      <el-icon :size="20"><CircleCloseFilled /></el-icon>
      <span>因累计爽约，您已被限制预约，{{ bannedUntilText }} 后自动解除，爽约次数将清零</span>
    </div>

    <div v-if="!loading && bookingList.length === 0" class="empty-state">
      <el-empty description="暂无预约记录">
        <el-button type="primary" @click="$router.push('/member/courses')">去预约课程</el-button>
      </el-empty>
    </div>

    <div v-else class="booking-list">
      <div v-for="booking in bookingList" :key="booking.id" class="booking-card">
        <div class="booking-left">
          <div class="status-dot" :class="statusClass(booking.status)"></div>
        </div>
        <div class="booking-main">
          <div class="booking-header">
            <h4 class="booking-title">{{ booking.courseName }}</h4>
            <el-tag :type="statusTagType(booking.status)" effect="dark" round size="small">
              {{ statusLabel(booking.status) }}
            </el-tag>
          </div>
          <div class="booking-meta">
            <span class="meta-item">
              <el-icon><User /></el-icon> {{ booking.coachName }}
            </span>
            <span class="meta-item">
              <el-icon><Clock /></el-icon> {{ formatDateTime(booking.startTime) }}
            </span>
            <span v-if="booking.endTime" class="meta-item meta-end">
              <el-icon><Timer /></el-icon> {{ formatTimeOnly(booking.endTime) }} 结束
            </span>
          </div>
        </div>
        <div class="booking-actions">
          <el-button
            v-if="canCheckIn(booking)"
            type="success"
            plain
            size="small"
            round
            :loading="checkingInId === booking.id"
            @click="handleCheckIn(booking)"
          >
            签到
          </el-button>
          <el-button
            v-if="booking.status === 2"
            type="danger"
            plain
            size="small"
            round
            @click="handleCancel(booking)"
          >
            取消预约
          </el-button>
          <el-button
            v-if="booking.status !== 2"
            type="info"
            plain
            size="small"
            round
            :loading="deletingId === booking.id"
            @click="handleDelete(booking)"
          >
            删除
          </el-button>
          <span v-if="booking.status !== 2 && booking.status !== 3 && booking.status !== 4 && !canCheckIn(booking)" class="action-hint">
            {{ statusMap[booking.status] || '--' }}
          </span>
          <span v-if="booking.status === 3" class="action-hint">已取消</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Clock, Timer, WarningFilled, CircleCloseFilled, Delete } from '@element-plus/icons-vue'
import { getMemberBookings, cancelBooking, checkInBooking } from '@/api/booking'
import { getMemberById } from '@/api/member'
import { deleteOrder } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { formatDateTime, formatTimeOnly } from '@/utils/date'

const userStore = useUserStore()
const loading = ref(false)
const bookingList = ref([])
const checkingInId = ref(null)
const deletingId = ref(null)
const memberInfo = ref(null)

const activeCount = computed(() =>
  bookingList.value.filter(b => b.status === 2).length
)

const noShowWarning = computed(() => {
  if (!memberInfo.value) return false
  const nsc = memberInfo.value.noShowCount || 0
  return nsc > 0 && nsc < 5 && memberInfo.value.isBanned !== 1
})

const nsc = computed(() => memberInfo.value?.noShowCount || 0)

const isBannedWarning = computed(() => {
  if (!memberInfo.value) return false
  return memberInfo.value.isBanned === 1
})

const bannedUntilText = computed(() => {
  if (!memberInfo.value?.bannedUntil) return ''
  return formatDateTime(memberInfo.value.bannedUntil)
})

const statusMap = { 1: '待支付', 2: '已预约', 3: '已取消', 4: '已完成', 5: '已爽约' }
const statusTagMap = { 1: 'primary', 2: 'success', 3: 'info', 4: '', 5: 'danger' }

const statusLabel = (s) => statusMap[s] || '未知'
const statusTagType = (s) => statusTagMap[s] || 'info'

const statusClass = (s) => {
  const map = { 1: 'dot-default', 2: 'dot-active', 3: 'dot-cancel', 4: 'dot-done', 5: 'dot-noshow' }
  return map[s] || 'dot-default'
}

const canCheckIn = (booking) => {
  if (booking.status !== 2) return false
  if (booking.checkIn === 1) return false
  if (!booking.startTime) return false
  const start = new Date(booking.startTime).getTime()
  const now = Date.now()
  const halfHour = 30 * 60 * 1000
  const twoHours = 2 * 60 * 60 * 1000
  return now >= start - halfHour && now <= start + twoHours
}

const loadMember = async () => {
  const memberId = userStore.user?.id
  if (!memberId) return
  try {
    const res = await getMemberById(memberId)
    if (res.code === 200) memberInfo.value = res.data
  } catch {}
}

const loadBookings = async () => {
  loading.value = true
  try {
    const memberId = userStore.user?.id
    if (!memberId) { ElMessage.error('请先登录'); return }
    const res = await getMemberBookings(memberId)
    if (res.code === 200) bookingList.value = res.data || []
  } catch (error) {
    console.error('加载预约记录失败:', error)
    ElMessage.error('加载预约记录失败')
  } finally { loading.value = false }
}

const handleCheckIn = async (booking) => {
  checkingInId.value = booking.id
  try {
    const res = await checkInBooking(booking.id)
    if (res.code === 200) {
      ElMessage.success('签到成功！')
      booking.status = 4
      booking.checkIn = 1
    } else {
      ElMessage.error(res.message || '签到失败')
    }
  } catch (error) {
    console.error('签到失败:', error)
    ElMessage.error('签到失败')
  } finally {
    checkingInId.value = null
  }
}

const handleCancel = async (booking) => {
  // 先判断是否超过取消截止时间（开课前 2 小时）
  if (booking.startTime) {
    const courseStart = new Date(booking.startTime).getTime()
    const now = Date.now()
    const twoHours = 2 * 60 * 60 * 1000
    const cancelDeadline = courseStart - twoHours
    
    if (now > cancelDeadline) {
      ElMessage.warning('开课前 2 小时内不可取消，如需帮助请联系管理员')
      return
    }
  }
  
  try {
    await ElMessageBox.confirm('确定要取消该预约吗？', '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    })
    const memberId = userStore.user?.id
    const res = await cancelBooking({ memberId: parseInt(memberId), bookingId: booking.id })
    if (res.code === 200) { ElMessage.success('取消成功'); loadBookings() }
    else ElMessage.error(res.message || '取消失败')
  } catch (error) {
    if (error !== 'cancel') { ElMessage.error('取消预约失败') }
  }
}

const handleDelete = async (booking) => {
  try {
    await ElMessageBox.confirm('确定要删除该预约记录吗？删除后无法恢复', '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    })
    deletingId.value = booking.id
    const res = await deleteOrder(booking.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadBookings()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') { ElMessage.error('删除失败') }
  } finally {
    deletingId.value = null
  }
}

onMounted(() => { loadMember(); loadBookings() })
</script>

<style scoped>
.bookings-page {
  padding: 24px;
  max-width: 900px;
  margin: 0 auto;
}

.page-hero {
  background: linear-gradient(135deg, #06b6d4 0%, #3b82f6 100%);
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

.hero-stats {
  display: flex;
  gap: 24px;
}

.mini-stat {
  text-align: center;
}

.mini-num {
  font-size: 28px;
  font-weight: 800;
  color: #fff;
  display: block;
  line-height: 1;
}

.mini-num.accent { color: #fde68a; }

.mini-label {
  font-size: 11px;
  color: rgba(255,255,255,0.7);
  margin-top: 4px;
  display: block;
}

.ban-warning {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #fef3c7;
  border: 1px solid #f59e0b;
  border-radius: var(--radius-md);
  padding: 14px 18px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #92400e;
}

.ban-critical {
  background: #fee2e2;
  border-color: #ef4444;
  color: #991b1b;
}

.empty-state {
  padding: 40px 0;
}

.booking-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.booking-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 18px 20px;
  transition: all var(--transition-fast);
}

.booking-card:hover {
  border-color: var(--color-primary-light);
  box-shadow: var(--shadow-sm);
}

.booking-left {
  flex-shrink: 0;
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  box-shadow: 0 0 0 4px currentColor;
  opacity: 0.2;
}

.dot-active { color: #22c55e; }
.dot-default { color: #6366f1; }
.dot-cancel { color: #9ca3af; }
.dot-done { color: #3b82f6; }
.dot-noshow { color: #ef4444; }

.booking-main {
  flex: 1;
  min-width: 0;
}

.booking-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.booking-title {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.booking-meta {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-secondary);
}

.meta-end {
  color: var(--text-muted);
}

.booking-actions {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.action-hint {
  font-size: 12px;
  color: var(--text-muted);
}

@media (max-width: 640px) {
  .page-hero { flex-direction: column; align-items: flex-start; }
  .booking-card { flex-wrap: wrap; }
  .booking-actions { flex-direction: row; }
}
</style>
