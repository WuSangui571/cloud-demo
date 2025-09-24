package com.sangui.order.feign;


import com.sangui.order.feign.fallback.ProductFeignClientFallback;
import com.sangui.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-24
 * @Description: 调用商品服务的用 Feign 实现的远程调用客户端接口
 * @Version: 1.0
 */
// 标记这是个 Feign 客户端，里面的 value 的值表示要远程调用的微服务的名字
// fallback 里写的就是兜底的数据返回
@FeignClient(value = "service-product", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {
    // 和 Controller 中的注解一样，只不过这里是主动发送，在 Controller 中是接收
    @GetMapping("/product/{productId}")
    Product getProductById(@PathVariable("productId") Long productId);
}
