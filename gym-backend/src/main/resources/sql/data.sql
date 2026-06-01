-- 健身房管理系统测试数据脚本
-- 创建时间：2026-05-21

-- 插入管理员数据
-- 密码：admin123（BCrypt 加密）
INSERT INTO admin (id, username, password, name, role) VALUES
(1, 'admin', '$2a$10$BNeEJy.qieZcLnVknQ.dxenRLpnSPghJtTT2gfBpW.ma5lyG2ClpO', '超级管理员', 2),
(2, 'operator', '$2a$10$BNeEJy.qieZcLnVknQ.dxenRLpnSPghJtTT2gfBpW.ma5lyG2ClpO', '操作员', 1);

-- 插入教练数据
INSERT INTO coach (id, name, gender, phone, email, expertise, schedule) VALUES
(1, '李教练', 1, '13900139001', 'licoach@example.com', '健身塑形、增肌训练', '{"monday": "09:00-12:00", "wednesday": "14:00-18:00", "friday": "09:00-12:00"}'),
(2, '王教练', 0, '13900139002', 'wangcoach@example.com', '瑜伽、普拉提', '{"tuesday": "10:00-13:00", "thursday": "15:00-19:00", "saturday": "09:00-12:00"}'),
(3, '张教练', 1, '13900139003', 'zhangcoach@example.com', '动感单车、HIIT', '{"monday": "14:00-17:00", "wednesday": "10:00-13:00", "friday": "15:00-18:00"}');

-- 插入课程数据
INSERT INTO course (id, name, type, coach_id, start_time, end_time, capacity, booked_count, price, status) VALUES
(1, '基础健身训练', 1, 1, '2026-05-22 09:00:00', '2026-05-22 10:30:00', 10, 3, 150.00, 1),
(2, '高级瑜伽课程', 0, 2, '2026-05-22 10:00:00', '2026-05-22 11:30:00', 8, 5, 200.00, 1),
(3, '动感单车挑战', 1, 3, '2026-05-22 14:00:00', '2026-05-22 15:00:00', 15, 10, 120.00, 1),
(4, '私教一对一', 0, 1, '2026-05-23 09:00:00', '2026-05-23 10:00:00', 1, 0, 500.00, 1),
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
