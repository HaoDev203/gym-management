package com.gym.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StatisticsResponse {

    private Long totalMembers;

    private Long totalCourses;

    private Long totalBookings;

    private BigDecimal totalRevenue;

    private Long activeMembers;

    private Long activeCourses;

    private BigDecimal revenueToday;

    private BigDecimal revenueWeek;

    private BigDecimal revenueMonth;

    private List<DailyTrend> bookingTrend;

    private List<DailyTrend> revenueTrend;

    private List<CourseRank> topCourses;

    private Distribution distribution;

    private List<MemberBookingFrequency> memberBookingFrequency;

    private List<DailyLimitExceed> dailyLimitExceedStats;
}
