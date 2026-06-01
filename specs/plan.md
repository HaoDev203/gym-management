# 健身房管理系统 - 技术方案设计

> **文档目标**：基于 `spec.md` 生成可落地的技术方案，用于后续任务拆分、接口设计、页面设计、测试设计和代码生成  
> **适用角色**：后端开发、前端开发、测试工程师、项目经理  
> **关联文档**：`spec.md`（产品需求）、`AGENTS.md`（代码规范）

---

## 1. 技术选型

### 1.1 技术栈

| 层级 | 技术选型 | 版本 | 选型理由 |
|------|----------|------|----------|
| **后端框架** | Spring Boot | 3.2+ | 快速开发、生态完善、企业级支持 |
| **持久层** | MyBatis-Plus | 3.5+ | 简化 CRUD、支持动态 SQL |
| **数据库** | MySQL | 8.0+ | 成熟稳定、事务支持完善 |
| **缓存** | Redis | 7.0+ | 高性能、支持分布式锁（并发预约） |
| **前端框架** | Vue 3 | 3.4+ | 响应式、组件化、生态丰富 |
| **UI 组件库** | Element Plus | 2.4+ | 企业级组件、文档完善 |
| **状态管理** | Pinia | 2.1+ | Vue 3 官方推荐、轻量 |
| **构建工具** | Vite | 5.0+ | 快速启动、热更新 |
| **接口文档** | Knife4j (Swagger) | 4.0+ | 自动生成 API 文档、支持在线测试 |
| **权限控制** | Spring Security + JWT | - | 无状态认证、支持角色权限 |
| **短信服务** | 阿里云短信 | - | 稳定可靠、到达率高 |

### 1.2 项目结构

```
gym-management-system/
├── gym-backend/                    # 后端项目
│   ├── src/main/java/com/gym/
│   │   ├── GymApplication.java
│   │   ├── config/                 # 配置类
│   │   │   ├── SecurityConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   ├── MyBatisPlusConfig.java
│   │   │   └── SwaggerConfig.java
│   │   ├── controller/             # Controller 层
│   │   │   ├── AuthController.java
│   │   │   ├── MemberController.java
│   │   │   ├── AdminController.java
│   │   │   ├── CoachController.java
│   │   │   ├── CourseController.java
│   │   │   ├── EquipmentController.java
│   │   │   ├── OrderController.java
│   │   │   ├── StatisticsController.java
│   │   │   └── NotificationController.java
│   │   ├── service/                # Service 层
│   │   │   ├── impl/
│   │   │   ├── MemberService.java
│   │   │   ├── AdminService.java
│   │   │   ├── CoachService.java
│   │   │   ├── CourseService.java
│   │   │   ├── EquipmentService.java
│   │   │   ├── OrderService.java
│   │   │   ├── StatisticsService.java
│   │   │   └── NotificationService.java
│   │   ├── mapper/                 # DAO 层
│   │   │   ├── MemberMapper.java
│   │   │   ├── AdminMapper.java
│   │   │   ├── CoachMapper.java
│   │   │   ├── CourseMapper.java
│   │   │   ├── EquipmentMapper.java
│   │   │   ├── OrderMapper.java
│   │   │   ├── NotificationMapper.java
│   │   │   └── NoShowRecordMapper.java
│   │   ├── entity/                 # 实体类
│   │   │   ├── Member.java
│   │   │   ├── Admin.java
│   │   │   ├── Coach.java
│   │   │   ├── Course.java
│   │   │   ├── Equipment.java
│   │   │   ├── Order.java
│   │   │   ├── Notification.java
│   │   │   └── NoShowRecord.java
│   │   ├── dto/                    # 数据传输对象
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── common/                 # 通用类
│   │   │   ├── BaseResponse.java
│   │   │   ├── BusinessException.java
│   │   │   ├── ErrorCode.java
│   │   │   └── Constants.java
│   │   ├── enums/                  # 枚举
│   │   │   ├── OrderStatus.java
│   │   │   ├── CourseType.java
│   │   │   ├── AdminRole.java
│   │   │   └── NotificationType.java
│   │   └── util/                   # 工具类
│   │       ├── JwtUtil.java
│   │       ├── SmsUtil.java
│   │       └── DateUtil.java
│   ├── src/main/resources/
│   │   ├── mapper/                 # MyBatis XML
│   │   ├── application.yml
│   │   └── application-dev.yml
│   └── pom.xml
│
├── gym-frontend/                   # 前端项目
│   ├── src/
│   │   ├── main.js
│   │   ├── App.vue
│   │   ├── api/                    # API 接口
│   │   │   ├── auth.js
│   │   │   ├── member.js
│   │   │   ├── course.js
│   │   │   ├── order.js
│   │   │   └── admin.js
│   │   ├── components/             # 公共组件
│   │   │   ├── Header.vue
│   │   │   ├── Sidebar.vue
│   │   │   └── CourseCard.vue
│   │   ├── views/                  # 页面
│   │   │   ├── member/             # 会员端页面
│   │   │   └── admin/              # 管理员端页面
│   │   ├── stores/                 # Pinia 状态管理
│   │   │   ├── user.js
│   │   │   └── notification.js
│   │   ├── router/                 # 路由配置
│   │   │   └── index.js
│   │   └── utils/                  # 工具函数
│   │       ├── request.js
│   │       └── auth.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
└── specs/
    ├── spec.md                     # 产品需求规格说明书
    ├── plan.md                     # 技术方案设计（本文档）
    └── AGENTS.md                   # 代码注释规范
```

