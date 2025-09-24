package com.sangui.order.feign.fallback;


import com.sangui.order.feign.ProductFeignClient;
import com.sangui.product.bean.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-24
 * @Description: ProductFeignClient 接口的 fallback
 * @Version: 1.0
 */
@Component
public class ProductFeignClientFallback implements ProductFeignClient {
    @Override
    public Product getProductById(Long productId) {
        System.out.println("Fallback...");

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("0"));
        product.setProductName("未知商品");
        product.setNumber(0);

        return product;
    }
}
