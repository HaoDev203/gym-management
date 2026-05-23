<template>
  <div class="dashboard-page" v-loading="loading">
    <h2>数据统计概览</h2>
    
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #409EFF;">
              <el-icon :size="30"><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalMembers || 0 }}</div>
              <div class="stat-label">会员总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #67C23A;">
              <el-icon :size="30"><UserFilled /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalCourses || 0 }}</div>
              <div class="stat-label">课程总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #E6A23C;">
              <el-icon :size="30"><Grid /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalBookings || 0 }}</div>
              <div class="stat-label">预约总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #F56C6C;">
              <el-icon :size="30"><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">¥{{ statistics.revenueToday || 0 }}</div>
              <div class="stat-label">今日收入</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3>最近订单</h3>
          </template>
          <el-table :data="recentOrders" style="width: 100%" :row-key="row => row.id">
            <el-table-column prop="memberName" label="会员" />
            <el-table-column prop="courseName" label="课程" />
            <el-table-column label="状态">
              <template #default="{ row }">
                <el-tag v-if="row.status === 3" type="info">已取消</el-tag>
                <el-tag v-else-if="row.status === 2" type="success">已预约</el-tag>
                <el-tag v-else-if="row.status === 4" type="warning">已完成</el-tag>
                <el-tag v-else-if="row.status === 5" type="danger">已爽约</el-tag>
                <el-tag v-else type="primary">待支付</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3>热门课程</h3>
          </template>
          <el-table :data="popularCourses" style="width: 100%" :row-key="row => row.id">
            <el-table-column prop="name" label="课程名称" />
            <el-table-column prop="coachName" label="教练" />
            <el-table-column label="预约人数">
              <template #default="{ row }">
                {{ row.bookedCount }}/{{ row.capacity }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { User, UserFilled, Grid, Document } from '@element-plus/icons-vue'
import { getStatistics } from '@/api/admin'
import { getAllOrders } from '@/api/order'
import { getAllCourses } from '@/api/course'

const loading = ref(false)
const statistics = reactive({
  totalMembers: 0,
  totalCourses: 0,
  totalBookings: 0,
  revenueToday: 0
})
const recentOrders = ref([])
const popularCourses = ref([])

const loadStatistics = async () => {
  loading.value = true
  try {
    const res = await getStatistics()
    if (res.code === 200) {
      Object.assign(statistics, res.data || {})
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadRecentOrders = async () => {
  try {
    const res = await getAllOrders()
    if (res.code === 200) {
      recentOrders.value = (res.data || []).slice(0, 5)
    }
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const loadPopularCourses = async () => {
  try {
    const res = await getAllCourses()
    if (res.code === 200) {
      popularCourses.value = (res.data || [])
        .sort((a, b) => b.bookedCount - a.bookedCount)
        .slice(0, 5)
    }
  } catch (error) {
    console.error('加载课程失败:', error)
  }
}

onMounted(() => {
  loadStatistics()
  loadRecentOrders()
  loadPopularCourses()
})
</script>

<style scoped>
.dashboard-page h2 {
  margin-bottom: 24px;
  color: var(--text-primary);
}

.dashboard-page h3 {
  margin: 0;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 600;
}

.stat-card {
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  transition: all var(--transition-normal);
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-md);
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
  min-width: 0;
}

.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.5px;
}

.stat-label {
  font-size: 13px;
  color: var(--text-muted);
  margin-top: 2px;
}
</style>
