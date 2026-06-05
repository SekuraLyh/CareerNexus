package com.cn.exception;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException() {
        super(404, "账号不存在");
    }

    public AccountNotFoundException(String msg) {
        super(404, msg);
    }

}
