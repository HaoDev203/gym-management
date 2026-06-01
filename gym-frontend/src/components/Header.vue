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
        <span class="username">{{ userStore.user?.name || '用户' }}</span>
        <el-dropdown @command="handleCommand">
          <span class="user-avatar">
            <el-avatar :size="32" :icon="User" />
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
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
import { Bell, User } from '@element-plus/icons-vue'
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
  } else if (command === 'profile') {
    router.push('/member/profile')
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

.user-avatar {
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 2px;
  border-radius: 50%;
  transition: box-shadow var(--transition-fast);
}

.user-avatar:hover {
  box-shadow: 0 0 0 3px var(--color-primary-bg);
}
</style>
