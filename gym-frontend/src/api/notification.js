import request from '@/utils/request'

/**
 * 获取通知列表
 */
export function getNotifications(params) {
  const memberId = params?.memberId || ''
  return request({
    url: `/api/notification/member/${memberId}`,
    method: 'get'
  })
}

/**
 * 获取未读消息数量
 */
export function getUnreadCount(memberId) {
  return request({
    url: '/api/notification/unread-count',
    method: 'get',
    params: { memberId }
  })
}

/**
 * 标记通知为已读
 */
export function markAsRead(id) {
  return request({
    url: `/api/notification/${id}/read`,
    method: 'post'
  })
}

/**
 * 批量标记通知为已读
 */
export function markBatchAsRead(ids) {
  return request({
    url: '/api/notification/batch-read',
    method: 'put',
    data: ids
  })
}

/**
 * 删除通知
 */
export function deleteNotification(id) {
  return request({
    url: `/api/notification/${id}`,
    method: 'delete'
  })
}
