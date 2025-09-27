package com.sangui.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-27
 * @Description: json 工具类，进行 java 对象与 json 字符串之间的相互转化
 * @Version: 1.0
 */
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 把 java 对象转成 json 字符串
     *
     * @param object java 对象
     * @return json 字符串
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把 json 字符串转成 java 对象
     *
     * @param json json 字符串
     * @param clazz 想要转的 java 对象 的类型
     * @return java 对象
     * @param <T> java 对象的类型
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
