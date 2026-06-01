// 管理员存储 key
const ADMIN_TOKEN_KEY = 'gym_admin_token'
const ADMIN_USER_KEY = 'gym_admin_user'
const ADMIN_ID_KEY = 'gym_admin_id'

// 会员存储 key
const MEMBER_TOKEN_KEY = 'gym_member_token'
const MEMBER_USER_KEY = 'gym_member_user'
const MEMBER_ID_KEY = 'gym_member_id'

// 当前激活的角色 ('ADMIN' 或 'MEMBER')
const ACTIVE_ROLE_KEY = 'gym_active_role'

function detectRoleFromUrl() {
  const hash = window.location.hash
  if (hash.startsWith('#/admin')) {
    return 'ADMIN'
  } else if (hash.startsWith('#/member')) {
    return 'MEMBER'
  }
  return localStorage.getItem(ACTIVE_ROLE_KEY)
}

export function getToken(role = null) {
  const currentRole = role || detectRoleFromUrl()
  if (currentRole === 'ADMIN') {
    return localStorage.getItem(ADMIN_TOKEN_KEY)
  } else if (currentRole === 'MEMBER') {
    return localStorage.getItem(MEMBER_TOKEN_KEY)
  }
  return null
}

export function getTokenForRole(role) {
  if (role === 'ADMIN') {
    return localStorage.getItem(ADMIN_TOKEN_KEY)
  } else if (role === 'MEMBER') {
    return localStorage.getItem(MEMBER_TOKEN_KEY)
  }
  return null
}

export function setToken(token, role) {
  if (role === 'ADMIN') {
    localStorage.setItem(ADMIN_TOKEN_KEY, token)
  } else if (role === 'MEMBER') {
    localStorage.setItem(MEMBER_TOKEN_KEY, token)
  }
}

export function removeToken(role = null) {
  const currentRole = role || localStorage.getItem(ACTIVE_ROLE_KEY)
  if (currentRole === 'ADMIN') {
    localStorage.removeItem(ADMIN_TOKEN_KEY)
    localStorage.removeItem(ADMIN_USER_KEY)
    localStorage.removeItem(ADMIN_ID_KEY)
  } else if (currentRole === 'MEMBER') {
    localStorage.removeItem(MEMBER_TOKEN_KEY)
    localStorage.removeItem(MEMBER_USER_KEY)
    localStorage.removeItem(MEMBER_ID_KEY)
  }
}

export function removeRoleToken(role) {
  if (role === 'ADMIN') {
    localStorage.removeItem(ADMIN_TOKEN_KEY)
    localStorage.removeItem(ADMIN_USER_KEY)
    localStorage.removeItem(ADMIN_ID_KEY)
  } else if (role === 'MEMBER') {
    localStorage.removeItem(MEMBER_TOKEN_KEY)
    localStorage.removeItem(MEMBER_USER_KEY)
    localStorage.removeItem(MEMBER_ID_KEY)
  }
}

export function getUser(role = null) {
  const currentRole = role || localStorage.getItem(ACTIVE_ROLE_KEY)
  if (currentRole === 'ADMIN') {
    const user = localStorage.getItem(ADMIN_USER_KEY)
    return user ? JSON.parse(user) : null
  } else if (currentRole === 'MEMBER') {
    const user = localStorage.getItem(MEMBER_USER_KEY)
    return user ? JSON.parse(user) : null
  }
  return null
}

export function setUser(user, role) {
  if (role === 'ADMIN') {
    localStorage.setItem(ADMIN_USER_KEY, JSON.stringify(user))
  } else if (role === 'MEMBER') {
    localStorage.setItem(MEMBER_USER_KEY, JSON.stringify(user))
  }
}

export function getUserRole() {
  return localStorage.getItem(ACTIVE_ROLE_KEY)
}

export function setRole(role) {
  localStorage.setItem(ACTIVE_ROLE_KEY, role)
}

export function getUserId(role = null) {
  const currentRole = role || localStorage.getItem(ACTIVE_ROLE_KEY)
  if (currentRole === 'ADMIN') {
    const id = localStorage.getItem(ADMIN_ID_KEY)
    return id ? parseInt(id) : null
  } else if (currentRole === 'MEMBER') {
    const id = localStorage.getItem(MEMBER_ID_KEY)
    return id ? parseInt(id) : null
  }
  return null
}

export function setUserId(id, role) {
  if (role === 'ADMIN') {
    localStorage.setItem(ADMIN_ID_KEY, id.toString())
  } else if (role === 'MEMBER') {
    localStorage.setItem(MEMBER_ID_KEY, id.toString())
  }
}

export function isRoleLoggedIn(role) {
  if (role === 'ADMIN') {
    return !!localStorage.getItem(ADMIN_TOKEN_KEY)
  } else if (role === 'MEMBER') {
    return !!localStorage.getItem(MEMBER_TOKEN_KEY)
  }
  return false
}

export function getLoggedInRoles() {
  const roles = []
  if (localStorage.getItem(ADMIN_TOKEN_KEY)) {
    roles.push('ADMIN')
  }
  if (localStorage.getItem(MEMBER_TOKEN_KEY)) {
    roles.push('MEMBER')
  }
  return roles
}
