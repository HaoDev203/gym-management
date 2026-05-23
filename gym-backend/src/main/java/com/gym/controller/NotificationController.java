package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.response.NotificationResponse;
import com.gym.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知管理控制器。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/member/{memberId}")
    public BaseResponse<List<NotificationResponse>> listNotifications(@PathVariable Long memberId) {
        List<NotificationResponse> notifications = notificationService.listMemberNotifications(memberId);
        return BaseResponse.success(notifications);
    }

    @GetMapping("/unread-count")
    public BaseResponse<Integer> getUnreadCount(@RequestParam(required = false) Long memberId) {
        if (memberId == null) {
            return BaseResponse.success(0);
        }
        int count = notificationService.getUnreadCount(memberId);
        return BaseResponse.success(count);
    }

    @PostMapping("/{id}/read")
    public BaseResponse<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteNotification(@PathVariable Long id) {
        notificationService.softDelete(id);
        return BaseResponse.success(null);
    }
}
