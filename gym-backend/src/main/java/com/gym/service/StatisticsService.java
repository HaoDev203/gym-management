package com.gym.service;

import com.gym.dto.response.StatisticsResponse;

public interface StatisticsService {

    /**
     * 获取系统概览统计数据（含趋势、排行、分布）。
     *
     * @return 统计数据响应
     */
    StatisticsResponse getOverview();
}
