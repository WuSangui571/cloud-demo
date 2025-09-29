package com.sangui.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-29
 * @Description: 每次请求增加一次性令牌的自定义过滤器
 * @Version: 1.0
 */
@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();

            String value = switch (config.getValue().toLowerCase()) {
                case "uuid" -> UUID.randomUUID().toString();
                case "jwt" -> "Test Token";
                default -> "";
            };

            HttpHeaders headers = response.getHeaders();
            headers.add(config.getName(), value);
        }));
    }
}
