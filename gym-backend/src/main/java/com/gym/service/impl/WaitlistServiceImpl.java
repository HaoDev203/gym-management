package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.BookingRequest;
import com.gym.dto.response.BookingResponse;
import com.gym.entity.*;
import com.gym.enums.OrderStatus;
import com.gym.enums.WaitlistStatus;
import com.gym.mapper.*;
import com.gym.service.BookingService;
import com.gym.service.NotificationService;
import com.gym.service.WaitlistService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 候补排队服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-23
 */
@Service
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistMapper waitlistMapper;

    private final CourseMapper courseMapper;

    private final MemberMapper memberMapper;

    private final BookingService bookingService;

    private final NotificationService notificationService;

    public WaitlistServiceImpl(WaitlistMapper waitlistMapper,
                               CourseMapper courseMapper,
                               MemberMapper memberMapper,
                               @Lazy BookingService bookingService,
                               @Lazy NotificationService notificationService) {
        this.waitlistMapper = waitlistMapper;
        this.courseMapper = courseMapper;
        this.memberMapper = memberMapper;
        this.bookingService = bookingService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public int joinWaitlist(Long memberId, Long courseId) {
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (member.isBanned()) {
            throw new BusinessException(ErrorCode.MEMBER_BANNED);
        }

        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }

        if (!course.isFull()) {
            throw new BusinessException(ErrorCode.COURSE_FULL, "课程未满，请直接预约");
        }

        LambdaQueryWrapper<Waitlist> dupCheck = new LambdaQueryWrapper<>();
        dupCheck.eq(Waitlist::getMemberId, memberId);
        dupCheck.eq(Waitlist::getCourseId, courseId);
        dupCheck.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        if (waitlistMapper.selectCount(dupCheck) > 0) {
            throw new BusinessException(ErrorCode.ORDER_DUPLICATE, "您已在候补队列中");
        }

        LambdaQueryWrapper<Waitlist> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Waitlist::getCourseId, courseId);
        countWrapper.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        int position = waitlistMapper.selectCount(countWrapper).intValue() + 1;

        Waitlist waitlist = new Waitlist();
        waitlist.setMemberId(memberId);
        waitlist.setCourseId(courseId);
        waitlist.setStatus(WaitlistStatus.QUEUING.getCode());
        waitlist.setQueuePosition(position);
        waitlist.setCreatedAt(LocalDateTime.now());
        waitlistMapper.insert(waitlist);

        return position;
    }

    @Override
    @Transactional
    public void leaveWaitlist(Long memberId, Long courseId) {
        LambdaQueryWrapper<Waitlist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Waitlist::getMemberId, memberId);
        wrapper.eq(Waitlist::getCourseId, courseId);
        wrapper.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        List<Waitlist> entries = waitlistMapper.selectList(wrapper);
        for (Waitlist entry : entries) {
            entry.setStatus(WaitlistStatus.EXPIRED.getCode());
            waitlistMapper.updateById(entry);
        }
    }

    @Override
    public int getWaitlistCount(Long courseId) {
        LambdaQueryWrapper<Waitlist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Waitlist::getCourseId, courseId);
        wrapper.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        return waitlistMapper.selectCount(wrapper).intValue();
    }

    @Override
    public int getMemberPosition(Long memberId, Long courseId) {
        LambdaQueryWrapper<Waitlist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Waitlist::getMemberId, memberId);
        wrapper.eq(Waitlist::getCourseId, courseId);
        wrapper.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        wrapper.orderByAsc(Waitlist::getCreatedAt);
        List<Waitlist> entries = waitlistMapper.selectList(wrapper);
        if (entries.isEmpty()) {
            return 0;
        }
        LambdaQueryWrapper<Waitlist> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Waitlist::getCourseId, courseId);
        countWrapper.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        countWrapper.le(Waitlist::getCreatedAt, entries.get(0).getCreatedAt());
        return waitlistMapper.selectCount(countWrapper).intValue();
    }

    @Override
    @Transactional
    public BookingResponse promoteFirst(Long courseId) {
        LambdaQueryWrapper<Waitlist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Waitlist::getCourseId, courseId);
        wrapper.eq(Waitlist::getStatus, WaitlistStatus.QUEUING.getCode());
        wrapper.orderByAsc(Waitlist::getCreatedAt);
        wrapper.last("LIMIT 1");
        Waitlist first = waitlistMapper.selectOne(wrapper);

        if (first == null) {
            return null;
        }

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCourseId(courseId);

        try {
            BookingResponse response = bookingService.book(first.getMemberId(), bookingRequest);

            first.setStatus(WaitlistStatus.PROMOTED.getCode());
            waitlistMapper.updateById(first);

            Course course = courseMapper.selectById(courseId);
            String courseName = course != null ? course.getName() : "课程";

            try {
                notificationService.send(first.getMemberId(),
                        "候补转正通知",
                        "您在「" + courseName + "」的候补排队已转正，预约成功！",
                        5);
            } catch (Exception ignored) {
            }

            return response;
        } catch (BusinessException e) {
            first.setStatus(WaitlistStatus.EXPIRED.getCode());
            waitlistMapper.updateById(first);
            return null;
        }
    }
}
