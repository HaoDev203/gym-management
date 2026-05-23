<template>
  <div class="courses-page">
    <div class="courses-hero">
      <div class="hero-text">
        <h2>发现你的课程</h2>
        <p>选择适合你的训练计划，开启健康生活</p>
      </div>
      <div class="hero-decoration">
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>
      </div>
    </div>

    <div class="filter-tabs">
      <span
        v-for="tab in filterTabs"
        :key="tab.value"
        class="filter-tab"
        :class="{ active: filterType === tab.value }"
        @click="switchFilter(tab.value)"
      >
        <el-icon v-if="tab.icon" class="tab-icon"><component :is="tab.icon" /></el-icon>
        {{ tab.label }}
      </span>
    </div>

    <div class="course-grid" v-loading="loading">
      <CourseCard
        v-for="course in courseList"
        :key="course.id"
        :course="course"
        :show-book-button="true"
        @book="handleBook"
      />
    </div>

    <el-empty v-if="!loading && courseList.length === 0" description="暂无课程">
      <el-button type="primary" @click="switchFilter('')">查看全部课程</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Grid, User, Star } from '@element-plus/icons-vue'
import CourseCard from '@/components/CourseCard.vue'
import { getCourseList } from '@/api/course'
import { bookCourse } from '@/api/booking'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const filterType = ref('')
const courseList = ref([])

const filterTabs = [
  { label: '全部课程', value: '', icon: Grid },
  { label: '团课', value: 1, icon: User },
  { label: '私教课', value: 0, icon: Star }
]

const switchFilter = (value) => {
  filterType.value = value
  loadCourses()
}

const loadCourses = async () => {
  loading.value = true
  try {
    const res = await getCourseList()
    if (res.code === 200) {
      let courses = res.data || []
      if (filterType.value !== '') {
        courses = courses.filter(c => c.type === filterType.value)
      }
      courseList.value = courses
    }
  } catch (error) {
    console.error('加载课程失败:', error)
    ElMessage.error('加载课程失败')
  } finally {
    loading.value = false
  }
}

const handleBook = async (course) => {
  const memberId = userStore.user?.id
  if (!memberId) { ElMessage.error('请先登录'); return }

  try {
    const res = await bookCourse({ memberId: parseInt(memberId), courseId: course.id })
    if (res.code === 200) {
      ElMessage.success('预约成功')
      loadCourses()
    } else {
      ElMessage.error(res.message || '预约失败')
    }
  } catch (error) {
    console.error('预约失败:', error)
    ElMessage.error('预约失败，请稍后重试')
  }
}

onMounted(() => { loadCourses() })
</script>

<style scoped>
.courses-page {
  padding: 24px;
  max-width: 1100px;
  margin: 0 auto;
}

.courses-hero {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #a78bfa 100%);
  border-radius: var(--radius-xl);
  padding: 28px 32px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.hero-text h2 {
  color: #fff;
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 6px 0;
}

.hero-text p {
  color: rgba(255,255,255,0.8);
  margin: 0;
  font-size: 14px;
}

.hero-decoration {
  position: relative;
  width: 100px;
  height: 80px;
  flex-shrink: 0;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255,255,255,0.12);
}

.deco-1 { width: 60px; height: 60px; top: 0; right: 10px; }
.deco-2 { width: 40px; height: 40px; bottom: 0; right: 50px; }
.deco-3 { width: 24px; height: 24px; top: 15px; left: 0; }

.filter-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  padding: 4px;
  width: fit-content;
}

.filter-tab {
  padding: 8px 18px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
  display: flex;
  align-items: center;
  gap: 6px;
  user-select: none;
}

.filter-tab:hover { color: var(--color-primary); }

.filter-tab.active {
  background: var(--color-primary);
  color: #fff;
  box-shadow: var(--shadow-sm);
}

.tab-icon { font-size: 16px; }

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

@media (max-width: 768px) {
  .course-grid { grid-template-columns: 1fr; }
  .courses-hero { flex-direction: column; align-items: flex-start; gap: 16px; }
  .hero-decoration { display: none; }
}
</style>
