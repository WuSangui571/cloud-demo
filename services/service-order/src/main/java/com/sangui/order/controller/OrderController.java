package com.sangui.order.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sangui.order.bean.Order;
import com.sangui.order.properties.OrderProperties;
import com.sangui.order.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description:
 * @Version: 1.0
 */
// @RefreshScope
@RequestMapping("/api/order")
@Slf4j
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
    // 写数据到数据库
    @GetMapping("/writeDb")
    public String writeDb() {
        return "writeDb success";
    }

    // 读数据
    @GetMapping("/readDb")
    public String read() {
        return "readDb success";
    }


    // 秒杀订单
    @GetMapping("/seckill")
    // 设置 SentinelResource ，名字需设计的和上面的 /seckill 不一样
    @SentinelResource(value = "seckill-order",fallback = "seckillFallback")
    public Order seckill(@RequestParam("productId") Long productId,
                             @RequestParam("userId") Long userId) {
        Order order = orderService.createOrder(productId, userId);
        order.setId(Long.MAX_VALUE);
        return order;
    }


    // 秒杀订单的兜底回调
    public Order seckillFallback(@RequestParam("productId") Long productId,
                                 @RequestParam("userId") Long userId, BlockException e) {
        Order order = new Order();
        order.setId(10086L);
        order.setAddress(e.getClass().getName());
        System.out.println("我是 seckill 的兜底回调");
        return order;
    }

    @GetMapping("/config")
    public String config() {
        return "orderTimeout=" + orderProperties.getTimeout() + ",orderAutoConfirm=" + orderProperties.getAutoConfirm();
    }
}
