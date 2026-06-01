package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
