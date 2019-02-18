package me.lotabout.springsecurityexample.common.struct;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ResultException extends RuntimeException {

    private static final long serialVersionUID = -7706260969045933277L;

    private int errorCode; // 错误代码, 0 成功, -1 未指定, 向后兼容
    private String description;
    private Object errorData;

    private ResultException(int errorCode, @NonNull String message, String description) {
        super(message);
        this.errorCode = errorCode;
        this.description = description;
    }

    private ResultException(int errorCode, @NonNull String message, String description,
                            Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.description = description;
    }

    public static ResultException of(int errorCode, @NonNull String message) {
        return new ResultException(errorCode, message, null);
    }

    public static ResultException of(@NonNull ErrorCode errorCode) {
        return new ResultException(
                errorCode.getErrorCode(),
                errorCode.getErrorMessage(),
                errorCode.getDescription());
    }

    public ResultException description(String description) {
        this.description = description;
        return this;
    }

    public ResultException errorData(Object errorData) {
        this.errorData = errorData;
        return this;
    }

    public ResultException cause(Throwable cause) {
        return new ResultException(
                this.getErrorCode(),
                this.getMessage(),
                this.getDescription(),
                cause);
    }
}

