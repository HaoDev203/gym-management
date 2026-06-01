# 健身房管理系统

> 一套基于 Spring Boot + Vue 3 的健身房管理系统，支持会员在线预约课程、管理员后台管理、数据统计等功能。

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.3-42b883.svg)](https://vuejs.org/)

---

## 目录

- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [测试账户](#测试账户)
- [角色权限](#角色权限)
- [常见问题](#常见问题)

---

## 功能特性

### 管理端

| 模块 | 功能说明 | 超级管理员 | 普通管理员 |
|------|---------|:----------:|:----------:|
| 仪表盘 | 会员/课程/预约/收入数据概览、最近订单、热门课程 | 完整 | 完整 |
| 会员管理 | 会员列表查看、禁用/解禁 | 完整 | 只读 |
| 教练管理 | 教练增删改查 | 完整 | 只读 |
| 课程管理 | 团课/私教课增删改查、容量与价格管理 | 完整 | 完整 |
| 器材管理 | 器材信息增删改查 | 完整 | 完整 |
| 订单管理 | 订单列表、签到、确认支付、取消、爽约标记 | 完整 | 只读 |
| 数据统计 | 课程排行、会员预约频率、每日趋势、分布统计 | 完整 | 不可见 |
| 管理员管理 | 添加/编辑/删除普通管理员 | 完整 | 不可见 |

### 会员端

- **课程浏览**：按类型筛选课程，查看课程详情与教练信息
- **在线预约**：预约课程、候补排队（满员时自动入队）
- **我的预约**：查看预约记录，支持签到、取消操作
- **我的订单**：订单列表与状态查看
- **个人中心**：查看和修改个人信息
- **消息通知**：课程提醒、预约状态变更通知
- **会员注册**：手机号注册新账号

---

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 核心框架 |
| Spring Security | - | 安全认证与授权 |
| MyBatis-Plus | 3.5.6 | ORM 框架 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 7.0+ | 缓存与 Token 存储（可选） |
| JJWT | 0.12.5 | JWT Token 生成与验证 |
| Knife4j | 4.5.0 | API 文档（Swagger） |
| Lombok | - | 简化 Java 代码 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | 3.3.4 | 核心框架（Composition API） |
| Vite | 4.4.9 | 构建工具与开发服务器 |
| Element Plus | 2.3.8 | UI 组件库 |
| Pinia | 2.1.6 | 状态管理 |
| Vue Router | 4.2.4 | 前端路由 |
| Axios | 1.4.0 | HTTP 请求库 |

---

## 项目结构

```
gym-management-system/
├── gym-backend/                        # 后端（Spring Boot）
│   └── src/main/java/com/gym/
│       ├── common/                     # 通用类（BaseResponse、BusinessException、ErrorCode）
│       ├── config/                     # 配置类（CORS、Security、Redis、Swagger）
│       ├── controller/                 # 控制器（11 个接口模块）
│       ├── service/                    # 服务层接口与实现
│       ├── mapper/                     # MyBatis-Plus 数据访问层
│       ├── entity/                     # 实体类
│       ├── dto/                        # 请求/响应 DTO
│       ├── enums/                      # 枚举（AdminRole、OrderStatus 等）
│       ├── filter/                     # JWT 认证过滤器
│       ├── task/                       # 定时任务（课程提醒、爽约检测、封禁解除）
│       └── util/                       # 工具类（JWT、日期、权限校验）
│
├── gym-frontend/                       # 前端（Vue 3 + Vite）
│   └── src/
│       ├── api/                        # API 接口定义（按模块划分）
│       ├── components/                 # 公共组件（Header、Sidebar、CourseCard）
│       ├── views/
│       │   ├── admin/                  # 管理端页面（10 个）
│       │   └── member/                 # 会员端页面（9 个）
│       ├── stores/                     # Pinia 状态管理
│       ├── router/                     # 路由配置与守卫
│       ├── utils/                      # 工具函数（auth、request、jwt、date）
│       └── styles/                     # 全局样式与主题变量
│
└── specs/                              # 项目文档（设计文档、任务清单）
```

---

## 环境要求

| 环境 | 最低版本 | 说明 | 必需 |
|------|---------|------|:----:|
| JDK | 17 | Java 运行环境 | 是 |
| Maven | 3.6 | Java 构建工具 | 是 |
| MySQL | 8.0 | 数据库 | 是 |
| Node.js | 18 | JavaScript 运行环境 | 是 |
| Redis | 7.0 | 缓存服务 | 否 |

---

## 快速开始

### 1. 初始化数据库

创建数据库并导入数据：

```sql
CREATE DATABASE gym_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**方式一：一键导入脚本（推荐）**

```bash
cd gym-backend/src/main/resources/sql

# Windows PowerShell
.\import.ps1

# Windows CMD
import.bat
```

> 如果 MySQL 密码不是 `123`，请先编辑脚本修改数据库连接配置。

**方式二：手动导入**

依次执行 `schema.sql`（建表）和 `data.sql`（初始数据）。

### 2. 启动后端

```bash
cd gym-backend
mvn spring-boot:run
```

启动成功后：
- API 文档：http://localhost:8080/doc.html
- 后端接口：http://localhost:8080/api

### 3. 启动前端

```bash
cd gym-frontend
npm install
npm run dev
```

浏览器访问 http://localhost:3000

---

## 测试账户

### 管理员

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 超级管理员 |
| operator | admin123 | 普通管理员 |

### 会员

| 手机号 | 密码 | 姓名 |
|--------|------|------|
| 13800138001 | 123456 | 张三 |
| 13800138002 | 123456 | 李四 |
| 13800138003 | 123456 | 王五 |

> 其他会员账号均为手机号格式，密码统一为 `123456`。

---

## 角色权限

系统定义了两级管理员角色：

### 超级管理员（role = 2）

- 可访问所有管理端功能
- 可管理其他管理员（添加、编辑、删除）
- 可操作所有模块的数据（增删改查）
- 可查看数据统计

### 普通管理员（role = 1）

- **可增删改查**：课程管理、器材管理
- **只读查看**：会员管理、教练管理、订单管理
- **不可见**：数据统计、管理员管理

> 超级管理员账号不可被编辑或删除，仅允许超级管理员登录后操作。

---

## 常见问题

### 数据库连接失败

1. 确认 MySQL 服务已启动
2. 检查 `gym-backend/src/main/resources/application-dev.yml` 中的数据库连接配置
3. 默认用户名 `root`，密码 `123`

### 前端依赖安装失败

```bash
# 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 清除缓存重试
rm -rf node_modules package-lock.json
npm install
```

### 端口冲突

- 后端端口（8080）：修改 `application-dev.yml` 中 `server.port`
- 前端端口（3000）：修改 `vite.config.js` 中 `server.port`

### Redis 不可用

Redis 为可选依赖。未启动 Redis 时系统正常运行，仅 Token 黑名单等缓存功能降级。

---

## 许可证

本项目采用 [MIT 许可证](LICENSE) 开源。
