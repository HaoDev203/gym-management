-- 为 notification 表添加 is_deleted 字段
ALTER TABLE notification ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 AFTER is_read;
