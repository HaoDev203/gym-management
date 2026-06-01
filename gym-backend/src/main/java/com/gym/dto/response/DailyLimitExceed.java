package com.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每日预约上限触发统计 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLimitExceed {

    private String date;

    private Long exceedCount;

    private Long totalMembers;

    private Long totalBookings;
}
