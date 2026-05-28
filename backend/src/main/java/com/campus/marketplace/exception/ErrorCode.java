package com.campus.marketplace.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 通用错误
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 用户相关 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    EMAIL_ALREADY_EXISTS(1002, "邮箱已被注册"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_LOCKED(1004, "账号已锁定，请稍后再试"),
    ACCOUNT_DISABLED(1005, "账号已被禁用"),
    VERIFY_FAILED(1006, "实名认证失败"),

    // 商品相关 2xxx
    PRODUCT_NOT_FOUND(2001, "商品不存在"),
    PRODUCT_STATUS_ERROR(2002, "商品状态异常"),
    PRODUCT_IMAGES_LIMIT(2003, "商品图片不能超过9张"),

    // 交易相关 3xxx
    TRANSACTION_NOT_FOUND(3001, "交易不存在"),
    TRANSACTION_STATUS_ERROR(3002, "交易状态异常"),

    // 消息相关 4xxx
    CONVERSATION_NOT_FOUND(4001, "会话不存在"),

    // 文件相关 5xxx
    FILE_UPLOAD_ERROR(5001, "文件上传失败"),
    FILE_TYPE_ERROR(5002, "文件类型不支持"),
    FILE_SIZE_ERROR(5003, "文件大小超过限制");

    private final int code;
    private final String message;
}
