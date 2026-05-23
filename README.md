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

## 快速开始

### 后端

1. 创建数据库并执行初始化脚本
```bash
mysql -u root -p < src/main/resources/sql/init.sql
```

2. 修改配置文件 `src/main/resources/application-dev.yml` 中的数据库连接信息

3. 启动应用
```bash
mvn spring-boot:run
```

4. 访问 API 文档：http://localhost:8080/api/doc.html

### 前端

1. 安装依赖
```bash
npm install
```

2. 启动开发服务器
```bash
npm run dev
```

3. 访问：http://localhost:3000

## 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+

## 开发规范

请参考 `AGENTS.md` 中的代码注释规范。

## 许可证

MIT
