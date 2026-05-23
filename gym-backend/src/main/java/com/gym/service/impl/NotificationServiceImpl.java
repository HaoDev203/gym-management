package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.dto.response.NotificationResponse;
import com.gym.entity.Notification;
import com.gym.mapper.NotificationMapper;
import com.gym.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void send(Long memberId, String title, String content, Integer type) {
        Notification notification = new Notification();
        notification.setMemberId(memberId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(0);
        notification.setCreatedAt(LocalDateTime.now());
        notificationMapper.insert(notification);
    }

    @Override
    public List<NotificationResponse> listMemberNotifications(Long memberId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getMemberId, memberId);
        wrapper.eq(Notification::getIsDeleted, 0);
        wrapper.orderByDesc(Notification::getCreatedAt);
        List<Notification> notifications = notificationMapper.selectList(wrapper);
        return notifications.stream().map(this::toNotificationResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null) {
            notification.markAsRead();
            notificationMapper.updateById(notification);
        }
    }

    @Override
    public int getUnreadCount(Long memberId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getMemberId, memberId);
        wrapper.eq(Notification::getIsRead, 0);
        wrapper.eq(Notification::getIsDeleted, 0);
        Long count = notificationMapper.selectCount(wrapper);
        return count != null ? count.intValue() : 0;
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        notificationMapper.softDelete(id);
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setType(notification.getType());
        response.setIsRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
