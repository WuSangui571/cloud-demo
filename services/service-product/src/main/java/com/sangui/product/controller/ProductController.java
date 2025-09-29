package com.sangui.product.controller;


import com.sangui.product.bean.Product;
import com.sangui.product.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 商品的 Controller
 * @Version: 1.0
 */
@RestController
public class ProductController {
    @Resource
    private ProductService productService;

    // 查询商品
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId){
        return productService.getProductById(productId);
    }
}
