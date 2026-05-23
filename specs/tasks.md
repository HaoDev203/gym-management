# 健身房管理系统 - 原子化任务列表

> **文档目标**：将 `plan.md` 技术方案分解为原子化的、有依赖关系的、可被 AI 直接执行的任务列表  
> **执行原则**：
> - **TDD 铁律**：测试先行，先生成测试任务，后生成实现任务
> - **原子化**：每个任务只涉及一个主要文件的修改或创建一个新文件
> - **并行标记**：标记 `[P]` 表示无依赖关系，可并行执行
> - **依赖追踪**：每个任务标注前置依赖任务

---

## Phase 1: Foundation & Skeleton

> **目标**：解决方案骨架、项目结构、基础配置、依赖注入、日志、环境配置、前端基础工程初始化  
> **不实现具体业务功能**

### 1.1 后端项目骨架

- [ ] **T1.1.1** 创建后端项目目录结构
  - 路径：`gym-backend/`
  - 内容：创建标准 Maven 项目目录结构
  - 依赖：无
  - 并行：[P]

- [ ] **T1.1.2** 创建 Maven pom.xml 配置文件
  - 路径：`gym-backend/pom.xml`
  - 内容：定义 Spring Boot 3.2、MyBatis-Plus、MySQL、Redis、JWT、Swagger 等依赖
  - 依赖：无
  - 并行：[P]

- [ ] **T1.1.3** 创建 Spring Boot 主应用类
  - 路径：`gym-backend/src/main/java/com/gym/GymApplication.java`
  - 内容：`@SpringBootApplication` 注解的主类
  - 依赖：T1.1.2
  - 并行：无

- [ ] **T1.1.4** 创建 application.yml 主配置文件
  - 路径：`gym-backend/src/main/resources/application.yml`
  - 内容：基础配置（应用名、端口、日志级别）
  - 依赖：T1.1.3
  - 并行：无

- [ ] **T1.1.5** 创建 application-dev.yml 开发环境配置
  - 路径：`gym-backend/src/main/resources/application-dev.yml`
  - 内容：开发环境数据库、Redis 连接配置
  - 依赖：T1.1.4
  - 并行：无

- [ ] **T1.1.6** 创建通用响应类 BaseResponse
  - 路径：`gym-backend/src/main/java/com/gym/common/BaseResponse.java`
  - 内容：统一 API 响应结构（code, message, data）
  - 依赖：T1.1.3
  - 并行：[P]

- [ ] **T1.1.7** 创建业务异常类 BusinessException
  - 路径：`gym-backend/src/main/java/com/gym/common/BusinessException.java`
  - 内容：自定义业务异常类
  - 依赖：T1.1.6
  - 并行：无

- [ ] **T1.1.8** 创建错误码枚举 ErrorCode
  - 路径：`gym-backend/src/main/java/com/gym/common/ErrorCode.java`
  - 内容：定义所有业务错误码（200, 400, 401, 1001-3002 等）
  - 依赖：T1.1.7
  - 并行：无

- [ ] **T1.1.9** 创建全局异常处理器
  - 路径：`gym-backend/src/main/java/com/gym/config/GlobalExceptionHandler.java`
  - 内容：`@RestControllerAdvice` 统一异常处理
  - 依赖：T1.1.7, T1.1.8
  - 并行：无

- [ ] **T1.1.10** 创建常量类 Constants
  - 路径：`gym-backend/src/main/java/com/gym/common/Constants.java`
  - 内容：定义系统常量（JWT 密钥、过期时间、爽约阈值等）
  - 依赖：T1.1.3
  - 并行：[P]

---

### 1.2 配置类

- [ ] **T1.2.1** 创建 MyBatis-Plus 配置类
  - 路径：`gym-backend/src/main/java/com/gym/config/MyBatisPlusConfig.java`
  - 内容：配置分页插件、驼峰映射
  - 依赖：T1.1.2
  - 并行：无

- [ ] **T1.2.2** 创建 Redis 配置类
  - 路径：`gym-backend/src/main/java/com/gym/config/RedisConfig.java`
  - 内容：配置 RedisTemplate、序列化方式
  - 依赖：T1.1.2
  - 并行：[P]

- [ ] **T1.2.3** 创建 Swagger/Knife4j 配置类
  - 路径：`gym-backend/src/main/java/com/gym/config/SwaggerConfig.java`
  - 内容：配置 API 文档、分组、认证
  - 依赖：T1.1.2
  - 并行：[P]

- [ ] **T1.2.4** 创建 Spring Security 配置类（基础版）
  - 路径：`gym-backend/src/main/java/com/gym/config/SecurityConfig.java`
  - 内容：配置安全过滤器链、放行接口（初始不启用认证）
  - 依赖：T1.1.2
  - 并行：[P]

---

### 1.3 枚举类

- [ ] **T1.3.1** 创建订单状态枚举 OrderStatus
  - 路径：`gym-backend/src/main/java/com/gym/enums/OrderStatus.java`
  - 内容：定义 1-已预约、2-已取消、3-已完成、4-已爽约
  - 依赖：T1.1.3
  - 并行：[P]

- [ ] **T1.3.2** 创建课程类型枚举 CourseType
  - 路径：`gym-backend/src/main/java/com/gym/enums/CourseType.java`
  - 内容：定义 1-团课、2-私教课
  - 依赖：T1.1.3
  - 并行：[P]

- [ ] **T1.3.3** 创建管理员角色枚举 AdminRole
  - 路径：`gym-backend/src/main/java/com/gym/enums/AdminRole.java`
  - 内容：定义 1-普通管理员、2-超级管理员
  - 依赖：T1.1.3
  - 并行：[P]

