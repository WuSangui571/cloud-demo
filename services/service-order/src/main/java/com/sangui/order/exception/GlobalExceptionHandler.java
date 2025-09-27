package com.sangui.order.exception;


import com.sangui.common.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-27
 * @Description: 全局异常处理器
 * @Version: 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public R handleException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e) throws Exception {
        return R.fail("出现异常");
    }
}
