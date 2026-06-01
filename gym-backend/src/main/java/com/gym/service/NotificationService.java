package com.gym.service;

import com.gym.dto.response.NotificationResponse;

import java.util.List;

/**
 * 通知管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface NotificationService {

    /**
     * 发送通知。
     *
     * @param memberId 会员 ID
     * @param title    通知标题
     * @param content  通知内容
     * @param type     通知类型
     */
    void send(Long memberId, String title, String content, Integer type);

    /**
     * 查询会员通知列表。
     *
     * @param memberId 会员 ID
     * @return 通知列表
     */
    List<NotificationResponse> listMemberNotifications(Long memberId);

    /**
     * 标记通知为已读。
     *
     * @param id 通知 ID
     */
    void markAsRead(Long id);

    /**
     * 获取会员未读通知数量。
     *
     * @param memberId 会员 ID
     * @return 未读通知数量
     */
    int getUnreadCount(Long memberId);

    /**
     * 软删除已读通知。
     *
     * @param id 通知 ID
     */
    void softDelete(Long id);
}
