package com.sangui.order.service.impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sangui.order.bean.Order;
import com.sangui.order.feign.ProductFeignClient;
import com.sangui.order.service.OrderService;
import com.sangui.product.bean.Product;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    ProductFeignClient productFeignClient;

    @Resource
    LoadBalancerClient loadBalancerClient;

    @Resource
    DiscoveryClient discoveryClient;

    @Resource
    RestTemplate restTemplate;

    // 加入注解，标记为一个资源，取名为：createOrder
    @SentinelResource(value="createOrder",blockHandler = "createOrderFallback")
    @Override
    public Order createOrder(Long productId, Long userId) {
        // 之前的调用远程获取商品的方法
        // Product product = getProductFromRemoteWithLoadBalanceAnnotation(productId);
        // 调用 openFeign 远程获取商品的方法
        Product product = productFeignClient.getProductById(productId);

        Order order = new Order();
        order.setId(1011L);

        // 使用商品获取总金额
        BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(product.getNumber()));
        order.setTotalAmount(totalAmount);

        order.setUserId(userId);
        order.setNickName("张三");
        order.setAddress("beijing");

        // 放入商品列表，我们模拟阶段，只放一个商品，修改 Order 实体类中的 productList 中 List 的数据类型为 Product
        order.setProductList(Arrays.asList(product));
        return order;
    }
    // createOrder 的兜底回调
    public Order createOrderFallback(Long productId, Long userId, BlockException e) {
        Order order = new Order();
        order.setId(-1L);
        order.setTotalAmount(new BigDecimal("0.00"));
        order.setUserId(userId);
        order.setNickName("未知用户");
        order.setAddress("未知地址，" + e.getClass());
        return order;
    }

    /**
     * 远程获取商品信息
     * @param productId 商品 Id
     * @return 商品信息
     */
    private Product getProductFromRemote(Long productId) {
        // 1、获取商品服务的所有实例，实例里包括其 IP + port
        List<ServiceInstance> serviceProductInstances = discoveryClient.getInstances("service-product");

        // 选择第一个实例
        ServiceInstance serviceProductInstance0 = serviceProductInstances.get(0);

        ServiceInstance serviceProductInstance = loadBalancerClient.choose("service-product");
        // 确定 url
        String url = "http://" + serviceProductInstance0.getHost() + ":" + serviceProductInstance0.getPort() + "/product/" + productId;
        // 上述 url 为 http://localhost:9000/product/{productId}

        // 2、发送远程请求
        log.info("远程请求 url:{}", url);
        return restTemplate.getForObject(url, Product.class);
    }

    /**
     * 负载均衡得远程获取商品信息
     * @param productId 商品 Id
     * @return 商品信息
     */
    private Product getProductFromRemoteWithLoadBalance(Long productId) {
        // 1、负载均衡得获取商品服务的所有实例，实例里包括其 IP + port
        ServiceInstance serviceProductInstance = loadBalancerClient.choose("service-product");

        // 确定 url
        String url = "http://" + serviceProductInstance.getHost() + ":" + serviceProductInstance.getPort() + "/product/" + productId;
        // 上述 url 为 http://localhost:9000/product/{productId}

        // 2、发送远程请求
        log.info("远程请求 url:{}", url);
        return restTemplate.getForObject(url, Product.class);
    }

    /**
     * 基于注解的负载均衡远程获取商品信息
     * @param productId 商品 Id
     * @return 商品信息
     */
    private Product getProductFromRemoteWithLoadBalanceAnnotation(Long productId) {

        // 确定 url，这里直接写 service-product 这个微服务名就好，restTemplate 会自动负载均衡对应的微服务
        String url = "http://service-product/product/" + productId;
        // 上述 url 为 http://localhost:????/product/{productId}

        // 2、发送远程请求
        return restTemplate.getForObject(url, Product.class);
    }




}