---

## 2. 数据库设计

### 2.1 ER 图概览

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   Member    │       │   Course    │       │   Coach     │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id          │       │ id          │       │ id          │
│ username    │       │ name        │       │ name        │
│ password    │       │ type        │◄──────│ phone       │
│ name        │       │ coach_id    │       │ expertise   │
│ phone       │       │ start_time  │       │ schedule    │
│ id_card     │       │ end_time    │       │ created_at  │
│ gender      │       │ capacity    │       │ updated_at  │
│ birthday    │       │ price       │       └─────────────┘
│ emergency_  │       │ status      │
│ contact     │       │ created_at  │
│ no_show_    │       │ updated_at  │
│ count       │       └─────────────┘
│ is_banned   │              │
│ banned_     │              │
│ until       │              ▼
│ created_at  │       ┌─────────────┐
│ updated_at  │       │    Order    │
└─────────────┘       ├─────────────┤
        │             │ id          │
        │             │ order_no    │
        │             │ member_id   │
        │             │ course_id   │
        │             │ status      │
        │             │ amount      │
        │             │ paid_amount │
        │             │ check_in    │
        │             │ created_at  │
        │             │ updated_at  │
        │             └─────────────┘
        │
        ▼
┌─────────────┐       ┌─────────────┐
│Notification │       │  Equipment  │
├─────────────┤       ├─────────────┤
│ id          │       │ id          │
│ member_id   │       │ name        │
│ title       │       │ quantity    │
│ content     │       │ location    │
│ type        │       │ created_at  │
│ is_read     │       │ updated_at  │
│ created_at  │       └─────────────┘
└─────────────┘

