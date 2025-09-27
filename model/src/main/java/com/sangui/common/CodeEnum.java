package com.sangui.common;


import lombok.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-27
 * @Description: R 状态的枚举类，返回给前端的 Code 枚举类。包含两个属性，code 和 msg
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public enum CodeEnum {
    OK(200,"成功"),
    FAIL(500,"失败"),
    TOKEN_IS_EMPTY(901,"登录请求 Token 为空"),
    TOKEN_IS_ERROR(902,"登录请求 Token 有误"),
    TOKEN_IS_EXPIRED(903,"登录请求 Token 已过期"),
    TOKEN_IS_NONE_MATCH(904,"登录请求 Token 不匹配"),
    USER_LOGOUT(200, "退出成功"),
    DATA_ACCESS_EXCEPTION(500,"数据库操作失败"),
    ACCESS_DENIED(500,"权限不足"),
    ;

    /**
     * 结果码
     */
    private int code;

    /**
     * 结果信息
     */
    @NonNull
    private String msg;

}