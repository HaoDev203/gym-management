package com.gym.service.impl;

import com.gym.common.BusinessException;
import com.gym.dto.request.BookingRequest;
import com.gym.entity.*;
import com.gym.enums.OrderStatus;
import com.gym.mapper.CoachMapper;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.MemberMapper;
import com.gym.mapper.OrderMapper;
import com.gym.service.WaitlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 预约服务测试类。
 *
 * @author liuxinsi
 * @date 2026-06-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("预约服务测试")
class BookingServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CoachMapper coachMapper;

    @Mock
    private WaitlistService waitlistService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Member testMember;
    private Course testCourse;
    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        // 初始化测试会员
        testMember = new Member();
        testMember.setId(1L);
        testMember.setUsername("13800138001");
        testMember.setName("测试用户");
        testMember.setPhone("13800138001");
        testMember.setNoShowCount(0);
        testMember.setIsBanned(0);

        // 初始化测试课程
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("瑜伽团课");
        testCourse.setType(1); // 团课
        testCourse.setCoachId(1L);
        testCourse.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0));
        testCourse.setEndTime(testCourse.getStartTime().plusHours(1));
        testCourse.setCapacity(10);
        testCourse.setBookedCount(5);
        testCourse.setPrice(new BigDecimal("100.00"));
        testCourse.setStatus(1); // 可预约

        // 初始化预约请求
        bookingRequest = new BookingRequest();
        bookingRequest.setCourseId(1L);
    }

    @Test
    @DisplayName("预约成功 - 正常场景")
    void testBookSuccess() {
        // Arrange
        when(memberMapper.selectById(1L)).thenReturn(testMember);
        when(courseMapper.selectById(1L)).thenReturn(testCourse);
        when(orderMapper.selectCount(any())).thenReturn(0L);
        when(coachMapper.selectById(1L)).thenReturn(new Coach());

        // Act
        assertDoesNotThrow(() -> bookingService.book(1L, bookingRequest));

        // Assert
        verify(orderMapper, times(1)).insert(any(Order.class));
    }

    @Test
    @DisplayName("预约失败 - 会员不存在")
    void testBookMemberNotFound() {
        // Arrange
        when(memberMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }

    @Test
    @DisplayName("预约失败 - 会员被限制")
    void testBookMemberBanned() {
        // Arrange
        testMember.setIsBanned(1);
        when(memberMapper.selectById(1L)).thenReturn(testMember);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }

    @Test
    @DisplayName("预约失败 - 课程不存在")
    void testBookCourseNotFound() {
        // Arrange
        when(memberMapper.selectById(1L)).thenReturn(testMember);
        when(courseMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }

    @Test
    @DisplayName("预约失败 - 课程已满")
    void testBookCourseFull() {
        // Arrange
        testCourse.setBookedCount(10);
        when(memberMapper.selectById(1L)).thenReturn(testMember);
        when(courseMapper.selectById(1L)).thenReturn(testCourse);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }

    @Test
    @DisplayName("预约失败 - 已预约过该课程")
    void testBookAlreadyBooked() {
        // Arrange
        when(memberMapper.selectById(1L)).thenReturn(testMember);
        when(courseMapper.selectById(1L)).thenReturn(testCourse);
        when(orderMapper.selectCount(any())).thenReturn(1L);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }

    @Test
    @DisplayName("预约失败 - 超过提前预约天数")
    void testBookTooEarly() {
        // Arrange
        testCourse.setStartTime(LocalDateTime.now().plusDays(10));
        when(memberMapper.selectById(1L)).thenReturn(testMember);
        when(courseMapper.selectById(1L)).thenReturn(testCourse);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }

    @Test
    @DisplayName("预约失败 - 超过预约截止时间")
    void testBookCutoffPassed() {
        // Arrange
        testCourse.setStartTime(LocalDateTime.now().plusMinutes(20)); // 距离开课不足 30 分钟
        when(memberMapper.selectById(1L)).thenReturn(testMember);
        when(courseMapper.selectById(1L)).thenReturn(testCourse);

        // Act & Assert
        assertThrows(BusinessException.class, () -> bookingService.book(1L, bookingRequest));
    }
}
