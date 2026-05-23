package com.gym.task;

import com.gym.entity.Member;
import com.gym.mapper.MemberMapper;
import com.gym.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 限制预约自动解除定时任务
 * 
 * <p>用途：每天凌晨自动检查并解除过期的禁用限制。</p>
 * 
 * @author AI Assistant
 * @date 2026-05-21
 */
@Slf4j
@Component
public class BanReleaseTask {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 自动解除限制任务
     * 每天凌晨 0:00 执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoReleaseBan() {
        log.info("开始执行自动解除限制任务");

        LocalDateTime now = LocalDateTime.now();
        List<Member> allMembers = memberMapper.selectAll();

        int releaseCount = 0;
        for (Member member : allMembers) {
            if (member.getIsBanned() != null && member.getIsBanned() == 1 && member.getBannedUntil() != null) {
                if (member.getBannedUntil().isBefore(now)) {
                    member.setIsBanned(0);
                    member.setBannedUntil(null);
                    member.setNoShowCount(0);
                    memberMapper.updateById(member);
                    releaseCount++;
                    log.info("已自动解除限制并重置爽约次数：会员 ID={}, 用户名={}", member.getId(), member.getUsername());

                    try {
                        notificationService.send(member.getId(),
                                "限制解除通知",
                                "您的预约限制已自动解除，爽约次数已清零。请注意按时参加课程。",
                                8);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        log.info("自动解除限制任务执行完成，共解除{}个会员的限制", releaseCount);
    }
}
