import request from '@/utils/request'

/**
 * 获取会员订单列表
 */
export function getMemberOrders(memberId) {
  return request({
    url: `/api/order/member/${memberId}`,
    method: 'get'
  })
}

/**
 * 获取所有订单列表（管理员）
 */
export function getAllOrders() {
  return request({
    url: '/api/order/list',
    method: 'get'
  })
}

/**
 * 确认支付
 */
export function confirmPayment(id) {
  return request({
    url: `/api/order/confirm/${id}`,
    method: 'put'
  })
}

/**
 * 取消订单
 */
export function cancelOrder(id) {
  return request({
    url: `/api/order/cancel/${id}`,
    method: 'put'
  })
}

/**
 * 完成订单（签到）
 */
export function checkInOrder(id) {
  return request({
    url: `/api/order/checkin/${id}`,
    method: 'put'
  })
}

/**
 * 管理员签到
 */
export function adminCheckInOrder(id) {
  return request({
    url: `/api/order/admin-checkin/${id}`,
    method: 'put'
  })
}

/**
 * 标记爽约
 */
export function markNoShow(id) {
  return request({
    url: `/api/order/no-show/${id}`,
    method: 'put'
  })
}

/**
 * 删除订单
 */
export function deleteOrder(id) {
  return request({
    url: `/api/order/${id}`,
    method: 'delete'
  })
}

/**
 * 标记为已支付
 */
export function markAsPaid(id) {
  return request({
    url: `/api/order/mark-paid/${id}`,
    method: 'put'
  })
}

/**
 * 标记为未支付
 */
export function markAsUnpaid(id) {
  return request({
    url: `/api/order/mark-unpaid/${id}`,
    method: 'put'
  })
}
