package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.response.StatisticsResponse;
import com.gym.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统计控制器。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取系统概览统计数据。
     *
     * @return 统计数据响应
     */
    @GetMapping("/overview")
    public BaseResponse<StatisticsResponse> getOverview() {
        StatisticsResponse statistics = statisticsService.getOverview();
        return BaseResponse.success(statistics);
    }
}
