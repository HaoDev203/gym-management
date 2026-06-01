# 健身房管理系统 - 数据库配置说明

> **最后更新**: 2026-05-21

---

## 📋 目录

1. [数据库连接信息](#-数据库连接信息)
2. [快速开始](#-快速开始)
3. [文件说明](#-文件说明)
4. [测试数据](#-测试数据)
5. [常用查询](#-常用查询)
6. [数据验证](#-数据验证)
7. [常见问题](#-常见问题)

---

## 🔧 数据库连接信息

### MySQL 配置
- **Host**: `127.0.0.1`
- **Port**: `3306`
- **Database**: `gym_management`
- **Username**: `<你的 MySQL 用户名>`
- **Password**: `<你的 MySQL 密码>`
- **Character Set**: `utf8mb4`
- **Timezone**: `Asia/Shanghai`

> ⚠️ **注意**: 请将上述用户名和密码替换为你实际的 MySQL 配置

### JDBC 连接字符串
```
jdbc:mysql://127.0.0.1:3306/gym_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
```

---

## 🚀 快速开始

### 1️⃣ 连接数据库

#### 使用命令行
```bash
mysql -h 127.0.0.1 -P 3306 -u <用户名> -p<密码>
```

#### 使用 MySQL Workbench
- Host: `127.0.0.1`
- Port: `3306`
- Username: `<你的 MySQL 用户名>`
- Password: `<你的 MySQL 密码>`

#### 使用 Navicat
- Host: `127.0.0.1`
- Port: `3306`
- Username: `<你的 MySQL 用户名>`
- Password: `<你的 MySQL 密码>`

### 2️⃣ 初始化数据库

**方法一：使用一键导入脚本（推荐）**

> ⚠️ **重要提示**：使用前必须先修改脚本中的数据库配置！

#### Windows 批处理
1. 用文本编辑器打开 `import.bat`
2. 找到第 48-60 行的 MySQL 命令
3. 将 `-u root -p123` 修改为你的用户名和密码
4. 保存文件
5. 双击运行 `import.bat`

```bash
# Windows 批处理
import.bat
```

#### PowerShell
1. 用文本编辑器打开 `import.ps1`
2. 找到第 11-12 行：
   ```powershell
   $mysqlUser   = "root"        # 修改为你的 MySQL 用户名
   $mysqlPass   = "123"         # 修改为你的 MySQL 密码
   ```
3. 修改为你的实际配置
4. 保存文件
5. 右键点击 `import.ps1` → "使用 PowerShell 运行"

```bash
# PowerShell
.\import.ps1
```

**方法二：手动执行 SQL**
```bash
# 1. 执行结构文件
mysql -h 127.0.0.1 -P 3306 -u <用户名> -p<密码> < schema.sql

# 2. 执行数据文件
mysql -h 127.0.0.1 -P 3306 -u <用户名> -p<密码> gym_management < data.sql
```

**方法三：使用图形化工具**

#### MySQL Workbench
1. 连接到数据库
2. 打开 `schema.sql` 文件并执行
3. 打开 `data.sql` 文件并执行

#### Navicat
1. 连接到数据库
2. 右键数据库 → 执行 SQL 文件
3. 依次选择 `schema.sql` 和 `data.sql`

---

## 📁 文件说明

### 核心文件

| 文件名 | 作用 | 执行顺序 | 是否需要修改 |
|--------|------|----------|-------------|
| **schema.sql** | 创建数据库表结构 | 第 1 步 | ❌ 不需要 |
| **data.sql** | 插入测试数据 | 第 2 步 | ❌ 不需要 |
| **import.bat** | Windows 一键导入脚本 | 自动执行 | ✅ **需要修改** |
| **import.ps1** | PowerShell 一键导入脚本 | 自动执行 | ✅ **需要修改** |

### 详细说明

#### schema.sql - 数据库结构文件
包含 9 个数据表的创建语句：
- `member` - 会员表
- `admin` - 管理员表
- `coach` - 教练表
- `course` - 课程表
- `equipment` - 器材表
- `orders` - 订单表
- `booking` - 预约表
- `notification` - 通知表
- `no_show_record` - 爽约记录表

**使用场景**：
- ✅ 首次部署项目
- ✅ 数据库迁移
- ✅ 重置数据库结构

#### data.sql - 测试数据文件
包含完整的测试数据：
- 管理员账户（2 个）
- 教练数据（3 个）
- 会员数据（3 个）
- 课程数据（5 个）
- 器材数据（8 个）
- 订单数据（4 个）
- 预约数据（4 个）
- 通知数据（3 个）
- 爽约记录（1 个）

**使用场景**：
- ✅ 开发环境测试
- ✅ 演示环境搭建
- ✅ 学习项目结构

#### import.bat / import.ps1 - 一键导入脚本
自动执行 schema.sql 和 data.sql，包含错误处理和进度提示。

> ⚠️ **重要提示**：
> - 这两个脚本包含硬编码的 MySQL 用户名和密码（root/123）
> - **使用前必须修改为你的实际配置**
> - 修改位置已在脚本中用注释标注

**使用方式**：
```bash
# Windows 批处理（先修改配置）
import.bat

# PowerShell（先修改配置）
.\import.ps1
```

---

## 📊 测试数据

### 测试账户

#### 管理员账户
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 超级管理员 |
| operator | admin123 | 普通管理员 |

#### 会员账户
| 用户名（手机号） | 密码 | 姓名 | 手机号 |
|----------------|------|------|--------|
| 13800138001 | 123456 | 张三 | 13800138001 |
| 13800138002 | 123456 | 李四 | 13800138002 |
| 13800138003 | 123456 | 王五 | 13800138003 |

#### 教练账户
| 姓名 | 性别 | 手机号 | 擅长领域 |
|------|------|--------|----------|
| 李教练 | 男 | 13900139001 | 健身塑形、增肌训练 |
| 王教练 | 女 | 13900139002 | 瑜伽、普拉提 |
| 张教练 | 男 | 13900139003 | 动感单车、HIIT |

### 数据分布

#### 课程类型
- 🏋️ **团课（type=1）**: 3 个
  - 基础健身训练、动感单车挑战、HIIT 高强度间歇训练

- 👨‍🏫 **私教课（type=0）**: 2 个
  - 高级瑜伽课程、私教一对一

#### 订单状态
- ⏳ **待支付**: 1 个
- ✅ **已支付**: 3 个
- ✔️ **已签到**: 1 个

---

## 🔍 常用查询

### 1️⃣ 查看今日课程
```sql
SELECT * FROM course 
WHERE DATE(start_time) = CURDATE();
```

### 2️⃣ 查看会员预约记录
```sql
SELECT o.*, m.name, m.phone, c.name as course_name 
FROM orders o 
JOIN member m ON o.member_id = m.id 
JOIN course c ON o.course_id = c.id 
WHERE m.id = 1;
```

### 3️⃣ 查看课程剩余名额
```sql
SELECT c.name, c.start_time, c.capacity, c.booked_count,
       (c.capacity - c.booked_count) as remaining 
FROM course c 
WHERE c.status = 1;
```

### 4️⃣ 查看器材状态
```sql
SELECT name, quantity, location, '可用' as status_desc 
FROM equipment;
```

### 5️⃣ 查看会员爽约次数
```sql
SELECT name, phone, no_show_count 
FROM member 
WHERE no_show_count > 0;
```

---

## ✅ 数据验证

### 验证步骤

#### 1. 检查数据库是否创建成功
```sql
SHOW DATABASES LIKE 'gym_management';
```

#### 2. 检查表是否创建成功
```sql
USE gym_management;
SHOW TABLES;
```

#### 3. 检查数据量
```sql
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
SELECT 'booking', COUNT(*) FROM booking
UNION ALL
SELECT 'notification', COUNT(*) FROM notification
UNION ALL
SELECT 'no_show_record', COUNT(*) FROM no_show_record;
```

**预期结果**：
| table_name | count |
|------------|-------|
| member | 3 |
| admin | 2 |
| coach | 3 |
| course | 5 |
| equipment | 8 |
| orders | 4 |
| booking | 4 |
| notification | 3 |
| no_show_record | 1 |

---

## ❓ 常见问题

### Q1: 无法连接数据库
**现象**：连接超时或拒绝连接

**解决方案**：
1. ✅ 检查 MySQL 服务是否启动
2. ✅ 检查端口 3306 是否开放
3. ✅ 检查用户名密码是否正确
4. ✅ 检查防火墙设置

### Q2: 导入脚本无法使用
**现象**：运行 import.bat 或 import.ps1 时连接失败

**解决方案**：
1. ✅ 打开脚本文件
2. ✅ 找到数据库配置部分
3. ✅ 修改用户名和密码为你的实际配置
4. ✅ 保存后重新运行

### Q3: 中文乱码
**现象**：查询结果中文显示为乱码

**解决方案**：
```sql
ALTER DATABASE gym_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Q4: 时区错误
**现象**：时间字段与预期不符

**解决方案**：
确保 JDBC URL 包含时区参数：
```
serverTimezone=Asia/Shanghai
```

---

## 📂 文件位置

```
gym-backend/src/main/resources/sql/
├── README.md              # 本说明文档
├── schema.sql             # 数据库结构文件（无需修改）
├── data.sql               # 测试数据文件（无需修改）
├── import.bat             # Windows 导入脚本（⚠️ 需要修改配置）
├── import.ps1             # PowerShell 导入脚本（⚠️ 需要修改配置）
└── application-dev.yml    # 数据库配置文件（上级目录）
```

---

## 🔐 安全提示

### 关于导入脚本的重要说明

`import.bat` 和 `import.ps1` 脚本中包含硬编码的 MySQL 用户名和密码：

```
默认配置：
- 用户名：root
- 密码：123
```

**使用前必须**：
1. 用文本编辑器打开脚本文件
2. 找到数据库配置部分
3. 修改为你的实际 MySQL 配置
4. 保存后再执行

**原因**：
- 不同的 MySQL 实例有不同的用户名和密码
- 硬编码的配置无法适用于所有环境
- 为了安全起见，不应在版本控制系统中提交真实密码

**修改位置**：
- **import.bat**: 第 48-60 行的 MySQL 命令中
- **import.ps1**: 第 11-12 行的变量定义处

---

## 📝 更新日期

2026-05-21
