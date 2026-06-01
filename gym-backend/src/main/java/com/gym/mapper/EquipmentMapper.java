package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 器材仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
}
