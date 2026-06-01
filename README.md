# 健身房管理系统

> 一套基于 Web 的企业级健身房管理系统，支持会员在线预约课程、管理员管理会员、教练、器材等功能。

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.4+-brightgreen.svg)](https://vuejs.org/)

---

## 📋 目录

- [功能特性](#-功能特性)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [环境要求](#-环境要求)
- [快速开始](#-快速开始)
- [测试账户](#-测试账户)
- [开发规范](#-开发规范)
- [常见问题](#-常见问题)
- [许可证](#-许可证)

---

## ✨ 功能特性

### 核心功能

- 🏋️ **课程管理**：团课、私教课预约，容量控制
- 👤 **会员管理**：会员信息、预约记录、爽约管理
- 👨‍🏫 **教练管理**：教练信息、课程安排
- 📅 **预约系统**：在线预约、签到、取消
- 🔔 **消息通知**：课程提醒、预约状态变更
- 📊 **数据统计**：课程统计、会员统计

### 用户角色

| 角色 | 权限 |
|------|------|
| **超级管理员** | 系统全部权限 |
| **普通管理员** | 会员、教练、课程管理 |
| **教练** | 查看课程安排 |
| **会员** | 预约课程、查看记录 |

---

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| **Spring Boot** | 3.2+ | 核心框架 |
| **MyBatis-Plus** | 3.5+ | ORM 框架 |
| **MySQL** | 8.0+ | 数据库 |
| **Redis** | 7.0+ | 缓存（可选） |
| **Spring Security** | - | 安全框架 |
| **JWT** | - | Token 认证 |
| **Knife4j** | 4.0+ | API 文档 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| **Vue 3** | 3.4+ | 核心框架 |
| **Vite** | 5.0+ | 构建工具 |
| **Element Plus** | 2.4+ | UI 组件库 |
| **Pinia** | 2.1+ | 状态管理 |
| **Vue Router** | 4.2+ | 路由管理 |

---

## 📁 项目结构

```
gym-management-system/
├── gym-backend/              # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gym/
│   │   │   │   ├── common/          # 通用类（响应、异常、错误码）
│   │   │   │   ├── config/          # 配置类
│   │   │   │   ├── controller/      # 控制器
│   │   │   │   ├── service/         # 服务层
│   │   │   │   ├── mapper/          # 数据访问层
│   │   │   │   ├── entity/          # 实体类
│   │   │   │   ├── dto/             # 数据传输对象
│   │   │   │   ├── enums/           # 枚举类
│   │   │   │   └── util/            # 工具类
│   │   │   └── resources/
│   │   │       ├── mapper/          # MyBatis XML
│   │   │       └── sql/             # SQL 脚本（含数据库初始化脚本）
│   │   └── test/                    # 测试代码
│   └── pom.xml                      # Maven 配置
└── gym-frontend/             # 前端项目
    ├── src/
    │   ├── api/               # API 服务
    │   ├── components/        # 组件
    │   ├── views/             # 页面
    │   ├── stores/            # 状态管理
    │   ├── router/            # 路由
    │   └── utils/             # 工具类
    ├── package.json           # 依赖配置
    └── vite.config.js         # Vite 配置
```

---

## 💻 环境要求

### 必需环境

| 环境 | 版本 | 说明 | 是否必需 |
|------|------|------|----------|
| **JDK** | 17+ | Java 运行环境 | ✅ 必需 |
| **Node.js** | 18+ | JavaScript 运行环境 | ✅ 必需 |
| **MySQL** | 8.0+ | 数据库 | ✅ 必需 |
| **Maven** | 3.6+ | Java 构建工具 | ✅ 必需 |
| **Git** | 最新 | 版本控制 | ✅ 必需 |

### 可选环境

| 环境 | 版本 | 说明 |
|------|------|------|
| **Redis** | 7.0+ | 缓存服务（无 Redis 时部分缓存功能降级） |

---

## 🚀 快速开始

### 步骤 1：克隆项目

```bash
git clone <项目地址>
cd gym-management-system
```

### 步骤 2：初始化数据库

> ⚠️ **重要提示**：使用前请确保已修改导入脚本中的 MySQL 用户名和密码！

**方式一：使用一键导入脚本（推荐）**

```bash
# 进入 SQL 目录
cd gym-backend\src\main\resources\sql

# Windows PowerShell（推荐）
.\import.ps1

# 或 Windows CMD
import.bat
```

> 📝 **修改配置**：如果 MySQL 密码不是 `123`，用记事本打开脚本，修改顶部的数据库配置即可。

**方式二：手动导入**

1. 创建数据库：
```sql
CREATE DATABASE gym_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 依次执行 SQL 文件：
   - `schema.sql` - 创建表结构
   - `data.sql` - 插入测试数据

> 📖 **详细说明**：请查看 [数据库配置文档](gym-backend/src/main/resources/sql/README.md)

### 步骤 3：配置后端

1. 修改数据库配置（如需要）：
   ```yaml
   # gym-backend/src/main/resources/application-dev.yml
   spring:
     datasource:
       url: jdbc:mysql://127.0.0.1:3306/gym_management?...
       username: your_username
       password: your_password
   ```

2. 启动后端：
```bash
cd gym-backend
mvn spring-boot:run
```

3. 验证启动：
   - 访问 Swagger 文档：http://localhost:8080/api/doc.html
   - 查看控制台日志，确保无错误

### 步骤 4：配置前端

1. 安装依赖：
```bash
cd gym-frontend
npm install
```

2. 启动前端：
```bash
npm run dev
```

3. 访问系统：
   - 浏览器打开：http://localhost:3000

---

## 👤 测试账户

系统预置了以下测试账户：

### 管理员账户

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | admin123 | 超级管理员 | 全部权限 |
| operator | admin123 | 普通管理员 | 会员、教练、课程管理 |

### 会员账户

| 用户名（手机号） | 密码 | 姓名 | 说明 |
|----------------|------|------|------|
| 13800138001 | 123456 | 张三 | 正常会员 |
| 13800138002 | 123456 | 李四 | 正常会员 |
| 13800138003 | 123456 | 王五 | 正常会员 |

> 💡 **提示**：其他会员账号均为手机号格式，密码均为 `123456`

---

## 📏 开发规范

请查看 [`AGENTS.md`](AGENTS.md) 文件了解：

- 📝 代码注释规范
- 🔧 代码风格要求
- ✅ 最佳实践

---

## ❓ 常见问题

### Q1: 数据库连接失败
**现象**：启动时报数据库连接错误

**解决方案**：
1. ✅ 检查 MySQL 服务是否启动
2. ✅ 检查 `application-dev.yml` 中的数据库配置
3. ✅ 确认用户名密码正确

### Q2: 前端依赖安装失败
**现象**：`npm install` 报错

**解决方案**：
1. ✅ 使用淘宝镜像：`npm config set registry https://registry.npmmirror.com`
2. ✅ 删除 `node_modules` 和 `package-lock.json` 后重试
3. ✅ 检查 Node.js 版本是否为 18+

### Q3: 端口被占用
**现象**：启动时提示端口已占用

**解决方案**：
- 后端：修改 `application-dev.yml` 中的 `server.port`
- 前端：修改 `vite.config.js` 中的 `server.port`

### Q4: Redis 连接失败
**现象**：启动时报 Redis 连接错误

**解决方案**：
- 方案 1：启动 Redis 服务
- 方案 2：在配置文件中禁用 Redis（部分缓存功能会降级）

---

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE) 开源。

---

##  致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis-Plus](https://baomidou.com/)

---

**Made with ❤️ by Gym Team**
