-- ========================================
-- Gym Management System - MySQL 5.5 Compatible
-- (Tables + Test Data)
-- ========================================
-- Database: gym_management
-- Host: 127.0.0.1
-- Port: 3306
-- User: root
-- Password: 123
-- ========================================

-- Drop and create database
DROP DATABASE IF EXISTS gym_management;
CREATE DATABASE IF NOT EXISTS gym_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gym_management;

-- Admin table
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role TINYINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Member table
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    birthday DATE,
    membership_level INT DEFAULT 1,
    no_show_count INT DEFAULT 0,
    banned_until DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_phone (phone),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Coach table
CREATE TABLE IF NOT EXISTS coach (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(255),
    introduction TEXT,
    skills VARCHAR(255),
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Course table
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type TINYINT NOT NULL,
    coach_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    max_capacity INT NOT NULL,
    booked_count INT DEFAULT 0,
    price DECIMAL(10, 2) NOT NULL DEFAULT 0,
    location VARCHAR(100),
    description TEXT,
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_start_time (start_time),
    INDEX idx_coach (coach_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment table
CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    location VARCHAR(100),
    status TINYINT DEFAULT 1,
    purchase_date DATE,
    maintenance_record TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    member_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    booking_time DATETIME NOT NULL,
    cancel_time DATETIME,
    cancel_reason VARCHAR(255),
    payment_status TINYINT DEFAULT 0,
    payment_time DATETIME,
    payment_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_member (member_id),
    INDEX idx_course (course_id),
    INDEX idx_status (status),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Notification table
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    type TINYINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    is_read TINYINT DEFAULT 0,
    send_time DATETIME DEFAULT NULL,
    read_time DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_member (member_id),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Waitlist table
CREATE TABLE IF NOT EXISTS waitlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    queue_position INT NOT NULL,
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_course (course_id),
    INDEX idx_member (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Attendance table
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    check_in_time DATETIME DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_member (member_id),
    INDEX idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert admin data
INSERT INTO admin (username, password, role) VALUES
('admin', 'admin123', 2),
('operator1', 'op123456', 1),
('operator2', 'op123456', 1);

-- Insert member data
INSERT INTO member (username, password, phone, email, nickname, gender, birthday, membership_level, no_show_count) VALUES
('member1', 'm123456', '13800138001', 'member1@test.com', 'ZhangSan', 1, '1990-01-15', 1, 0),
('member2', 'm123456', '13800138002', 'member2@test.com', 'LiSi', 2, '1992-03-20', 2, 0),
('member3', 'm123456', '13800138003', 'member3@test.com', 'WangWu', 1, '1988-07-10', 1, 1),
('member4', 'm123456', '13800138004', 'member4@test.com', 'ZhaoLiu', 2, '1995-11-05', 3, 0),
('member5', 'm123456', '13800138005', 'member5@test.com', 'QianQi', 1, '1993-09-18', 1, 2),
('test_user', 'test123', '13900139000', 'test@test.com', 'TestUser', 1, '2000-01-01', 1, 0);

-- Insert coach data
INSERT INTO coach (username, password, nickname, phone, email, introduction, skills, status) VALUES
('coach1', 'c123456', 'Coach-LiMing', '13700137001', 'coach1@gym.com', '10 years experience', 'Muscle,Fat Loss', 1),
('coach2', 'c123456', 'Coach-WangFang', '13700137002', 'coach2@gym.com', 'Yoga instructor', 'Yoga,Pilates', 1),
('coach3', 'c123456', 'Coach-LiuQiang', '13700137003', 'coach3@gym.com', 'Boxing coach', 'Boxing,Muay Thai', 1),
('coach4', 'c123456', 'Coach-ChenJie', '13700137004', 'coach4@gym.com', 'Swimming coach', 'Swimming', 1),
('coach5', 'c123456', 'Coach-ZhaoXue', '13700137005', 'coach5@gym.com', 'Spinning coach', 'Spinning,HIIT', 1);

-- Insert course data (Group classes)
INSERT INTO course (name, type, coach_id, start_time, end_time, max_capacity, booked_count, price, location, description, status) VALUES
('Morning Yoga', 1, 2, '2026-05-21 07:00:00', '2026-05-21 08:00:00', 20, 5, 50.00, 'Yoga Room 1F', 'Morning yoga class', 1),
('Fat Loss Boxing', 1, 3, '2026-05-21 18:00:00', '2026-05-21 19:00:00', 15, 8, 80.00, 'Boxing Area 2F', 'High intensity boxing', 1),
('Spinning', 1, 5, '2026-05-21 19:00:00', '2026-05-21 20:00:00', 30, 15, 60.00, 'Spinning Room 3F', 'Spinning class', 1),
('Evening Stretch', 1, 2, '2026-05-21 20:30:00', '2026-05-21 21:30:00', 20, 3, 40.00, 'Yoga Room 1F', 'Evening stretching', 1),
('Core Strength', 1, 1, '2026-05-22 10:00:00', '2026-05-22 11:00:00', 15, 10, 70.00, 'Strength Area 2F', 'Core training', 1),
('Beginner Yoga', 1, 2, '2026-05-22 15:00:00', '2026-05-22 16:00:00', 25, 12, 50.00, 'Yoga Room 1F', 'Beginner yoga', 1),
('HIIT', 1, 5, '2026-05-22 18:00:00', '2026-05-22 19:00:00', 20, 20, 90.00, 'Spinning Room 3F', 'Full class', 2),
('Muay Thai Basic', 1, 3, '2026-05-23 09:00:00', '2026-05-23 10:00:00', 12, 6, 100.00, 'Boxing Area 2F', 'Muay Thai basics', 1);

-- Insert course data (Private classes)
INSERT INTO course (name, type, coach_id, start_time, end_time, max_capacity, booked_count, price, location, description, status) VALUES
('Muscle PT', 2, 1, '2026-05-21 14:00:00', '2026-05-21 15:00:00', 1, 1, 300.00, 'PT Area 2F', '1-on-1 muscle training', 1),
('Fat Loss PT', 2, 1, '2026-05-21 16:00:00', '2026-05-21 17:00:00', 1, 0, 300.00, 'PT Area 2F', '1-on-1 fat loss', 1),
('Yoga PT', 2, 2, '2026-05-21 17:00:00', '2026-05-21 18:00:00', 1, 1, 280.00, 'Yoga Room 1F', 'Private yoga', 1),
('Swimming PT', 2, 4, '2026-05-22 10:00:00', '2026-05-22 11:00:00', 1, 0, 350.00, 'Swimming Pool', 'Private swimming', 1),
('Boxing PT', 2, 3, '2026-05-22 14:00:00', '2026-05-22 15:00:00', 1, 1, 320.00, 'Boxing Area 2F', 'Private boxing', 1),
('Rehab PT', 2, 4, '2026-05-22 16:00:00', '2026-05-22 17:00:00', 1, 0, 380.00, 'Swimming Pool', 'Water rehab', 1),
('Fitness Test', 2, 1, '2026-05-23 10:00:00', '2026-05-23 11:00:00', 1, 0, 200.00, 'Test Room', 'Body assessment', 1);

-- Insert equipment data
INSERT INTO equipment (name, type, location, status, purchase_date, maintenance_record) VALUES
('Treadmill A1', 'Cardio', 'Cardio Area 1F', 1, '2025-01-10', '2026-04-15 Maintenance'),
('Treadmill A2', 'Cardio', 'Cardio Area 1F', 1, '2025-01-10', '2026-04-15 Maintenance'),
('Elliptical B1', 'Cardio', 'Cardio Area 1F', 1, '2025-02-20', '2026-03-10 Parts replaced'),
('Spinning Bike C1', 'Cardio', 'Spinning Room 3F', 1, '2025-03-15', NULL),
('Spinning Bike C2', 'Cardio', 'Spinning Room 3F', 2, '2025-03-15', NULL),
('Dumbbell 5kg', 'Strength', 'Strength Area 2F', 1, '2024-12-01', NULL),
('Dumbbell 10kg', 'Strength', 'Strength Area 2F', 1, '2024-12-01', NULL),
('Dumbbell 15kg', 'Strength', 'Strength Area 2F', 1, '2024-12-01', NULL),
('Barbell Set', 'Strength', 'Strength Area 2F', 1, '2025-01-05', '2026-05-01 Safety check'),
('Smith Machine', 'Strength', 'Strength Area 2F', 1, '2024-11-20', NULL),
('Yoga Mat', 'Accessory', 'Yoga Room 1F', 1, '2025-04-01', NULL),
('Yoga Ball', 'Accessory', 'Yoga Room 1F', 1, '2025-04-01', NULL),
('Boxing Gloves', 'Boxing', 'Boxing Area 2F', 1, '2025-02-10', NULL),
('Punching Bag', 'Boxing', 'Boxing Area 2F', 0, '2024-10-15', '2026-05-18 Need repair'),
('Rowing Machine', 'Cardio', 'Cardio Area 1F', 1, '2025-05-01', NULL);

-- Insert orders data
INSERT INTO orders (order_no, member_id, course_id, status, booking_time, payment_status, payment_time, payment_amount) VALUES
('ORD20260520001', 1, 1, 3, '2026-05-19 10:30:00', 1, '2026-05-19 10:31:00', 50.00),
('ORD20260520002', 2, 2, 1, '2026-05-19 11:00:00', 0, NULL, 80.00),
('ORD20260520003', 3, 3, 1, '2026-05-19 14:20:00', 0, NULL, 60.00),
('ORD20260520004', 1, 9, 3, '2026-05-18 09:00:00', 1, '2026-05-18 09:01:00', 300.00),
('ORD20260520005', 4, 10, 1, '2026-05-20 08:00:00', 0, NULL, 300.00),
('ORD20260520006', 2, 11, 3, '2026-05-17 16:00:00', 1, '2026-05-17 16:01:00', 280.00),
('ORD20260520007', 5, 4, 2, '2026-05-19 20:00:00', 0, '2026-05-19 20:05:00', 40.00),
('ORD20260520008', 3, 5, 1, '2026-05-20 10:00:00', 0, NULL, 70.00),
('ORD20260520009', 1, 6, 1, '2026-05-20 10:30:00', 0, NULL, 50.00),
('ORD20260520010', 4, 7, 4, '2026-05-18 12:00:00', 1, '2026-05-18 12:01:00', 90.00);

-- Insert notification data
INSERT INTO notification (member_id, type, title, content, is_read, send_time) VALUES
(1, 1, 'Booking Success', 'Yoga class booked', 1, '2026-05-19 10:31:00'),
(2, 1, 'Booking Success', 'Boxing class booked', 0, '2026-05-19 11:01:00'),
(3, 1, 'Booking Success', 'Spinning class booked', 0, '2026-05-19 14:21:00'),
(1, 3, 'Reminder', 'Class starts in 30min', 1, '2026-05-21 06:30:00'),
(2, 3, 'Reminder', 'Class starts in 30min', 0, '2026-05-21 17:30:00'),
(4, 5, 'Waitlist', 'Added to waitlist', 0, '2026-05-20 09:00:00'),
(5, 2, 'Cancelled', 'Class cancelled', 1, '2026-05-19 20:05:00'),
(1, 4, 'Starting Soon', 'PT class starting', 0, '2026-05-21 13:55:00'),
(3, 6, 'Class Cancelled', 'Coach unavailable', 0, '2026-05-22 08:00:00'),
(2, 1, 'Booking Success', 'Core class booked', 0, '2026-05-20 10:01:00');

-- Insert waitlist data
INSERT INTO waitlist (member_id, course_id, queue_position, status) VALUES
(4, 7, 1, 2),
(5, 7, 2, 1),
(6, 7, 3, 1),
(3, 3, 1, 1),
(1, 6, 1, 1);

-- Insert attendance data
INSERT INTO attendance (member_id, course_id, check_in_time) VALUES
(1, 1, '2026-05-21 06:50:00'),
(2, 11, '2026-05-21 16:55:00'),
(1, 9, '2026-05-21 13:55:00'),
(3, 3, '2026-05-21 18:50:00'),
(5, 3, '2026-05-21 18:55:00');

-- Verification
SELECT '========================================' as message;
SELECT 'Database import completed!' as message;
SELECT '========================================' as message;
SELECT '' as message;
SELECT 'Tables:' as message;
SHOW TABLES;
SELECT '' as message;
SELECT 'Record counts:' as message;
SELECT 'admin' as tbl, COUNT(*) as cnt FROM admin
UNION ALL SELECT 'member', COUNT(*) FROM member
UNION ALL SELECT 'coach', COUNT(*) FROM coach
UNION ALL SELECT 'course', COUNT(*) FROM course
UNION ALL SELECT 'equipment', COUNT(*) FROM equipment
UNION ALL SELECT 'orders', COUNT(*) FROM orders
UNION ALL SELECT 'notification', COUNT(*) FROM notification
UNION ALL SELECT 'waitlist', COUNT(*) FROM waitlist
UNION ALL SELECT 'attendance', COUNT(*) FROM attendance;

SELECT '' as message;
SELECT 'Test accounts:' as message;
SELECT 'Admin: admin/admin123' as account;
SELECT 'Member: member1/m123456' as account;
SELECT 'Coach: coach1/c123456' as account;
