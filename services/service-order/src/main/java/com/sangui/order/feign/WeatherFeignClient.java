package com.sangui.order.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-24
 * @Description: 远程调用第三方天气 API 的 Feign 客户端接口
 * @Version: 1.0
 */
// 第三方 API 中，并没有微服务名字，所以 value 值随意写，但是要写请求地址 url
// 使用 HTTP 协议，因为经测试，目标端点 aliv18.data.moji.com 的 HTTPS 证书缺少 SAN 匹配
@FeignClient(value = "weather-client", url = "http://aliv18.data.moji.com")
public interface WeatherFeignClient {
    /**
     *
     * @param auth 在 Header 中添加 Authorization 值，该值是实际就是 API 的密匙
     * @param cityId 可根据文档查询对应城市的 cityId，查找后，沈阳浑南的 cityId 为 284698
     * @param token 固定值，为：50b53ff8dd7d9fa320d3d3ca32cf8ed1
     * @return 该 city 的实况天气的 json 数据
     */
    @PostMapping("/whapi/json/alicityweather/condition")
    String getWeather(@RequestHeader("Authorization") String auth,
                           @RequestParam("cityId") String cityId,
                           @RequestParam("token") String token);

}
