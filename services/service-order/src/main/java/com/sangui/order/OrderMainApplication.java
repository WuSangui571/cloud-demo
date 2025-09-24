package com.sangui.order;


import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-21
 * @Description: 订单微服务的主入口程序
 * @Version: 1.0
 */
// 开启 Feign 的远程调用
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OrderMainApplication {
    /**
     * 我们的整个 web 程序启动后，就会执行这个 applicationRunner 里面的方法。
     * @param nacosConfigManager 注入的对象，用于配置监听
     * @return 具体的方法
     */
    @Bean
    ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager) {
        // 1.项目启动就去监听配置文件的变化
        return args->{
            // System.out.println("============");

            // 拿到配置服务
            ConfigService configService = nacosConfigManager.getConfigService();
            // 添加监听器，监听 yaml 配置文件，注意，这里也可以监控非 Nacos 的其他配置文件
            configService.addListener("service-order.yaml", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);
                }

                @Override
                public void receiveConfigInfo(String s) {
                    // 2.发生变化后就拿到变化值
                    System.out.println("service-order.yaml 中改变了，改变内容为：" + s);
                    // 3.发送邮件
                    System.out.println("邮件通知（模拟）......");
                }
            });
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }
}
