package com.gym.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据脱敏工具类测试。
 *
 * @author liuxinsi
 * @date 2026-06-03
 */
@DisplayName("数据脱敏工具测试")
class DesensitizationUtilTest {

    @Test
    @DisplayName("手机号脱敏 - 正常 11 位")
    void testDesensitizePhoneNormal() {
        // Arrange
        String phone = "13812345678";

        // Act
        String result = DesensitizationUtil.desensitizePhone(phone);

        // Assert
        assertEquals("138****5678", result);
    }

    @Test
    @DisplayName("手机号脱敏 - null 值")
    void testDesensitizePhoneNull() {
        // Act
        String result = DesensitizationUtil.desensitizePhone(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("手机号脱敏 - 长度不足")
    void testDesensitizePhoneInvalidLength() {
        // Arrange
        String phone = "1381234";

        // Act
        String result = DesensitizationUtil.desensitizePhone(phone);

        // Assert
        assertEquals("1381234", result); // 不处理
    }

    @Test
    @DisplayName("身份证号脱敏 - 18 位")
    void testDesensitizeIdCard18Digits() {
        // Arrange
        String idCard = "110101199001011234";

        // Act
        String result = DesensitizationUtil.desensitizeIdCard(idCard);

        // Assert
        assertEquals("110***********1234", result);
    }

    @Test
    @DisplayName("身份证号脱敏 - 15 位")
    void testDesensitizeIdCard15Digits() {
        // Arrange
        String idCard = "110101900101123";

        // Act
        String result = DesensitizationUtil.desensitizeIdCard(idCard);

        // Assert
        assertEquals("110********1123", result); // 15 位：3+8+4
    }

    @Test
    @DisplayName("身份证号脱敏 - null 值")
    void testDesensitizeIdCardNull() {
        // Act
        String result = DesensitizationUtil.desensitizeIdCard(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("身份证号脱敏 - 长度不足")
    void testDesensitizeIdCardTooShort() {
        // Arrange
        String idCard = "12345";

        // Act
        String result = DesensitizationUtil.desensitizeIdCard(idCard);

        // Assert
        assertEquals("12345", result); // 不处理
    }

    @Test
    @DisplayName("姓名脱敏 - 2 个字")
    void testDesensitizeNameTwoChars() {
        // Arrange
        String name = "张三";

        // Act
        String result = DesensitizationUtil.desensitizeName(name);

        // Assert
        assertEquals("张*", result);
    }

    @Test
    @DisplayName("姓名脱敏 - 3 个字")
    void testDesensitizeNameThreeChars() {
        // Arrange
        String name = "张三丰";

        // Act
        String result = DesensitizationUtil.desensitizeName(name);

        // Assert
        assertEquals("张**", result);
    }

    @Test
    @DisplayName("姓名脱敏 - 1 个字")
    void testDesensitizeNameOneChar() {
        // Arrange
        String name = "张";

        // Act
        String result = DesensitizationUtil.desensitizeName(name);

        // Assert
        assertEquals("张", result); // 单字不脱敏
    }

    @Test
    @DisplayName("姓名脱敏 - null 值")
    void testDesensitizeNameNull() {
        // Act
        String result = DesensitizationUtil.desensitizeName(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("邮箱脱敏 - 正常")
    void testDesensitizeEmailNormal() {
        // Arrange
        String email = "test@example.com";

        // Act
        String result = DesensitizationUtil.desensitizeEmail(email);

        // Assert
        assertEquals("te**@example.com", result);
    }

    @Test
    @DisplayName("邮箱脱敏 - 短用户名")
    void testDesensitizeEmailShortUsername() {
        // Arrange
        String email = "ab@example.com";

        // Act
        String result = DesensitizationUtil.desensitizeEmail(email);

        // Assert
        assertEquals("ab**@example.com", result);
    }

    @Test
    @DisplayName("邮箱脱敏 - null 值")
    void testDesensitizeEmailNull() {
        // Act
        String result = DesensitizationUtil.desensitizeEmail(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("邮箱脱敏 - 无@符号")
    void testDesensitizeEmailInvalidFormat() {
        // Arrange
        String email = "invalidemail";

        // Act
        String result = DesensitizationUtil.desensitizeEmail(email);

        // Assert
        assertEquals("invalidemail", result); // 不处理
    }

    @Test
    @DisplayName("紧急联系人电话脱敏 - 正常")
    void testDesensitizeEmergencyPhoneNormal() {
        // Arrange
        String phone = "13987654321";

        // Act
        String result = DesensitizationUtil.desensitizeEmergencyPhone(phone);

        // Assert
        assertEquals("139****4321", result);
    }

    @Test
    @DisplayName("紧急联系人电话脱敏 - null 值")
    void testDesensitizeEmergencyPhoneNull() {
        // Act
        String result = DesensitizationUtil.desensitizeEmergencyPhone(null);

        // Assert
        assertNull(result);
    }
}
