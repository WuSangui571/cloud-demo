package com.sangui.order.controller;


import com.sangui.order.bean.Order;
import com.sangui.order.properties.OrderProperties;
import com.sangui.order.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description:
 * @Version: 1.0
 */
// @RefreshScope
@RestController
public class OrderController {
    //    @Value("${order.timeout}")
    //    String orderTimeout;
    //    @Value("${order.auto-confirm}")
    //    String orderAutoConfirm;

    @Resource
    OrderProperties orderProperties;

    @Resource
    private OrderService orderService;

    @GetMapping("/create")
    public Order createOrder(@RequestParam("productId") Long productId,
                             @RequestParam("userId") Long userId) {
        return orderService.createOrder(productId, userId);
    }

    @GetMapping("/config")
    public String config() {
        return "orderTimeout=" + orderProperties.getTimeout() + ",orderAutoConfirm=" + orderProperties.getAutoConfirm();
    }
}
