package com.gym.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信工具类
 * 
 * <p>用途：封装阿里云短信 SDK，用于发送验证码、通知等短信（当前为 Mock 实现）。</p>
 * 
 * @author AI Assistant
 * @date 2026-05-21
 */
@Slf4j
@Component
public class SmsUtil {

    @Value("${sms.aliyun.access-key-id:}")
    private String accessKeyId;

    @Value("${sms.aliyun.access-key-secret:}")
    private String accessKeySecret;

    @Value("${sms.aliyun.sign-name:健身房}")
    private String signName;

    @Value("${sms.aliyun.template-code:SMS_123456789}")
    private String templateCode;

    /**
     * 发送验证码短信
     * 
     * @param phone 手机号
     * @param code 验证码
     * @return 发送是否成功
     */
    public boolean sendVerificationCode(String phone, String code) {
        log.info("【Mock】发送验证码短信：手机号={}, 验证码={}", phone, code);
        
        // TODO: 接入阿里云短信 SDK
        // 示例代码：
        // DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        // IAcsClient client = new DefaultAcsClient(profile);
        // SendSmsRequest request = new SendSmsRequest();
        // request.setPhoneNumbers(phone);
        // request.setSignName(signName);
        // request.setTemplateCode(templateCode);
        // request.setTemplateParameter("{\"code\":\"" + code + "\"}");
        // SendSmsResponse response = client.getAcsResponse(request);
        
        return true;
    }

    /**
     * 发送通知短信
     * 
     * @param phone 手机号
     * @param content 短信内容
     * @return 发送是否成功
     */
    public boolean sendNotification(String phone, String content) {
        log.info("【Mock】发送通知短信：手机号={}, 内容={}", phone, content);
        
        // TODO: 接入阿里云短信 SDK
        
        return true;
    }

    /**
     * 发送课程提醒短信
     * 
     * @param phone 手机号
     * @param courseName 课程名称
     * @param courseTime 课程时间
     * @return 发送是否成功
     */
    public boolean sendCourseReminder(String phone, String courseName, String courseTime) {
        String content = String.format("【%s】您预约的%s课程将于%s开始，请准时参加。", signName, courseName, courseTime);
        return sendNotification(phone, content);
    }

    /**
     * 发送预约成功短信
     * 
     * @param phone 手机号
     * @param courseName 课程名称
     * @param courseTime 课程时间
     * @return 发送是否成功
     */
    public boolean sendBookingSuccess(String phone, String courseName, String courseTime) {
        String content = String.format("【%s】您已成功预约%s课程，时间：%s，请按时参加。", signName, courseName, courseTime);
        return sendNotification(phone, content);
    }
}
