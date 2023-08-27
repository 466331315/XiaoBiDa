package com.zjs.waimai.common;

/**
 * 自定义异常类，用来处理删除分类时的关联错误
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
