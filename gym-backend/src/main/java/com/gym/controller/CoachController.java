package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.CoachRequest;
import com.gym.dto.response.CoachResponse;
import com.gym.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 教练管理控制器。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/coach")
public class CoachController {

    @Autowired
    private CoachService coachService;

    /**
     * 获取所有教练列表。
     *
     * @return 教练列表响应
     */
    @GetMapping("/list")
    public BaseResponse<List<CoachResponse>> listCoaches() {
        List<CoachResponse> coaches = coachService.listAll();
        return BaseResponse.success(coaches);
    }

    /**
     * 根据 ID 获取教练信息。
     *
     * @param id 教练 ID
     * @return 教练信息响应
     */
    @GetMapping("/{id}")
    public BaseResponse<CoachResponse> getCoach(@PathVariable Long id) {
        CoachResponse coach = coachService.getCoachById(id);
        return BaseResponse.success(coach);
    }

    /**
     * 创建教练。
     *
     * @param request 教练创建请求
     * @return 教练 ID 响应
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCoach(@RequestBody @Valid CoachRequest request) {
        CoachResponse response = coachService.createCoach(request);
        return BaseResponse.success(response.getId());
    }

    /**
     * 更新教练信息。
     *
     * @param id      教练 ID
     * @param request 教练更新请求
     * @return 空响应
     */
    @PutMapping("/{id}")
    public BaseResponse<Void> updateCoach(@PathVariable Long id, @RequestBody @Valid CoachRequest request) {
        coachService.updateCoach(id, request);
        return BaseResponse.success(null);
    }

    /**
     * 删除教练。
     *
     * @param id 教练 ID
     * @return 空响应
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return BaseResponse.success(null);
    }
}
