import request from '@/utils/request'

/**
 * 预约课程
 */
export function bookCourse(data) {
  return request({
    url: '/api/booking/book',
    method: 'post',
    params: { memberId: data.memberId },
    data: { courseId: data.courseId }
  })
}

/**
 * 取消预约
 */
export function cancelBooking(data) {
  return request({
    url: '/api/booking/cancel',
    method: 'post',
    params: { memberId: data.memberId, orderId: data.bookingId }
  })
}

/**
 * 获取会员预约记录
 */
export function getMemberBookings(memberId) {
  return request({
    url: `/api/booking/member/${memberId}`,
    method: 'get'
  })
}

/**
 * 签到
 */
export function checkInBooking(orderId) {
  return request({
    url: `/api/order/checkin/${orderId}`,
    method: 'put'
  })
}
