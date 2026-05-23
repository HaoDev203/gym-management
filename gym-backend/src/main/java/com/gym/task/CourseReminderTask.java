package com.gym.task;

import com.gym.entity.Course;
import com.gym.entity.Notification;
import com.gym.entity.Order;
import com.gym.enums.NotificationType;
import com.gym.enums.OrderStatus;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.NotificationMapper;
import com.gym.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程提醒定时任务
 * 
 * <p>用途：在课程开始前自动发送提醒通知给已预约的会员。</p>
 * 
 * @author AI Assistant
 * @date 2026-05-21
 */
@Slf4j
@Component
public class CourseReminderTask {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 课前 1 小时提醒任务
     * 每天 6:00-22:00 每小时执行一次
     */
    @Scheduled(cron = "0 0 6-22 * * ?")
    public void oneHourBeforeReminder() {
        log.info("开始执行课前 1 小时提醒任务");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusHours(1);
        
        List<Course> courses = courseMapper.selectAvailableCourses();
        
        int reminderCount = 0;
        for (Course course : courses) {
            if (isSameTime(course.getStartTime(), targetTime, 60)) {
                List<Order> orders = orderMapper.selectByCourseId(course.getId());
                for (Order order : orders) {
                    if (order.getStatus() == OrderStatus.PAID.getValue()) {
                        sendReminder(order.getMemberId(), course, 60);
                        reminderCount++;
                    }
                }
            }
        }
        
        log.info("课前 1 小时提醒任务执行完成，共发送{}条提醒", reminderCount);
    }

    /**
     * 课前 5 分钟提醒任务
     * 每 5 分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void fiveMinutesBeforeReminder() {
        log.info("开始执行课前 5 分钟提醒任务");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(5);
        
        List<Course> courses = courseMapper.selectAvailableCourses();
        
        int reminderCount = 0;
        for (Course course : courses) {
            if (isSameTime(course.getStartTime(), targetTime, 5)) {
                List<Order> orders = orderMapper.selectByCourseId(course.getId());
                for (Order order : orders) {
                    if (order.getStatus() == OrderStatus.PAID.getValue()) {
                        sendReminder(order.getMemberId(), course, 5);
                        reminderCount++;
                    }
                }
            }
        }
        
        log.info("课前 5 分钟提醒任务执行完成，共发送{}条提醒", reminderCount);
    }

    /**
     * 判断两个时间是否在指定分钟误差范围内
     */
    private boolean isSameTime(LocalDateTime time1, LocalDateTime time2, int minutes) {
        long diffMinutes = Math.abs(java.time.Duration.between(time1, time2).toMinutes());
        return diffMinutes <= minutes;
    }

    /**
     * 发送提醒通知
     */
    private void sendReminder(Long memberId, Course course, int minutesBefore) {
        Notification notification = new Notification();
        notification.setMemberId(memberId);
        notification.setTitle("课程提醒");
        notification.setContent(String.format(
            "您预约的%s课程将于%d分钟后开始（%s），请准时参加。",
            course.getName(),
            minutesBefore,
            course.getStartTime().format(DATE_FORMATTER)
        ));
        notification.setType(NotificationType.COURSE_REMINDER.getValue());
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
        log.info("已发送课程提醒：会员 ID={}, 课程={}, {}分钟前", memberId, course.getName(), minutesBefore);
    }
}
