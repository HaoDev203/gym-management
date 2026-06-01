import { createRouter, createWebHashHistory } from 'vue-router'
import { getTokenForRole, setRole, isRoleLoggedIn } from '@/utils/auth'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/member/Register.vue'),
    meta: { title: '会员注册' }
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/Login.vue'),
    meta: { title: '管理员登录' }
  },
  {
    path: '/member',
    name: 'MemberLayout',
    component: () => import('@/views/member/Layout.vue'),
    redirect: '/member/courses',
    meta: { title: '会员端', requiresAuth: true, role: 'MEMBER' },
    children: [
      {
        path: 'courses',
        name: 'MemberCourses',
        component: () => import('@/views/member/Courses.vue'),
        meta: { title: '课程列表' }
      },
      {
        path: 'course/:id',
        name: 'MemberCourseDetail',
        component: () => import('@/views/member/CourseDetail.vue'),
        meta: { title: '课程详情' }
      },
      {
        path: 'bookings',
        name: 'MemberBookings',
        component: () => import('@/views/member/Bookings.vue'),
        meta: { title: '我的预约' }
      },
      {
        path: 'orders',
        name: 'MemberOrders',
        component: () => import('@/views/member/Orders.vue'),
        meta: { title: '我的订单' }
      },
      {
        path: 'profile',
        name: 'MemberProfile',
        component: () => import('@/views/member/Profile.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: 'notifications',
        name: 'MemberNotifications',
        component: () => import('@/views/member/Notifications.vue'),
        meta: { title: '消息中心' }
      }
    ]
  },
  {
    path: '/admin',
    name: 'AdminLayout',
    component: () => import('@/views/admin/Layout.vue'),
    redirect: '/admin/dashboard',
    meta: { title: '管理端', requiresAuth: true, role: 'ADMIN' },
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'members',
        name: 'AdminMembers',
        component: () => import('@/views/admin/Members.vue'),
        meta: { title: '会员管理' }
      },
      {
        path: 'coaches',
        name: 'AdminCoaches',
        component: () => import('@/views/admin/Coaches.vue'),
        meta: { title: '教练管理' }
      },
      {
        path: 'courses',
        name: 'AdminCourses',
        component: () => import('@/views/admin/Courses.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'equipment',
        name: 'AdminEquipment',
        component: () => import('@/views/admin/Equipment.vue'),
        meta: { title: '器材管理' }
      },
      {
        path: 'orders',
        name: 'AdminOrders',
        component: () => import('@/views/admin/Orders.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('@/views/admin/Statistics.vue'),
        meta: { title: '数据统计' }
      },
      {
        path: 'admins',
        name: 'AdminAdmins',
        component: () => import('@/views/admin/Admins.vue'),
        meta: { title: '管理员管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '健身房管理系统'}`

  // 根据 URL 自动切换活跃角色
  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    setRole('ADMIN')
  } else if (to.path.startsWith('/member')) {
    setRole('MEMBER')
  }

  // 登录和注册页面始终可访问，不清除任何登录数据
  if (to.path === '/login' || to.path === '/register' || to.path === '/admin/login') {
    next()
    return
  }

  // 需要认证的页面
  if (to.meta.requiresAuth) {
    const requiredRole = to.meta.role
    if (!isRoleLoggedIn(requiredRole)) {
      next({ path: '/login', replace: true })
      return
    }
    next()
    return
  }

  next()
})

export default router
