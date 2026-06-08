<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">健身房管理系统</h2>

      <div class="login-type-switch">
        <span
          class="switch-btn"
          :class="{ active: loginType === 'member' }"
          @click="switchType('member')"
        >会员登录</span>
        <span
          class="switch-btn"
          :class="{ active: loginType === 'admin' }"
          @click="switchType('admin')"
        >管理员登录</span>
      </div>

      <el-form ref="formRef" :model="loginForm" :rules="currentRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            :placeholder="loginType === 'member' ? '请输入手机号' : '请输入用户名'"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
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
        <div class="login-footer" v-if="loginType === 'member'">
          <span>还没有账号？</span>
          <router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login as memberLogin } from '@/api/auth'
import { adminLogin, getCurrentAdmin } from '@/api/admin'
import { getMemberById } from '@/api/member'
import { useUserStore } from '@/stores/user'
import { getUserIdFromToken } from '@/utils/jwt'
import { setUser } from '@/utils/auth'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const loginType = ref('member')

const loginForm = reactive({
  username: '',
  password: ''
})

const memberRules = {
  username: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ]
}

const adminRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ]
}

const currentRules = computed(() => {
  return loginType.value === 'member' ? memberRules : adminRules
})

function switchType(type) {
  loginType.value = type
  loginForm.username = ''
  loginForm.password = ''
  formRef.value?.clearValidate()
}

const handleLogin = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    if (loginType.value === 'admin') {
      const res = await adminLogin(loginForm)
      if (res.code === 200) {
        const token = res.data
        const adminId = getUserIdFromToken(token)
        
        // 获取真实管理员信息
        const adminInfo = await getCurrentAdmin(adminId)
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
        router.replace('/admin/dashboard')
      } else {
        ElMessage.error(res.message || '登录失败')
      }
    } else {
      const res = await memberLogin(loginForm)
      if (res.code === 200) {
        const token = res.data
        const userId = getUserIdFromToken(token)
        userStore.login(token, {
          role: 'MEMBER',
          name: '',
          phone: '',
          email: ''
        })
        const memberRes = await getMemberById(userId)
        if (memberRes.code === 200) {
          const member = memberRes.data
          userStore.user = {
            role: 'MEMBER',
            name: member.name,
            phone: member.phone,
            email: member.email,
            id: userId
          }
          setUser(userStore.user, 'MEMBER')
          ElMessage.success('登录成功')
          router.replace('/member/courses')
        } else {
          ElMessage.error(memberRes.message || '获取用户信息失败')
        }
      } else {
        ElMessage.error(res.message || '登录失败')
      }
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '登录失败，请稍后重试'
    ElMessage.error(errorMsg)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  width: 600px;
  height: 600px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  top: -200px;
  right: -150px;
}

.login-container::after {
  content: '';
  position: absolute;
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.04);
  border-radius: 50%;
  bottom: -150px;
  left: -100px;
}

.login-box {
  width: 420px;
  padding: 44px 40px 36px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  position: relative;
  z-index: 1;
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

.login-type-switch {
  display: flex;
  justify-content: center;
  margin-bottom: 28px;
  background: var(--border-light);
  border-radius: var(--radius-sm);
  padding: 4px;
}

.switch-btn {
  flex: 1;
  text-align: center;
  padding: 8px 20px;
  cursor: pointer;
  color: var(--text-secondary);
  font-size: 14px;
  border-radius: 6px;
  transition: all var(--transition-normal);
  font-weight: 500;
}

.switch-btn.active {
  color: var(--text-inverse);
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark));
  box-shadow: var(--shadow-sm);
}

.switch-btn:hover:not(.active) {
  color: var(--color-primary);
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
