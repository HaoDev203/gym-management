---
name: "code-optimizer"
description: "优化代码结构、性能、可读性。当用户要求优化代码、重构代码、改进代码质量时触发。"
---

# 代码优化器

## 功能说明

本技能用于分析和优化代码质量，提供结构优化、性能优化、可读性改进建议。

## 触发场景

- 用户要求"优化这段代码"
- 用户要求"重构这个类"
- 用户要求"改进代码质量"
- 用户要求"提升性能"
- 检测到代码异味（Code Smell）时

## 优化维度

### 1. 代码结构优化

#### 单一职责原则
```java
// ❌ 优化前：一个方法做太多事情
public void processOrder(Order order) {
    // 验证订单
    if (order == null) throw new Exception();
    // 计算价格
    double total = order.getPrice() * order.getQuantity();
    // 保存订单
    orderMapper.insert(order);
    // 发送通知
    emailService.send(order.getUserEmail());
}

// ✅ 优化后：职责分离
public void processOrder(Order order) {
    validateOrder(order);
    calculateTotal(order);
    saveOrder(order);
    sendNotification(order);
}
```

#### 方法长度控制
- 方法不超过 50 行
- 一个方法只做一件事
- 复杂逻辑提取为私有方法

### 2. 性能优化

#### 循环优化
```java
// ❌ 优化前：循环内查询数据库
for (Order order : orders) {
    User user = userMapper.findById(order.getUserId());
}

// ✅ 优化后：批量查询
List<Long> userIds = orders.stream().map(Order::getUserId).collect(Collectors.toList());
Map<Long, User> userMap = userMapper.findByIds(userIds).stream()
    .collect(Collectors.toMap(User::getId, u -> u));
```

#### 集合使用优化
```java
// ❌ 优化前：使用 ArrayList 判断包含
if (list.contains(element)) { }

// ✅ 优化后：使用 HashSet
Set<Long> idSet = new HashSet<>(list);
if (idSet.contains(element)) { }
```

### 3. 可读性优化

#### 命名规范
```java
// ❌ 优化前
String s = "13800138001";
List<Map<String, Object>> l = new ArrayList<>();

// ✅ 优化后
String phone = "13800138001";
List<Member> members = new ArrayList<>();
```

#### 注释规范
```java
// ❌ 优化前：无意义的注释
// 设置名称
member.setName(name);

// ✅ 优化后：说明原因
// 脱敏处理，保护用户隐私
member.setName(desensitizeName(name));
```

### 4. 异常处理优化

```java
// ❌ 优化前：捕获异常不处理
try {
    // ...
} catch (Exception e) {
    // 空的 catch
}

// ✅ 优化后：正确处理异常
try {
    // ...
} catch (BusinessException e) {
    log.error("业务异常：{}", e.getMessage());
    throw e;
} catch (Exception e) {
    log.error("系统异常", e);
    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
}
```

### 5. 空指针防护

```java
// ❌ 优化前
String name = member.getName().trim();

// ✅ 优化后
String name = Optional.ofNullable(member)
    .map(Member::getName)
    .map(String::trim)
    .orElse("");
```

## 优化检查清单

### Java 代码
- [ ] 方法是否超过 50 行
- [ ] 是否有重复代码
- [ ] 是否使用合适的集合类型
- [ ] 是否有空指针风险
- [ ] 异常处理是否合理
- [ ] 日志记录是否完整
- [ ] 注释是否清晰
- [ ] 命名是否语义化

### Vue 代码
- [ ] 组件是否超过 300 行
- [ ] 是否使用 Composition API
- [ ] 计算属性是否合理使用
- [ ] 事件处理是否规范
- [ ] 样式是否使用 scoped

## 输出格式

### 优化报告

```markdown
## 代码优化报告

### 文件：MemberServiceImpl.java

#### 问题 1：方法过长
- **位置**: `updateMember()` 方法（120 行）
- **建议**: 拆分为 `validateMember()`, `updateBasicInfo()`, `updateContact()`
- **优先级**: 🔴 高

#### 问题 2：循环内查询
- **位置**: 第 85 行 for 循环
- **建议**: 使用批量查询替代
- **优先级**: 🟡 中

#### 问题 3：硬编码字符串
- **位置**: 第 42 行 "MEMBER"
- **建议**: 提取为常量
- **优先级**: 🟢 低
```

## 重构原则

1. **小步前进**: 每次只重构一小部分
2. **测试保护**: 确保有测试覆盖
3. **保持功能**: 不改变外部行为
4. **持续改进**: 逐步优化，不追求一次完美
