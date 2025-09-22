package com.sangui.order;


import lombok.Value;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 导入配置的测试类
 * @Version: 1.0
 */
@SpringBootTest
public class ConfigImportTest {
    @Test
    void testConfigImport() {
        @Value("${order.timeout}")
        String orderTimeout;
        @Value("${order.auto-confirm}")
        String orderAutoConfirm;
    }
}
