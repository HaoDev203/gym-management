package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Waitlist;
import org.apache.ibatis.annotations.Mapper;

/**
 * 候补排队仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-23
 */
@Mapper
public interface WaitlistMapper extends BaseMapper<Waitlist> {
}
