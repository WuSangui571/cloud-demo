package com.sangui.order.bean;


import com.sangui.product.bean.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 订单的实体类
 * @Version: 1.0
 */
@Data
public class Order {
    private Long id;
    private BigDecimal totalAmount;
    private Long userId;
    private String nickName;
    private String address;
    private List<Product> productList;
}
