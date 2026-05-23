package com.gym.service;

import com.gym.dto.request.CourseRequest;
import com.gym.dto.response.CourseResponse;

import java.util.List;

/**
 * 课程管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface CourseService {

    /**
     * 创建课程。
     *
     * @param request 课程创建请求
     * @return 课程响应
     */
    CourseResponse createCourse(CourseRequest request);

    /**
     * 查询可预约课程列表。
     *
     * @return 可预约课程列表
     */
    List<CourseResponse> listBookableCourses();

    /**
     * 查询所有课程列表（管理员）。
     *
     * @return 所有课程列表
     */
    List<CourseResponse> listAllCourses();

    /**
     * 根据 ID 查询课程。
     *
     * @param id 课程 ID
     * @return 课程响应
     */
    CourseResponse getCourseById(Long id);

    /**
     * 更新课程信息。
     *
     * @param id      课程 ID
     * @param request 更新请求
     * @return 更新后的课程响应
     */
    CourseResponse updateCourse(Long id, CourseRequest request);

    /**
     * 取消课程。
     *
     * @param id 课程 ID
     */
    void cancelCourse(Long id);

    /**
     * 删除课程（物理删除）。
     *
     * @param id 课程 ID
     */
    void deleteCourse(Long id);

    /**
     * 恢复已取消的课程。
     *
     * @param id 课程 ID
     */
    void restoreCourse(Long id);
}
