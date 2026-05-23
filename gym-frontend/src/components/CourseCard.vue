<template>
  <div class="course-card" @click="goDetail">
    <div class="card-cover" :class="coverClass">
      <div class="cover-overlay">
        <el-tag :type="course.type === 1 ? 'primary' : 'success'" effect="dark" round size="small">
          {{ course.type === 1 ? '团课' : '私教课' }}
        </el-tag>
        <el-tag v-if="course.status === 0" type="danger" effect="dark" round size="small">已取消</el-tag>
        <el-tag v-else-if="isFull && waitlistCount > 0" type="warning" effect="dark" round size="small">
          {{ waitlistCount }}人排队
        </el-tag>
      </div>
      <div class="cover-icon">
        <el-icon :size="48"><TrophyBase /></el-icon>
      </div>
    </div>
    <div class="card-body">
      <h3 class="course-title">{{ course.name }}</h3>
      <div class="course-meta">
        <div class="meta-item">
          <el-icon class="meta-icon"><User /></el-icon>
          <span>{{ course.coachName }}</span>
        </div>
        <div class="meta-item">
          <el-icon class="meta-icon"><Clock /></el-icon>
          <span>{{ formatTime(course.startTime) }}</span>
        </div>
      </div>
      <div class="card-bottom">
        <div class="price-tag">
          <span class="price-symbol">¥</span>
          <span class="price-value">{{ course.price }}</span>
        </div>
        <div class="capacity-info">
          <el-progress
            :percentage="Math.round((course.bookedCount / course.capacity) * 100)"
            :stroke-width="6"
            :color="capacityColor"
            :show-text="false"
          />
          <span class="capacity-text">{{ course.bookedCount }}/{{ course.capacity }}</span>
        </div>
      </div>
      <div v-if="showBookButton && course.status !== 0" class="card-actions">
        <template v-if="isExpired && course.type === 1">
          <el-button type="info" disabled class="book-btn" round>已过期</el-button>
        </template>
        <template v-else-if="hasBooked">
          <el-button type="success" disabled class="book-btn" round>已预约</el-button>
        </template>
        <template v-else-if="inWaitlist">
          <el-tag type="warning" effect="plain" round class="waitlist-tag">
            排队中 · 第 {{ myPosition }} 位
          </el-tag>
          <el-button type="danger" plain size="small" round @click.stop="handleLeaveWaitlist">
            取消
          </el-button>
        </template>
        <template v-else-if="!isFull">
          <el-button type="primary" :loading="booking" @click.stop="handleBook" class="book-btn" round>
            立即预约
          </el-button>
        </template>
        <template v-else>
          <el-button type="warning" :loading="waitlisting" @click.stop="handleWaitlist" class="book-btn" round>
            {{ waitlisting ? '排队中...' : '候补排队' }}
          </el-button>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Clock, TrophyBase } from '@element-plus/icons-vue'
import { formatTime } from '@/utils/date'
import { joinWaitlist, leaveWaitlist, getWaitlistCount, getMyWaitlistPosition } from '@/api/waitlist'
import { getMemberBookings } from '@/api/booking'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const props = defineProps({
  course: { type: Object, required: true },
  showBookButton: { type: Boolean, default: true }
})

const emit = defineEmits(['book'])

const booking = ref(false)
const waitlisting = ref(false)
const waitlistCount = ref(0)
const hasBooked = ref(false)
const inWaitlist = ref(false)
const myPosition = ref(0)

const isFull = computed(() => (props.course.bookedCount || 0) >= (props.course.capacity || 100))

const isExpired = computed(() => props.course.isExpired === true)

const coverClass = computed(() => {
  if (props.course.status === 0) return 'cover-disabled'
  if (isExpired.value) return 'cover-expired'
  if (isFull.value) return 'cover-warning'
  return props.course.type === 1 ? 'cover-group' : 'cover-private'
})

const capacityColor = computed(() => {
  const pct = props.course.bookedCount / props.course.capacity
  if (pct >= 1) return '#ef4444'
  if (pct >= 0.8) return '#f59e0b'
  return '#6366f1'
})