┌─────────────┐       ┌─────────────┐
│    Admin    │       │NoShowRecord │
├─────────────┤       ├─────────────┤
│ id          │       │ id          │
│ username    │       │ member_id   │
│ password    │       │ order_id    │
│ role        │       │ course_time │
│ created_at  │       │ created_at  │
│ updated_at  │       └─────────────┘
└─────────────┘
```

### 2.2 数据表详细设计

#### 2.2.1 会员表 (member)

```sql
CREATE TABLE `member` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员 ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名（手机号）',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt 加密）',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `phone` VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
  `id_card` VARCHAR(18) COMMENT '身份证号',
  `gender` TINYINT DEFAULT 1 COMMENT '性别 0-女 1-男',
  `birthday` DATE COMMENT '生日',
  `emergency_contact` VARCHAR(50) COMMENT '紧急联系人姓名',
  `emergency_phone` VARCHAR(20) COMMENT '紧急联系人电话',
  `no_show_count` INT DEFAULT 0 COMMENT '爽约次数',
  `is_banned` TINYINT DEFAULT 0 COMMENT '是否被限制预约 0-否 1-是',
  `banned_until` DATETIME COMMENT '限制预约截止时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_phone` (`phone`),
  INDEX `idx_banned` (`is_banned`, `banned_until`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';
```

**对应 spec 需求**：3.1 会员管理 - 会员信息字段

---

#### 2.2.2 管理员表 (admin)

```sql
CREATE TABLE `admin` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员 ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt 加密）',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `role` TINYINT NOT NULL DEFAULT 1 COMMENT '角色 1-普通管理员 2-超级管理员',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
```

**对应 spec 需求**：2.2 管理员 - 分级管理

---

#### 2.2.3 教练表 (coach)

```sql
CREATE TABLE `coach` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '教练 ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系方式',
  `expertise` VARCHAR(500) COMMENT '擅长课程（JSON 数组）',
  `schedule` JSON COMMENT '可排班时间（JSON 格式）',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练表';
```

**对应 spec 需求**：3.2 员工管理 - 员工信息字段

---

#### 2.2.4 课程表 (course)

```sql
CREATE TABLE `course` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '课程名称',
  `type` TINYINT NOT NULL COMMENT '课程类型 1-团课 2-私教课',
  `coach_id` BIGINT NOT NULL COMMENT '教练 ID',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `capacity` INT NOT NULL DEFAULT 1 COMMENT '课程容量',
  `booked_count` INT DEFAULT 0 COMMENT '已预约人数',
  `price` DECIMAL(10,2) NOT NULL COMMENT '课程价格',
  `status` TINYINT DEFAULT 1 COMMENT '状态 1-可预约 2-已满 3-已结束 4-已取消',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_start_time` (`start_time`),
  INDEX `idx_coach` (`coach_id`),
  INDEX `idx_status` (`status`),
  CONSTRAINT `fk_course_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';
```

**对应 spec 需求**：3.4 课程管理 - 课程信息字段

---

#### 2.2.5 器材表 (equipment)

```sql
CREATE TABLE `equipment` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '器材 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '器材名称',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `location` VARCHAR(200) COMMENT '位置',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_location` (`location`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='器材表';
```

**对应 spec 需求**：3.3 器材管理 - 器材信息字段

---

#### 2.2.6 订单表 (orders)

```sql
CREATE TABLE `orders` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单 ID',
  `order_no` VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
  `member_id` BIGINT NOT NULL COMMENT '会员 ID',
  `course_id` BIGINT NOT NULL COMMENT '课程 ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1-已预约 2-已取消 3-已完成 4-已爽约',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '应付金额',
  `paid_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '实付金额',
  `check_in` TINYINT DEFAULT 0 COMMENT '是否签到 0-否 1-是',
  `check_in_time` DATETIME COMMENT '签到时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_member` (`member_id`),
  INDEX `idx_course` (`course_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_order_no` (`order_no`),
  CONSTRAINT `fk_order_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `fk_order_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

**对应 spec 需求**：3.5 预约与订单 - 订单信息字段

---

#### 2.2.7 通知表 (notification)

```sql
CREATE TABLE `notification` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知 ID',
  `member_id` BIGINT NOT NULL COMMENT '会员 ID',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `content` VARCHAR(500) NOT NULL COMMENT '内容',
  `type` TINYINT NOT NULL COMMENT '类型 1-预约成功 2-取消通知 3-课前提醒 4-即将开始 5-候补成功 6-课程取消',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读 0-否 1-是',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_member` (`member_id`),
  INDEX `idx_is_read` (`is_read`),
  CONSTRAINT `fk_notification_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';
```

**对应 spec 需求**：3.6 通知提醒 - 通知场景

---

#### 2.2.8 爽约记录表 (no_show_record)

```sql
CREATE TABLE `no_show_record` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录 ID',
  `member_id` BIGINT NOT NULL COMMENT '会员 ID',
  `order_id` BIGINT NOT NULL COMMENT '订单 ID',
  `course_time` DATETIME NOT NULL COMMENT '课程时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_member` (`member_id`),
  CONSTRAINT `fk_no_show_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `fk_no_show_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='爽约记录表';
```

**对应 spec 需求**：3.5 预约与订单 - 签到规则（爽约记录永久保存）

---

### 2.3 核心业务索引优化

| 表 | 索引字段 | 优化目的 |
|----|----------|----------|
| course | start_time, status | 快速查询可预约课程 |
| orders | member_id, course_id, status | 快速查询会员订单、课程订单 |
| member | phone, is_banned | 登录验证、预约资格校验 |
| notification | member_id, is_read | 快速查询未读通知 |

---

## 3. API 接口设计

### 3.1 接口规范

**统一响应格式**（遵循 AGENTS.md 规范）：

```java
/**
 * 统一返回结构
 *
 * @param <T> 数据类型
 */
public class BaseResponse<T> {
    private Integer code;      // 状态码
    private String message;    // 提示信息
    private T data;           // 数据
}
```

**错误码定义**：

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录 |
| 403 | 无权限 |
| 1001 | 用户名或密码错误 |
| 1002 | 会员已被限制预约 |
| 2001 | 课程不存在 |
| 2002 | 课程已满 |
| 2003 | 超过预约截止时间 |
| 2004 | 时间冲突 |
| 2005 | 超过每日预约上限 |
| 3001 | 不允许取消（开课前 2 小时内） |
| 3002 | 订单不存在 |

---

### 3.2 接口清单

#### 3.2.1 认证模块 (Auth)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 会员注册 | POST | `/api/auth/member/register` | 无 | 会员自助注册 |
| 会员登录 | POST | `/api/auth/member/login` | 无 | 会员登录获取 Token |
| 管理员登录 | POST | `/api/auth/admin/login` | 无 | 管理员登录获取 Token |
| 退出登录 | POST | `/api/auth/logout` | 登录 | 退出登录 |
| 获取当前用户信息 | GET | `/api/auth/me` | 登录 | 获取当前登录用户信息 |

**对应 spec 需求**：2.1 会员注册方式、7.1 用户注册/登录

---

#### 3.2.2 会员模块 (Member)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 查看个人信息 | GET | `/api/member/info` | 会员 | 查看当前会员信息 |
| 修改个人信息 | PUT | `/api/member/info` | 会员 | 修改会员基本信息 |
| 修改密码 | PUT | `/api/member/password` | 会员 | 修改登录密码 |
| 会员列表 | GET | `/api/admin/member/list` | 管理员 | 分页查询会员列表 |
| 会员详情 | GET | `/api/admin/member/{id}` | 管理员 | 查看会员详情 |
| 编辑会员 | PUT | `/api/admin/member/{id}` | 管理员 | 编辑会员信息 |
| 删除会员 | DELETE | `/api/admin/member/{id}` | 管理员 | 删除会员账号 |
| 清除爽约记录 | POST | `/api/admin/member/{id}/clear-no-show` | 管理员 | 手动清除爽约记录 |

**对应 spec 需求**：3.1 会员管理、7.1 查看/修改个人信息、7.2 会员管理

---

#### 3.2.3 课程模块 (Course)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 课程列表 | GET | `/api/course/list` | 无 | 分页查询可预约课程 |
| 课程详情 | GET | `/api/course/{id}` | 无 | 查看课程详情 |
| 创建课程 | POST | `/api/admin/course` | 管理员 | 创建新课程 |
| 编辑课程 | PUT | `/api/admin/course/{id}` | 管理员 | 编辑课程信息 |
| 删除课程 | DELETE | `/api/admin/course/{id}` | 管理员 | 删除课程 |
| 取消课程 | POST | `/api/admin/course/{id}/cancel` | 管理员 | 取消课程并通知会员 |

**对应 spec 需求**：3.4 课程管理、7.1 查看课程列表、7.2 课程管理

---

#### 3.2.4 预约模块 (Booking)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 预约课程 | POST | `/api/booking/{courseId}` | 会员 | 预约课程 |
| 取消预约 | POST | `/api/booking/{orderId}/cancel` | 会员 | 取消预约 |
| 我的预约列表 | GET | `/api/booking/my-list` | 会员 | 查看个人预约记录 |
| 候补预约 | POST | `/api/booking/{courseId}/waitlist` | 会员 | 加入候补排队 |
| 取消候补 | POST | `/api/booking/{waitlistId}/cancel` | 会员 | 取消候补 |

**对应 spec 需求**：3.5 预约与订单、预约限制规则、取消规则

---

#### 3.2.5 订单模块 (Order)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 订单列表 | GET | `/api/admin/order/list` | 管理员 | 分页查询订单 |
| 订单详情 | GET | `/api/admin/order/{id}` | 管理员 | 查看订单详情 |
| 记录收费 | PUT | `/api/admin/order/{id}/payment` | 管理员 | 记录线下收费 |
| 会员签到 | POST | `/api/admin/order/{id}/checkin` | 管理员 | 会员上课签到 |
| 标记爽约 | POST | `/api/admin/order/{id}/no-show` | 管理员 | 标记会员爽约 |

**对应 spec 需求**：3.5 预约与订单、签到规则、收费方式

---

#### 3.2.6 教练模块 (Coach)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 教练列表 | GET | `/api/admin/coach/list` | 管理员 | 分页查询教练列表 |
| 教练详情 | GET | `/api/admin/coach/{id}` | 管理员 | 查看教练详情 |
| 创建教练 | POST | `/api/admin/coach` | 管理员 | 创建教练信息 |
| 编辑教练 | PUT | `/api/admin/coach/{id}` | 管理员 | 编辑教练信息 |
| 删除教练 | DELETE | `/api/admin/coach/{id}` | 管理员 | 删除教练 |

**对应 spec 需求**：3.2 员工管理、7.2 教练管理

---

#### 3.2.7 器材模块 (Equipment)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 器材列表 | GET | `/api/admin/equipment/list` | 无 | 查询器材列表 |
| 创建器材 | POST | `/api/admin/equipment` | 管理员 | 创建器材 |
| 编辑器材 | PUT | `/api/admin/equipment/{id}` | 管理员 | 编辑器材 |
| 删除器材 | DELETE | `/api/admin/equipment/{id}` | 管理员 | 删除器材 |

**对应 spec 需求**：3.3 器材管理、7.2 器材管理

---

#### 3.2.8 通知模块 (Notification)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 通知列表 | GET | `/api/notification/list` | 会员 | 查看个人通知列表 |
| 标记已读 | POST | `/api/notification/{id}/read` | 会员 | 标记通知已读 |
| 全部已读 | POST | `/api/notification/read-all` | 会员 | 标记所有通知已读 |
| 未读数量 | GET | `/api/notification/unread-count` | 会员 | 获取未读通知数量 |

**对应 spec 需求**：3.6 通知提醒、7.1 查看站内信通知

---

#### 3.2.9 统计模块 (Statistics)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 课程报名统计 | GET | `/api/admin/statistics/course` | 管理员 | 课程报名统计数据 |
| 会员活跃度统计 | GET | `/api/admin/statistics/member` | 管理员 | 会员活跃度统计 |
| 教练上课统计 | GET | `/api/admin/statistics/coach` | 管理员 | 教练上课统计 |
| 收入统计 | GET | `/api/admin/statistics/revenue` | 管理员 | 收入统计 |

**对应 spec 需求**：4 数据统计与报表

---

#### 3.2.10 管理员模块 (Admin)

| 接口 | 方法 | 路径 | 权限 | 描述 |
|------|------|------|------|------|
| 管理员列表 | GET | `/api/admin/admin/list` | 超级管理员 | 查询管理员列表 |
| 创建管理员 | POST | `/api/admin/admin` | 超级管理员 | 创建管理员账号 |
| 编辑管理员 | PUT | `/api/admin/admin/{id}` | 超级管理员 | 编辑管理员信息 |
| 删除管理员 | DELETE | `/api/admin/admin/{id}` | 超级管理员 | 删除管理员 |

**对应 spec 需求**：2.2 管理员分级管理、7.2 管理员账号管理

---

## 4. 核心业务逻辑设计

### 4.1 预约流程（含并发控制）

```java
/**
 * 预约课程核心逻辑
 * 
 * 1. 验证会员状态（是否被限制预约）
 * 2. 验证课程状态（是否可预约、是否已满）
 * 3. 验证预约时间（是否超过截止时间）
 * 4. 验证时间冲突（同一时间段是否已有预约）
 * 5. 验证每日预约上限
 * 6. Redis 分布式锁扣减课程名额
 * 7. 创建订单
 * 8. 发送预约成功通知
 */
@Transactional(rollbackFor = Exception.class)
public BaseResponse<Long> bookCourse(Long courseId, Long memberId) {
    // 1. 验证会员状态
    Member member = memberMapper.selectById(memberId);
    if (member.getIsBanned() && member.getBannedUntil().after(new Date())) {
        throw new BusinessException(ErrorCode.MEMBER_BANNED);
    }
    
    // 2. 验证课程状态
    Course course = courseMapper.selectById(courseId);
    if (course == null || course.getStatus() != 1) {
        throw new BusinessException(ErrorCode.COURSE_NOT_AVAILABLE);
    }
    if (course.getBookedCount() >= course.getCapacity()) {
        throw new BusinessException(ErrorCode.COURSE_FULL);
    }
    
    // 3. 验证预约时间
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime courseStartTime = course.getStartTime();
    LocalDateTime bookingDeadline = courseStartTime.minusMinutes(30);
    if (now.isAfter(bookingDeadline)) {
        throw new BusinessException(ErrorCode.BEYOND_BOOKING_DEADLINE);
    }
    
    // 4. 验证时间冲突
    boolean hasConflict = orderMapper.checkTimeConflict(memberId, courseStartTime, course.getEndTime());
    if (hasConflict) {
        throw new BusinessException(ErrorCode.TIME_CONFLICT);
    }
    
    // 5. 验证每日预约上限
    int todayCount = orderMapper.countTodayBookings(memberId, now.toLocalDate());
    if (todayCount >= 3) {
        throw new BusinessException(ErrorCode.DAILY_LIMIT_EXCEEDED);
    }
    
    // 6. Redis 分布式锁扣减名额
    String lockKey = "course:book:" + courseId;
    RLock lock = redissonClient.getLock(lockKey);
    if (!lock.tryLock(3, 10, TimeUnit.SECONDS)) {
        throw new BusinessException(ErrorCode.SYSTEM_BUSY);
    }
    try {
        // 双重检查课程名额
        course = courseMapper.selectById(courseId);
        if (course.getBookedCount() >= course.getCapacity()) {
            throw new BusinessException(ErrorCode.COURSE_FULL);
        }
        
        // 扣减名额
        courseMapper.incrementBookedCount(courseId);
        
        // 7. 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setMemberId(memberId);
        order.setCourseId(courseId);
        order.setStatus(OrderStatus.BOOKED);
        order.setAmount(course.getPrice());
        orderMapper.insert(order);
        
        // 8. 发送通知
        notificationService.sendBookingSuccess(memberId, course);
        
        return BaseResponse.success(order.getId());
    } finally {
        lock.unlock();
    }
}
```

**对应 spec 需求**：3.5 预约与订单、预约限制规则、6.2 并发预约

---

### 4.2 取消预约逻辑

```java
/**
 * 取消预约核心逻辑
 * 
 * 1. 验证订单状态
 * 2. 验证取消时间（开课前 2 小时）
 * 3. 更新订单状态
 * 4. 恢复课程名额
 * 5. 通知候补会员（如有）
 */
@Transactional(rollbackFor = Exception.class)
public BaseResponse<Void> cancelBooking(Long orderId, Long memberId) {
    Order order = orderMapper.selectById(orderId);
    if (order == null || !order.getMemberId().equals(memberId)) {
        throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
    }
    if (order.getStatus() != OrderStatus.BOOKED) {
        throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR);
    }
    
    // 验证取消时间
    Course course = courseMapper.selectById(order.getCourseId());
    LocalDateTime cancelDeadline = course.getStartTime().minusHours(2);
    if (LocalDateTime.now().isAfter(cancelDeadline)) {
        throw new BusinessException(ErrorCode.BEYOND_CANCEL_DEADLINE);
    }
    
    // 更新订单状态
    order.setStatus(OrderStatus.CANCELLED);
    orderMapper.updateById(order);
    
    // 恢复课程名额
    courseMapper.decrementBookedCount(course.getId());
    
    // 通知候补会员
    notificationService.notifyWaitlistMember(course.getId());
    
    return BaseResponse.success(null);
}
```

**对应 spec 需求**：3.5 预约与订单 - 取消规则

---

### 4.3 签到与爽约处理逻辑

```java
/**
 * 签到核心逻辑
 * 
 * 1. 验证订单状态
 * 2. 更新签到状态
 * 3. 更新订单状态为已完成
 */
@Transactional(rollbackFor = Exception.class)
public BaseResponse<Void> checkIn(Long orderId) {
    Order order = orderMapper.selectById(orderId);
    if (order == null || order.getStatus() != OrderStatus.BOOKED) {
        throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR);
    }
    
    order.setCheckIn(1);
    order.setCheckInTime(new Date());
    order.setStatus(OrderStatus.COMPLETED);
    orderMapper.updateById(order);
    
    return BaseResponse.success(null);
}

