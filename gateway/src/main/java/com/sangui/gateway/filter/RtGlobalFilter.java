package com.sangui.gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-29
 * @Description: 获取每次请求的响应时间的全局过滤器，通过 请求后的时间 减去 请求开始的时间 获取
 * @Version: 1.0
 */
@Slf4j
@Component
// 实现 GlobalFilter 以实现全局过滤，再可选实现 ordered 调整多个全局过滤器的执行顺序
public class RtGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().toString();
        long start = System.currentTimeMillis();
        log.info("请求 [{}] 开始，时间：{}", uri, start);
        //System.out.println("请求 [" + uri + "] 开始，时间：" + start);
        return chain.filter(exchange)
                .doFinally(res -> {
                    long end = System.currentTimeMillis();
                    log.info("请求 [{}] 结束，时间：{}，耗时：{}ms", uri, start, end - start);
                    //System.out.println("请求 [" + uri + "] 结束，时间：" + start + "，耗时：" + (end - start) + "ms" + start);
                });
    }

    @Override
    public int getOrder() {
        // 决定过滤器的顺序
        return 0;
    }
}
