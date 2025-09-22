package com.sangui.order;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-21
 * @Description: 订单微服务的主入口程序
 * @Version: 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);

        // 1.项目启动就去监听配置文件的变化
        // 2.发生变化后就拿到变化值
        // 3.发送邮件
    }
}
