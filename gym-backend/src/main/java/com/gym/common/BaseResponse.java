package com.gym.common;

import lombok.Data;

/**
 * 统一 API 响应结构。
 *
 * @param <T> 响应数据类型
 * @author liuxinsi
 * @date 2026-05-20
 */
@Data
public class BaseResponse<T> {

    private int code;

    private String message;

    private T data;

    private BaseResponse() {
    }

    /**
     * 创建成功响应。
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.code = 200;
        response.message = "success";
        response.data = data;
        return response;
    }

    /**
     * 创建成功响应（无数据）。
     *
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    /**
     * 创建失败响应。
     *
     * @param errorCode 错误码枚举
     * @param <T>       数据类型
     * @return 失败响应
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        BaseResponse<T> response = new BaseResponse<>();
        response.code = errorCode.getCode();
        response.message = errorCode.getMessage();
        return response;
    }

    /**
     * 创建失败响应（自定义消息）。
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败响应
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.code = code;
        response.message = message;
        return response;
    }
}
