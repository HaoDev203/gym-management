package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.ChangePasswordRequest;
import com.gym.dto.request.MemberUpdateRequest;
import com.gym.dto.response.MemberInfoResponse;
import com.gym.dto.response.MemberResponse;
import com.gym.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员管理接口控制器。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/{id}")
    public BaseResponse<MemberResponse> getMember(@PathVariable Long id) {
        MemberResponse member = memberService.getMemberById(id);
        return BaseResponse.success(member);
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateMember(@PathVariable Long id, @RequestBody @Valid MemberUpdateRequest request) {
        memberService.updateMember(id, request);
        return BaseResponse.success(null);
    }

    @GetMapping("/list")
    public BaseResponse<List<MemberInfoResponse>> listMembers(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "100") int size) {
        List<MemberInfoResponse> members = memberService.listMembers(page, size);
        return BaseResponse.success(members);
    }

    @PutMapping("/ban/{id}")
    public BaseResponse<Void> banMember(@PathVariable Long id) {
        memberService.banMember(id);
        return BaseResponse.success(null);
    }

    @PutMapping("/unban/{id}")
    public BaseResponse<Void> unbanMember(@PathVariable Long id) {
        memberService.unbanMember(id);
        return BaseResponse.success(null);
    }

    /**
     * 修改会员密码。
     *
     * @param memberId 会员 ID
     * @param request  修改密码请求（包含旧密码和新密码）
     * @return 统一返回结构
     */
    @PostMapping("/change-password/{memberId}")
    public BaseResponse<Void> changePassword(@PathVariable Long memberId, @RequestBody @Valid ChangePasswordRequest request) {
        memberService.changePassword(memberId, request.getOldPassword(), request.getNewPassword());
        return BaseResponse.success(null);
    }
}
