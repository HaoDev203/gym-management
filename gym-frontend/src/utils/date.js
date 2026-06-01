/**
 * 日期格式化工具
 * @param {string|Date} date 日期对象或日期字符串
 * @returns {string} 格式化后的日期字符串 YYYY-MM-DD
 */
export function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/**
 * 时间格式化
 * @param {string|Date} date 日期对象或日期字符串
 * @returns {string} 格式化后的时间字符串 HH:mm
 */
export function formatTime(date) {
  if (!date) return ''
  const d = new Date(date)
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`
}

/**
 * 时间格式化（同 formatTime）。
 * @param {string|Date} date 日期对象或日期字符串
 * @returns {string} 格式化后的时间字符串 HH:mm
 */
export const formatTimeOnly = formatTime

/**
 * 日期时间格式化
 * @param {string|Date} date 日期对象或日期字符串
 * @returns {string} 格式化后的日期时间字符串 YYYY-MM-DD HH:mm
 */
export function formatDateTime(date) {
  if (!date) return ''
  return `${formatDate(date)} ${formatTime(date)}`
}

/**
 * 相对时间格式化
 * @param {string|Date} date 日期对象或日期字符串
 * @returns {string} 相对时间描述
 */
export function formatRelativeTime(date) {
  if (!date) return ''
  const now = new Date()
  const target = new Date(date)
  const diff = now - target
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) {
    return '刚刚'
  } else if (minutes < 60) {
    return `${minutes}分钟前`
  } else if (hours < 24) {
    return `${hours}小时前`
  } else if (days < 30) {
    return `${days}天前`
  } else {
    return formatDate(date)
  }
}
