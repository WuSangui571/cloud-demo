package com.sangui.order;


import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 负载均衡的测试
 * @Version: 1.0
 */
@SpringBootTest
public class LoadBalancerTest {
    @Resource
    private LoadBalancerClient loadBalancerClient;

    @Test
    void loadBalancerTest() {
        for (int i = 0; i < 10; i++) {
            ServiceInstance serviceInstance = loadBalancerClient.choose("service-product");
            System.out.println("该实例 IP 为：" + serviceInstance.getHost() + "，端口号为：" + serviceInstance.getPort());
        }

    }
}