/**
 * 爽约处理核心逻辑
 * 
 * 1. 标记订单为爽约
 * 2. 记录爽约记录
 * 3. 更新会员爽约次数
 * 4. 检查是否达到限制阈值（3 次）
 * 5. 如达到阈值，限制预约 7 天
 */
@Transactional(rollbackFor = Exception.class)
public BaseResponse<Void> handleNoShow(Long orderId) {
    Order order = orderMapper.selectById(orderId);
    if (order == null || order.getStatus() != OrderStatus.BOOKED) {
        throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR);
    }
    
    // 标记订单为爽约
    order.setStatus(OrderStatus.NO_SHOW);
    orderMapper.updateById(order);
    
    // 记录爽约记录
    NoShowRecord record = new NoShowRecord();
    record.setMemberId(order.getMemberId());
    record.setOrderId(orderId);
    record.setCourseTime(order.getCourse().getStartTime());
    noShowRecordMapper.insert(record);
    
    // 更新会员爽约次数
    Member member = memberMapper.selectById(order.getMemberId());
    member.setNoShowCount(member.getNoShowCount() + 1);
    
    // 检查是否达到限制阈值
    if (member.getNoShowCount() >= 3) {
        member.setIsBanned(1);
        member.setBannedUntil(DateUtils.addDays(new Date(), 7));
    }
    
    memberMapper.updateById(member);
    
    return BaseResponse.success(null);
}
```

**对应 spec 需求**：3.5 预约与订单 - 签到规则、爽约处罚机制

---

### 4.4 定时任务设计

| 任务 | 执行时间 | 逻辑 | 对应 spec 需求 |
|------|----------|------|----------------|
| 课程开始前提醒 | 每天每 5 分钟执行 | 查询 1 小时后开始的课程，发送提醒通知 | 3.6 通知提醒 |
| 课程即将开始提醒 | 每天每 5 分钟执行 | 查询 5 分钟后开始的课程，发送提醒通知 | 3.6 通知提醒 |
| 爽约检测 | 每天课程结束后执行 | 查询已过期未签到的订单，标记爽约 | 3.5 签到规则 |
| 限制预约自动解除 | 每天凌晨执行 | 查询已过限制期限的会员，自动解除限制 | 3.5 签到规则 |

---

## 5. 前端页面设计

### 5.1 会员端页面

| 页面 | 路由 | 功能 | 对应 spec 需求 |
|------|------|------|----------------|
| 登录页 | `/login` | 会员登录 | 7.1 用户登录 |
| 注册页 | `/register` | 会员注册 | 7.1 用户注册 |
| 首页 | `/member/home` | 课程列表、快速预约 | 7.1 查看课程列表 |
| 课程详情 | `/member/course/:id` | 课程详情、预约 | 7.1 预约课程 |
| 我的预约 | `/member/bookings` | 预约记录、取消预约 | 7.1 查看/取消预约 |
| 个人中心 | `/member/profile` | 查看/修改个人信息 | 7.1 查看/修改个人信息 |
| 通知中心 | `/member/notifications` | 查看通知、标记已读 | 7.1 查看站内信通知 |

---

### 5.2 管理员端页面

| 页面 | 路由 | 功能 | 对应 spec 需求 |
|------|------|------|----------------|
| 登录页 | `/admin/login` | 管理员登录 | 7.2 管理员登录 |
| 控制台 | `/admin/dashboard` | 数据统计概览 | 7.2 数据统计报表 |
| 会员管理 | `/admin/members` | 会员列表、增删改查 | 7.2 会员管理 |
| 教练管理 | `/admin/coaches` | 教练列表、增删改查 | 7.2 教练管理 |
| 器材管理 | `/admin/equipment` | 器材列表、增删改查 | 7.2 器材管理 |
| 课程管理 | `/admin/courses` | 课程列表、增删改查 | 7.2 课程管理 |
| 订单管理 | `/admin/orders` | 订单列表、收费、签到 | 7.2 订单管理 |
| 管理员管理 | `/admin/admins` | 管理员列表、增删改查（仅超级管理员） | 7.2 管理员账号管理 |

---

### 5.3 关键页面原型

#### 5.3.1 课程列表页（会员端）

```
┌─────────────────────────────────────────────────────────┐
│  健身房管理系统                    [通知] [个人中心]     │
├─────────────────────────────────────────────────────────┤
│  课程筛选                                                │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐      │
│  │ 课程类型 ▼│ │ 日期选择 ▼│ │ 教练选择 ▼│ │  搜索   │      │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘      │
├─────────────────────────────────────────────────────────┤
│  课程列表                                                │
│  ┌─────────────────────────────────────────────────┐   │
│  │ 瑜伽课                                          │   │
│  │ 教练：张三          时间：2026-05-21 10:00     │   │
│  │ 容量：20/15           价格：¥50                │   │
│  │ [立即预约]                                      │   │
│  └─────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────┐   │
│  │ 动感单车                                        │   │
│  │ 教练：李四          时间：2026-05-21 14:00     │   │
│  │ 容量：15/15（已满）   价格：¥60                │   │
│  │ [加入候补]                                      │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## 6. 开发计划

