package me.lotabout.springsecurityexample.common.struct;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {

    private boolean success; // 调用成功与否
    private int error; // 错误代码, 0 为 成功，-1 未指定，向后兼容
    private String message; // 简短的错误说明，每个 message 固定
    private String description; // 具体的错误说明，如错误发生的数据，位置等
    private T data; // 成功时携带的数据
    private Object errorData; // 失败时携带的数据

    public static <T> Response<T> ok(T data) {
        return new Response<>(true, 0, null, null, data, null);
    }

    public static Response ok() {
        return new Response<>(true, 0, null, null, null, null);
    }

    public static <T> Response<T> error(int errorCode, @NonNull String errorMessage) {
        if (errorCode <= 0) {
            throw new IllegalArgumentException(
                    String.format("errorCode should be > 0, got: %d", errorCode));
        }

        return new Response<>(false, errorCode, errorMessage, null, null, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> Response<T> error(@NonNull ErrorCode errorCode) {
        return (Response<T>) error(errorCode.getErrorCode(), errorCode.getErrorMessage())
                .description(errorCode.getDescription());
    }

    public static <T> Response<T> of(@NonNull ResultException exception) {
        return new Response<>(
                false,
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getDescription(),
                null,
                exception.getErrorData());
    }

    public static <T> Response<T> of(@NonNull Result<T> result) {
        if (result.isSuccess()) {
            return Response.ok(result.getData());
        } else {
            return new Response<>(false, result.getErrorCode(), result.getMessage(),
                    result.getDescription(), null, result.getErrorData());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Response<T> description(String description) {
        if (isSuccess()) {
            throw new IllegalStateException(
                    "description() could only be called if the result is not success");
        }
        this.description = description;
        return (Response<T>) this;
    }

    @SuppressWarnings("unchecked")
    public <T> Response<T> errorData(Object errorData) {
        if (isSuccess()) {
            throw new IllegalStateException(
                    "description() could only be called if the result is not success");
        }
        this.errorData = errorData;
        return (Response<T>) this;
    }
}
