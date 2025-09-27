package com.sangui.order.exception;


import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sangui.common.JsonUtils;
import com.sangui.common.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-27
 * @Description: 我的自定义 web 接口的 Handler
 * @Version: 1.0
 */
@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s, BlockException e) throws Exception {
        httpServletResponse.setStatus(429);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();

        // 自定义返回信息
        R r = R.fail("此服务：" + s + "，被 Sentinel 限制了，原因：" +  e.getClass());
        // 返回 json 格式的数据
        writer.write(JsonUtils.toJson(r));
        writer.flush();
        writer.close();
    }
}
