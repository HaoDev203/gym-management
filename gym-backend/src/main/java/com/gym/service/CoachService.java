package com.gym.service;

import com.gym.dto.request.CoachRequest;
import com.gym.dto.response.CoachResponse;

import java.util.List;

/**
 * 教练管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface CoachService {

    /**
     * 创建教练。
     *
     * @param request 教练创建请求
     * @return 教练响应
     */
    CoachResponse createCoach(CoachRequest request);

    /**
     * 查询所有教练。
     *
     * @return 教练列表
     */
    List<CoachResponse> listAll();

    /**
     * 根据 ID 查询教练。
     *
     * @param id 教练 ID
     * @return 教练响应
     */
    CoachResponse getCoachById(Long id);

    /**
     * 更新教练信息。
     *
     * @param id      教练 ID
     * @param request 更新请求
     * @return 更新后的教练响应
     */
    CoachResponse updateCoach(Long id, CoachRequest request);

    /**
     * 删除教练。
     *
     * @param id 教练 ID
     */
    void deleteCoach(Long id);
}
