package me.lotabout.springsecurityexample.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lotabout.springsecurityexample.common.struct.ErrorCode;

// @formatter:off

@Getter
@AllArgsConstructor
public enum MyError implements ErrorCode {
    ERROR_UNKNOWN(701998, "未定义错误"),
    ERROR_END(701999, "错误结束");

    private int errorCode;
    private String errorMessage;
}

// @formatter:on
