package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员仓储接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 根据用户名查询会员
     *
     * @param username 用户名
     * @return 会员实体
     */
    Member selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询会员
     *
     * @param phone 手机号
     * @return 会员实体
     */
    Member selectByPhone(@Param("phone") String phone);

    /**
     * 查询所有会员
     *
     * @return 会员列表
     */
    List<Member> selectAll();

    /**
     * 增加爽约次数
     *
     * @param id 会员 ID
     */
    void incrementNoShowCount(@Param("id") Long id);

    /**
     * 更新禁用状态
     *
     * @param id 会员 ID
     * @param banned 是否禁用
     * @param bannedUntil 禁用截止时间
     */
    void updateBanStatus(@Param("id") Long id, @Param("banned") Integer banned, @Param("bannedUntil") LocalDateTime bannedUntil);
}
