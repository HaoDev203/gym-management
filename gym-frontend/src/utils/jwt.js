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
