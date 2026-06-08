<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">健身房管理系统 - 管理员登录</h2>
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="rules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入管理员账号"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
            block
          >
            登录
          </el-button>
        </el-form-item>
        <div class="login-footer">
          <span>返回会员登录？</span>
          <router-link to="/login">会员入口</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminLogin } from '@/api/admin'
import { getCurrentAdmin } from '@/api/admin'
import { getUserIdFromToken } from '@/utils/jwt'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        clearOldAdminData()

        const res = await adminLogin(loginForm)
        if (res.code === 200) {
          const token = res.data

          localStorage.setItem('gym_admin_token', token)

          const adminId = getUserIdFromToken(token)

          // 获取真实管理员信息（不再需要传 adminId）
          const adminInfo = await getCurrentAdmin()
          const adminRole = adminInfo.data.role
          const isSuperAdmin = adminRole == 2

          const userInfo = {
            role: 'ADMIN',
            id: adminId,
            username: adminInfo.data.username,
            name: adminInfo.data.name,
            adminRole,
            isAdmin: isSuperAdmin
          }

          userStore.login(token, userInfo)

          ElMessage.success(`欢迎回来，${isSuperAdmin ? '超级管理员' : '管理员'} ${userInfo.name}`)
          router.push('/admin/dashboard')
        } else {
          ElMessage.error(res.message || '登录失败')
        }
      } catch (error) {
        console.error('登录失败:', error)
        const errorMsg = error.response?.data?.message || error.message || '登录失败，请稍后重试'
        ElMessage.error(errorMsg)
      } finally {
        loading.value = false
      }
    }
  })
}

function clearOldAdminData() {
  localStorage.removeItem('gym_admin_token')
  localStorage.removeItem('gym_admin_user')
  localStorage.removeItem('gym_admin_id')
  localStorage.removeItem('gym_active_role')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
}

.login-box {
  width: 420px;
  padding: 44px 40px 36px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

.login-title {
  text-align: center;
  margin-bottom: 28px;
  font-size: 26px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--color-primary), var(--color-info));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-form {
  margin-top: 8px;
}

.login-footer {
  text-align: center;
  margin-top: 16px;
  color: var(--text-muted);
  font-size: 14px;
}

.login-footer a {
  color: var(--color-primary);
  text-decoration: none;
  margin-left: 6px;
  font-weight: 500;
}

.login-footer a:hover {
  color: var(--color-primary-light);
}
</style>
