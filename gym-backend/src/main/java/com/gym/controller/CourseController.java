package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.CourseRequest;
import com.gym.dto.response.CourseResponse;
import com.gym.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程管理接口控制器。
 *
 * <p>提供课程列表查询、课程详情、课程预约等接口。</p>
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public BaseResponse<List<CourseResponse>> listCourses() {
        List<CourseResponse> courses = courseService.listBookableCourses();
        return BaseResponse.success(courses);
    }

    @GetMapping("/admin/list")
    public BaseResponse<List<CourseResponse>> listAllCoursesForAdmin() {
        List<CourseResponse> courses = courseService.listAllCourses();
        return BaseResponse.success(courses);
    }

    @GetMapping("/{id}")
    public BaseResponse<CourseResponse> getCourse(@PathVariable Long id) {
        CourseResponse course = courseService.getCourseById(id);
        return BaseResponse.success(course);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addCourse(@RequestBody @Valid CourseRequest request) {
        CourseResponse response = courseService.createCourse(request);
        return BaseResponse.success(response.getId());
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateCourse(@PathVariable Long id, @RequestBody @Valid CourseRequest request) {
        courseService.updateCourse(id, request);
        return BaseResponse.success(null);
    }

    @PutMapping("/cancel/{id}")
    public BaseResponse<Void> cancelCourse(@PathVariable Long id) {
        courseService.cancelCourse(id);
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return BaseResponse.success(null);
    }

    @PutMapping("/restore/{id}")
    public BaseResponse<Void> restoreCourse(@PathVariable Long id) {
        courseService.restoreCourse(id);
        return BaseResponse.success(null);
    }
}
