package com.sangui.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-27
 * @Description: 统一封装 web 层向前端页面返回的结果
 * @Version: 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class R {
    /**
     * 表示返回的结果码，比如 200 成功，500 失败
     */
    private int code;

    /**
     * 表示返回的结果信息，比如用户登录状态失效了，请求参数格式有误等
     */
    private String msg;

    /**
     * 表示返回的结果数据，数据可能是一个对象，也可以是一个 List 集合等
     */
    private Object data;

    /**
     * 此方法返回给前端的信息为：code：200，msg：成功
     * @return R 实体
     */
    public static R ok() {
        return R.builder()
                .code(CodeEnum.OK.getCode())
                .msg(CodeEnum.OK.getMsg())
                .build();
    }

    /**
     * 此方法返回给前端的信息为：code：{自定义}，msg：{自定义}
     * @param code 结果码
     * @param msg 结果信息
     * @return R 实体
     */
    public static R ok(int code, String msg) {
        return R.builder()
                .code(code)
                .msg(msg)
                .build();
    }

    /**
     * 此方法返回给前端的信息为：code：200，msg：成功，data：{自定义}
     * @param data 结果数据
     * @return R 实体
     */
    public static R ok(Object data) {
        return R.builder()
                .code(CodeEnum.OK.getCode())
                .msg(CodeEnum.OK.getMsg())
                .data(data)
                .build();
    }

    /**
     * 此方法返回给前端的信息为：code：{自定义}，msg：{自定义}
     * @param codeEnum Code 枚举类
     * @return R 实体
     */
    public static R ok(CodeEnum codeEnum) {
        return R.builder()
                .code(codeEnum.getCode())
                .msg(codeEnum.getMsg())
                .build();
    }

    /**
     * 此方法返回给前端的信息为：code：500，msg：失败
     * @return R 实体
     */
    public static R fail() {
        return R.builder()
                .code(CodeEnum.FAIL.getCode())
                .msg(CodeEnum.FAIL.getMsg())
                .build();
    }

    /**
     * 此方法返回给前端的信息为：code：500，msg：{自定义}
     * @param msg 结果信息
     * @return R 实体
     */
    public static R fail(String msg) {
        return R.builder()
                .code(CodeEnum.FAIL.getCode())
                .msg(msg)
                .build();
    }

    /**
     * 此方法返回给前端的信息为：code：{自定义}，msg：{自定义}
     * @param codeEnum Code 枚举类
     * @return R 实体
     */
    public static R fail(CodeEnum codeEnum) {
        return R.builder()
                .code(codeEnum.getCode())
                .msg(codeEnum.getMsg())
                .build();
    }
}
