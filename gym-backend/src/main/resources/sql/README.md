# 健身房管理系统 - 数据库配置说明

## 数据库连接信息

### MySQL 配置
- **Host**: `127.0.0.1`
- **Port**: `3306`
- **Database**: `gym_management`
- **Username**: `root`
- **Password**: `123`
- **Character Set**: `utf8mb4`
- **Timezone**: `Asia/Shanghai`

### JDBC 连接字符串
```
jdbc:mysql://127.0.0.1:3306/gym_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
```

---

## 快速开始

### 1. 连接数据库

#### 使用命令行
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p123
```

#### 使用 MySQL Workbench
- Host: `127.0.0.1`
- Port: `3306`
- Username: `root`
- Password: `123`

#### 使用 Navicat
- Host: `127.0.0.1`
- Port: `3306`
- Username: `root`
- Password: `123`

---

## 初始化数据库

### 方法 1: 命令行导入
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p123 < src/main/resources/sql/test-data.sql
```

### 方法 2: MySQL Workbench
1. 打开 MySQL Workbench
2. 连接到数据库
3. 打开 `test-data.sql` 文件
4. 执行脚本（Lightning 图标）

### 方法 3: Navicat
1. 连接到数据库
2. 右键数据库 → 执行 SQL 文件
3. 选择 `test-data.sql`
4. 开始执行

---

## 测试账户

### 管理员账户
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 超级管理员 |
| operator1 | op123456 | 普通管理员 |
| operator2 | op123456 | 普通管理员 |

### 会员账户
| 用户名 | 密码 | 昵称 | 手机号 |
|--------|------|------|--------|
| member1 | m123456 | 张三 | 13800138001 |
| member2 | m123456 | 李四 | 13800138002 |
| member3 | m123456 | 王五 | 13800138003 |
| member4 | m123456 | 赵六 | 13800138004 |
| member5 | m123456 | 钱七 | 13800138005 |
| test_user | test123 | 测试用户 | 13900139000 |

### 教练账户
| 用户名 | 密码 | 昵称 | 擅长领域 |
|--------|------|------|----------|
| coach1 | c123456 | 金牌教练 - 李明 | 增肌，减脂，功能性训练 |
| coach2 | c123456 | 瑜伽导师 - 王芳 | 瑜伽，普拉提，冥想 |
| coach3 | c123456 | 拳击教练 - 刘强 | 拳击，泰拳，搏击 |
| coach4 | c123456 | 游泳教练 - 陈杰 | 游泳，水中康复 |
| coach5 | c123456 | 动感单车 - 赵雪 | 动感单车，HIIT |

---

## 测试数据概览

### 数据表统计
| 表名 | 记录数 | 说明 |
|------|--------|------|
| admin | 3 | 管理员 |
| member | 6 | 会员 |
| coach | 5 | 教练 |
| course | 15 | 课程（8 个团课 + 7 个私教课） |
| equipment | 15 | 器材 |
| orders | 10 | 预约订单 |
| notification | 10 | 通知 |
| waitlist | 5 | 候补队列 |
| attendance | 5 | 签到记录 |

### 课程类型
- **团课（Group）**: 8 个
  - 晨间瑜伽、减脂搏击、动感单车、晚间拉伸
  - 核心力量、初级瑜伽、高强度间歇、泰拳基础

- **私教课（Private）**: 7 个
  - 增肌私教、减脂私教、瑜伽私教
  - 游泳私教、拳击私教、康复训练、体能测试

### 订单状态分布
- **已预约（BOOKED）**: 5 个
- **已取消（CANCELLED）**: 1 个
- **已完成（COMPLETED）**: 3 个
- **已爽约（NO_SHOW）**: 1 个

---

## 常用查询 SQL

### 1. 查看今日课程
```sql
SELECT * FROM course 
WHERE DATE(start_time) = CURDATE();
```

### 2. 查看会员预约记录
```sql
SELECT o.*, m.nickname, c.name as course_name 
FROM orders o 
JOIN member m ON o.member_id = m.id 
JOIN course c ON o.course_id = c.id 
WHERE m.id = 1;
```

### 3. 查看课程剩余名额
```sql
SELECT c.name, c.start_time, c.max_capacity, c.booked_count,
       (c.max_capacity - c.booked_count) as remaining 
FROM course c 
WHERE c.status = 1;
```

### 4. 查看器材状态
```sql
SELECT name, type, location, 
       CASE status 
           WHEN 0 THEN '维修中' 
           WHEN 1 THEN '可用' 
           WHEN 2 THEN '已借出' 
       END as status_desc 
FROM equipment;
```

### 5. 查看会员爽约次数
```sql
SELECT nickname, phone, no_show_count 
FROM member 
WHERE no_show_count > 0;
```

---

## 数据验证

### 验证数据库连接
```sql
-- 检查数据库是否创建成功
SHOW DATABASES LIKE 'gym_management';

-- 检查表是否创建成功
USE gym_management;
SHOW TABLES;

-- 检查数据量
SELECT 
    'member' as table_name, COUNT(*) as count FROM member
UNION ALL
SELECT 'admin', COUNT(*) FROM admin
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
```

---

## 常见问题

### Q1: 无法连接数据库
**解决方案**:
1. 检查 MySQL 服务是否启动
2. 检查端口 3306 是否开放
3. 检查用户名密码是否正确
4. 检查防火墙设置

### Q2: 中文乱码
**解决方案**:
```sql
ALTER DATABASE gym_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Q3: 时区错误
**解决方案**:
确保 JDBC URL 包含 `serverTimezone=Asia/Shanghai`

---

## 文件位置

- **测试数据 SQL**: `src/main/resources/sql/test-data.sql`
- **初始化 SQL**: `src/main/resources/sql/init.sql`
- **配置文件**: `src/main/resources/application-dev.yml`

---

## 更新日期
2026-05-20
