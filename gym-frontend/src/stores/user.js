import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken, getUser, setUser, getUserRole, setUserId, getUserId, setRole } from '@/utils/auth'
import { getUserIdFromToken } from '@/utils/jwt'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const user = ref(getUser())
  const role = ref(getUserRole())
  const userId = ref(getUserId())

  function login(newToken, userInfo) {
    token.value = newToken
    role.value = userInfo.role
    const id = getUserIdFromToken(newToken)
    user.value = { ...userInfo, id }
    userId.value = id
    setToken(newToken, userInfo.role)
    setUser(user.value, userInfo.role)
    setUserId(id, userInfo.role)
    setRole(userInfo.role)
  }

  function logout() {
    token.value = null
    user.value = null
    role.value = null
    userId.value = null
    removeToken()
  }

  function isLoggedIn() {
    return !!token.value
  }

  return {
    token,
    user,
    role,
    userId,
    login,
    logout,
    isLoggedIn
  }
})