### 6.1 里程碑规划

| 阶段 | 周期 | 目标 | 交付物 |
|------|------|------|--------|
| **阶段 1** | 第 1-2 周 | 基础架构搭建、数据库设计、认证模块 | 可运行的基础框架、登录注册功能 |
| **阶段 2** | 第 3-4 周 | 核心业务模块（会员/教练/器材/课程） | CRUD 功能完成 |
| **阶段 3** | 第 5-6 周 | 预约与订单模块、通知模块 | 预约流程完整、通知功能 |
| **阶段 4** | 第 7 周 | 统计报表模块、爽约处罚机制 | 统计功能、爽约处理 |
| **阶段 5** | 第 8 周 | 前端页面开发、联调测试 | 完整的前后端功能 |
| **阶段 6** | 第 9 周 | 测试优化、Bug 修复、部署上线 | 可上线的生产系统 |

---

### 6.2 任务拆分（第一阶段示例）

#### 阶段 1：基础架构搭建（第 1-2 周）

**Week 1:**
- [ ] 初始化 Spring Boot 项目
- [ ] 配置 MyBatis-Plus、Redis、Swagger
- [ ] 设计并创建数据库表
- [ ] 实现 JWT 工具类
- [ ] 实现 Spring Security 配置

**Week 2:**
- [ ] 实现会员注册接口
- [ ] 实现会员登录接口
- [ ] 实现管理员登录接口
- [ ] 实现退出登录接口
- [ ] 实现获取当前用户信息接口
- [ ] 前端初始化 Vue 3 项目
- [ ] 实现登录/注册页面

