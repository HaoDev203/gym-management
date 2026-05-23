package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.CoachRequest;
import com.gym.dto.response.CoachResponse;
import com.gym.entity.Coach;
import com.gym.mapper.CoachMapper;
import com.gym.service.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教练管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService {

    private final CoachMapper coachMapper;

    @Override
    @Transactional
    public CoachResponse createCoach(CoachRequest request) {
        LambdaQueryWrapper<Coach> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coach::getPhone, request.getPhone());
        if (coachMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.COACH_PHONE_EXIST);
        }

        Coach coach = new Coach();
        coach.setName(request.getName());
        coach.setGender(request.getGender());
        coach.setPhone(request.getPhone());
        coach.setEmail(request.getEmail());
        coach.setExpertise(request.getExpertise());
        coach.setSchedule(request.getSchedule());
        coach.setCreatedAt(LocalDateTime.now());
        coach.setUpdatedAt(LocalDateTime.now());
        coachMapper.insert(coach);
        return toCoachResponse(coach);
    }

    @Override
    public List<CoachResponse> listAll() {
        List<Coach> coaches = coachMapper.selectList(new LambdaQueryWrapper<>());
        return coaches.stream().map(this::toCoachResponse).collect(Collectors.toList());
    }

    @Override
    public CoachResponse getCoachById(Long id) {
        Coach coach = coachMapper.selectById(id);
        if (coach == null) {
            throw new BusinessException(ErrorCode.COACH_NOT_FOUND);
        }
        return toCoachResponse(coach);
    }

    @Override
    @Transactional
    public CoachResponse updateCoach(Long id, CoachRequest request) {
        Coach coach = coachMapper.selectById(id);
        if (coach == null) {
            throw new BusinessException(ErrorCode.COACH_NOT_FOUND);
        }
        if (request.getName() != null) {
            coach.setName(request.getName());
        }
        if (request.getGender() != null) {
            coach.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            coach.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            coach.setEmail(request.getEmail());
        }
        if (request.getExpertise() != null) {
            coach.setExpertise(request.getExpertise());
        }
        if (request.getSchedule() != null) {
            coach.setSchedule(request.getSchedule());
        }
        coach.setUpdatedAt(LocalDateTime.now());
        coachMapper.updateById(coach);
        return toCoachResponse(coach);
    }

    @Override
    @Transactional
    public void deleteCoach(Long id) {
        Coach coach = coachMapper.selectById(id);
        if (coach == null) {
            throw new BusinessException(ErrorCode.COACH_NOT_FOUND);
        }
        coachMapper.deleteById(id);
    }

    private CoachResponse toCoachResponse(Coach coach) {
        CoachResponse response = new CoachResponse();
        response.setId(coach.getId());
        response.setName(coach.getName());
        response.setGender(coach.getGender());
        response.setPhone(coach.getPhone());
        response.setEmail(coach.getEmail());
        response.setExpertise(coach.getExpertise());
        response.setSchedule(coach.getSchedule());
        response.setCreatedAt(coach.getCreatedAt());
        return response;
    }
}
