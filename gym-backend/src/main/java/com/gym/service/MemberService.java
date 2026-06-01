package com.gym.service;

import com.gym.dto.request.MemberUpdateRequest;
import com.gym.dto.response.MemberInfoResponse;
import com.gym.dto.response.MemberResponse;

import java.util.List;

/**
 * 会员管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface MemberService {

    /**
     * 根据 ID 查询会员详情。
     *
     * @param id 会员 ID
     * @return 会员详情响应
     */
    MemberResponse getMemberById(Long id);

    /**
     * 更新会员信息。
     *
     * @param id      会员 ID
     * @param request 更新请求
     * @return 更新后的会员响应
     */
    MemberResponse updateMember(Long id, MemberUpdateRequest request);

    /**
     * 分页查询会员列表。
     *
     * @param page 页码
     * @param size 每页数量
     * @return 会员列表
     */
    List<MemberInfoResponse> listMembers(int page, int size);

    /**
     * 根据手机号查询会员。
     *
     * @param phone 手机号
     * @return 会员实体，不存在返回 null
     */
    com.gym.entity.Member getMemberByPhone(String phone);

    /**
     * 修改会员密码。
     *
     * @param memberId    会员 ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long memberId, String oldPassword, String newPassword);

    /**
     * 增加会员爽约次数。
     *
     * @param memberId 会员 ID
     */
    void incrementNoShowCount(Long memberId);

    /**
     * 禁用会员。
     *
     * @param memberId 会员 ID
     */
    void banMember(Long memberId);

    /**
     * 解禁会员。
     *
     * @param memberId 会员 ID
     */
    void unbanMember(Long memberId);
}
