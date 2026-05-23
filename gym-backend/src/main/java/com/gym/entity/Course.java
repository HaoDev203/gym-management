package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("course")
public class Course {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer type;

    private Long coachId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer capacity;

    private Integer bookedCount = 0;

    private BigDecimal price;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 判断课程是否可预约。
     *
     * @return true 表示可预约
     */
    public boolean isBookable() {
        if (this.status == null || this.status != 1) {
            return false;
        }
        return !isFull();
    }

    /**
     * 判断课程是否已满。
     *
     * @return true 表示已满
     */
    public boolean isFull() {
        if (this.capacity == null) {
            return false;
        }
        int booked = this.bookedCount != null ? this.bookedCount : 0;
        return booked >= this.capacity;
    }

    /**
     * 增加已预约人数。
     */
    public void incrementBookedCount() {
        if (this.bookedCount == null) {
            this.bookedCount = 0;
        }
        this.bookedCount++;
    }

    /**
     * 减少已预约人数。
     */
    public void decrementBookedCount() {
        if (this.bookedCount == null) {
            this.bookedCount = 0;
        }
        if (this.bookedCount > 0) {
            this.bookedCount--;
        }
    }

    /**
     * 判断是否已过预约截止时间（开课前30分钟）。
     *
     * @return true 表示已截止
     */
    public boolean isCutoffPassed() {
        if (this.startTime == null) {
            return true;
        }
        return LocalDateTime.now().plusMinutes(30).isAfter(this.startTime);
    }

    /**
     * 取消课程。
     */
    public void cancel() {
        this.status = 0;
    }
}
