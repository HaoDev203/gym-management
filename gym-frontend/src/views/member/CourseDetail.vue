<template>
  <div class="detail-page" v-loading="loading">
    <div v-if="course" class="detail-content">
      <div class="detail-hero" :class="heroClass">
        <el-button class="back-btn" @click="goBack" :icon="ArrowLeft" circle size="small" />
        <div class="hero-body">
          <el-tag :type="course.type === 1 ? 'success' : ''" effect="dark" round size="small">
            {{ course.type === 1 ? '团课' : '私教课' }}
          </el-tag>
          <h1 class="detail-title">{{ course.name }}</h1>
          <div class="hero-price">
            <span class="hero-price-symbol">¥</span>
            <span class="hero-price-value">{{ course.price }}</span>
          </div>
        </div>
      </div>

      <div class="detail-cards">
        <div class="detail-card">
          <div class="card-label">
            <el-icon><UserFilled /></el-icon> 教练
          </div>
          <div class="card-value">{{ course.coachName }}</div>
        </div>
        <div class="detail-card">
          <div class="card-label">
            <el-icon><Calendar /></el-icon> 日期
          </div>
          <div class="card-value">{{ formatDate(course.startTime) }}</div>
        </div>
        <div class="detail-card">
          <div class="card-label">
            <el-icon><Clock /></el-icon> 时间
          </div>
          <div class="card-value">{{ formatTime(course.startTime) }} - {{ formatTime(course.endTime) }}</div>
        </div>
      </div>

      <div class="info-section">
        <h3 class="section-title">课程信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">余位</span>
            <span class="info-value">
              <el-progress
                :percentage="Math.round((course.bookedCount / course.capacity) * 100)"
                :stroke-width="8"
                :color="capacityColor"
                :show-text="false"
                style="width: 120px"
              />
              <span class="capacity-text">{{ course.capacity - course.bookedCount }}/{{ course.capacity }}</span>
            </span>
          </div>
          <div class="info-item">
            <span class="info-label">状态</span>
            <span class="info-value">
              <el-tag v-if="course.status === 0" type="danger" size="small">已取消</el-tag>
              <el-tag v-else-if="course.bookedCount >= course.capacity" type="warning" size="small">已满员</el-tag>
              <el-tag v-else type="success" size="small">可预约</el-tag>
            </span>
          </div>
          <div class="info-item">
            <span class="info-label">课程类型</span>
            <span class="info-value">{{ course.type === 1 ? '团课' : '私教课' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">课程时长</span>
            <span class="info-value">{{ durationText }}</span>
          </div>
        </div>
      </div>

      <div class="action-bar">
        <el-button @click="goBack" size="large">返回列表</el-button>
        <el-button
          v-if="showBookButton"
          type="primary"
          size="large"
          :disabled="course.bookedCount >= course.capacity || course.status === 0"
          @click="handleBook"
        >
          {{ course.bookedCount >= course.capacity ? '已满员' : '立即预约' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, UserFilled, Calendar, Clock } from '@element-plus/icons-vue'
import { getCourseById } from '@/api/course'
import { bookCourse } from '@/api/booking'
import { useUserStore } from '@/stores/user'
import { formatDate, formatTime } from '@/utils/date'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const course = ref(null)

const heroClass = computed(() => {
  if (!course.value) return ''
  return course.value.type === 1 ? 'hero-group' : 'hero-private'
})

const capacityColor = computed(() => {
  if (!course.value) return '#6366f1'
  const pct = course.value.bookedCount / course.value.capacity
  if (pct >= 1) return '#ef4444'
  if (pct >= 0.8) return '#f59e0b'
  return '#6366f1'
})

const durationText = computed(() => {
  if (!course.value) return ''
  const start = new Date(course.value.startTime)
  const end = new Date(course.value.endTime)
  const diff = Math.round((end - start) / (1000 * 60))
  return diff >= 60 ? `${Math.round(diff / 60)} 小时` : `${diff} 分钟`
})

const showBookButton = computed(() => userStore.user?.role === 'MEMBER')

const loadCourse = async () => {
  loading.value = true
  try {
    const res = await getCourseById(route.params.id)
    if (res.code === 200) course.value = res.data
  } catch (error) {
    console.error('加载课程详情失败:', error)
    ElMessage.error('加载课程详情失败')
  } finally {
    loading.value = false
  }
}

const handleBook = async () => {
  const memberId = userStore.user?.id
  if (!memberId) { ElMessage.error('请先登录'); return }
  try {
    const res = await bookCourse({ memberId: parseInt(memberId), courseId: course.value.id })
    if (res.code === 200) {
      ElMessage.success('预约成功')
      router.push('/member/bookings')
    } else {
      ElMessage.error(res.message || '预约失败')
    }
  } catch (error) {
    console.error('预约失败:', error)
    ElMessage.error('预约失败，请稍后重试')
  }
}

const goBack = () => router.back()

onMounted(() => { loadCourse() })
</script>

<style scoped>
.detail-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.detail-hero {
  border-radius: var(--radius-xl);
  padding: 28px 28px 32px;
  position: relative;
  overflow: hidden;
  margin-bottom: 24px;
}

.hero-group {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
}

.hero-private {
  background: linear-gradient(135deg, #f59e0b 0%, #ef4444 100%);
}

.detail-hero::after {
  content: '';
  position: absolute;
  width: 180px;
  height: 180px;
  border-radius: 50%;
  background: rgba(255,255,255,0.06);
  top: -50px;
  right: -50px;
}

.back-btn {
  position: relative;
  z-index: 1;
  margin-bottom: 16px;
}

.hero-body {
  position: relative;
  z-index: 1;
}

.detail-title {
  color: #fff;
  font-size: 26px;
  font-weight: 800;
  margin: 12px 0 16px;
  line-height: 1.3;
}

.hero-price {
  line-height: 1;
}

.hero-price-symbol {
  font-size: 18px;
  color: rgba(255,255,255,0.7);
}

.hero-price-value {
  font-size: 36px;
  font-weight: 800;
  color: #fff;
}

.detail-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
  margin-bottom: 24px;
}

.detail-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 16px;
  text-align: center;
}

.card-label {
  font-size: 12px;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.card-value {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.info-section {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 24px;
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 20px 0;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: var(--bg-page);
  border-radius: var(--radius-sm);
}

.info-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.info-value {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: 8px;
}

.capacity-text {
  font-size: 13px;
  color: var(--text-muted);
}

.action-bar {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

@media (max-width: 640px) {
  .detail-cards { grid-template-columns: 1fr; }
  .info-grid { grid-template-columns: 1fr; }
}
</style>
