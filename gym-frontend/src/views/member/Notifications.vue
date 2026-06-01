<template>
  <div class="notifications-page" v-loading="loading">
    <div class="page-hero">
      <div class="hero-left">
        <h2>消息中心</h2>
        <p>课程提醒与系统通知</p>
      </div>
      <div class="hero-badge" v-if="unreadCount > 0">
        <span class="badge-num">{{ unreadCount }}</span>
        <span class="badge-label">条未读</span>
      </div>
    </div>

    <div v-if="!loading && notificationList.length === 0" class="empty-state">
      <el-empty description="暂无消息">
        <template #image>
          <el-icon :size="60" color="var(--text-muted)"><Message /></el-icon>
        </template>
      </el-empty>
    </div>

    <div v-else class="notification-list">
      <div
        v-for="notification in notificationList"
        :key="notification.id"
        class="notification-card"
        :class="{ unread: notification.isRead === 0 }"
        @click="handleMarkAsRead(notification)"
      >
        <div class="notif-icon-wrap">
          <div class="notif-icon" :class="typeIconClass(notification.type)">
            <el-icon :size="18"><component :is="typeIconName(notification.type)" /></el-icon>
          </div>
        </div>
        <div class="notif-body">
          <div class="notif-header">
            <h4 class="notif-title">{{ notification.title }}</h4>
            <div class="notif-badges">
              <el-tag :type="typeTagType(notification.type)" effect="dark" round size="small">
                {{ typeLabel(notification.type) }}
              </el-tag>
              <span v-if="notification.isRead === 0" class="unread-dot"></span>
            </div>
          </div>
          <p class="notif-content">{{ notification.content }}</p>
          <div class="notif-footer">
            <span class="notif-time">{{ formatDateTime(notification.createdAt) }}</span>
            <el-button
              v-if="notification.isRead === 1"
              type="danger"
              size="small"
              plain
              @click.stop="handleDelete(notification)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Message, Bell, CircleCheck, WarningFilled, Clock, InfoFilled,
  Timer, List
} from '@element-plus/icons-vue'
import { getNotifications, markAsRead, deleteNotification } from '@/api/notification'
import { useUserStore } from '@/stores/user'
import { formatDateTime } from '@/utils/date'

const userStore = useUserStore()
const loading = ref(false)
const notificationList = ref([])

const unreadCount = computed(() =>
  notificationList.value.filter(n => n.isRead === 0).length
)

const typeMap = {
  1: '预约成功', 2: '取消通知', 3: '课前提醒', 4: '即将开始',
  5: '排队成功', 6: '课程取消', 7: '课程提醒', 8: '系统通知'
}

const typeTagMap = {
  1: 'success', 2: 'warning', 3: 'warning', 4: 'danger',
  5: 'success', 6: 'info', 7: 'warning', 8: 'info'
}

const typeIconMap = {
  1: CircleCheck, 2: WarningFilled, 3: Clock, 4: Timer,
  5: CircleCheck, 6: WarningFilled, 7: Bell, 8: InfoFilled
}

const typeIconClassMap = {
  1: 'ic-success', 2: 'ic-warning', 3: 'ic-info', 4: 'ic-danger',
  5: 'ic-success', 6: 'ic-info', 7: 'ic-warning', 8: 'ic-default'
}

const typeLabel = (t) => typeMap[t] || '其他'
const typeTagType = (t) => typeTagMap[t] || 'info'
const typeIconName = (t) => typeIconMap[t] || List
const typeIconClass = (t) => typeIconClassMap[t] || 'ic-default'

const loadNotifications = async () => {
  loading.value = true
  try {
    const memberId = userStore.user?.id
    if (!memberId) { ElMessage.error('请先登录'); return }
    const res = await getNotifications({ memberId })
    if (res.code === 200) notificationList.value = res.data || []
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  } finally { loading.value = false }
}

const handleMarkAsRead = async (notification) => {
  if (notification.isRead === 1) return
  try {
    const res = await markAsRead(notification.id)
    if (res.code === 200) notification.isRead = 1
    else ElMessage.error(res.message || '标记失败')
  } catch (error) {
    console.error('标记失败:', error)
  }
}

const handleDelete = async (notification) => {
  try {
    const res = await deleteNotification(notification.id)
    if (res.code === 200) {
      notificationList.value = notificationList.value.filter(n => n.id !== notification.id)
      ElMessage.success('删除成功')
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

onMounted(() => { loadNotifications() })
</script>

<style scoped>
.notifications-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.page-hero {
  background: linear-gradient(135deg, #8b5cf6 0%, #6366f1 100%);
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

.hero-badge {
  background: rgba(255,255,255,0.2);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-md);
  padding: 10px 18px;
  text-align: center;
}

.badge-num {
  font-size: 28px;
  font-weight: 800;
  color: #fde68a;
  display: block;
  line-height: 1;
}

.badge-label {
  font-size: 11px;
  color: rgba(255,255,255,0.7);
  margin-top: 2px;
  display: block;
}

.empty-state {
  padding: 40px 0;
}

.notification-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notification-card {
  display: flex;
  gap: 16px;
  padding: 16px 18px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.notification-card:hover {
  border-color: var(--color-primary-light);
  box-shadow: var(--shadow-sm);
}

.notification-card.unread {
  background: var(--color-primary-bg);
  border-color: rgba(99,102,241,0.15);
}

.notif-icon-wrap {
  flex-shrink: 0;
}

.notif-icon {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.ic-success { background: #22c55e; }
.ic-warning { background: #f59e0b; }
.ic-info { background: #3b82f6; }
.ic-danger { background: #ef4444; }
.ic-default { background: #6366f1; }

.notif-body {
  flex: 1;
  min-width: 0;
}

.notif-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.notif-title {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.notif-badges {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background: var(--color-primary);
  border-radius: 50%;
  box-shadow: 0 0 0 3px rgba(99,102,241,0.25);
}

.notif-content {
  font-size: 13px;
  color: var(--text-secondary);
  margin: 0 0 8px 0;
  line-height: 1.5;
}

.notif-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notif-time {
  font-size: 12px;
  color: var(--text-muted);
}

@media (max-width: 640px) {
  .page-hero { flex-direction: column; align-items: flex-start; }
}
</style>
