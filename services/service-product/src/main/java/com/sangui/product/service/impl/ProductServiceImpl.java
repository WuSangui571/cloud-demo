package com.sangui.product.service.impl;


import com.sangui.product.bean.Product;
import com.sangui.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 商品的 Service 类 的实现类
 * @Version: 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long productId) {
        System.out.println("是我");
        // 暂且模拟数据库
        return new Product(productId,new BigDecimal("32.5"),"创可贴",2);
    }
}
