package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.MemberUpdateRequest;
import com.gym.dto.response.MemberInfoResponse;
import com.gym.dto.response.MemberResponse;
import com.gym.entity.Member;
import com.gym.mapper.MemberMapper;
import com.gym.service.MemberService;
import com.gym.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    private final PasswordEncoder passwordEncoder;

    private final NotificationService notificationService;

    @Override
    public MemberResponse getMemberById(Long id) {
        Member member = memberMapper.selectById(id);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return toMemberResponse(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberMapper.selectById(id);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (request.getName() != null) {
            member.setName(request.getName());
        }
        if (request.getGender() != null) {
            member.setGender(request.getGender());
        }
        if (request.getBirthday() != null) {
            member.setBirthday(request.getBirthday());
        }
        if (request.getIdCard() != null) {
            member.setIdCard(request.getIdCard());
        }
        if (request.getEmergencyContact() != null) {
            member.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getEmergencyPhone() != null) {
            member.setEmergencyPhone(request.getEmergencyPhone());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            member.setEmail(request.getEmail());
        }
        if (request.getAge() != null) {
            member.setAge(request.getAge());
        }
        member.setUpdatedAt(LocalDateTime.now());
        memberMapper.updateById(member);
        return toMemberResponse(member);
    }

    @Override
    public List<MemberInfoResponse> listMembers(int page, int size) {
        Page<Member> memberPage = new Page<>(page, size);
        Page<Member> result = memberMapper.selectPage(memberPage, new LambdaQueryWrapper<>());
        return result.getRecords().stream().map(this::toMemberInfoResponse).collect(Collectors.toList());
    }

    @Override
    public Member getMemberByPhone(String phone) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getPhone, phone);
        return memberMapper.selectOne(wrapper);
    }

    private MemberResponse toMemberResponse(Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setUsername(member.getUsername());
        response.setName(member.getName());
        response.setPhone(member.getPhone());
        response.setEmail(member.getEmail());
        response.setAge(member.getAge());
        response.setGender(member.getGender());
        response.setBirthday(member.getBirthday());
        response.setIdCard(member.getIdCard());
        response.setEmergencyContact(member.getEmergencyContact());
        response.setEmergencyPhone(member.getEmergencyPhone());
        response.setNoShowCount(member.getNoShowCount());
        response.setIsBanned(member.getIsBanned());
        response.setBannedUntil(member.getBannedUntil());
        response.setCreatedAt(member.getCreatedAt());
        return response;
    }

    private MemberInfoResponse toMemberInfoResponse(Member member) {
        MemberInfoResponse response = new MemberInfoResponse();
        response.setId(member.getId());
        response.setUsername(member.getUsername());
        response.setName(member.getName());
        response.setPhone(member.getPhone());
        response.setEmail(member.getEmail());
        response.setAge(member.getAge());
        response.setGender(member.getGender());
        response.setBirthday(member.getBirthday());
        response.setNoShowCount(member.getNoShowCount());
        response.setIsBanned(member.getIsBanned());
        response.setCreatedAt(member.getCreatedAt());
        return response;
    }

    @Override
    @Transactional
    public void incrementNoShowCount(Long memberId) {
        memberMapper.incrementNoShowCount(memberId);

        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            return;
        }

        int count = member.getNoShowCount() == null ? 0 : member.getNoShowCount();

        if (count >= 5) {
            memberMapper.updateBanStatus(memberId, 1, LocalDateTime.now().plusDays(7));
            try {
                notificationService.send(memberId,
                        "爽约处罚通知",
                        "因累计爽约 " + count + " 次，您已被限制预约 7 天，到期后自动解除并清零爽约次数。",
                        8);
            } catch (Exception ignored) {
            }
        } else {
            try {
                notificationService.send(memberId,
                        "爽约提醒",
                        "您已累计爽约 " + count + " 次，累计 5 次将被限制预约 7 天，请注意按时参加课程。",
                        8);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    @Transactional
    public void changePassword(Long memberId, String oldPassword, String newPassword) {
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new BusinessException(ErrorCode.MEMBER_PASSWORD_ERROR);
        }
        
        // 更新密码
        member.setPassword(passwordEncoder.encode(newPassword));
        member.setUpdatedAt(LocalDateTime.now());
        memberMapper.updateById(member);
    }

    @Override
    @Transactional
    public void banMember(Long memberId) {
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        member.setIsBanned(1);
        member.setBannedUntil(LocalDateTime.now().plusDays(30));
        member.setUpdatedAt(LocalDateTime.now());
        memberMapper.updateById(member);
    }

    @Override
    @Transactional
    public void unbanMember(Long memberId) {
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        member.setIsBanned(0);
        member.setBannedUntil(null);
        member.setUpdatedAt(LocalDateTime.now());
        memberMapper.updateById(member);
    }
}