- [ ] **T1.3.4** 创建通知类型枚举 NotificationType
  - 路径：`gym-backend/src/main/java/com/gym/enums/NotificationType.java`
  - 内容：定义 1-预约成功、2-取消通知、3-课前提醒、4-即将开始、5-候补成功、6-课程取消
  - 依赖：T1.1.3
  - 并行：[P]

---

### 1.4 前端项目骨架

- [ ] **T1.4.1** 创建前端项目目录结构
  - 路径：`gym-frontend/`
  - 内容：创建 Vite + Vue 3 项目结构
  - 依赖：无
  - 并行：[P]

- [ ] **T1.4.2** 创建 package.json 配置文件
  - 路径：`gym-frontend/package.json`
  - 内容：定义 Vue 3、Element Plus、Pinia、Vue Router、Axios 等依赖
  - 依赖：T1.4.1
  - 并行：无

- [ ] **T1.4.3** 创建 vite.config.js 配置文件
  - 路径：`gym-frontend/vite.config.js`
  - 内容：配置 Vite 构建、代理、别名
  - 依赖：T1.4.2
  - 并行：无

- [ ] **T1.4.4** 创建 index.html 入口文件
  - 路径：`gym-frontend/index.html`
  - 内容：HTML 入口、CDN 资源
  - 依赖：T1.4.2
  - 并行：无

- [ ] **T1.4.5** 创建 main.js 应用入口
  - 路径：`gym-frontend/src/main.js`
  - 内容：初始化 Vue 应用、注册全局组件、引入 Element Plus
  - 依赖：T1.4.3
  - 并行：无

- [ ] **T1.4.6** 创建 App.vue 根组件
  - 路径：`gym-frontend/src/App.vue`
  - 内容：根组件模板、样式
  - 依赖：T1.4.5
  - 并行：无

- [ ] **T1.4.7** 创建 API 请求工具 request.js
  - 路径：`gym-frontend/src/utils/request.js`
  - 内容：封装 Axios、拦截器、错误处理
  - 依赖：T1.4.5
  - 并行：无

- [ ] **T1.4.8** 创建路由配置文件
  - 路径：`gym-frontend/src/router/index.js`
  - 内容：定义基础路由结构（会员端、管理员端）
  - 依赖：T1.4.5
  - 并行：无

- [ ] **T1.4.9** 创建 Pinia 状态管理 store
  - 路径：`gym-frontend/src/stores/user.js`
  - 内容：用户状态管理（登录信息、权限）
  - 依赖：T1.4.5
  - 并行：无

- [ ] **T1.4.10** 创建认证工具 auth.js
  - 路径：`gym-frontend/src/utils/auth.js`
  - 内容：Token 存储、读取、验证
  - 依赖：T1.4.7
  - 并行：无

---

### 1.5 基础工具类

- [ ] **T1.5.1** 创建 JWT 工具类 JwtUtil
  - 路径：`gym-backend/src/main/java/com/gym/util/JwtUtil.java`
  - 内容：JWT 生成、解析、验证
  - 依赖：T1.1.10
  - 并行：无

- [ ] **T1.5.2** 创建日期工具类 DateUtil
  - 路径：`gym-backend/src/main/java/com/gym/util/DateUtil.java`
  - 内容：日期格式化、计算、比较
  - 依赖：T1.1.10
  - 并行：[P]

---

**Phase 1 完成标志**：
- ✅ 后端项目可启动
- ✅ 前端项目可运行
- ✅ Swagger 可访问
- ✅ 基础配置完成

---

## Phase 2: Domain Model & Domain Tests (TDD)

> **目标**：领域实体、值对象、聚合、领域服务、仓储抽象、领域规则测试  
> **必须先生成测试任务，再生成实现任务**

### 2.1 会员领域 (Member)

- [ ] **T2.1.1** 【测试】创建 Member 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/MemberTest.java`
  - 内容：测试会员基本信息验证、爽约计数逻辑、限制预约逻辑
  - 依赖：Phase 1 完成
  - 并行：无

- [ ] **T2.1.2** 【实现】创建会员实体类 Member
  - 路径：`gym-backend/src/main/java/com/gym/entity/Member.java`
  - 内容：定义会员字段、getter/setter、业务方法（增加爽约次数、检查是否被限制等）
  - 依赖：T2.1.1
  - 并行：无

- [ ] **T2.1.3** 【测试】创建会员仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/MemberMapperTest.java`
  - 内容：测试会员 CRUD 操作
  - 依赖：T2.1.2
  - 并行：无

- [ ] **T2.1.4** 【实现】创建会员仓储接口 MemberMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/MemberMapper.java`
  - 内容：定义 CRUD 方法、自定义查询方法
  - 依赖：T2.1.3
  - 并行：无

---

### 2.2 管理员领域 (Admin)

- [ ] **T2.2.1** 【测试】创建 Admin 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/AdminTest.java`
  - 内容：测试管理员角色验证、权限检查
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.2.2** 【实现】创建管理员实体类 Admin
  - 路径：`gym-backend/src/main/java/com/gym/entity/Admin.java`
  - 内容：定义管理员字段、getter/setter
  - 依赖：T2.2.1
  - 并行：无

- [ ] **T2.2.3** 【测试】创建管理员仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/AdminMapperTest.java`
  - 内容：测试管理员 CRUD 操作
  - 依赖：T2.2.2
  - 并行：无

- [ ] **T2.2.4** 【实现】创建管理员仓储接口 AdminMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/AdminMapper.java`
  - 内容：定义 CRUD 方法
  - 依赖：T2.2.3
  - 并行：无

---

### 2.3 教练领域 (Coach)

