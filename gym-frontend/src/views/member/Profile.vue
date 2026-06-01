<template>
  <div class="profile-page" v-loading="loading">
    <div class="profile-hero">
      <div class="profile-avatar-section">
        <div class="avatar-ring">
          <el-avatar :size="80" :icon="UserFilled" class="profile-avatar" />
        </div>
        <div class="profile-intro">
          <h2 class="member-name">{{ profileForm.name || '健身达人' }}</h2>
          <p class="member-tagline">坚持运动，遇见更好的自己 💪</p>
          <div class="member-badges">
            <el-tag type="primary" effect="dark" round size="small">会员</el-tag>
            <el-tag v-if="memberStats.noShowCount === 0" type="success" effect="plain" round size="small">全勤达人</el-tag>
          </div>
        </div>
      </div>
      <div class="profile-stats">
        <div class="stat-block">
          <div class="stat-number">{{ memberStats.bookingCount || 0 }}</div>
          <div class="stat-label">预约次数</div>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-block">
          <div class="stat-number">{{ memberStats.orderCount || 0 }}</div>
          <div class="stat-label">订单数</div>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-block">
          <div class="stat-number" :class="{ 'stat-warning': memberStats.noShowCount > 0 }">
            {{ memberStats.noShowCount || 0 }}
          </div>
          <div class="stat-label">爽约次数</div>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <div class="profile-section">
        <div class="section-header">
          <el-icon class="section-icon"><User /></el-icon>
          <h3>基本信息</h3>
        </div>
        <el-form
          ref="formRef"
          :model="profileForm"
          :rules="rules"
          label-width="90px"
          class="profile-form"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="手机号">
                <el-input v-model="profileForm.username" disabled>
                  <template #prefix>
                    <el-icon><Phone /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="profileForm.name" placeholder="请输入姓名">
                  <template #prefix>
                    <el-icon><EditPen /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="性别">
                <el-radio-group v-model="profileForm.gender" class="gender-group">
                  <el-radio-button :label="1">
                    <el-icon><Male /></el-icon> 男
                  </el-radio-button>
                  <el-radio-button :label="0">
                    <el-icon><Female /></el-icon> 女
                  </el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年龄" prop="age">
                <el-input-number v-model="profileForm.age" :min="10" :max="99" controls-position="right" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="profileForm.email" placeholder="请输入邮箱">
                  <template #prefix>
                    <el-icon><Message /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号">
                <el-input v-model="profileForm.phone" placeholder="请输入手机号" prop="phone">
                  <template #prefix>
                    <el-icon><Phone /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <div class="profile-section">
        <div class="section-header">
          <el-icon class="section-icon"><Lock /></el-icon>
          <h3>账户安全</h3>
        </div>
        <div class="security-row">
          <div class="security-info">
            <el-icon class="security-dot"><CircleCheckFilled /></el-icon>
            <span>登录密码</span>
            <span class="security-hint">建议定期更换密码</span>
          </div>
          <el-button type="primary" plain size="small" @click="handleChangePassword">
            修改密码
          </el-button>
        </div>
      </div>

      <div class="profile-actions">
        <el-button type="primary" :loading="saving" @click="handleSave" size="large">
          <el-icon><Check /></el-icon> 保存修改
        </el-button>
      </div>
    </div>

    <el-dialog
      v-model="passwordDialogVisible"
      title="修改密码"
      width="420px"
      :close-on-click-modal="false"
      class="password-dialog"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="80px"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            show-password
            size="large"
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
            size="large"
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
            size="large"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="changingPassword" @click="handleConfirmChangePassword">
          确定修改
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  UserFilled, User, Phone, EditPen, Male, Female, Message,
  Lock, CircleCheckFilled, Check
} from '@element-plus/icons-vue'
import { getMemberById, updateMember, changePassword } from '@/api/member'
import { getMemberBookings } from '@/api/booking'
import { getMemberOrders } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { setUser } from '@/utils/auth'

const userStore = useUserStore()
const formRef = ref(null)
const passwordFormRef = ref(null)
const loading = ref(false)
const saving = ref(false)
const changingPassword = ref(false)
const passwordDialogVisible = ref(false)

const profileForm = reactive({
  id: '',
  username: '',
  name: '',
  gender: 0,
  phone: '',
  email: '',
  age: 18
})

