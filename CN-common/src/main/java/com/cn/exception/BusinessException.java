package com.cn.exception;

/**
 * 业务异常 — 由 GlobalExceptionHandler 统一转换为对应的 HTTP 状态码 + Result 响应体。
 * <p>
 * 用法：在 Controller / Service 中直接 throw，无需手动构建错误响应。
 * <pre>
 *     throw new BusinessException(401, "用户名或密码错误");
 *     throw new BusinessException(404, "职位不存在");
 *     throw new BusinessException(400, "参数校验失败");
 * </pre>
 */
public class BusinessException extends RuntimeException {

    /** HTTP 状态码（如 400 / 401 / 403 / 404 / 500） */
    private final int statusCode;

    public BusinessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