- [ ] **T2.3.1** 【测试】创建 Coach 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/CoachTest.java`
  - 内容：测试教练信息验证、擅长课程解析
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.3.2** 【实现】创建教练实体类 Coach
  - 路径：`gym-backend/src/main/java/com/gym/entity/Coach.java`
  - 内容：定义教练字段、getter/setter
  - 依赖：T2.3.1
  - 并行：无

- [ ] **T2.3.3** 【测试】创建教练仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/CoachMapperTest.java`
  - 内容：测试教练 CRUD 操作
  - 依赖：T2.3.2
  - 并行：无

- [ ] **T2.3.4** 【实现】创建教练仓储接口 CoachMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/CoachMapper.java`
  - 内容：定义 CRUD 方法
  - 依赖：T2.3.3
  - 并行：无

---

### 2.4 课程领域 (Course)

- [ ] **T2.4.1** 【测试】创建 Course 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/CourseTest.java`
  - 内容：测试课程容量验证、状态流转、预约截止时间计算
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.4.2** 【实现】创建课程实体类 Course
  - 路径：`gym-backend/src/main/java/com/gym/entity/Course.java`
  - 内容：定义课程字段、getter/setter、业务方法（是否可预约、是否已满等）
  - 依赖：T2.4.1
  - 并行：无

- [ ] **T2.4.3** 【测试】创建课程仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/CourseMapperTest.java`
  - 内容：测试课程 CRUD 操作、查询可预约课程
  - 依赖：T2.4.2
  - 并行：无

- [ ] **T2.4.4** 【实现】创建课程仓储接口 CourseMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/CourseMapper.java`
  - 内容：定义 CRUD 方法、自定义查询方法（按时间、状态查询）
  - 依赖：T2.4.3
  - 并行：无

---

### 2.5 器材领域 (Equipment)

- [ ] **T2.5.1** 【测试】创建 Equipment 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/EquipmentTest.java`
  - 内容：测试器材基本信息验证
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.5.2** 【实现】创建器材实体类 Equipment
  - 路径：`gym-backend/src/main/java/com/gym/entity/Equipment.java`
  - 内容：定义器材字段、getter/setter
  - 依赖：T2.5.1
  - 并行：无

- [ ] **T2.5.3** 【测试】创建器材仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/EquipmentMapperTest.java`
  - 内容：测试器材 CRUD 操作
  - 依赖：T2.5.2
  - 并行：无

- [ ] **T2.5.4** 【实现】创建器材仓储接口 EquipmentMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/EquipmentMapper.java`
  - 内容：定义 CRUD 方法
  - 依赖：T2.5.3
  - 并行：无

---

### 2.6 订单领域 (Order)

- [ ] **T2.6.1** 【测试】创建 Order 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/OrderTest.java`
  - 内容：测试订单状态流转、签到逻辑、取消逻辑
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.6.2** 【实现】创建订单实体类 Order
  - 路径：`gym-backend/src/main/java/com/gym/entity/Order.java`
  - 内容：定义订单字段、getter/setter、业务方法（取消、签到等）
  - 依赖：T2.6.1
  - 并行：无

- [ ] **T2.6.3** 【测试】创建订单仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/OrderMapperTest.java`
  - 内容：测试订单 CRUD 操作、查询会员订单、时间冲突检测
  - 依赖：T2.6.2
  - 并行：无

- [ ] **T2.6.4** 【实现】创建订单仓储接口 OrderMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/OrderMapper.java`
  - 内容：定义 CRUD 方法、自定义查询方法（时间冲突检测、每日预约计数）
  - 依赖：T2.6.3
  - 并行：无

---

### 2.7 通知领域 (Notification)

- [ ] **T2.7.1** 【测试】创建 Notification 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/NotificationTest.java`
  - 内容：测试通知基本信息、已读状态
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.7.2** 【实现】创建通知实体类 Notification
  - 路径：`gym-backend/src/main/java/com/gym/entity/Notification.java`
  - 内容：定义通知字段、getter/setter
  - 依赖：T2.7.1
  - 并行：无

- [ ] **T2.7.3** 【测试】创建通知仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/NotificationMapperTest.java`
  - 内容：测试通知 CRUD 操作、查询未读通知
  - 依赖：T2.7.2
  - 并行：无

