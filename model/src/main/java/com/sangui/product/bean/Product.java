package com.sangui.product.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 商品类
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private BigDecimal price;
    private String productName;
    private Integer number;
}
