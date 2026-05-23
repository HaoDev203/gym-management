package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.EquipmentRequest;
import com.gym.dto.response.EquipmentResponse;
import com.gym.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 器材管理控制器。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/list")
    public BaseResponse<List<EquipmentResponse>> listEquipment() {
        List<EquipmentResponse> equipment = equipmentService.listAll();
        return BaseResponse.success(equipment);
    }

    @GetMapping("/{id}")
    public BaseResponse<EquipmentResponse> getEquipment(@PathVariable Long id) {
        EquipmentResponse equipment = equipmentService.getEquipmentById(id);
        return BaseResponse.success(equipment);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addEquipment(@RequestBody @Valid EquipmentRequest request) {
        EquipmentResponse response = equipmentService.createEquipment(request);
        return BaseResponse.success(response.getId());
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateEquipment(@PathVariable Long id, @RequestBody @Valid EquipmentRequest request) {
        equipmentService.updateEquipment(id, request);
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return BaseResponse.success(null);
    }
}