- [ ] **T2.7.4** 【实现】创建通知仓储接口 NotificationMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/NotificationMapper.java`
  - 内容：定义 CRUD 方法、自定义查询方法
  - 依赖：T2.7.3
  - 并行：无

---

### 2.8 爽约记录领域 (NoShowRecord)

- [ ] **T2.8.1** 【测试】创建 NoShowRecord 实体测试类
  - 路径：`gym-backend/src/test/java/com/gym/entity/NoShowRecordTest.java`
  - 内容：测试爽约记录基本信息
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T2.8.2** 【实现】创建爽约记录实体类 NoShowRecord
  - 路径：`gym-backend/src/main/java/com/gym/entity/NoShowRecord.java`
  - 内容：定义爽约记录字段、getter/setter
  - 依赖：T2.8.1
  - 并行：无

- [ ] **T2.8.3** 【测试】创建爽约记录仓储接口测试
  - 路径：`gym-backend/src/test/java/com/gym/mapper/NoShowRecordMapperTest.java`
  - 内容：测试爽约记录 CRUD 操作、查询会员爽约次数
  - 依赖：T2.8.2
  - 并行：无

- [ ] **T2.8.4** 【实现】创建爽约记录仓储接口 NoShowRecordMapper
  - 路径：`gym-backend/src/main/java/com/gym/mapper/NoShowRecordMapper.java`
  - 内容：定义 CRUD 方法、自定义查询方法
  - 依赖：T2.8.3
  - 并行：无

---

**Phase 2 完成标志**：
- ✅ 所有领域实体类创建完成
- ✅ 所有仓储接口定义完成
- ✅ 领域测试覆盖所有实体
- ✅ 测试全部通过

---

## Phase 3: Application Use Cases & Application Tests (TDD)

> **目标**：应用服务 / Use Case、输入输出模型、业务编排、事务边界抽象、应用层测试  
> **必须先生成测试任务，再生成实现任务**

### 3.1 DTO 定义（输入输出模型）

- [x] **T3.1.1** 创建会员请求 DTO 包
  - 路径：`gym-backend/src/main/java/com/gym/dto/request/`
  - 内容：创建 MemberRegisterRequest、MemberUpdateRequest、MemberLoginRequest
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.2** 创建会员响应 DTO 包
  - 路径：`gym-backend/src/main/java/com/gym/dto/response/`
  - 内容：创建 MemberResponse、MemberInfoResponse
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.3** 创建课程请求 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/request/CourseRequest.java`
  - 内容：课程创建/更新请求模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.4** 创建课程响应 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/response/CourseResponse.java`
  - 内容：课程信息响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.5** 创建预约请求 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/request/BookingRequest.java`
  - 内容：预约请求模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.6** 创建预约响应 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/response/BookingResponse.java`
  - 内容：预约响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.7** 创建订单响应 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/response/OrderResponse.java`
  - 内容：订单信息响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.8** 创建教练 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/request/CoachRequest.java`、`response/CoachResponse.java`
  - 内容：教练请求/响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.9** 创建器材 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/request/EquipmentRequest.java`、`response/EquipmentResponse.java`
  - 内容：器材请求/响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.10** 创建通知响应 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/response/NotificationResponse.java`
  - 内容：通知信息响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

- [x] **T3.1.11** 创建统计响应 DTO
  - 路径：`gym-backend/src/main/java/com/gym/dto/response/StatisticsResponse.java`
  - 内容：统计数据响应模型
  - 依赖：Phase 2 完成
  - 并行：[P]

---

### 3.2 认证应用服务

- [x] **T3.2.1** 【测试】创建认证服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/AuthServiceTest.java`
  - 内容：测试会员注册、登录、管理员登录
  - 依赖：T3.1.1, T3.1.2
  - 并行：无

- [x] **T3.2.2** 【实现】创建认证服务接口 AuthService
  - 路径：`gym-backend/src/main/java/com/gym/service/AuthService.java`
  - 内容：定义注册、登录接口
  - 依赖：T3.2.1
  - 并行：无

- [x] **T3.2.3** 【实现】创建认证服务实现 AuthServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/AuthServiceImpl.java`
  - 内容：实现注册、登录逻辑（密码加密、JWT 生成）
  - 依赖：T3.2.2, T1.5.1
  - 并行：无

---

### 3.3 会员应用服务

- [x] **T3.3.1** 【测试】创建会员服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/MemberServiceTest.java`
  - 内容：测试会员信息查询、更新、密码修改
  - 依赖：T3.1.1, T3.1.2
  - 并行：无

- [x] **T3.3.2** 【实现】创建会员服务接口 MemberService
  - 路径：`gym-backend/src/main/java/com/gym/service/MemberService.java`
  - 内容：定义会员管理接口
  - 依赖：T3.3.1
  - 并行：无

- [x] **T3.3.3** 【实现】创建会员服务实现 MemberServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/MemberServiceImpl.java`
  - 内容：实现会员 CRUD、信息更新、密码修改
  - 依赖：T3.3.2
  - 并行：无

---

### 3.4 教练应用服务

- [x] **T3.4.1** 【测试】创建教练服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/CoachServiceTest.java`
  - 内容：测试教练 CRUD 操作
  - 依赖：T3.1.8
  - 并行：无

- [x] **T3.4.2** 【实现】创建教练服务接口 CoachService
  - 路径：`gym-backend/src/main/java/com/gym/service/CoachService.java`
  - 内容：定义教练管理接口
  - 依赖：T3.4.1
  - 并行：无

- [x] **T3.4.3** 【实现】创建教练服务实现 CoachServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/CoachServiceImpl.java`
  - 内容：实现教练 CRUD 逻辑
  - 依赖：T3.4.2
  - 并行：无

---

### 3.5 器材应用服务

- [x] **T3.5.1** 【测试】创建器材服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/EquipmentServiceTest.java`
  - 内容：测试器材 CRUD 操作
  - 依赖：T3.1.9
  - 并行：无

- [x] **T3.5.2** 【实现】创建器材服务接口 EquipmentService
  - 路径：`gym-backend/src/main/java/com/gym/service/EquipmentService.java`
  - 内容：定义器材管理接口
  - 依赖：T3.5.1
  - 并行：无

- [x] **T3.5.3** 【实现】创建器材服务实现 EquipmentServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/EquipmentServiceImpl.java`
  - 内容：实现器材 CRUD 逻辑
  - 依赖：T3.5.2
  - 并行：无

---

### 3.6 课程应用服务

- [x] **T3.6.1** 【测试】创建课程服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/CourseServiceTest.java`
  - 内容：测试课程 CRUD、查询可预约课程
  - 依赖：T3.1.3, T3.1.4
  - 并行：无

- [x] **T3.6.2** 【实现】创建课程服务接口 CourseService
  - 路径：`gym-backend/src/main/java/com/gym/service/CourseService.java`
  - 内容：定义课程管理接口
  - 依赖：T3.6.1
  - 并行：无

