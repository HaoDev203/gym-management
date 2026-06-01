package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.service.WaitlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 候补排队控制器。
 *
 * @author liuxinsi
 * @date 2026-05-23
 */
@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    @Autowired
    private WaitlistService waitlistService;

    /**
     * 加入候补队列。
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     * @return 排队位置
     */
    @PostMapping("/join")
    public BaseResponse<Map<String, Integer>> joinWaitlist(@RequestParam Long memberId, @RequestParam Long courseId) {
        int position = waitlistService.joinWaitlist(memberId, courseId);
        return BaseResponse.success(Map.of("position", position));
    }

    /**
     * 退出候补队列。
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     */
    @DeleteMapping("/leave")
    public BaseResponse<Void> leaveWaitlist(@RequestParam Long memberId, @RequestParam Long courseId) {
        waitlistService.leaveWaitlist(memberId, courseId);
        return BaseResponse.success(null);
    }

    /**
     * 查询排队人数。
     *
     * @param courseId 课程 ID
     * @return 排队人数
     */
    @GetMapping("/count/{courseId}")
    public BaseResponse<Map<String, Integer>> getWaitlistCount(@PathVariable Long courseId) {
        int count = waitlistService.getWaitlistCount(courseId);
        return BaseResponse.success(Map.of("count", count));
    }

    /**
     * 查询当前会员在指定课程的候补位置。
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     * @return 排队位置（0 表示不在队列中）
     */
    @GetMapping("/position")
    public BaseResponse<Map<String, Integer>> getMyPosition(@RequestParam Long memberId, @RequestParam Long courseId) {
        int position = waitlistService.getMemberPosition(memberId, courseId);
        return BaseResponse.success(Map.of("position", position));
    }
}
