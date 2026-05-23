import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} data 登录数据 {username, password}
 * @returns {Promise} 返回 Promise 对象
 */
export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户注册
 * @param {Object} data 注册数据 {username, password, name, phone}
 * @returns {Promise} 返回 Promise 对象
 */
export function register(data) {
  return request({
    url: '/api/auth/register',
    method: 'post',
    data
  })
}

/**
 * 管理员登录
 * @param {Object} data 登录数据 {username, password}
 * @returns {Promise} 返回 Promise 对象
 */
export function adminLogin(data) {
  return request({
    url: '/api/auth/admin/login',
    method: 'post',
    data
  })
}
