import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken, getUser, setUser, getUserRole, setUserId, getUserId, setRole } from '@/utils/auth'
import { getUserIdFromToken } from '@/utils/jwt'
import { getCurrentAdmin } from '@/api/admin'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const storedUser = getUser()
  const role = ref(getUserRole())
  const userId = ref(getUserId())

  if (storedUser && storedUser.role === 'ADMIN' && storedUser.adminRole === undefined) {
    const id = storedUser.id || getUserIdFromToken(token.value)
    if (id) {
      getCurrentAdmin(id).then(res => {
        if (user.value && user.value.adminRole !== undefined) return
        const adminRole = res.data.role
        storedUser.adminRole = adminRole
        storedUser.isAdmin = adminRole == 2
        user.value = { ...storedUser }
        setUser(storedUser, 'ADMIN')
      }).catch(() => {})
    }
  }

  if (storedUser && storedUser.role === 'ADMIN' && storedUser.isAdmin === undefined && storedUser.adminRole !== undefined) {
    storedUser.isAdmin = storedUser.adminRole == 2
  }

  const user = ref(storedUser)

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
