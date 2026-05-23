-- ========================================
-- 健身房管理系统 - 数据验证查询
-- ========================================

USE gym_management;

-- 1. 验证所有表已创建
SELECT '表验证' as check_type;
SHOW TABLES;

-- 2. 统计各表数据量
SELECT '数据量统计' as check_type;
SELECT 
    'admin' as table_name, COUNT(*) as record_count FROM admin
UNION ALL
SELECT 'member', COUNT(*) FROM member
UNION ALL
SELECT 'coach', COUNT(*) FROM coach
UNION ALL
SELECT 'course', COUNT(*) FROM course
UNION ALL
SELECT 'equipment', COUNT(*) FROM equipment
UNION ALL
SELECT 'orders', COUNT(*) FROM orders
UNION ALL
SELECT 'notification', COUNT(*) FROM notification
UNION ALL
SELECT 'waitlist', COUNT(*) FROM waitlist
UNION ALL
SELECT 'attendance', COUNT(*) FROM attendance;

-- 3. 验证管理员账户
SELECT '管理员账户' as check_type;
SELECT id, username, 
       CASE role 
           WHEN 1 THEN '普通管理员' 
           WHEN 2 THEN '超级管理员' 
       END as role 
FROM admin;

-- 4. 验证会员账户
SELECT '会员账户' as check_type;
SELECT id, username, nickname, phone, membership_level, no_show_count 
FROM member;

-- 5. 验证教练信息
SELECT '教练信息' as check_type;
SELECT id, username, nickname, skills, status 
FROM coach;

-- 6. 验证课程安排
SELECT '课程安排' as check_type;
SELECT 
    id, name,
    CASE type WHEN 1 THEN '团课' WHEN 2 THEN '私教课' END as course_type,
    start_time, end_time,
    max_capacity, booked_count,
    (max_capacity - booked_count) as remaining,
    price,
    CASE status 
        WHEN 0 THEN '取消' 
        WHEN 1 THEN '可预约' 
        WHEN 2 THEN '已满' 
        WHEN 3 THEN '已结束' 
    END as status
FROM course
ORDER BY start_time;

-- 7. 验证今日课程
SELECT '今日课程' as check_type;
SELECT 
    name,
    CASE type WHEN 1 THEN '团课' WHEN 2 THEN '私教课' END as course_type,
    start_time, end_time,
    location,
    (max_capacity - booked_count) as remaining
FROM course
WHERE DATE(start_time) = CURDATE()
ORDER BY start_time;

-- 8. 验证预约订单
SELECT '预约订单' as check_type;
SELECT 
    o.order_no,
    m.nickname as member_name,
    c.name as course_name,
    CASE o.status 
        WHEN 1 THEN '已预约' 
        WHEN 2 THEN '已取消' 
        WHEN 3 THEN '已完成' 
        WHEN 4 THEN '已爽约' 
    END as status,
    o.payment_status,
    o.payment_amount
FROM orders o
JOIN member m ON o.member_id = m.id
JOIN course c ON o.course_id = c.id
ORDER BY o.booking_time DESC
LIMIT 10;

-- 9. 验证器材状态
SELECT '器材状态' as check_type;
SELECT 
    name, type, location,
    CASE status 
        WHEN 0 THEN '维修中' 
        WHEN 1 THEN '可用' 
        WHEN 2 THEN '已借出' 
    END as status
FROM equipment
ORDER BY type, location;

-- 10. 验证通知记录
SELECT '通知记录' as check_type;
SELECT 
    m.nickname as member_name,
    CASE n.type 
        WHEN 1 THEN '预约成功' 
        WHEN 2 THEN '取消通知' 
        WHEN 3 THEN '课前提醒' 
        WHEN 4 THEN '即将开始' 
        WHEN 5 THEN '候补成功' 
        WHEN 6 THEN '课程取消' 
    END as notification_type,
    n.title,
    n.is_read,
    n.send_time
FROM notification n
JOIN member m ON n.member_id = m.id
ORDER BY n.send_time DESC
LIMIT 10;

-- 11. 验证候补队列
SELECT '候补队列' as check_type;
SELECT 
    m.nickname as member_name,
    c.name as course_name,
    w.queue_position,
    CASE w.status 
        WHEN 0 THEN '取消' 
        WHEN 1 THEN '等待中' 
        WHEN 2 THEN '已转正' 
    END as status
FROM waitlist w
JOIN member m ON w.member_id = m.id
JOIN course c ON w.course_id = c.id
ORDER BY w.created_at;

-- 12. 验证签到记录
SELECT '签到记录' as check_type;
SELECT 
    m.nickname as member_name,
    c.name as course_name,
    a.check_in_time
FROM attendance a
JOIN member m ON a.member_id = m.id
JOIN course c ON a.course_id = c.id
ORDER BY a.check_in_time DESC;

-- 13. 课程预约率统计
SELECT '课程预约率' as check_type;
SELECT 
    name,
    max_capacity,
    booked_count,
    ROUND(booked_count * 100.0 / max_capacity, 2) as booking_rate
FROM course
WHERE type = 1
ORDER BY booking_rate DESC;

-- 14. 会员活跃度统计
SELECT '会员活跃度' as check_type;
SELECT 
    m.nickname,
    m.phone,
    COUNT(o.id) as total_orders,
    SUM(CASE WHEN o.status = 3 THEN 1 ELSE 0 END) as completed,
    SUM(CASE WHEN o.status = 2 THEN 1 ELSE 0 END) as cancelled,
    SUM(CASE WHEN o.status = 4 THEN 1 ELSE 0 END) as no_show
FROM member m
LEFT JOIN orders o ON m.id = o.member_id
GROUP BY m.id, m.nickname, m.phone
ORDER BY total_orders DESC;

-- 15. 教练课程统计
SELECT '教练课程统计' as check_type;
SELECT 
    c.nickname as coach_name,
    c.skills,
    COUNT(course.id) as total_courses,
    SUM(course.booked_count) as total_bookings
FROM coach c
LEFT JOIN course ON c.id = course.coach_id
GROUP BY c.id, c.nickname, c.skills
ORDER BY total_bookings DESC;

-- ========================================
-- 数据完整性检查
-- ========================================

-- 检查是否有孤立的订单（会员或课程不存在）
SELECT '孤立订单检查' as check_type;
SELECT COUNT(*) as orphan_orders
FROM orders o
LEFT JOIN member m ON o.member_id = m.id
LEFT JOIN course c ON o.course_id = c.id
WHERE m.id IS NULL OR c.id IS NULL;

-- 检查是否有重复的手机号
SELECT '重复手机号检查' as check_type;
SELECT phone, COUNT(*) as count
FROM member
WHERE deleted = 0
GROUP BY phone
HAVING COUNT(*) > 1;

-- 检查是否有重复的用户名
SELECT '重复用户名检查' as check_type;
SELECT username, COUNT(*) as count
FROM member
WHERE deleted = 0
GROUP BY username
HAVING COUNT(*) > 1;

-- ========================================
-- 查询完成
-- ========================================
SELECT '数据验证完成！' as message;
