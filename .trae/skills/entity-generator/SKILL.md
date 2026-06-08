---
name: "entity-generator"
description: "自动生成实体类、DTO、VO 等数据对象。当用户需要创建新的数据表对象、DTO、响应对象时触发。"
---

# 实体生成器

## 功能说明

本技能用于自动生成符合项目规范的实体类、DTO、VO 等数据对象，保持代码风格一致。

## 触发场景

- 用户要求"创建实体类"
- 用户要求"生成 DTO"
- 用户要求"创建新的数据表对象"
- 数据库表结构变更后
- 需要创建 Request/Response 对象时

## 生成规范

### 1. Entity 实体类

```java
package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 会员实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("member")
public class Member {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    private Integer gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
```

**规范要求**:
- ✅ 使用 `@Data` 简化代码
- ✅ 使用 `@TableName` 指定表名
- ✅ 主键使用 `@TableId(type = IdType.AUTO)`
- ✅ 添加完整的 JavaDoc 注释
- ✅ 包含作者和日期信息

### 2. DTO（数据传输对象）

#### Request DTO
```java
package com.gym.dto.request;

import lombok.Data;
import javax.validation.constraints.*;

/**
 * 会员更新请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberUpdateRequest {

    @NotNull(message = "ID 不能为空")
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能超过 20")
    private String name;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Min(value = 10, message = "年龄不能小于 10")
    @Max(value = 99, message = "年龄不能大于 99")
    private Integer age;
}
```

#### Response DTO
```java
package com.gym.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 会员信息响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberResponse {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    private Integer gender;

    private LocalDateTime createdAt;
}
```

### 3. VO（视图对象）

```java
package com.gym.vo;

import lombok.Data;

/**
 * 会员信息视图对象。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberVO {

    private Long id;

    private String name;

    // 脱敏后的手机号
    private String desensitizedPhone;

    private String genderLabel;
}
```

## 命名规范

### 类命名
- Entity: `<业务名>.java` - `Member.java`
- Request: `<业务名>Request.java` - `MemberUpdateRequest.java`
- Response: `<业务名>Response.java` - `MemberResponse.java`
- VO: `<业务名>VO.java` - `MemberVO.java`
- DTO: `<业务名>DTO.java` - `MemberDTO.java`

### 字段命名
- 使用驼峰命名法
- 布尔类型使用 `is` 前缀（如 `isActive`）
- 集合类型使用复数（如 `members`, `orders`）

## 字段类型映射

### 数据库 → Java

| 数据库类型 | Java 类型 | 说明 |
|------------|----------|------|
| BIGINT | Long | 主键、ID |
| VARCHAR | String | 字符串 |
| TEXT | String | 长文本 |
| INT | Integer | 整数 |
| DECIMAL | BigDecimal | 金额、精度 |
| DATETIME | LocalDateTime | 日期时间 |
| DATE | LocalDate | 日期 |
| TINYINT | Integer | 状态（0/1） |

## 注释模板

### 类注释
```java
/**
 * <功能描述>。
 *
 * @author liuxinsi
 * @date <生成日期>
 */
```

### 字段注释
```java
/**
 * 会员姓名。
 */
private String name;
```

## 生成示例

### 输入：创建课程实体

**数据库表**: `course`

**字段**:
- id (BIGINT, 主键)
- name (VARCHAR)
- coach_id (BIGINT)
- max_capacity (INT)
- start_time (DATETIME)
- end_time (DATETIME)
- created_at (DATETIME)

### 输出：Course.java

```java
package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程实体类。
 *
 * @author liuxinsi
 * @date 2026-06-03
 */
@Data
@TableName("course")
public class Course {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称。
     */
    private String name;

    /**
     * 教练 ID。
     */
    private Long coachId;

    /**
     * 最大人数。
     */
    private Integer maxCapacity;

    /**
     * 开始时间。
     */
    private LocalDateTime startTime;

    /**
     * 结束时间。
     */
    private LocalDateTime endTime;

    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
}
```

## 最佳实践

1. **使用 Lombok**: 简化 Getter/Setter/toString
2. **参数校验**: Request DTO 添加 `@Validated` 注解
3. **字段脱敏**: Response 中敏感数据需要脱敏
4. **时间类型**: 统一使用 `LocalDateTime`
5. **注释完整**: 所有类、字段必须有注释