const memberStats = reactive({
  bookingCount: 0,
  orderCount: 0,
  noShowCount: 0
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
  ]
}

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在 6-20 位之间', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在 6-20 位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const loadProfile = async () => {
  loading.value = true
  try {
    const memberId = userStore.user?.id
    if (!memberId) {
      ElMessage.error('请先登录')
      return
    }

    const res = await getMemberById(memberId)
    if (res.code === 200) {
      const data = res.data
      profileForm.id = data.id
      profileForm.username = data.username
      profileForm.name = data.name || ''
      profileForm.gender = data.gender || 0
      profileForm.phone = data.phone || ''
      profileForm.email = data.email || ''
      profileForm.age = data.age || 18
      memberStats.noShowCount = data.noShowCount || 0
    }

    try {
      const bookingsRes = await getMemberBookings(memberId)
      if (bookingsRes.code === 200) {
        memberStats.bookingCount = (bookingsRes.data || []).length
      }
    } catch {}

    try {
      const ordersRes = await getMemberOrders(memberId)
      if (ordersRes.code === 200) {
        memberStats.orderCount = (ordersRes.data || []).length
      }
    } catch {}
  } catch (error) {
    console.error('加载个人信息失败:', error)
    ElMessage.error('加载个人信息失败')
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        const res = await updateMember({
          id: profileForm.id,
          name: profileForm.name,
          gender: profileForm.gender,
          phone: profileForm.phone,
          email: profileForm.email,
          age: profileForm.age
        })
        if (res.code === 200) {
          ElMessage.success('保存成功')
          await loadProfile()
          userStore.user = {
            ...userStore.user,
            name: profileForm.name,
            phone: profileForm.phone,
            email: profileForm.email,
            age: profileForm.age
          }
          setUser(userStore.user)
        } else {
          ElMessage.error(res.message || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败，请稍后重试')
      } finally {
        saving.value = false
      }
    }
  })
}

const handleChangePassword = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

const handleConfirmChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      changingPassword.value = true
      try {
        const memberId = userStore.user?.id
        if (!memberId) {
          ElMessage.error('请先登录')
          return
        }

        const res = await changePassword(memberId, {
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })

        if (res.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          passwordDialogVisible.value = false
          setTimeout(() => {
            userStore.logout()
            window.location.href = '/#/login'
          }, 1000)
        } else {
          ElMessage.error(res.message || '修改密码失败')
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        ElMessage.error(error.response?.data?.message || '修改密码失败，请稍后重试')
      } finally {
        changingPassword.value = false
      }
    }
  })
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.profile-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.profile-hero {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-info) 100%);
  border-radius: var(--radius-xl);
  padding: 32px;
  margin-bottom: 24px;
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 24px;
  position: relative;
  overflow: hidden;
}

.profile-hero::before {
  content: '';
  position: absolute;
  width: 200px;
  height: 200px;
  background: rgba(255, 255, 255, 0.06);
  border-radius: 50%;
  top: -60px;
  right: -40px;
}

.profile-hero::after {
  content: '';
  position: absolute;
  width: 120px;
  height: 120px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  bottom: -30px;
  left: 30%;
}

.profile-avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
  position: relative;
  z-index: 1;
}

.avatar-ring {
  padding: 3px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
}

.profile-avatar {
  border: 3px solid rgba(255, 255, 255, 0.6);
}

.profile-intro {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.member-name {
  font-size: 22px;
  font-weight: 700;
  color: #fff;
  margin: 0;
}

.member-tagline {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.member-badges {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.profile-stats {
  display: flex;
  align-items: center;
  gap: 0;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-lg);
  padding: 16px 24px;
  position: relative;
  z-index: 1;
}

.stat-block {
  text-align: center;
  min-width: 70px;
}

.stat-number {
  font-size: 26px;
  font-weight: 700;
  color: #fff;
  line-height: 1.2;
}

.stat-number.stat-warning {
  color: #fde68a;
}

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 2px;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 0 20px;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.profile-section {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 24px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.section-icon {
  font-size: 20px;
  color: var(--color-primary);
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.profile-form {
  margin-top: 4px;
}

.profile-form .el-form-item {
  margin-bottom: 18px;
}

.gender-group {
  width: 100%;
}

.gender-group .el-radio-button__inner {
  display: flex;
  align-items: center;
  gap: 4px;
}

.security-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.security-info {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--text-secondary);
  font-size: 14px;
}

.security-dot {
  color: var(--color-success);
  font-size: 18px;
}

.security-hint {
  color: var(--text-muted);
  font-size: 12px;
}

.profile-actions {
  display: flex;
  justify-content: center;
  padding-top: 4px;
}

.profile-actions .el-button--large {
  padding: 12px 48px;
  font-size: 15px;
  border-radius: var(--radius-sm);
}
</style>
