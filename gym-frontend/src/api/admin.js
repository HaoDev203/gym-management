import request from '@/utils/request'

/**
 * 获取教练列表
 */
export function getCoachList() {
  return request({
    url: '/api/coach/list',
    method: 'get'
  })
}

/**
 * 创建教练
 */
export function createCoach(data) {
  return request({
    url: '/api/coach/add',
    method: 'post',
    data
  })
}

/**
 * 更新教练
 */
export function updateCoach(id, data) {
  return request({
    url: `/api/coach/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除教练
 */
export function deleteCoach(id) {
  return request({
    url: `/api/coach/${id}`,
    method: 'delete'
  })
}

/**
 * 获取器材列表
 */
export function getEquipmentList() {
  return request({
    url: '/api/equipment/list',
    method: 'get'
  })
}

/**
 * 创建器材
 */
export function createEquipment(data) {
  return request({
    url: '/api/equipment/add',
    method: 'post',
    data
  })
}

/**
 * 更新器材
 */
export function updateEquipment(id, data) {
  return request({
    url: `/api/equipment/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除器材
 */
export function deleteEquipment(id) {
  return request({
    url: `/api/equipment/${id}`,
    method: 'delete'
  })
}

/**
 * 获取统计数据
 */
export function getStatistics() {
  return request({
    url: '/api/statistics/overview',
    method: 'get'
  })
}

/**
 * 获取管理员列表
 */
export function getAdminList() {
  return request({
    url: '/api/admin/list',
    method: 'get'
  })
}

/**
 * 创建管理员
 */
export function createAdmin(data) {
  return request({
    url: '/api/admin/add',
    method: 'post',
    data
  })
}

/**
 * 更新管理员
 */
export function updateAdmin(data) {
  return request({
    url: `/api/admin/${data.id}`,
    method: 'put',
    data
  })
}

/**
 * 删除管理员
 */
export function deleteAdmin(id) {
  return request({
    url: `/api/admin/${id}`,
    method: 'delete'
  })
}

/**
 * 管理员登录
 */
export function adminLogin(data) {
  return request({
    url: '/api/auth/admin/login',
    method: 'post',
    data
  })
}