- [x] **T3.6.3** 【实现】创建课程服务实现 CourseServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/CourseServiceImpl.java`
  - 内容：实现课程 CRUD、查询可预约课程
  - 依赖：T3.6.2
  - 并行：无

---

### 3.7 预约应用服务（核心）

- [x] **T3.7.1** 【测试】创建预约服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/BookingServiceTest.java`
  - 内容：测试预约流程、取消预约、时间冲突检测、每日上限检测
  - 依赖：T3.1.5, T3.1.6, T3.1.7
  - 并行：无

- [x] **T3.7.2** 【实现】创建预约服务接口 BookingService
  - 路径：`gym-backend/src/main/java/com/gym/service/BookingService.java`
  - 内容：定义预约、取消预约接口
  - 依赖：T3.7.1
  - 并行：无

- [x] **T3.7.3** 【实现】创建预约服务实现 BookingServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/BookingServiceImpl.java`
  - 内容：实现预约核心逻辑（验证、锁名额、创建订单）
  - 依赖：T3.7.2
  - 并行：无

---

### 3.8 订单应用服务

- [x] **T3.8.1** 【测试】创建订单服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/OrderServiceTest.java`
  - 内容：测试订单查询、签到、收费记录、爽约处理
  - 依赖：T3.1.7
  - 并行：无

- [x] **T3.8.2** 【实现】创建订单服务接口 OrderService
  - 路径：`gym-backend/src/main/java/com/gym/service/OrderService.java`
  - 内容：定义订单管理接口
  - 依赖：T3.8.1
  - 并行：无

- [x] **T3.8.3** 【实现】创建订单服务实现 OrderServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/OrderServiceImpl.java`
  - 内容：实现订单查询、签到、收费、爽约处理
  - 依赖：T3.8.2
  - 并行：无

---

### 3.9 通知应用服务

- [x] **T3.9.1** 【测试】创建通知服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/NotificationServiceTest.java`
  - 内容：测试通知发送、查询、标记已读
  - 依赖：T3.1.10
  - 并行：无

- [x] **T3.9.2** 【实现】创建通知服务接口 NotificationService
  - 路径：`gym-backend/src/main/java/com/gym/service/NotificationService.java`
  - 内容：定义通知管理接口
  - 依赖：T3.9.1
  - 并行：无

- [x] **T3.9.3** 【实现】创建通知服务实现 NotificationServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/NotificationServiceImpl.java`
  - 内容：实现通知发送、查询、标记已读
  - 依赖：T3.9.2
  - 并行：无

---

### 3.10 统计应用服务

- [x] **T3.10.1** 【测试】创建统计服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/StatisticsServiceTest.java`
  - 内容：测试课程报名统计、会员活跃度、教练上课统计、收入统计
  - 依赖：T3.1.11
  - 并行：无

- [x] **T3.10.2** 【实现】创建统计服务接口 StatisticsService
  - 路径：`gym-backend/src/main/java/com/gym/service/StatisticsService.java`
  - 内容：定义统计查询接口
  - 依赖：T3.10.1
  - 并行：无

- [x] **T3.10.3** 【实现】创建统计服务实现 StatisticsServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/StatisticsServiceImpl.java`
  - 内容：实现各类统计查询逻辑
  - 依赖：T3.10.2
  - 并行：无

---

### 3.11 管理员应用服务

- [x] **T3.11.1** 【测试】创建管理员服务测试类
  - 路径：`gym-backend/src/test/java/com/gym/service/AdminServiceTest.java`
  - 内容：测试管理员 CRUD、角色权限验证
  - 依赖：Phase 2
  - 并行：无

- [x] **T3.11.2** 【实现】创建管理员服务接口 AdminService
  - 路径：`gym-backend/src/main/java/com/gym/service/AdminService.java`
  - 内容：定义管理员管理接口
  - 依赖：T3.11.1
  - 并行：无

- [x] **T3.11.3** 【实现】创建管理员服务实现 AdminServiceImpl
  - 路径：`gym-backend/src/main/java/com/gym/service/impl/AdminServiceImpl.java`
  - 内容：实现管理员 CRUD 逻辑
  - 依赖：T3.11.2
  - 并行：无

---

**Phase 3 完成标志**：
- ✅ 所有应用服务接口定义完成
- ✅ 所有应用服务实现完成
- ✅ 应用层测试覆盖所有服务
- ✅ 测试全部通过

---

## Phase 4: API Contracts & Web API (TDD)

> **目标**：API DTO、错误响应模型、Controller / Minimal API、请求校验、DTO 映射、Result 到 HTTP 响应映射、接口测试  
> **必须先生成接口测试任务，再生成实现任务**

### 4.1 认证接口

- [ ] **T4.1.1** 【测试】创建认证接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/AuthControllerTest.java`
  - 内容：测试注册、登录接口（成功、失败场景）
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.1.2** 【实现】创建认证控制器 AuthController
  - 路径：`gym-backend/src/main/java/com/gym/controller/AuthController.java`
  - 内容：实现 `/api/auth/member/register`、`/api/auth/member/login`、`/api/auth/admin/login`
  - 依赖：T4.1.1, T3.2.3
  - 并行：无

---

### 4.2 会员接口

- [ ] **T4.2.1** 【测试】创建会员接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/MemberControllerTest.java`
  - 内容：测试会员信息 CRUD 接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.2.2** 【实现】创建会员控制器 MemberController
  - 路径：`gym-backend/src/main/java/com/gym/controller/MemberController.java`
  - 内容：实现会员端和管理员端会员管理接口
  - 依赖：T4.2.1, T3.3.3
  - 并行：无

---

### 4.3 教练接口

- [ ] **T4.3.1** 【测试】创建教练接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/CoachControllerTest.java`
  - 内容：测试教练 CRUD 接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.3.2** 【实现】创建教练控制器 CoachController
  - 路径：`gym-backend/src/main/java/com/gym/controller/CoachController.java`
  - 内容：实现教练管理接口
  - 依赖：T4.3.1, T3.4.3
  - 并行：无

