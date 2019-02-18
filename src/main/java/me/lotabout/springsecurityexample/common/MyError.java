package me.lotabout.springsecurityexample.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lotabout.springsecurityexample.common.struct.ErrorCode;

// @formatter:off

@Getter
@AllArgsConstructor
public enum MyError implements ErrorCode {
    TOKEN_NOT_FOUND(700026, "密钥不正确"),
    LOGIN_ERROR(700027, "登录失败"),
    LOGIN_ACCOUNT_DISABLED(700028, "帐户失效"),
    LOGIN_BAD_CREDENTIAL(700029, "用户名或密码错误"),
    ERROR_UNKNOWN(701998, "未定义错误"),
    ERROR_END(701999, "错误结束");

    private int errorCode;
    private String errorMessage;
}

// @formatter:on
