import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getToken, removeToken } from './auth'
import { isTokenExpired } from './jwt'

function detectRoleFromUrl() {
  const hash = window.location.hash
  if (hash.startsWith('#/admin')) {
    return 'ADMIN'
  } else if (hash.startsWith('#/member')) {
    return 'MEMBER'
  }
  return localStorage.getItem('gym_active_role')
}

const request = axios.create({
  baseURL: '',
  timeout: 15000
})

request.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      // 检查 token 是否已过期
      if (isTokenExpired(token)) {
        console.warn('Token 已过期，清除旧 token')
        removeToken(detectRoleFromUrl())
        return Promise.reject(new Error('Token expired'))
      }
      
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res
    }
    if (res.code === 401) {
      removeToken(detectRoleFromUrl())
      ElMessage.error(res.message || '登录已过期，请重新登录')
      router.push('/login')
      return Promise.reject(new Error(res.message || '登录已过期'))
    }
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    ElMessage.error('网络异常，请稍后再试')
    return Promise.reject(error)
  }
)

export default request
