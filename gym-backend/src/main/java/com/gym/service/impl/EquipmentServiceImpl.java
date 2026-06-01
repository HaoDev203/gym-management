package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.EquipmentRequest;
import com.gym.dto.response.EquipmentResponse;
import com.gym.entity.Equipment;
import com.gym.mapper.EquipmentMapper;
import com.gym.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 器材管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentMapper equipmentMapper;

    @Override
    @Transactional
    public EquipmentResponse createEquipment(EquipmentRequest request) {
        Equipment equipment = new Equipment();
        equipment.setName(request.getName());
        equipment.setQuantity(request.getQuantity());
        equipment.setLocation(request.getLocation());
        equipment.setCreatedAt(LocalDateTime.now());
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentMapper.insert(equipment);
        return toEquipmentResponse(equipment);
    }

    @Override
    public List<EquipmentResponse> listAll() {
        List<Equipment> list = equipmentMapper.selectList(new LambdaQueryWrapper<>());
        return list.stream().map(this::toEquipmentResponse).collect(Collectors.toList());
    }

    @Override
    public EquipmentResponse getEquipmentById(Long id) {
        Equipment equipment = equipmentMapper.selectById(id);
        if (equipment == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        return toEquipmentResponse(equipment);
    }

    @Override
    @Transactional
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentMapper.selectById(id);
        if (equipment == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        if (request.getName() != null) {
            equipment.setName(request.getName());
        }
        if (request.getQuantity() != null) {
            equipment.setQuantity(request.getQuantity());
        }
        if (request.getLocation() != null) {
            equipment.setLocation(request.getLocation());
        }
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentMapper.updateById(equipment);
        return toEquipmentResponse(equipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentMapper.selectById(id);
        if (equipment == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        equipmentMapper.deleteById(id);
    }

    private EquipmentResponse toEquipmentResponse(Equipment equipment) {
        EquipmentResponse response = new EquipmentResponse();
        response.setId(equipment.getId());
        response.setName(equipment.getName());
        response.setQuantity(equipment.getQuantity());
        response.setLocation(equipment.getLocation());
        response.setCreatedAt(equipment.getCreatedAt());
        return response;
    }
}
