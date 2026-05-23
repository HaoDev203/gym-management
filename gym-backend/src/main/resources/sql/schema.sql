-- 健身房管理系统数据库初始化脚本
-- 创建时间：2026-05-21

-- 会员表
CREATE TABLE IF NOT EXISTS member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名（手机号）',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    id_card VARCHAR(18) COMMENT '身份证号',
    gender TINYINT DEFAULT 1 COMMENT '性别 1-男 2-女',
    birthday DATE COMMENT '生日',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
    no_show_count INT DEFAULT 0 COMMENT '爽约次数',
    is_banned TINYINT DEFAULT 0 COMMENT '是否被禁用 0-否 1-是',
    banned_until DATETIME COMMENT '禁用截止时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    role TINYINT NOT NULL DEFAULT 1 COMMENT '角色 1-超级管理员 2-普通管理员',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 教练表
CREATE TABLE IF NOT EXISTS coach (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '教练 ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    expertise VARCHAR(500) COMMENT '专长',
    schedule TEXT COMMENT '课程安排（JSON 格式）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练表';

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程 ID',
    name VARCHAR(100) NOT NULL COMMENT '课程名称',
    type TINYINT NOT NULL COMMENT '课程类型 1-团课 2-私教 3-体验课',
    coach_id BIGINT NOT NULL COMMENT '教练 ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    capacity INT NOT NULL DEFAULT 1 COMMENT '课程容量',
    booked_count INT DEFAULT 0 COMMENT '已预约人数',
    price DECIMAL(10,2) NOT NULL COMMENT '课程价格',
    status TINYINT DEFAULT 1 COMMENT '状态 1-可预约 2-已满 3-取消',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (coach_id) REFERENCES coach(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 器材表
CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '器材 ID',
    name VARCHAR(100) NOT NULL COMMENT '器材名称',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    location VARCHAR(200) COMMENT '位置',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='器材表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单 ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1-待支付 2-已支付 3-已取消 4-已完成 5-已退款',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已支付金额',
    check_in TINYINT DEFAULT 0 COMMENT '是否签到 0-否 1-是',
    check_in_time DATETIME COMMENT '签到时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 预约表
CREATE TABLE IF NOT EXISTS booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预约 ID',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1-已预约 2-已签到 3-已取消 4-爽约',
    booking_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    UNIQUE KEY uk_member_course (member_id, course_id) COMMENT '同一会员不能重复预约同一课程'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 通知表
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知 ID',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content VARCHAR(500) NOT NULL COMMENT '通知内容',
    type TINYINT NOT NULL COMMENT '通知类型 1-课程提醒 2-预约成功 3-预约取消 4-系统通知',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读 0-否 1-是',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 爽约记录表
CREATE TABLE IF NOT EXISTS no_show_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '爽约记录 ID',
    member_id BIGINT NOT NULL COMMENT '会员 ID',
    order_id BIGINT NOT NULL COMMENT '订单 ID',
    course_time DATETIME NOT NULL COMMENT '课程时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='爽约记录表';

-- 创建索引
CREATE INDEX idx_member_phone ON member(phone);
CREATE INDEX idx_member_username ON member(username);
CREATE INDEX idx_course_coach ON course(coach_id);
CREATE INDEX idx_course_time ON course(start_time, end_time);
CREATE INDEX idx_order_member ON orders(member_id);
CREATE INDEX idx_order_course ON orders(course_id);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_booking_member ON booking(member_id);
CREATE INDEX idx_booking_course ON booking(course_id);
CREATE INDEX idx_notification_member ON notification(member_id);
CREATE INDEX idx_no_show_member ON no_show_record(member_id);
