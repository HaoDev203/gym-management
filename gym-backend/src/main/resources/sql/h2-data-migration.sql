-- ========================================
-- H2 数据库测试数据迁移到 MySQL
-- ========================================
-- 数据库配置：
--   HOST: 127.0.0.1
--   PORT: 3306
--   DB_USERNAME: root
--   DB_PASSWORD: 123
--   DATABASE: gym_management
-- ========================================

USE gym_management;

-- ========================================
-- 1. 删除旧表
-- ========================================
DROP TABLE IF EXISTS no_show_record;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS coach;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS member;

-- ========================================
-- 2. 创建新表（与 H2 结构一致）
-- ========================================

-- Member table
CREATE TABLE member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100),
    age INT,
    id_card VARCHAR(18),
    gender TINYINT DEFAULT 1,
    birthday DATE,
    emergency_contact VARCHAR(50),
    emergency_phone VARCHAR(20),
    no_show_count INT DEFAULT 0,
    is_banned TINYINT DEFAULT 0,
    banned_until DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Admin table
CREATE TABLE admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role TINYINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Coach table
CREATE TABLE coach (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    gender TINYINT DEFAULT 1,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    expertise VARCHAR(500),
    schedule TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Course table
CREATE TABLE course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type TINYINT NOT NULL,
    coach_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    capacity INT NOT NULL DEFAULT 1,
    booked_count INT DEFAULT 0,
    price DECIMAL(10,2) NOT NULL,
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_coach (coach_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment table
CREATE TABLE equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    location VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Orders table
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    member_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) DEFAULT 0,
    check_in TINYINT DEFAULT 0,
    check_in_time DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_member (member_id),
    INDEX idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Booking table
CREATE TABLE booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    booking_time DATETIME DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_member (member_id),
    INDEX idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Notification table
CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(500) NOT NULL,
    type TINYINT NOT NULL,
    is_read TINYINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_member (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- No Show Record table
CREATE TABLE no_show_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    course_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_member (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- 3. 插入测试数据
-- ========================================

-- 插入管理员数据
-- 密码：admin123（BCrypt 加密）
INSERT INTO admin (id, username, password, name, role) VALUES
(1, 'admin', '$2a$10$BNeEJy.qieZcLnVknQ.dxenRLpnSPghJtTT2gfBpW.ma5lyG2ClpO', '超级管理员', 1),
(2, 'operator', '$2a$10$BNeEJy.qieZcLnVknQ.dxenRLpnSPghJtTT2gfBpW.ma5lyG2ClpO', '操作员', 2);

-- 插入教练数据
INSERT INTO coach (id, name, gender, phone, email, expertise, schedule) VALUES
(1, '李教练', 1, '13900139001', 'licoach@example.com', '健身塑形、增肌训练', '{"monday": "09:00-12:00", "wednesday": "14:00-18:00", "friday": "09:00-12:00"}'),
(2, '王教练', 0, '13900139002', 'wangcoach@example.com', '瑜伽、普拉提', '{"tuesday": "10:00-13:00", "thursday": "15:00-19:00", "saturday": "09:00-12:00"}'),
(3, '张教练', 1, '13900139003', 'zhangcoach@example.com', '动感单车、HIIT', '{"monday": "14:00-17:00", "wednesday": "10:00-13:00", "friday": "15:00-18:00"}');

-- 插入课程数据
INSERT INTO course (id, name, type, coach_id, start_time, end_time, capacity, booked_count, price, status) VALUES
(1, '基础健身训练', 1, 1, '2026-05-22 09:00:00', '2026-05-22 10:30:00', 10, 3, 150.00, 1),
(2, '高级瑜伽课程', 2, 2, '2026-05-22 10:00:00', '2026-05-22 11:30:00', 8, 5, 200.00, 1),
(3, '动感单车挑战', 1, 3, '2026-05-22 14:00:00', '2026-05-22 15:00:00', 15, 10, 120.00, 1),
(4, '私教一对一', 2, 1, '2026-05-23 09:00:00', '2026-05-23 10:00:00', 1, 0, 500.00, 1),
(5, 'HIIT 高强度间歇训练', 1, 3, '2026-05-23 15:00:00', '2026-05-23 16:00:00', 12, 8, 180.00, 1);

-- 插入器材数据
INSERT INTO equipment (id, name, quantity, location) VALUES
(1, '跑步机', 10, '一楼有氧区'),
(2, '动感单车', 15, '二楼团课室'),
(3, '哑铃套装', 20, '一楼力量区'),
(4, '瑜伽垫', 30, '二楼瑜伽室'),
(5, '杠铃套装', 8, '一楼力量区'),
(6, '椭圆机', 6, '一楼有氧区'),
(7, '划船机', 4, '一楼有氧区'),
(8, '史密斯机', 3, '一楼力量区');

-- 插入会员测试数据（密码：123456）
INSERT INTO member (id, username, password, name, phone, email, age, id_card, gender, birthday, emergency_contact, emergency_phone, no_show_count, is_banned, banned_until) VALUES
(1, '13800138001', '$2a$10$tECcfWEjx7eWh5PGkJNgf.YlyNr0B67HMcFejpbq/3LsLQoGui3JW', '张三', '13800138001', 'zhangsan@example.com', 30, '110101199001011234', 1, '1990-01-01', '李四', '13800138002', 0, 0, NULL),
(2, '13800138002', '$2a$10$tECcfWEjx7eWh5PGkJNgf.YlyNr0B67HMcFejpbq/3LsLQoGui3JW', '李四', '13800138002', 'lisi@example.com', 28, '110101199202022345', 2, '1992-02-02', '王五', '13800138003', 1, 0, NULL),
(3, '13800138003', '$2a$10$tECcfWEjx7eWh5PGkJNgf.YlyNr0B67HMcFejpbq/3LsLQoGui3JW', '王五', '13800138003', 'wangwu@example.com', 25, '110101199503033456', 1, '1995-03-03', '赵六', '13800138004', 3, 1, '2026-06-01 00:00:00');

-- 插入通知数据
INSERT INTO notification (id, member_id, title, content, type, is_read) VALUES
(1, 1, '课程提醒', '您预约的基础健身训练课程将于今天 09:00 开始，请准时参加。', 1, 0),
(2, 1, '预约成功', '您已成功预约高级瑜伽课程，请按时参加。', 2, 1),
(3, 2, '系统通知', '健身房将于本周五进行设备维护，请合理安排锻炼时间。', 4, 0);

-- 插入订单测试数据
INSERT INTO orders (id, order_no, member_id, course_id, status, amount, paid_amount, check_in, check_in_time) VALUES
(1, 'ORD202605220001', 1, 1, 2, 150.00, 150.00, 1, '2026-05-22 08:50:00'),
(2, 'ORD202605220002', 1, 2, 2, 200.00, 200.00, 0, NULL),
(3, 'ORD202605220003', 2, 2, 2, 200.00, 200.00, 0, NULL),
(4, 'ORD202605220004', 2, 3, 1, 120.00, 0.00, 0, NULL);

-- 插入预约测试数据
INSERT INTO booking (id, member_id, course_id, status, booking_time) VALUES
(1, 1, 1, 2, '2026-05-21 10:00:00'),
(2, 1, 2, 1, '2026-05-21 10:05:00'),
(3, 2, 2, 1, '2026-05-21 10:10:00'),
(4, 2, 3, 1, '2026-05-21 10:15:00');

-- 插入爽约记录测试数据
INSERT INTO no_show_record (id, member_id, order_id, course_time) VALUES
(1, 2, 4, '2026-05-20 14:00:00');

-- ========================================
-- 4. 验证数据
-- ========================================
SELECT 'Migration completed! Data summary:' AS message;
SELECT 'admin' AS table_name, COUNT(*) AS count FROM admin
UNION ALL SELECT 'member', COUNT(*) FROM member
UNION ALL SELECT 'coach', COUNT(*) FROM coach
UNION ALL SELECT 'course', COUNT(*) FROM course
UNION ALL SELECT 'equipment', COUNT(*) FROM equipment
UNION ALL SELECT 'orders', COUNT(*) FROM orders
UNION ALL SELECT 'booking', COUNT(*) FROM booking
UNION ALL SELECT 'notification', COUNT(*) FROM notification
UNION ALL SELECT 'no_show_record', COUNT(*) FROM no_show_record;

-- ========================================
-- 5. 测试账户信息
-- ========================================
SELECT '========================================' AS message;
SELECT 'Test Accounts:' AS message;
SELECT '========================================' AS message;
SELECT 'Admin: admin / admin123' AS message;
SELECT 'Member: 13800138001 / 123456' AS message;
SELECT '========================================' AS message;
