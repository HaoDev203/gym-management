-- 更新课程时间为当前时间附近，使定时提醒任务可触发
-- 课程 1：基础健身训练 - 5 分钟后开始（触发课前 5 分钟提醒）
UPDATE course SET start_time = DATE_ADD(NOW(), INTERVAL 5 MINUTE), end_time = DATE_ADD(NOW(), INTERVAL 95 MINUTE) WHERE id = 1;
-- 课程 2：高级瑜伽课程 - 1 小时后开始（触发课前 1 小时提醒）  
UPDATE course SET start_time = DATE_ADD(NOW(), INTERVAL 60 MINUTE), end_time = DATE_ADD(NOW(), INTERVAL 150 MINUTE) WHERE id = 2;
-- 课程 3：动感单车挑战 - 2 小时后开始
UPDATE course SET start_time = DATE_ADD(NOW(), INTERVAL 120 MINUTE), end_time = DATE_ADD(NOW(), INTERVAL 180 MINUTE) WHERE id = 3;
-- 课程 4：私教一对一 - 明天
UPDATE course SET start_time = DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 9 HOUR, end_time = DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 10 HOUR WHERE id = 4;
-- 课程 5：HIIT 高强度间歇训练 - 明天
UPDATE course SET start_time = DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 15 HOUR, end_time = DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 16 HOUR WHERE id = 5;
