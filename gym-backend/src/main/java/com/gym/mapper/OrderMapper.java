package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单实体
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据会员 ID 查询订单
     *
     * @param memberId 会员 ID
     * @return 订单列表
     */
    List<Order> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据课程 ID 查询订单
     *
     * @param courseId 课程 ID
     * @return 订单列表
     */
    List<Order> selectByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据会员 ID 和课程 ID 查询订单
     *
     * @param memberId 会员 ID
     * @param courseId 课程 ID
     * @return 订单列表
     */
    List<Order> selectByMemberIdAndCourseId(@Param("memberId") Long memberId, @Param("courseId") Long courseId);

    /**
     * 检查时间冲突
     *
     * @param memberId 会员 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 冲突的订单列表
     */
    List<Order> checkTimeConflict(@Param("memberId") Long memberId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 更新订单状态
     *
     * @param id 订单 ID
     * @param status 状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新签到状态
     *
     * @param id 订单 ID
     */
    void updateCheckIn(@Param("id") Long id);
}