---

### 4.4 器材接口

- [ ] **T4.4.1** 【测试】创建器材接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/EquipmentControllerTest.java`
  - 内容：测试器材 CRUD 接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.4.2** 【实现】创建器材控制器 EquipmentController
  - 路径：`gym-backend/src/main/java/com/gym/controller/EquipmentController.java`
  - 内容：实现器材管理接口
  - 依赖：T4.4.1, T3.5.3
  - 并行：无

---

### 4.5 课程接口

- [ ] **T4.5.1** 【测试】创建课程接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/CourseControllerTest.java`
  - 内容：测试课程 CRUD、查询接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.5.2** 【实现】创建课程控制器 CourseController
  - 路径：`gym-backend/src/main/java/com/gym/controller/CourseController.java`
  - 内容：实现课程管理接口
  - 依赖：T4.5.1, T3.6.3
  - 并行：无

---

### 4.6 预约接口

- [ ] **T4.6.1** 【测试】创建预约接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/BookingControllerTest.java`
  - 内容：测试预约、取消预约、候补接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.6.2** 【实现】创建预约控制器 BookingController
  - 路径：`gym-backend/src/main/java/com/gym/controller/BookingController.java`
  - 内容：实现预约相关接口
  - 依赖：T4.6.1, T3.7.3
  - 并行：无

---

### 4.7 订单接口

- [ ] **T4.7.1** 【测试】创建订单接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/OrderControllerTest.java`
  - 内容：测试订单查询、签到、收费、爽约处理接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.7.2** 【实现】创建订单控制器 OrderController
  - 路径：`gym-backend/src/main/java/com/gym/controller/OrderController.java`
  - 内容：实现订单管理接口
  - 依赖：T4.7.1, T3.8.3
  - 并行：无

---

### 4.8 通知接口

- [ ] **T4.8.1** 【测试】创建通知接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/NotificationControllerTest.java`
  - 内容：测试通知查询、标记已读接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.8.2** 【实现】创建通知控制器 NotificationController
  - 路径：`gym-backend/src/main/java/com/gym/controller/NotificationController.java`
  - 内容：实现通知管理接口
  - 依赖：T4.8.1, T3.9.3
  - 并行：无

---

### 4.9 统计接口

- [ ] **T4.9.1** 【测试】创建统计接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/StatisticsControllerTest.java`
  - 内容：测试各类统计接口
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.9.2** 【实现】创建统计控制器 StatisticsController
  - 路径：`gym-backend/src/main/java/com/gym/controller/StatisticsController.java`
  - 内容：实现统计查询接口
  - 依赖：T4.9.1, T3.10.3
  - 并行：无

---

### 4.10 管理员接口

- [ ] **T4.10.1** 【测试】创建管理员接口测试类
  - 路径：`gym-backend/src/test/java/com/gym/controller/AdminControllerTest.java`
  - 内容：测试管理员 CRUD 接口（仅超级管理员可访问）
  - 依赖：Phase 3 完成
  - 并行：无

- [ ] **T4.10.2** 【实现】创建管理员控制器 AdminController
  - 路径：`gym-backend/src/main/java/com/gym/controller/AdminController.java`
  - 内容：实现管理员管理接口
  - 依赖：T4.10.1, T3.11.3
  - 并行：无

---

**Phase 4 完成标志**：
- ✅ 所有 Controller 实现完成
- ✅ 所有接口测试通过
- ✅ Swagger 文档完整
- ✅ 所有 API 可正常调用

---

## Phase 5: Infrastructure & Integration

> **目标**：仓储实现、数据库映射、DbContext、外部服务适配、认证落地、配置实现、集成测试支撑

### 5.1 数据库初始化

- [ ] **T5.1.1** 创建数据库初始化 SQL 脚本
  - 路径：`gym-backend/src/main/resources/sql/schema.sql`
  - 内容：创建所有数据表（参考 plan.md 2.2 节）
  - 依赖：Phase 2 完成
  - 并行：[P]

- [ ] **T5.1.2** 创建测试数据 SQL 脚本
  - 路径：`gym-backend/src/main/resources/sql/data.sql`
  - 内容：插入测试数据（管理员、教练、课程等）
  - 依赖：T5.1.1
  - 并行：无

---

### 5.2 MyBatis XML 映射文件

- [ ] **T5.2.1** 创建 MemberMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/MemberMapper.xml`
  - 内容：定义会员 SQL 映射（含自定义查询）
  - 依赖：T2.1.4
  - 并行：[P]

- [ ] **T5.2.2** 创建 AdminMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/AdminMapper.xml`
  - 内容：定义管理员 SQL 映射
  - 依赖：T2.2.4
  - 并行：[P]

- [ ] **T5.2.3** 创建 CoachMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/CoachMapper.xml`
  - 内容：定义教练 SQL 映射
  - 依赖：T2.3.4
  - 并行：[P]

- [ ] **T5.2.4** 创建 CourseMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/CourseMapper.xml`
  - 内容：定义课程 SQL 映射（含自定义查询）
  - 依赖：T2.4.4
  - 并行：[P]

- [ ] **T5.2.5** 创建 EquipmentMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/EquipmentMapper.xml`
  - 内容：定义器材 SQL 映射
  - 依赖：T2.5.4
  - 并行：[P]

- [ ] **T5.2.6** 创建 OrderMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/OrderMapper.xml`
  - 内容：定义订单 SQL 映射（含时间冲突检测）
  - 依赖：T2.6.4
  - 并行：[P]