---

## 7. 测试策略

### 7.1 单元测试

| 模块 | 测试重点 | 覆盖率要求 |
|------|----------|------------|
| Service 层 | 业务逻辑、边界条件 | ≥80% |
| 工具类 | 各种输入场景 | ≥90% |
| 实体类 | Getter/Setter | 可选 |

---

### 7.2 接口测试

| 接口类型 | 测试工具 | 测试内容 |
|----------|----------|----------|
| REST API | Postman/Knife4j | 功能验证、参数校验、错误处理 |
| 并发场景 | JMeter | 并发预约、超卖检测 |

---

### 7.3 核心场景测试用例

| 场景 | 测试步骤 | 预期结果 | 对应 spec 需求 |
|------|----------|----------|----------------|
| 正常预约 | 会员选择可预约课程→提交预约 | 预约成功、生成订单、发送通知 | 3.5 预约流程 |
| 时间冲突 | 会员预约同一时间段已有课程 | 提示时间冲突、预约失败 | 3.5 时间冲突检测 |
| 超过每日上限 | 会员当天第 4 次预约 | 提示超过每日上限、预约失败 | 3.5 预约数量限制 |
| 超过预约截止 | 开课前 20 分钟预约 | 提示超过预约截止时间、预约失败 | 3.5 临时预约 |
| 课程已满 | 会员预约已满课程 | 提示课程已满、可加入候补 | 5 边缘场景 |
| 取消预约（合规） | 开课前 3 小时取消 | 取消成功、恢复名额 | 3.5 取消规则 |
| 取消预约（超时） | 开课前 1 小时取消 | 提示不允许取消、取消失败 | 3.5 取消规则 |
| 爽约处理 | 课程结束未签到 | 记录爽约、更新次数 | 3.5 签到规则 |
| 累计爽约 3 次 | 会员第 3 次爽约 | 限制预约 7 天 | 3.5 爽约处罚 |

