import request from '@/utils/request'

/**
 * 获取会员信息
 */
export function getMemberById(id) {
  return request({
    url: `/api/member/${id}`,
    method: 'get'
  })
}

/**
 * 更新会员信息
 */
export function updateMember(data) {
  return request({
    url: `/api/member/${data.id}`,
    method: 'put',
    data
  })
}

/**
 * 获取所有会员列表（管理员）
 */
export function getAllMembers() {
  return request({
    url: '/api/member/list',
    method: 'get'
  })
}

/**
 * 禁用会员
 */
export function banMember(id) {
  return request({
    url: `/api/member/ban/${id}`,
    method: 'put'
  })
}

/**
 * 解禁会员
 */
export function unbanMember(id) {
  return request({
    url: `/api/member/unban/${id}`,
    method: 'put'
  })
}

/**
 * 修改会员密码
 */
export function changePassword(memberId, data) {
  return request({
    url: `/api/member/change-password/${memberId}`,
    method: 'post',
    data
  })
}
