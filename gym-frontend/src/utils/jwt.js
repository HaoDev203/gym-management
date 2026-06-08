export function parseJwt(token) {
  try {
    const payload = token.split('.')[1]
    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    return JSON.parse(decoded)
  } catch (e) {
    return null
  }
}

export function getUserIdFromToken(token) {
  const claims = parseJwt(token)
  return claims ? parseInt(claims.sub) : null
}

/**
 * 检查 JWT token 是否已过期
 * @param {string} token - JWT token
 * @returns {boolean} - true 表示已过期或无效
 */
export function isTokenExpired(token) {
  try {
    const claims = parseJwt(token)
    if (!claims) return true
    
    // 检查 exp 字段（过期时间）
    if (claims.exp) {
      const now = Date.now() / 1000
      return now > claims.exp
    }
    
    return false
  } catch (e) {
    return true
  }
}
