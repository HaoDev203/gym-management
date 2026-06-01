package com.gym.enums;

import lombok.Getter;

/**
 * 订单状态枚举。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Getter
public enum OrderStatus {

    PENDING(1, "待支付"),

    PAID(2, "已支付"),

    CANCELLED(3, "已取消"),

    COMPLETED(4, "已完成"),

    NO_SHOW(5, "已爽约"),

    REFUNDED(6, "已退款");

    private final int code;

    private final String desc;

    OrderStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getValue() {
        return this.code;
    }
}