---

## 8. 部署方案

### 8.1 环境规划

| 环境 | 用途 | 服务器配置 |
|------|------|------------|
| 开发环境 | 开发测试 | 本地 Docker |
| 测试环境 | 集成测试 | 2 核 4G × 1 |
| 生产环境 | 正式运行 | 4 核 8G × 2（负载均衡） |

---

### 8.2 技术组件部署

| 组件 | 部署方式 | 备注 |
|------|----------|------|
| Spring Boot | Docker 容器化 | 使用多阶段构建优化镜像大小 |
| Vue 前端 | Nginx 静态托管 | 开启 Gzip 压缩 |
| MySQL | Docker/云服务 | 主从复制、定时备份 |
| Redis | Docker/云服务 | 持久化配置 |

---

## 9. 风险与应对

| 风险 | 影响 | 应对措施 |
|------|------|----------|
| 并发预约超卖 | 高 | Redis 分布式锁 + 数据库乐观锁 |
| 短信服务失败 | 中 | 失败重试 + 降级为站内信 |
| 数据库性能瓶颈 | 中 | 索引优化 + 读写分离（后续扩展） |
| 预约规则复杂 | 中 | 充分单元测试 + 边界场景验证 |

---

## 10. 附录

### 10.1 术语表

| 术语 | 定义 |
|------|------|
| 团课 | 一个教练对多个会员的课程形式 |
| 私教课 | 一个教练对一个会员的课程形式 |
| 爽约 | 会员预约课程后未签到 |
| 候补 | 课程已满时排队等待名额 |

