package com.sangui.product;


import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 测试服务发现
 * @Version: 1.0
 */
@SpringBootTest
public class DiscoveryTest {
    // 自动注入即可
    @Resource
    DiscoveryClient discoveryClient;

    @Resource
    NacosServiceDiscovery nacosServiceDiscovery;

    @Test
    void nacosDiscoveryClientTest() throws NacosException {
        // 遍历打印所有微服务
        for (String service : nacosServiceDiscovery.getServices()) {
            System.out.println("MyService:" + service);
            /*
            上述打印结果：
            MyService:service-order
            MyService:service-product
             */

            // 获取某一个微服务的所有实例
            List<ServiceInstance> instances = nacosServiceDiscovery.getInstances(service);
            for (ServiceInstance instance : instances) {
                System.out.println("该微服务的名字是：" + service + "，该实例 IP 为：" + instance.getHost() + "，端口号为：" + instance.getPort());
                /*
                上述打印结果：
                该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8001
                该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8000
                该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9000
                 */
            }
        }
    }

    /**
     * 测试 DiscoveryClient
     */
    @Test
    void discoveryClientTest() {
        // 遍历打印所有微服务
        for (String service : discoveryClient.getServices()) {
            System.out.println("MyService:" + service);
            /*
            上述打印结果：
            MyService:service-order
            MyService:service-product
             */

            // 获取某一个微服务的所有实例
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                System.out.println("该微服务的名字是：" + service + "，该实例 IP 为：" + instance.getHost() + "，端口号为：" + instance.getPort());
                /*
                上述打印结果：
                该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8001
                该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8000
                该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9000
                 */
            }
        }
    }
}
