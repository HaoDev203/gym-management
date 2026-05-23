package com.gym.service;

import com.gym.dto.response.BookingResponse;

/**
 * 候补排队服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-23
 */
public interface WaitlistService {

    /**
     * 加入候补队列。
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     * @return 排队位置（第几位）
     */
    int joinWaitlist(Long memberId, Long courseId);

    /**
     * 退出候补队列。
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     */
    void leaveWaitlist(Long memberId, Long courseId);

    /**
     * 获取课程候补排队人数。
     *
     * @param courseId 课程 ID
     * @return 排队人数
     */
    int getWaitlistCount(Long courseId);

    /**
     * 获取会员在指定课程候补队列中的位置。
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     * @return 排队位置，不在队列中返回 0
     */
    int getMemberPosition(Long memberId, Long courseId);

    /**
     * 自动转正：课程名额释放时，将队列第一个候补会员转为正式预约。
     *
     * @param courseId 课程 ID
     * @return 转正成功的预约响应，无候补时返回 null
     */
    BookingResponse promoteFirst(Long courseId);
}
