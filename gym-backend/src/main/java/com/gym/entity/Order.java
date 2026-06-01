package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gym.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long memberId;

    private Long courseId;

    private Integer status;

    private BigDecimal amount;

    private BigDecimal paidAmount;

    private Integer checkIn;

    private LocalDateTime checkInTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer isDeleted;

    /**
     * 判断订单是否处于已预约状态。
     *
     * @return true 表示已预约
     */
    public boolean isBooked() {
        return this.status != null && this.status == OrderStatus.PAID.getCode();
    }

    /**
     * 判断订单是否已取消。
     *
     * @return true 表示已取消
     */
    public boolean isCancelled() {
        return this.status != null && this.status == OrderStatus.CANCELLED.getCode();
    }

    /**
     * 判断订单是否可以取消。
     *
     * @return true 表示可以取消
     */
    public boolean canCancel() {
        return isBooked();
    }

    /**
     * 取消订单。
     */
    public void cancel() {
        if (canCancel()) {
            this.status = OrderStatus.CANCELLED.getCode();
        }
    }

    /**
     * 判断是否已签到。
     *
     * @return true 表示已签到
     */
    public boolean isCheckedIn() {
        return this.checkIn != null && this.checkIn == 1;
    }

    /**
     * 判断是否可以签到。
     *
     * @return true 表示可以签到
     */
    public boolean canCheckIn() {
        return isBooked() && !isCheckedIn();
    }

    /**
     * 签到。
     */
    public void checkIn() {
        if (canCheckIn()) {
            this.checkIn = 1;
            this.checkInTime = LocalDateTime.now();
        }
    }

    /**
     * 标记为爽约。
     */
    public void markAsNoShow() {
        this.status = OrderStatus.NO_SHOW.getCode();
    }

    /**
     * 判断是否可以确认支付。
     *
     * @return true 表示可以确认支付
     */
    public boolean canConfirmPayment() {
        return this.status != null && this.status == OrderStatus.PENDING.getCode();
    }

    /**
     * 确认支付。
     */
    public void confirmPayment() {
        if (canConfirmPayment()) {
            this.status = OrderStatus.PAID.getCode();
        }
    }
}
