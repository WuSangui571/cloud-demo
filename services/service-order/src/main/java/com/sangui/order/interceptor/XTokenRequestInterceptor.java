package com.sangui.order.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-24
 * @Description: 请求拦截器，用于增加请求头的 XToken
 * @Version: 1.0
 */
@Component
public class XTokenRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        System.out.println("XTokenRequestInterceptor execute!");
        // 这里的 template 即为这次请求的封装
        template.header("X-Token", UUID.randomUUID().toString());
    }
}
