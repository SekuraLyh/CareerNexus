package com.cn.exception;

/**
 * 删除被拒绝异常（如：有关联数据不允许删除）
 */
public class DeletionNotAllowedException extends BusinessException {

    public DeletionNotAllowedException(String msg) {
        super(403, msg);
    }

}
