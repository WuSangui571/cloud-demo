package com.sangui.product.service;


import com.sangui.product.bean.Product;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 商品的 Service 类
 * @Version: 1.0
 */
public interface ProductService {
    Product getProductById(Long productId);
}
