---
name: "test-generator"
description: "自动生成单元测试代码。当用户要求编写测试、补充测试用例、生成 JUnit 测试时触发。"
---

# 测试生成器

## 功能说明

本技能用于自动生成高质量的单元测试代码，支持 JUnit 5、Mockito 等测试框架。

## 触发场景

- 用户要求"生成测试代码"
- 用户要求"为 XXX 类编写测试"
- 用户要求"补充单元测试"
- 用户要求"生成 JUnit 测试"
- 检测到新的 Service/Util 类但没有对应的测试文件

## 测试生成规范

### 1. 测试类命名
- 格式：`<被测试类名>Test.java`
- 示例：`MemberServiceImplTest.java`

### 2. 测试方法命名
- 使用中文描述测试场景
- 格式：`test<测试场景>()`
- 示例：`testUpdateMemberSuccess()`

### 3. 测试覆盖范围

每个 Service 方法应覆盖：
- ✅ 正常场景（成功情况）
- ✅ 异常场景（参数校验失败）
- ✅ 边界情况（空值、null、极限值）
- ✅ 业务规则（业务逻辑验证）

### 4. 测试代码结构

```java
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("测试场景描述")
    void testMethod() {
        // Given - 准备测试数据
        Member member = new Member();
        when(memberMapper.selectById(1L)).thenReturn(member);

        // When - 执行测试
        MemberResponse result = memberService.getMemberById(1L);

        // Then - 验证结果
        assertNotNull(result);
        assertEquals(member.getId(), result.getId());
        verify(memberMapper).selectById(1L);
    }
}
```

### 5. 断言规范

- 使用 `assertEquals(expected, actual)`
- 使用 `assertNotNull(object)`
- 使用 `assertTrue(condition)`
- 使用 `assertThrows(Exception.class, () -> {...})`

### 6. Mock 使用规范

- 使用 `@Mock` 标注依赖对象
- 使用 `@InjectMocks` 标注被测试类
- 使用 `when(...).thenReturn(...)` 模拟行为
- 使用 `verify(...)` 验证调用

## 输出要求

1. 生成完整的测试类文件
2. 包含所有必要的 import 语句
3. 添加 `@DisplayName` 中文描述
4. 注释说明测试目的
5. 确保测试可独立运行

## 示例

为用户生成 `BookingServiceImpl` 的测试：

```java
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("预约成功 - 正常场景")
    void testBookingSuccess() {
        // Given
        Long memberId = 1L;
        Long courseId = 100L;
        when(bookingMapper.countByCourseIdAndMemberId(any(), any())).thenReturn(0);

        // When
        bookingService.createBooking(memberId, courseId);

        // Then
        verify(bookingMapper).insert(any());
    }
}
```