- [ ] **T5.2.7** 创建 NotificationMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/NotificationMapper.xml`
  - 内容：定义通知 SQL 映射
  - 依赖：T2.7.4
  - 并行：[P]

- [ ] **T5.2.8** 创建 NoShowRecordMapper.xml
  - 路径：`gym-backend/src/main/resources/mapper/NoShowRecordMapper.xml`
  - 内容：定义爽约记录 SQL 映射
  - 依赖：T2.8.4
  - 并行：[P]

---

### 5.3 短信服务集成

- [ ] **T5.3.1** 创建短信工具类 SmsUtil
  - 路径：`gym-backend/src/main/java/com/gym/util/SmsUtil.java`
  - 内容：封装阿里云短信 SDK（可先 Mock）
  - 依赖：Phase 1 完成
  - 并行：无

---

### 5.4 定时任务实现

- [ ] **T5.4.1** 创建课程提醒定时任务
  - 路径：`gym-backend/src/main/java/com/gym/task/CourseReminderTask.java`
  - 内容：实现课前 1 小时、5 分钟提醒
  - 依赖：T3.9.3
  - 并行：无

- [ ] **T5.4.2** 创建爽约检测定时任务
  - 路径：`gym-backend/src/main/java/com/gym/task/NoShowDetectionTask.java`
  - 内容：实现课程结束后自动检测爽约
  - 依赖：T3.8.3
  - 并行：无

- [ ] **T5.4.3** 创建限制预约自动解除定时任务
  - 路径：`gym-backend/src/main/java/com/gym/task/BanReleaseTask.java`
  - 内容：实现每天凌晨自动解除过期限制
  - 依赖：Phase 2
  - 并行：无

---

### 5.5 认证落地

- [ ] **T5.5.1** 创建 JWT 认证过滤器
  - 路径：`gym-backend/src/main/java/com/gym/filter/JwtAuthenticationFilter.java`
  - 内容：解析 JWT、设置用户上下文
  - 依赖：T1.5.1
  - 并行：无

- [ ] **T5.5.2** 更新 Spring Security 配置
  - 路径：`gym-backend/src/main/java/com/gym/config/SecurityConfig.java`
  - 内容：启用 JWT 认证、配置权限规则
  - 依赖：T5.5.1
  - 并行：无

- [ ] **T5.5.3** 创建用户详情服务 UserDetailsService 实现
  - 路径：`gym-backend/src/main/java/com/gym/service/UserDetailsServiceImpl.java`
  - 内容：加载用户信息供 Spring Security 使用
  - 依赖：Phase 2
  - 并行：无

---

### 5.6 集成测试支撑

- [ ] **T5.6.1** 创建集成测试基类
  - 路径：`gym-backend/src/test/java/com/gym/common/IntegrationTest.java`
  - 内容：定义集成测试基础配置
  - 依赖：Phase 4 完成
  - 并行：无

- [ ] **T5.6.2** 创建预约流程集成测试
  - 路径：`gym-backend/src/test/java/com/gym/integration/BookingIntegrationTest.java`
  - 内容：测试完整预约流程（注册→登录→预约→签到）
  - 依赖：T5.6.1
  - 并行：无

---

**Phase 5 完成标志**：
- ✅ 数据库表创建完成
- ✅ MyBatis XML 映射完整
- ✅ 定时任务正常运行
- ✅ JWT 认证生效
- ✅ 集成测试通过

---

## Phase 6: Frontend UI & Interaction

> **目标**：路由、页面、组件、布局、API Service、表单处理、页面状态、鉴权态、联调任务

### 6.1 公共组件

- [ ] **T6.1.1** 创建 Header 组件
  - 路径：`gym-frontend/src/components/Header.vue`
  - 内容：顶部导航栏、用户信息、通知入口
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.1.2** 创建 Sidebar 组件
  - 路径：`gym-frontend/src/components/Sidebar.vue`
  - 内容：侧边栏菜单（根据角色动态显示）
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.1.3** 创建 CourseCard 组件
  - 路径：`gym-frontend/src/components/CourseCard.vue`
  - 内容：课程卡片展示
  - 依赖：Phase 1 完成
  - 并行：[P]

---

### 6.2 API Service 封装

- [ ] **T6.2.1** 创建认证 API Service
  - 路径：`gym-frontend/src/api/auth.js`
  - 内容：封装注册、登录接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.2.2** 创建会员 API Service
  - 路径：`gym-frontend/src/api/member.js`
  - 内容：封装会员信息接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.2.3** 创建课程 API Service
  - 路径：`gym-frontend/src/api/course.js`
  - 内容：封装课程查询接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.2.4** 创建预约 API Service
  - 路径：`gym-frontend/src/api/booking.js`
  - 内容：封装预约接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.2.5** 创建订单 API Service
  - 路径：`gym-frontend/src/api/order.js`
  - 内容：封装订单接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.2.6** 创建通知 API Service
  - 路径：`gym-frontend/src/api/notification.js`
  - 内容：封装通知接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

- [ ] **T6.2.7** 创建管理员 API Service
  - 路径：`gym-frontend/src/api/admin.js`
  - 内容：封装管理员接口调用
  - 依赖：Phase 1 完成
  - 并行：[P]

---

### 6.3 会员端页面

- [ ] **T6.3.1** 创建会员登录页面
  - 路径：`gym-frontend/src/views/member/Login.vue`
  - 内容：登录表单、表单验证、登录逻辑
  - 依赖：T6.2.1
  - 并行：无

- [ ] **T6.3.2** 创建会员注册页面
  - 路径：`gym-frontend/src/views/member/Register.vue`
  - 内容：注册表单、表单验证、注册逻辑
  - 依赖：T6.2.1
  - 并行：无

- [ ] **T6.3.3** 创建会员首页（课程列表）
  - 路径：`gym-frontend/src/views/member/Home.vue`
  - 内容：课程筛选、课程列表、预约入口
  - 依赖：T6.2.3, T6.1.3
  - 并行：无

- [ ] **T6.3.4** 创建课程详情页面
  - 路径：`gym-frontend/src/views/member/CourseDetail.vue`
  - 内容：课程详情展示、预约按钮
  - 依赖：T6.2.3
  - 并行：无

- [ ] **T6.3.5** 创建我的预约页面
  - 路径：`gym-frontend/src/views/member/Bookings.vue`
  - 内容：预约记录列表、取消预约
  - 依赖：T6.2.5
  - 并行：无

- [ ] **T6.3.6** 创建个人中心页面
  - 路径：`gym-frontend/src/views/member/Profile.vue`
  - 内容：个人信息展示、编辑表单
  - 依赖：T6.2.2
  - 并行：无

- [ ] **T6.3.7** 创建通知中心页面
  - 路径：`gym-frontend/src/views/member/Notifications.vue`
  - 内容：通知列表、标记已读
  - 依赖：T6.2.6
  - 并行：无

---

### 6.4 管理员端页面

- [ ] **T6.4.1** 创建管理员登录页面
  - 路径：`gym-frontend/src/views/admin/Login.vue`
  - 内容：管理员登录表单
  - 依赖：T6.2.1
  - 并行：无

- [ ] **T6.4.2** 创建管理员控制台页面
  - 路径：`gym-frontend/src/views/admin/Dashboard.vue`
  - 内容：数据统计概览、图表展示
  - 依赖：T6.2.7
  - 并行：无

- [ ] **T6.4.3** 创建会员管理页面
  - 路径：`gym-frontend/src/views/admin/Members.vue`
  - 内容：会员列表、增删改查、爽约记录管理
  - 依赖：T6.2.7
  - 并行：无

- [ ] **T6.4.4** 创建教练管理页面
  - 路径：`gym-frontend/src/views/admin/Coaches.vue`
  - 内容：教练列表、增删改查
  - 依赖：T6.2.7
  - 并行：无

- [ ] **T6.4.5** 创建器材管理页面
  - 路径：`gym-frontend/src/views/admin/Equipment.vue`
  - 内容：器材列表、增删改查
  - 依赖：T6.2.7
  - 并行：无

- [ ] **T6.4.6** 创建课程管理页面
  - 路径：`gym-frontend/src/views/admin/Courses.vue`
  - 内容：课程列表、增删改查、取消课程
  - 依赖：T6.2.7
  - 并行：无

- [ ] **T6.4.7** 创建订单管理页面
  - 路径：`gym-frontend/src/views/admin/Orders.vue`
  - 内容：订单列表、收费记录、签到、爽约处理
  - 依赖：T6.2.7
  - 并行：无

- [ ] **T6.4.8** 创建管理员管理页面（仅超级管理员）
  - 路径：`gym-frontend/src/views/admin/Admins.vue`
  - 内容：管理员列表、增删改查
  - 依赖：T6.2.7
  - 并行：无

---

### 6.5 路由与权限

- [ ] **T6.5.1** 完善路由配置
  - 路径：`gym-frontend/src/router/index.js`
  - 内容：配置所有路由、路由守卫、角色权限
  - 依赖：Phase 1 完成
  - 并行：无

- [ ] **T6.5.2** 创建路由守卫
  - 路径：`gym-frontend/src/router/guards.js`
  - 内容：登录验证、角色权限验证
  - 依赖：T6.5.1
  - 并行：无

---

### 6.6 联调测试

- [ ] **T6.6.1** 会员端联调测试
  - 路径：`gym-frontend/src/tests/member.test.js`
  - 内容：测试会员端完整流程（注册→登录→预约→查看）
  - 依赖：Phase 6.3 完成
  - 并行：无

- [ ] **T6.6.2** 管理员端联调测试
  - 路径：`gym-frontend/src/tests/admin.test.js`
  - 内容：测试管理员端完整流程
  - 依赖：Phase 6.4 完成
  - 并行：无

---

**Phase 6 完成标志**：
- ✅ 所有页面实现完成
- ✅ 所有组件正常工作
- ✅ 前后端联调通过
- ✅ 用户可完成完整业务流程

---

## 任务统计汇总

| Phase | 任务数 | 测试任务 | 实现任务 |
|-------|--------|----------|----------|
| Phase 1: Foundation & Skeleton | 24 | 0 | 24 |
| Phase 2: Domain Model & Domain Tests | 32 | 16 | 16 |
| Phase 3: Application Use Cases & Application Tests | 34 | 11 | 23 |
| Phase 4: API Contracts & Web API | 20 | 10 | 10 |
| Phase 5: Infrastructure & Integration | 16 | 2 | 14 |
| Phase 6: Frontend UI & Interaction | 22 | 2 | 20 |
| **总计** | **148** | **41** | **107** |

---

## 任务执行顺序建议

### 迭代 1（Week 1-2）：Phase 1 + Phase 2
- 完成基础架构搭建
- 完成所有领域模型和测试

### 迭代 2（Week 3-4）：Phase 3
- 完成所有应用服务层
- 确保业务逻辑测试通过

### 迭代 3（Week 5-6）：Phase 4 + Phase 5
- 完成所有 Controller
- 完成数据库映射、定时任务、认证

### 迭代 4（Week 7-8）：Phase 6
- 完成所有前端页面
- 前后端联调

### 迭代 5（Week 9）：测试优化
- 集成测试
- Bug 修复
- 性能优化

---

**文档版本：** v1.0  
**创建日期：** 2026-05-20  
**创建者：** 技术组长  
**审核状态：** 待审核
