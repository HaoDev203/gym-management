import request from '@/utils/request'

/**
 * 加入候补队列
 */
export function joinWaitlist(memberId, courseId) {
  return request({
    url: '/api/waitlist/join',
    method: 'post',
    params: { memberId, courseId }
  })
}

/**
 * 退出候补队列
 */
export function leaveWaitlist(memberId, courseId) {
  return request({
    url: '/api/waitlist/leave',
    method: 'delete',
    params: { memberId, courseId }
  })
}

/**
 * 查询候补排队人数
 */
export function getWaitlistCount(courseId) {
  return request({
    url: `/api/waitlist/count/${courseId}`,
    method: 'get'
  })
}

/**
 * 查询当前会员的候补位置
 */
export function getMyWaitlistPosition(memberId, courseId) {
  return request({
    url: '/api/waitlist/position',
    method: 'get',
    params: { memberId, courseId }
  })
}
