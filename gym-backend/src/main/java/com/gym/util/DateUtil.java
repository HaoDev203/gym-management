package com.gym.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 日期工具类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public final class DateUtil {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    /**
     * 格式化日期为 yyyy-MM-dd。
     *
     * @param date LocalDate 对象
     * @return 格式化后的日期字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
    }

    /**
     * 格式化日期时间为 yyyy-MM-dd HH:mm:ss。
     *
     * @param dateTime LocalDateTime 对象
     * @return 格式化后的日期时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN));
    }

    /**
     * 解析日期字符串为 LocalDate。
     *
     * @param dateStr 日期字符串 (yyyy-MM-dd)
     * @return LocalDate 对象
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
    }

    /**
     * 解析日期时间字符串为 LocalDateTime。
     *
     * @param dateTimeStr 日期时间字符串 (yyyy-MM-dd HH:mm:ss)
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN));
    }

    /**
     * 计算两个日期之间的天数差。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的小时差。
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 小时差
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.HOURS.between(start, end);
    }

    /**
     * 判断指定日期时间是否在当前时间之前。
     *
     * @param dateTime 待判断的日期时间
     * @return true 表示已过期
     */
    public static boolean isBeforeNow(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * 在指定日期上增加天数。
     *
     * @param date 原始日期
     * @param days 增加的天数
     * @return 新日期
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * 在指定日期时间上增加小时。
     *
     * @param dateTime 原始日期时间
     * @param hours    增加的小时数
     * @return 新日期时间
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }
}
