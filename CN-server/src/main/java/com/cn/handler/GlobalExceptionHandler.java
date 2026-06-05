package com.cn.handler;

import com.cn.constant.MessageConstant;
import com.cn.exception.AccountNotFoundException;
import com.cn.exception.BusinessException;
import com.cn.exception.DeletionNotAllowedException;
import com.cn.exception.LoginFailedException;
import com.cn.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器 — 将所有异常统一转换为 {@link Result} + 正确的 HTTP 状态码。
 * <p>
 * Controller 只需正常返回 Result.success() 或直接 throw BusinessException，
 * 不再需要手动构建错误响应。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常 → 按其 statusCode 返回对应的 HTTP 状态
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常 [{}]: {}", e.getStatusCode(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode())
                .body(Result.error(e.getMessage()));
    }

    /**
     * 登录失败
     */
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<Result<Void>> handleLoginFailedException(LoginFailedException e) {
        log.warn("登录失败: {}", e.getMessage());
        return ResponseEntity.status(401).body(Result.error(e.getMessage()));
    }

    /**
     * 账号不存在
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Result<Void>> handleAccountNotFoundException(AccountNotFoundException e) {
        log.warn("账号不存在: {}", e.getMessage());
        return ResponseEntity.status(404).body(Result.error(e.getMessage()));
    }

    /**
     * 删除被拒绝
     */
    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<Result<Void>> handleDeletionNotAllowedException(DeletionNotAllowedException e) {
        log.warn("删除被拒绝: {}", e.getMessage());
        return ResponseEntity.status(403).body(Result.error(e.getMessage()));
    }

    /**
     * 捕获 SQL 唯一约束异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String name = split[2];
            return ResponseEntity.status(400).body(Result.error(name + MessageConstant.ALREADY_EXIST));
        }
        return ResponseEntity.status(500).body(Result.error(MessageConstant.UNKNOWN_ERROR));
    }

    /**
     * 未预料的异常 → HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResponseEntity.status(500)
                .body(Result.error("服务器内部错误"));
    }
}
