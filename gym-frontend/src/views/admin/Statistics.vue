<template>
  <div class="statistics-page" v-loading="loading">
    <div class="page-header">
      <h2>数据统计</h2>
      <p class="header-desc">系统运营数据概览与趋势分析</p>
    </div>

    <div class="stat-cards">
      <div class="stat-card card-purple">
        <div class="card-icon-wrap"><el-icon :size="24"><UserFilled /></el-icon></div>
        <div class="card-info">
          <span class="card-num">{{ stats.totalMembers || 0 }}</span>
          <span class="card-label">会员总数</span>
        </div>
      </div>
      <div class="stat-card card-blue">
        <div class="card-icon-wrap"><el-icon :size="24"><Collection /></el-icon></div>
        <div class="card-info">
          <span class="card-num">{{ stats.totalCourses || 0 }}</span>
          <span class="card-label">课程总数</span>
          <span class="card-sub">活跃 {{ stats.activeCourses || 0 }}</span>
        </div>
      </div>
      <div class="stat-card card-green">
        <div class="card-icon-wrap"><el-icon :size="24"><Finished /></el-icon></div>
        <div class="card-info">
          <span class="card-num">{{ stats.totalBookings || 0 }}</span>
          <span class="card-label">有效预约</span>
        </div>
      </div>
      <div class="stat-card card-orange">
        <div class="card-icon-wrap"><el-icon :size="24"><Money /></el-icon></div>
        <div class="card-info">
          <span class="card-num">¥{{ fmtPrice(stats.totalRevenue) }}</span>
          <span class="card-label">总收入</span>
        </div>
      </div>
      <div class="stat-card card-teal">
        <div class="card-icon-wrap"><el-icon :size="24"><Calendar /></el-icon></div>
        <div class="card-info">
          <span class="card-num">¥{{ fmtPrice(stats.revenueToday) }}</span>
          <span class="card-label">今日收入</span>
        </div>
      </div>
      <div class="stat-card card-pink">
        <div class="card-icon-wrap"><el-icon :size="24"><TrendCharts /></el-icon></div>
        <div class="card-info">
          <span class="card-num">¥{{ fmtPrice(stats.revenueMonth) }}</span>
          <span class="card-label">近30天收入</span>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-panel">
        <h3>近7日预约趋势</h3>
        <div class="bar-chart">
          <div v-for="(item, idx) in bookingTrend" :key="'b'+idx" class="bar-col">
            <div class="bar-value">{{ item.count || 0 }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ height: barHeight(item.count, maxBooking) + '%' }"></div>
            </div>
            <span class="bar-label">{{ formatDateLabel(item.date) }}</span>
          </div>
        </div>
      </div>
      <div class="chart-panel">
        <h3>近7日收入趋势</h3>
        <div class="bar-chart">
          <div v-for="(item, idx) in revenueTrend" :key="'r'+idx" class="bar-col">
            <div class="bar-value">¥{{ fmtPrice(item.amount) }}</div>
            <div class="bar-track">
              <div class="bar-fill bar-fill-green" :style="{ height: barHeight(item.amount || 0, maxRevenue) + '%' }"></div>
            </div>
            <span class="bar-label">{{ formatDateLabel(item.date) }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-panel">
        <h3>热门课程 TOP5</h3>
        <div v-if="topCourses.length === 0" class="empty-hint">暂无数据</div>
        <div v-else class="rank-list">
          <div v-for="(item, idx) in topCourses" :key="'rk'+idx" class="rank-item">
            <span class="rank-num" :class="'rank-' + (idx + 1)">{{ idx + 1 }}</span>
            <div class="rank-info">
              <span class="rank-name">{{ item.courseName }}</span>
            </div>
            <span class="rank-count">{{ item.bookingCount }}单</span>
          </div>
        </div>
      </div>
      <div class="chart-panel">
        <h3>数据分布</h3>
        <div class="dist-grid">
          <div class="dist-item di-1">
            <span class="dist-num">{{ dist.groupClass || 0 }}</span>
            <span class="dist-label">团课</span>
          </div>
          <div class="dist-item di-2">
            <span class="dist-num">{{ dist.privateClass || 0 }}</span>
            <span class="dist-label">私教</span>
          </div>
          <div class="dist-item di-3">
            <span class="dist-num">{{ dist.completedOrder || 0 }}</span>
            <span class="dist-label">已完成</span>
          </div>
          <div class="dist-item di-4">
            <span class="dist-num">{{ dist.noShowOrder || 0 }}</span>
            <span class="dist-label">爽约</span>
          </div>
          <div class="dist-item di-5">
            <span class="dist-num">{{ dist.activeCourse || 0 }}</span>
            <span class="dist-label">运行中</span>
          </div>
          <div class="dist-item di-6">
            <span class="dist-num">{{ dist.cancelledCourse || 0 }}</span>
            <span class="dist-label">已取消</span>
          </div>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-panel">
        <h3>近 7 日每日预约上限触发统计</h3>
        <div v-if="dailyLimitStats.length === 0" class="empty-hint">暂无数据</div>
        <div v-else class="bar-chart">
          <div v-for="(item, idx) in dailyLimitStats" :key="'d'+idx" class="bar-col">
            <div class="bar-value">{{ item.exceedCount || 0 }}</div>
            <div class="bar-track">
              <div class="bar-fill bar-fill-red" :style="{ height: barHeight(item.exceedCount || 0, maxExceed) + '%' }"></div>
            </div>
            <span class="bar-label">{{ formatDateLabel(item.date) }}</span>
          </div>
        </div>
        <div v-if="dailyLimitStats.length > 0" class="stat-hint">每日预约上限：3 次/人</div>
      </div>
      <div class="chart-panel">
        <h3>会员预约频次 TOP10</h3>
        <div v-if="memberFrequency.length === 0" class="empty-hint">暂无数据</div>
        <div v-else class="rank-list">
          <div v-for="(item, idx) in memberFrequency" :key="'mf'+idx" class="rank-item">
            <span class="rank-num" :class="'rank-' + (idx + 1)">{{ idx + 1 }}</span>
            <div class="rank-info">
              <span class="rank-name">{{ item.memberName }}</span>
              <span class="rank-phone">{{ item.memberPhone }}</span>
            </div>
            <div class="rank-stats">
              <span class="rank-tag">今日 {{ item.todayCount }}</span>
              <span class="rank-tag">本周 {{ item.weekCount }}</span>
              <span class="rank-tag">本月 {{ item.monthCount }}</span>
              <span class="rank-total">总计 {{ item.bookingCount }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { UserFilled, Collection, Finished, Money, Calendar, TrendCharts } from '@element-plus/icons-vue'
import { getStatistics } from '@/api/admin'

const loading = ref(false)
const stats = reactive({
  totalMembers: 0, totalCourses: 0, totalBookings: 0,
  totalRevenue: 0, activeMembers: 0, activeCourses: 0,
  revenueToday: 0, revenueWeek: 0, revenueMonth: 0,
  bookingTrend: [], revenueTrend: [], topCourses: [], distribution: {},
  memberBookingFrequency: [], dailyLimitExceedStats: []
})

const fmtPrice = (v) => {
  if (!v) return '0'
  return Number(v).toLocaleString(undefined, { maximumFractionDigits: 0 })
}

const formatDateLabel = (d) => {
  if (!d) return ''
  const parts = d.split('-')
  return (parts[1] || '') + '/' + (parts[2] || '')
}

const bookingTrend = computed(() => stats.bookingTrend || [])
const revenueTrend = computed(() => stats.revenueTrend || [])
const topCourses = computed(() => stats.topCourses || [])
const dist = computed(() => stats.distribution || {})
const memberFrequency = computed(() => stats.memberBookingFrequency || [])
const dailyLimitStats = computed(() => stats.dailyLimitExceedStats || [])

const maxBooking = computed(() => {
  const arr = bookingTrend.value.map(i => i.count || 0)
  return Math.max(1, ...arr)
})

const maxRevenue = computed(() => {
  const arr = revenueTrend.value.map(i => i.amount || 0)
  return Math.max(1, ...arr)
})

const maxExceed = computed(() => {
  const arr = dailyLimitStats.value.map(i => i.exceedCount || 0)
  return Math.max(1, ...arr)
})

const barHeight = (val, max) => {
  if (!max || max === 0) return 0
  return Math.round((val / max) * 100)
}

const loadStatistics = async () => {
  loading.value = true
  try {
    const res = await getStatistics()
    if (res.code === 200) {
      Object.assign(stats, res.data || {})
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => { loadStatistics() })
</script>

<style scoped>
.statistics-page {
  padding: 28px 32px;
  max-width: 1200px;
}

.page-header {
  margin-bottom: 28px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 4px 0;
}

.header-desc {
  margin: 0;
  font-size: 14px;
  color: var(--text-muted);
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 28px;
}

.stat-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 20px 22px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.card-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.card-purple .card-icon-wrap { background: #ede9fe; color: #7c3aed; }
.card-blue .card-icon-wrap   { background: #dbeafe; color: #2563eb; }
.card-green .card-icon-wrap  { background: #d1fae5; color: #059669; }
.card-orange .card-icon-wrap { background: #ffedd5; color: #ea580c; }
.card-teal .card-icon-wrap   { background: #ccfbf1; color: #0d9488; }
.card-pink .card-icon-wrap   { background: #fce7f3; color: #db2777; }

.card-info {
  display: flex;
  flex-direction: column;
}

.card-num {
  font-size: 22px;
  font-weight: 800;
  color: var(--text-primary);
  line-height: 1.2;
}

.card-label {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
}

.card-sub {
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 1px;
}

.charts-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 20px;
}

.chart-panel {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 20px 22px;
}

.chart-panel h3 {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 16px 0;
}

.bar-chart {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 180px;
  gap: 12px;
}

.bar-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
}

.bar-value {
  font-size: 11px;
  font-weight: 700;
  color: var(--text-secondary);
  margin-bottom: 6px;
  white-space: nowrap;
}

.bar-track {
  flex: 1;
  width: 100%;
  max-width: 40px;
  background: #f1f5f9;
  border-radius: 6px 6px 0 0;
  position: relative;
  overflow: hidden;
}

.bar-fill {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(180deg, #818cf8 0%, #6366f1 100%);
  border-radius: 6px 6px 0 0;
  transition: height 0.5s ease;
  min-height: 4px;
}

.bar-fill-green {
  background: linear-gradient(180deg, #34d399 0%, #059669 100%);
}

.bar-fill-red {
  background: linear-gradient(180deg, #f87171 0%, #dc2626 100%);
}

.stat-hint {
  margin-top: 12px;
  font-size: 12px;
  color: var(--text-muted);
  text-align: center;
}

.bar-label {
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 8px;
}

.rank-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: #f8fafc;
  border-radius: var(--radius-sm);
  transition: background var(--transition-fast);
}

.rank-item:hover {
  background: #f1f5f9;
}

.rank-num {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: #fff;
  flex-shrink: 0;
}

.rank-1 { background: #f59e0b; }
.rank-2 { background: #94a3b8; }
.rank-3 { background: #b45309; }
.rank-4, .rank-5 { background: #cbd5e1; color: #64748b; }

.rank-info {
  flex: 1;
  min-width: 0;
}

.rank-phone {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
  display: block;
}

.rank-stats {
  display: flex;
  gap: 8px;
  align-items: center;
}

.rank-tag {
  font-size: 11px;
  padding: 2px 8px;
  background: #e0e7ff;
  color: #4338ca;
  border-radius: 4px;
}

.rank-total {
  font-size: 13px;
  font-weight: 700;
  color: #059669;
  margin-left: auto;
}

.rank-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
}

.rank-count {
  font-size: 14px;
  font-weight: 700;
  color: var(--color-primary);
  flex-shrink: 0;
}

.dist-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.dist-item {
  text-align: center;
  padding: 14px 8px;
  border-radius: var(--radius-sm);
  background: #f8fafc;
}

.di-1 .dist-num { color: #7c3aed; }
.di-2 .dist-num { color: #db2777; }
.di-3 .dist-num { color: #059669; }
.di-4 .dist-num { color: #ef4444; }
.di-5 .dist-num { color: #2563eb; }
.di-6 .dist-num { color: #94a3b8; }

.dist-num {
  font-size: 22px;
  font-weight: 800;
  display: block;
  line-height: 1.2;
}

.dist-label {
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 2px;
  display: block;
}

.empty-hint {
  text-align: center;
  color: var(--text-muted);
  padding: 30px 0;
  font-size: 14px;
}

@media (max-width: 900px) {
  .stat-cards { grid-template-columns: repeat(2, 1fr); }
  .charts-row { grid-template-columns: 1fr; }
}

@media (max-width: 600px) {
  .stat-cards { grid-template-columns: 1fr; }
  .dist-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
