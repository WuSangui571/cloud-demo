package com.sangui.order.config;


import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class OrderServiceConfig {
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Logger.Level feignlogLevel() {
        // 指定 OpenFeign 发请求时，日志级别为 FULL
        return Logger.Level.FULL;
    }
}
