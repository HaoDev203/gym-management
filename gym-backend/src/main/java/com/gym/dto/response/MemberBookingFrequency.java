package com.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员预约频次统计 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBookingFrequency {

    private Long memberId;

    private String memberName;

    private String memberPhone;

    private Long bookingCount;

    private Long todayCount;

    private Long weekCount;

    private Long monthCount;
}
