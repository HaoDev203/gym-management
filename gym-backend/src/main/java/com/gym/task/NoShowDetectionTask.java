package com.gym.task;

import com.gym.entity.Course;
import com.gym.entity.NoShowRecord;
import com.gym.entity.Order;
import com.gym.enums.OrderStatus;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.NoShowRecordMapper;
import com.gym.mapper.OrderMapper;
import com.gym.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 爽约检测定时任务
 * 
 * <p>用途：课程结束后自动检测未签到的订单，记录为爽约。</p>
 * 
 * @author AI Assistant
 * @date 2026-05-21
 */
@Slf4j
@Component
public class NoShowDetectionTask {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private NoShowRecordMapper noShowRecordMapper;

    @Autowired
    private MemberService memberService;

    /**
     * 爽约检测任务
     * 每 30 分钟执行一次
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void detectNoShow() {
        log.info("开始执行爽约检测任务");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursAgo = now.minusHours(2);
        List<Course> courses = courseMapper.selectList(null);

        int noShowCount = 0;
        for (Course course : courses) {
            if (course.getEndTime() == null || course.getEndTime().isAfter(now)) {
                continue;
            }
            if (course.getEndTime().isBefore(twoHoursAgo)) {
                continue;
            }
            List<Order> orders = orderMapper.selectByCourseId(course.getId());
            for (Order order : orders) {
                if (order.getStatus() == OrderStatus.PAID.getValue() && order.getCheckIn() == 0) {
                    recordNoShow(order, course);
                    noShowCount++;
                }
            }
        }

        log.info("爽约检测任务执行完成，共记录{}个爽约", noShowCount);
    }

    /**
     * 记录爽约
     */
    private void recordNoShow(Order order, Course course) {
        try {
            NoShowRecord noShowRecord = new NoShowRecord();
            noShowRecord.setMemberId(order.getMemberId());
            noShowRecord.setOrderId(order.getId());
            noShowRecord.setCourseTime(course.getStartTime());

            noShowRecordMapper.insert(noShowRecord);

            memberService.incrementNoShowCount(order.getMemberId());

            order.setStatus(OrderStatus.NO_SHOW.getValue());
            order.setUpdatedAt(LocalDateTime.now());
            orderMapper.updateById(order);

            log.info("已记录爽约：订单 ID={}, 会员 ID={}, 课程={}", order.getId(), order.getMemberId(), course.getName());
        } catch (Exception e) {
            log.error("记录爽约失败：订单 ID={}, 错误={}", order.getId(), e.getMessage());
        }
    }
}
