package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据教练 ID 查询课程
     *
     * @param coachId 教练 ID
     * @return 课程列表
     */
    List<Course> selectByCoachId(@Param("coachId") Long coachId);

    /**
     * 查询可预约的课程
     *
     * @return 课程列表
     */
    List<Course> selectAvailableCourses();

    /**
     * 检查时间冲突
     *
     * @param coachId 教练 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 冲突的课程列表
     */
    List<Course> checkTimeConflict(@Param("coachId") Long coachId, 
                                   @Param("startTime") LocalDateTime startTime, 
                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 增加已预约人数
     *
     * @param id 课程 ID
     */
    void incrementBookedCount(@Param("id") Long id);

    /**
     * 减少已预约人数
     *
     * @param id 课程 ID
     */
    void decrementBookedCount(@Param("id") Long id);
}
