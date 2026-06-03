package com.gym.util;

/**
 * 数据脱敏工具类。
 * 用于对敏感信息进行脱敏处理，如手机号、身份证号等。
 *
 * @author liuxinsi
 * @date 2026-06-03
 */
public final class DesensitizationUtil {

    private DesensitizationUtil() {
    }

    /**
     * 手机号脱敏：保留前 3 位和后 4 位，中间用 **** 替代。
     * 示例：13812345678 -> 138****5678
     *
     * @param phone 手机号
     * @return 脱敏后的手机号，如果为 null 则返回 null
     */
    public static String desensitizePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 身份证号脱敏：保留前 3 位和后 4 位，中间用 ******** 替代。
     * 示例：110101199001011234 -> 110***********1234
     *
     * @param idCard 身份证号
     * @return 脱敏后的身份证号，如果为 null 则返回 null
     */
    public static String desensitizeIdCard(String idCard) {
        if (idCard == null || idCard.length() < 15) {
            return idCard;
        }
        // 15 位身份证：保留前 3 位和后 4 位
        if (idCard.length() == 15) {
            return idCard.replaceAll("(\\d{3})\\d{8}(\\d{4})", "$1********$2");
        }
        // 18 位身份证：保留前 3 位和后 4 位
        if (idCard.length() == 18) {
            return idCard.replaceAll("(\\d{3})\\d{11}(\\d{4})", "$1***********$2");
        }
        return idCard;
    }

    /**
     * 姓名脱敏：只显示第一个字，后面用 * 替代。
     * 示例：张三 -> 张*，张三丰 -> 张**
     *
     * @param name 姓名
     * @return 脱敏后的姓名，如果为 null 则返回 null
     */
    public static String desensitizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() == 1) {
            return name;
        }
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }

    /**
     * 邮箱脱敏：保留 @ 前面的前 2 个字符和完整的域名。
     * 示例：test@example.com -> te**@example.com
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱，如果为 null 则返回 null
     */
    public static String desensitizeEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }
        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() <= 2) {
            return localPart + "**@" + domain;
        }
        return localPart.substring(0, 2) + "*".repeat(localPart.length() - 2) + "@" + domain;
    }

    /**
     * 紧急联系人电话脱敏：同手机号脱敏规则。
     *
     * @param phone 紧急联系人电话
     * @return 脱敏后的电话，如果为 null 则返回 null
     */
    public static String desensitizeEmergencyPhone(String phone) {
        return desensitizePhone(phone);
    }
}
