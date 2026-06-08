---
name: "api-docs-generator"
description: "自动生成 API 接口文档。当用户要求生成接口文档、更新文档、查看接口说明时触发。"
---

# API 文档生成器

## 功能说明

本技能用于自动生成标准、完整的 API 接口文档，支持 Swagger/OpenAPI 规范。

## 触发场景

- 用户要求"生成 API 文档"
- 用户要求"更新接口文档"
- 用户要求"查看 XXX 接口文档"
- 用户要求"导出接口说明"
- 新增 Controller 或修改接口后

## 文档生成规范

### 1. 文档结构

每个接口应包含：
- ✅ 接口名称
- ✅ 请求路径
- ✅ 请求方法（GET/POST/PUT/DELETE）
- ✅ 接口描述
- ✅ 请求参数（包含类型、必填、说明）
- ✅ 返回参数（包含类型、说明）
- ✅ 错误码说明
- ✅ 使用示例

### 2. Controller 注释规范

```java
/**
 * 用户管理接口。
 * 
 * <p>提供用户注册、登录、信息查询等功能。</p>
 * 
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 用户登录接口。
     * 
     * <p>用途：验证用户身份，返回 JWT Token。</p>
     * 
     * @param loginRequest 登录请求（包含手机号和密码）
     * @return 统一返回结构，data 为 JWT Token
     * @throws BusinessException 密码错误/用户不存在时抛出
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody LoginRequest loginRequest) {
        // ...
    }
}
```

### 3. 请求参数说明格式

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| phone | String | 是 | 手机号 | 13800138001 |
| password | String | 是 | 密码（6-20 位） | 123456 |

### 4. 返回参数说明格式

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 响应码（200 成功） |
| message | String | 响应消息 |
| data | Object | 响应数据 |

### 5. 错误码说明

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 400 | 参数错误 | 检查请求参数格式 |
| 401 | 未登录 | 请先登录 |
| 403 | 权限不足 | 联系管理员 |
| 404 | 资源不存在 | 检查资源 ID |
| 500 | 服务器错误 | 联系技术支持 |

## 输出格式

### Markdown 格式示例

```markdown
## 用户登录接口

**接口路径**: `POST /api/user/login`

**接口描述**: 验证用户身份，返回 JWT Token

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| phone | String | 是 | 手机号 | 13800138001 |
| password | String | 是 | 密码 | 123456 |

### 返回参数

```json
{
  "code": 200,
  "message": "登录成功",
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 错误码

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户不存在 |
| 1002 | 密码错误 |

### 使用示例

```javascript
fetch('/api/user/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    phone: '13800138001',
    password: '123456'
  })
})
```
```

## Swagger 注解规范

```java
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public BaseResponse<String> login(
        @ApiParam("登录请求") @RequestBody LoginRequest request) {
        // ...
    }
}
```

## 文档更新策略

1. 新增接口时自动生成文档
2. 修改接口时同步更新文档
3. 删除接口时标记为已废弃
4. 定期导出完整文档
