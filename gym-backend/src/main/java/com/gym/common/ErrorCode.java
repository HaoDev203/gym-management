package com.gym.common;

import lombok.Getter;

/**
 * 业务错误码枚举。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),

    BAD_REQUEST(400, "请求参数错误"),

    UNAUTHORIZED(401, "未登录或登录已过期"),

    FORBIDDEN(403, "无权限访问"),

    NOT_FOUND(404, "资源不存在"),

    INTERNAL_ERROR(500, "服务器内部错误"),

    MEMBER_NOT_FOUND(1001, "会员不存在"),

    MEMBER_PHONE_EXIST(1002, "手机号已注册"),

    MEMBER_BANNED(1003, "会员已被限制预约"),

    MEMBER_PASSWORD_ERROR(1004, "密码错误"),

    ADMIN_NOT_FOUND(1101, "管理员不存在"),

    ADMIN_USERNAME_EXIST(1102, "管理员用户名已存在"),

    ADMIN_PASSWORD_ERROR(1103, "密码错误"),

    COACH_NOT_FOUND(1201, "教练不存在"),

    COACH_PHONE_EXIST(1202, "教练联系方式已存在"),

    COURSE_NOT_FOUND(1301, "课程不存在"),

    COURSE_FULL(1302, "课程已满"),

    COURSE_TIME_CONFLICT(1303, "课程时间冲突"),

    COURSE_CANNOT_CANCEL(1304, "课程不可取消"),

    COURSE_CANCELLED(1305, "课程已取消，无法预约"),

    EQUIPMENT_NOT_FOUND(1401, "器材不存在"),

    ORDER_NOT_FOUND(1501, "订单不存在"),

    ORDER_DUPLICATE(1502, "已预约该课程，不可重复预约"),

    ORDER_CANNOT_CANCEL(1503, "订单不可取消"),

    ORDER_CANNOT_CHECK_IN(1504, "订单不可签到"),

    ORDER_TIME_CONFLICT(1505, "该时间段已有其他课程预约"),

    ORDER_DAILY_LIMIT(1506, "每天最多预约3节课程"),

    ORDER_TOO_EARLY(1507, "最多可提前7天预约"),

    ORDER_CUTOFF_PASSED(1508, "开课前30分钟截止预约"),

    ORDER_CANCEL_TOO_LATE(1509, "开课前2小时内不可取消"),

    NO_SHOW_EXCEEDED(2001, "爽约次数已达上限"),

    SMS_SEND_FAILED(3001, "短信发送失败"),

    RATE_LIMIT_EXCEEDED(3002, "操作频率过高，请稍后再试");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
