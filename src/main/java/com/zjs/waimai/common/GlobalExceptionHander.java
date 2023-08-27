package com.zjs.waimai.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})//表示拦截哪些类型的controller注解
public class GlobalExceptionHander {
    /**
     * 异常处理方法
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exception(SQLIntegrityConstraintViolationException ex) {
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] spilt = ex.getMessage().split(" ");
            String msg = spilt[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");

    }

    @ExceptionHandler(CustomException.class)
    public R<String> exception(CustomException ex) {
        log.info(ex.getMessage());

        return R.error(ex.getMessage());

    }

}
