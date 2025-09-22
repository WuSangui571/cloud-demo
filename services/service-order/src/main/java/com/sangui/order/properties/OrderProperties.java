package com.sangui.order.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 订单的配置文件类
 * @Version: 1.0
 */
// 批量绑定的前缀
@ConfigurationProperties(prefix = "order")
@Component
@Data
public class OrderProperties {

    String timeout;
    // 配置文件中是 auto-confirm 也没关系，会自动驼峰映射成 autoConfirm
    String autoConfirm;
}
