package com.cn.exception;

/**
 * 登录失败异常
 */
public class LoginFailedException extends BusinessException {

    public LoginFailedException(String msg) {
        super(401, msg);
    }

}