---

### 10.2 Spec-Plan 追踪矩阵

| Spec 章节 | Plan 章节 | 实现方式 |
|-----------|-----------|----------|
| 2.1 会员 | 2.2.1 会员表、3.2.2 会员接口 | 数据库 + API |
| 2.2 管理员 | 2.2.2 管理员表、3.2.10 管理员接口 | 数据库 + API |
| 3.1 会员管理 | 3.2.2 会员接口 | API |
| 3.2 员工管理 | 2.2.3 教练表、3.2.6 教练接口 | 数据库 + API |
| 3.3 器材管理 | 2.2.5 器材表、3.2.7 器材接口 | 数据库 + API |
| 3.4 课程管理 | 2.2.4 课程表、3.2.3 课程接口 | 数据库 + API |
| 3.5 预约与订单 | 2.2.6 订单表、3.2.4 预约接口、4.1-4.3 业务逻辑 | 数据库 + API+ 业务逻辑 |
| 3.6 通知提醒 | 2.2.7 通知表、3.2.8 通知接口、4.4 定时任务 | 数据库 + API+ 定时任务 |
| 4 数据统计 | 3.2.9 统计接口 | API |
| 5 边缘场景 | 4.1-4.3 业务逻辑、7.3 测试用例 | 业务逻辑 + 测试 |
| 6 非功能需求 | 4.1 并发控制、8 部署方案 | 技术架构 |

---

**文档版本：** v1.0  
**创建日期：** 2026-05-20  
**创建者：** 首席架构师  
**审核状态：** 待审核
