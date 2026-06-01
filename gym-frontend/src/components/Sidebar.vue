<template>
  <aside class="sidebar">
    <el-menu
      :default-active="activeMenu"
      router
      class="sidebar-menu"
    >
      <template v-if="userStore.user?.role === 'MEMBER'">
        <el-menu-item index="/member/courses">
          <el-icon><Grid /></el-icon>
          <span>课程列表</span>
        </el-menu-item>
        <el-menu-item index="/member/bookings">
          <el-icon><Calendar /></el-icon>
          <span>我的预约</span>
        </el-menu-item>
        <el-menu-item index="/member/profile">
          <el-icon><User /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
        <el-menu-item index="/member/notifications">
          <el-icon><Bell /></el-icon>
          <span>消息中心</span>
        </el-menu-item>
      </template>

      <template v-else-if="userStore.user?.role === 'ADMIN'">
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/members">
          <el-icon><User /></el-icon>
          <span>会员管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/coaches">
          <el-icon><UserFilled /></el-icon>
          <span>教练管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/courses">
          <el-icon><Grid /></el-icon>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/equipment">
          <el-icon><Setting /></el-icon>
          <span>器材管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/statistics">
          <el-icon><DataLine /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/admin/admins" v-if="userStore.user?.isAdmin">
          <el-icon><Lock /></el-icon>
          <span>管理员管理</span>
        </el-menu-item>
      </template>
    </el-menu>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  Grid,
  Calendar,
  User,
  Bell,
  DataAnalysis,
  UserFilled,
  Setting,
  Document,
  Lock,
  DataLine
} from '@element-plus/icons-vue'

const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
</script>

<style scoped>
.sidebar {
  width: 220px;
  height: calc(100vh - 64px);
  background: var(--bg-sidebar);
  border-right: 1px solid var(--border-color);
  overflow-y: auto;
  padding: 12px 0;
}

.sidebar-menu {
  border-right: none !important;
  background: transparent !important;
}

.sidebar-menu .el-menu-item {
  height: 44px;
  line-height: 44px;
  margin: 4px 12px;
  border-radius: var(--radius-sm);
  font-size: 14px;
  transition: all var(--transition-fast);
}

.sidebar-menu .el-menu-item:hover {
  background: var(--color-primary-bg) !important;
  color: var(--color-primary) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background: var(--color-primary-bg) !important;
  color: var(--color-primary) !important;
  font-weight: 600;
}

.sidebar-menu .el-menu-item .el-icon {
  font-size: 18px;
}
</style>
