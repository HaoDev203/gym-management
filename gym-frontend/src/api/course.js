import request from '@/utils/request'

/**
 * 获取课程列表
 */
export function getCourseList() {
  return request({
    url: '/api/course/list',
    method: 'get'
  })
}

/**
 * 获取课程详情
 */
export function getCourseById(id) {
  return request({
    url: `/api/course/${id}`,
    method: 'get'
  })
}

/**
 * 获取所有课程列表（管理员）
 */
export function getAllCourses() {
  return request({
    url: '/api/course/admin/list',
    method: 'get'
  })
}

/**
 * 创建课程
 */
export function createCourse(data) {
  return request({
    url: '/api/course/add',
    method: 'post',
    data
  })
}

/**
 * 更新课程
 */
export function updateCourse(id, data) {
  return request({
    url: `/api/course/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除课程
 */
export function deleteCourse(id) {
  return request({
    url: `/api/course/${id}`,
    method: 'delete'
  })
}

/**
 * 恢复已取消的课程
 */
export function restoreCourse(id) {
  return request({
    url: `/api/course/restore/${id}`,
    method: 'put'
  })
}

/**
 * 取消课程
 */
export function cancelCourse(id) {
  return request({
    url: `/api/course/cancel/${id}`,
    method: 'put'
  })
}
