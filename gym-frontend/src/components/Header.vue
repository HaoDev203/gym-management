<template>
  <header class="header">
    <div class="header-left">
      <h2 class="logo">健身房管理系统</h2>
    </div>
    <div class="header-right">
      <div class="user-info">
        <el-badge v-if="showNotification" :value="unreadCount" :hidden="unreadCount === 0" class="notification-badge">
          <el-icon @click="goToNotifications" class="notification-icon">
            <Bell />
          </el-icon>
        </el-badge>
        <el-dropdown @command="handleCommand" trigger="click">
          <div class="user-trigger">
            <el-avatar :size="32" :icon="User" class="user-avatar" />
            <span class="username">{{ userStore.user?.name || userStore.user?.username || '用户' }}</span>
            <el-icon class="arrow-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <div class="user-card">
                <el-avatar :size="48" :icon="User" class="card-avatar" />
                <div class="card-name">{{ userStore.user?.name || '用户' }}</div>
                <div class="card-username">@{{ userStore.user?.username }}</div>
                <div class="card-role">
                  <el-tag v-if="userStore.user?.role === 'ADMIN' && userStore.user?.isAdmin" type="warning" size="small" effect="plain">超级管理员</el-tag>
                  <el-tag v-else-if="userStore.user?.role === 'ADMIN'" type="primary" size="small" effect="plain">普通管理员</el-tag>
                  <el-tag v-else type="success" size="small" effect="plain">会员</el-tag>
                </div>
              </div>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Bell, User, ArrowDown, SwitchButton } from '@element-plus/icons-vue'
import { getUnreadCount } from '@/api/notification'

defineProps({
  showNotification: {
    type: Boolean,
    default: true
  }
})

const router = useRouter()
const userStore = useUserStore()

const unreadCount = ref(0)

const goToNotifications = () => {
  router.push('/member/notifications')
}

const handleCommand = (command) => {
  if (command === 'logout') {
    handleLogout()
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const loadUnreadCount = async () => {
  try {
    const memberId = userStore.user?.id
    if (!memberId) return
    const res = await getUnreadCount(memberId)
    if (res.code === 200) {
      unreadCount.value = res.data
    }
  } catch (error) {
    console.error('加载未读消息数失败:', error)
  }
}

onMounted(() => {
  loadUnreadCount()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 28px;
  height: 64px;
  background: var(--bg-header);
  border-bottom: 1px solid var(--border-color);
  backdrop-filter: blur(12px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--color-primary), var(--color-info));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
  letter-spacing: 1px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.notification-badge {
  cursor: pointer;
}

.notification-icon {
  font-size: 22px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: color var(--transition-fast), transform var(--transition-fast);
  padding: 6px;
  border-radius: var(--radius-sm);
}

.notification-icon:hover {
  color: var(--color-primary);
  background: var(--color-primary-bg);
  transform: scale(1.1);
}

.username {
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
}

.user-trigger {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: var(--radius-md);
  transition: background var(--transition-fast);
}

.user-trigger:hover {
  background: var(--color-primary-bg);
}

.user-avatar {
  flex-shrink: 0;
}

.arrow-icon {
  font-size: 12px;
  color: var(--text-muted);
  transition: transform var(--transition-fast);
}

.user-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 24px 12px;
  min-width: 180px;
}

.card-avatar {
  margin-bottom: 10px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-info));
}

.card-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 2px;
}

.card-username {
  font-size: 12px;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.card-role {
  margin-bottom: 4px;
}
</style>
