package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    void softDelete(Long id);
}
