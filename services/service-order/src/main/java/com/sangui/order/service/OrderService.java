package com.sangui.order.service;


import com.sangui.order.bean.Order;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description:
 * @Version: 1.0
 */
public interface OrderService {
    Order createOrder(Long productId,Long userId);
}
