-- 健身房管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS gym_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gym_management;

-- 会员表
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '会员 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像 URL',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    birthday DATE COMMENT '生日',
    membership_level INT DEFAULT 1 COMMENT '会员等级',
    no_show_count INT DEFAULT 0 COMMENT '爽约次数',
    banned_until DATETIME COMMENT '限制预约截止时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_phone (phone),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    role TINYINT NOT NULL DEFAULT 1 COMMENT '角色 1-普通管理员 2-超级管理员',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 教练表
CREATE TABLE IF NOT EXISTS coach (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '教练 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) NOT NULL COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像 URL',
    introduction TEXT COMMENT '简介',
    skills VARCHAR(255) COMMENT '擅长领域',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练表';

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '课程 ID',
    name VARCHAR(100) NOT NULL COMMENT '课程名称',
    type TINYINT NOT NULL COMMENT '课程类型 1-团课 2-私教课',
    coach_id BIGINT NOT NULL COMMENT '教练 ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    max_capacity INT NOT NULL COMMENT '最大人数',
    booked_count INT DEFAULT 0 COMMENT '已预约人数',
    price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '价格',
    location VARCHAR(100) COMMENT '地点',
    description TEXT COMMENT '课程描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-取消 1-可预约 2-已满 3-已结束',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_start_time (start_time),
    INDEX idx_coach (coach_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 器材表
CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '器材 ID',
    name VARCHAR(100) NOT NULL COMMENT '器材名称',
    type VARCHAR(50) COMMENT '器材类型',
    location VARCHAR(100) COMMENT '放置位置',
    status TINYINT DEFAULT 1 COMMENT '状态 0-维修中 1-可用 2-已借出',
    purchase_date DATE COMMENT '购买日期',
    maintenance_record TEXT COMMENT '维护记录',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='器材表';

-- 预约订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单 ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '订单状态 1-已预约 2-已取消 3-已完成 4-已爽约',
    booking_time DATETIME NOT NULL COMMENT '预约时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(255) COMMENT '取消原因',
    payment_status TINYINT DEFAULT 0 COMMENT '支付状态 0-未支付 1-已支付',
    payment_time DATETIME COMMENT '支付时间',
    payment_amount DECIMAL(10, 2) COMMENT '支付金额',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_member (member_id),
    INDEX idx_course (course_id),
    INDEX idx_status (status),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约订单表';

-- 通知表
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知 ID',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    type TINYINT NOT NULL COMMENT '通知类型',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    send_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    read_time DATETIME COMMENT '阅读时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_member (member_id),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 候补队列表
CREATE TABLE IF NOT EXISTS waitlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '候补 ID',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    queue_position INT NOT NULL COMMENT '排队位置',
    status TINYINT DEFAULT 1 COMMENT '状态 0-取消 1-等待中 2-已转正',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_course (course_id),
    INDEX idx_member (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候补队列表';

-- 签到记录表
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '签到 ID',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    check_in_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_member (member_id),
    INDEX idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- 初始化管理员账户（密码为 admin123，实际应加密）
INSERT INTO admin (username, password, role) VALUES 
('admin', 'admin123', 2);
