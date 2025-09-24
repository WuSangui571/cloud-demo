package com.sangui.order;


import com.sangui.order.feign.WeatherFeignClient;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-24
 * @Description: openFeign 的第三方 API 的远程调用测试
 * @Version: 1.0
 */
@SpringBootTest
public class WeatherTest {
    @Resource
    WeatherFeignClient weatherFeignClient;

    @Test
    void getWeatherTest() {
        String weather = weatherFeignClient.getWeather("APPCODE 614ca01deaf94b3d8d65b22c9485bb50",
                "284698", "50b53ff8dd7d9fa320d3d3ca32cf8ed1");
        System.out.println("沈阳浑南的天气实况：" + weather);
    }
}
