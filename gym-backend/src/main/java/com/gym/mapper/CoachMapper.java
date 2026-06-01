package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Coach;
import org.apache.ibatis.annotations.Mapper;

/**
 * 教练仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface CoachMapper extends BaseMapper<Coach> {
}
