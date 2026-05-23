package com.gym.service;

import com.gym.dto.request.EquipmentRequest;
import com.gym.dto.response.EquipmentResponse;

import java.util.List;

/**
 * 器材管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface EquipmentService {

    /**
     * 创建器材。
     *
     * @param request 器材创建请求
     * @return 器材响应
     */
    EquipmentResponse createEquipment(EquipmentRequest request);

    /**
     * 查询所有器材。
     *
     * @return 器材列表
     */
    List<EquipmentResponse> listAll();

    /**
     * 根据 ID 查询器材。
     *
     * @param id 器材 ID
     * @return 器材响应
     */
    EquipmentResponse getEquipmentById(Long id);

    /**
     * 更新器材信息。
     *
     * @param id      器材 ID
     * @param request 更新请求
     * @return 更新后的器材响应
     */
    EquipmentResponse updateEquipment(Long id, EquipmentRequest request);

    /**
     * 删除器材。
     *
     * @param id 器材 ID
     */
    void deleteEquipment(Long id);
}
