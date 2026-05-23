# 健身房管理系统

## 项目简介

一套基于 Web 的企业级健身房管理系统，支持会员在线预约课程、管理员管理会员、教练、器材等功能。

## 技术栈

### 后端
- Spring Boot 3.2+
- MyBatis-Plus 3.5+
- MySQL 8.0+
- Redis 7.0+
- Spring Security + JWT
- Knife4j (Swagger) 4.0+

### 前端
- Vue 3 3.4+
- Vite 5.0+
- Element Plus 2.4+
- Pinia 2.1+
- Vue Router 4.2+

## 项目结构

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
│   │   │       └── sql/             # SQL 脚本
│   │   └── test/                    # 测试代码
│   └── pom.xml
└── gym-frontend/             # 前端项目
    ├── src/
    │   ├── api/               # API 服务
    │   ├── components/        # 组件
    │   ├── views/             # 页面
    │   ├── stores/            # 状态管理
    │   ├── router/            # 路由
    │   └── utils/             # 工具类
    ├── package.json
    └── vite.config.js
```

## 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+（可选，无 Redis 时部分缓存功能降级）

## 快速开始

### 1. 初始化数据库

**方式一：一键脚本（推荐）**

```bash
# Windows PowerShell
cd gym-backend\src\main\resources\sql
.\import.ps1

# 或 CMD
import.bat
```

脚本会自动：建库 → 建表 → 导入测试数据。

如果你的 MySQL 密码不是 123，用记事本打开脚本修改顶部的 `$mysqlPass` 变量即可。

**方式二：手动导入**

用 Navicat 等工具依次执行 `schema.sql` 和 `data.sql`。

> 数据库名固定为 `gym_management`，如需修改请同步改 `application-dev.yml`。

### 2. 启动后端

```bash
cd gym-backend
mvn spring-boot:run
```

启动后访问 Swagger 文档：http://localhost:8080/api/doc.html

### 3. 启动前端

```bash
cd gym-frontend
npm install
npm run dev
```

浏览器打开：http://localhost:3000

### 测试账户

| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | admin123 |
| 会员 | 13800138001 | 123456 |

## 开发规范

请参考 `AGENTS.md` 中的代码注释规范。

## 许可证

MIT