const handleBook = () => emit('book', props.course)

const handleWaitlist = async () => {
  const memberId = userStore.user?.id
  if (!memberId) { ElMessage.error('请先登录'); return }

  waitlisting.value = true
  try {
    const res = await joinWaitlist(memberId, props.course.id)
    if (res.code === 200) {
      const position = res.data?.position || waitlistCount.value + 1
      waitlistCount.value = position
      inWaitlist.value = true
      myPosition.value = position
      ElMessage.success(`排队成功，您当前排在第 ${position} 位`)
    } else {
      ElMessage.error(res.message || '排队失败')
    }
  } catch (error) {
    console.error('排队失败:', error)
    ElMessage.error('排队失败，请稍后重试')
  } finally {
    waitlisting.value = false
  }
}

const handleLeaveWaitlist = async () => {
  const memberId = userStore.user?.id
  if (!memberId) return

  try {
    const res = await leaveWaitlist(memberId, props.course.id)
    if (res.code === 200) {
      inWaitlist.value = false
      myPosition.value = 0
      waitlistCount.value = Math.max(0, waitlistCount.value - 1)
      ElMessage.success('已取消候补')
    } else {
      ElMessage.error(res.message || '取消失败')
    }
  } catch (error) {
    console.error('取消候补失败:', error)
    ElMessage.error('取消候补失败')
  }
}

const loadStatus = async () => {
  const memberId = userStore.user?.id
  if (!memberId) return

  try {
    const bookingsRes = await getMemberBookings(memberId)
    if (bookingsRes.code === 200) {
      hasBooked.value = (bookingsRes.data || []).some(
        b => b.courseId === props.course.id && b.status === 2
      )
    }
  } catch {}

  try {
    const posRes = await getMyWaitlistPosition(memberId, props.course.id)
    if (posRes.code === 200 && posRes.data?.position > 0) {
      inWaitlist.value = true
      myPosition.value = posRes.data.position
    }
  } catch {}

  if (isFull.value) {
    loadWaitlistCount()
  }
}

const loadWaitlistCount = async () => {
  try {
    const res = await getWaitlistCount(props.course.id)
    if (res.code === 200) {
      waitlistCount.value = res.data?.count || 0
    }
  } catch {}
}

const goDetail = () => {
  if (props.course.id) {
    router.push(`/member/course/${props.course.id}`)
  }
}

onMounted(() => { loadStatus() })
</script>

<style scoped>
.course-card {
  background: var(--bg-card);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-normal);
  margin-bottom: 20px;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--color-primary-light);
}

.card-cover {
  height: 110px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.cover-group {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
}

.cover-private {
  background: linear-gradient(135deg, #f59e0b 0%, #ef4444 100%);
}

.cover-expired {
  background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
}

.cover-warning {
  background: linear-gradient(135deg, #9ca3af 0%, #6b7280 100%);
}

.cover-disabled {
  background: linear-gradient(135deg, #d1d5db 0%, #9ca3af 100%);
}

.cover-overlay {
  position: absolute;
  top: 12px;
  left: 12px;
  right: 12px;
  display: flex;
  gap: 8px;
  z-index: 1;
}

.cover-icon {
  opacity: 0.2;
  color: #fff;
}

.card-body {
  padding: 16px 18px 18px;
}

.course-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 12px 0;
  line-height: 1.3;
}

.course-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
}

.meta-icon {
  color: var(--text-muted);
  font-size: 15px;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 14px;
  gap: 12px;
}

.price-tag {
  flex-shrink: 0;
}

.price-symbol {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-danger);
}

.price-value {
  font-size: 22px;
  font-weight: 800;
  color: var(--color-danger);
  line-height: 1;
}

.capacity-info {
  flex: 1;
  min-width: 0;
}

.capacity-text {
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 2px;
  display: block;
}

.card-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.book-btn {
  flex: 1;
  font-weight: 600;
  height: 38px;
}

.waitlist-tag {
  flex: 1;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 13px;
}
</style>
