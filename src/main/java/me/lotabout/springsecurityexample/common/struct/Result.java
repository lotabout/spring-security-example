package me.lotabout.springsecurityexample.common.struct;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Result is modeled after {@code Optional} that contains errorCode, message and other stuff.
 * {@code errorCode == 0} means success and {@code errorCode < 0} are reserved for internal usage.
 *
 * A {@code Result}'s status could be checked by {@code isSuccess()}.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -3761201947076186117L;

    private static final Result<?> EMPTY = new Result<>(false, -1, "", null, null, null);

    private boolean success;
    private int errorCode;
    private String message;
    private String description;
    private T data;
    private Object errorData;

    /**
     * Return an empty {@code Result} instance, It is an error with errorCode -1 and empty message.
     *
     * @param <T> Type of non-existent value
     * @return an empty {@code Result}
     * @apiNote Normally you should not use this.
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> empty() {
        return (Result<T>) EMPTY;
    }

    /**
     * @param <T> the type of the data
     * @param data could be null, the success value to be passed on
     * @return {@code Result} instance containing the success data
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(true, 0, null, null, data, null);
    }

    /**
     * @return {@code Result} instance containing the success data
     */
    public static Result ok() {
        return new Result(true, 0, null, null, null, null);
    }

    /**
     * Return an {@code Result} representing Error (with errorCode and error message)
     *
     * @param errorCode should > 0, the error code
     * @param errorMessage the error message, could not be null
     * @param <T> Type of non-existent value
     * @return {@code Result} representing the specified error
     */
    public static <T> Result<T> error(int errorCode, @NonNull String errorMessage) {
        if (errorCode <= 0) {
            throw new IllegalArgumentException(
                    String.format("errorCode should be > 0, got: %d", errorCode));
        }

        return new Result<>(
                false,
                errorCode,
                errorMessage,
                null,
                null,
                null);
    }

    /**
     * Return an {@code Result} representing Error (With the class implement {@code ErrorCode})
     *
     * @param errorCode {@code ErrorCode} instance that contains error code and message.
     * @param <T> Type of non-existent value
     * @return {@code Result} representing the specified error
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> error(@NonNull ErrorCode errorCode) {
        return (Result<T>) error(errorCode.getErrorCode(), errorCode.getErrorMessage())
                .description(errorCode.getDescription());
    }

    /**
     * Return an {@code Result} representing Error (With the {@code ResultException})
     *
     * @param exception {@code ResultException} instance that contains error code and message.
     * @param <T> Type of non-existent value
     * @return {@code Result} representing the specified error
     */
    public static <T> Result<T> of(@NonNull ResultException exception) {
        return new Result<>(
                false,
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getDescription(),
                null,
                exception.getErrorData());
    }

    /**
     * Setter for description, could be used to change description of a {@code Result}
     *
     * @param description the description of an error
     * @return {@code Result} instance with the specified description
     */
    @SuppressWarnings("unchecked")
    public <T> Result<T> description(String description) {
        if (isSuccess()) {
            throw new IllegalStateException(
                    "description() could only be called if the result is not success");
        }
        this.description = description;
        return (Result<T>) this;
    }

    /**
     * Setter for errorData, could be used to change the errorData of a {@code Result}
     *
     * @param errorData the data of an error
     * @return {@code Result} instance with the specified description
     */
    @SuppressWarnings("unchecked")
    public <T> Result<T> errorData(Object errorData) {
        if (isSuccess()) {
            throw new IllegalStateException(
                    "errorData() could only be called if the result is not success");
        }
        this.errorData = errorData;
        return (Result<T>) this;
    }

    /**
     * Return {@code true} if the result is success, otherwise {@code false}
     *
     * @return {@code true} if the result is success, otherwise {@code false}
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Return the error message. If the result is success, return null;
     *
     * @return the error message. If the result is success, return null;
     */
    public String getMessage() {
        return message;
    }

    /**
     * Return the error description. If the result is success, return null;
     *
     * @return the error description. If the result is success, return null;
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the error code. If the result is success, return 0;
     *
     * @return the error code. If the result is success, return 0;
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Return the success data. If the result is success, throw {@code ResultException};
     *
     * @return the data
     * @throws ResultException if the result is not success
     */
    public T getData() {
        if (this.isSuccess()) {
            return this.data;
        }

        throw ResultException.of(this.getErrorCode(), this.getMessage())
                .description(this.getDescription())
                .errorData(this.getErrorData());
    }

    /**
     * Return the error data. If the result is success, return null;
     *
     * @return the error data. If the result is success, return null;
     */
    public Object getErrorData() {
        return errorData;
    }


    /**
     * If the result is success, invoke specified consumer with the data, otherwise do nothing.
     *
     * @param consumer block to be executed if a result is success
     * @throws NullPointerException if result is success and {@code consumer} is null
     */
    public void ifSuccess(Consumer<? super T> consumer) {
        if (isSuccess()) {
            consumer.accept(this.data);
        }
    }

    /**
     * If a value is present, and the value matches the given predicate, return an {@code Optional}
     * describing the value, otherwise return an empty {@code Optional}.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an {@code Optional} describing the value of this {@code Optional} if a value is
     *         present and the value matches the given predicate, otherwise an empty
     *         {@code Optional}
     * @throws NullPointerException if the predicate is null
     */
    public Result<T> filter(@NonNull Predicate<? super T> predicate) {
        if (!isSuccess()) {
            return this;
        } else {
            return predicate.test(this.data) ? this : empty();
        }
    }

    /**
     * If a result is success, apply the provided mapping function to it.
     *
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the success data
     * @return a {@code Result} containing the mapped value if the original result is Success,
     *         otherwise return the original result.
     * @throws NullPointerException if the mapping function is null
     */
    @SuppressWarnings("unchecked")
    public <U> Result<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        if (!isSuccess()) {
            return (Result<U>) this;
        } else {
            return Result.ok(mapper.apply(this.data));
        }
    }

    /**
     * If the result is success, apply the provided mapping function to it, the function will return
     * a new {@Result} instance, that instance is returned.
     *
     * @param <U> The type parameter to the {@code Result} returned by
     * @param mapper mapping function to apply to the success data, if present the mapping function
     * @return the result of applying an {@code Result}-bearing mapping function to success value,
     *         if a value is present, otherwise return the original error.
     * @throws NullPointerException if the mapping function is null or returns a null result
     */
    @SuppressWarnings("unchecked")
    public <U> Result<U> flatMap(@NonNull Function<? super T, Result<U>> mapper) {
        if (!isSuccess()) {
            return (Result<U>) this;
        } else {
            return Objects.requireNonNull(mapper.apply(this.data));
        }
    }

    /**
     * Return the value if Result is success, otherwise return {@code other}.
     *
     * @param other the value to be returned if there the result contains error, may be mull
     * @return the value, if Result is success, otherwise {@code other}
     */
    public T orElse(T other) {
        return isSuccess() ? data : other;
    }

    /**
     * Return the value Result is success, otherwise invoke {@code other} and return the result of
     * that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if Result is not success
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if the Result is false and {@code other} is null
     */
    public T orElseGet(Supplier<? extends T> other) {
        return isSuccess() ? data : other.get();
    }

    /**
     * Return the success data if the Result is success, otherwise throw an exception to be created
     * by the provided supplier.
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to be thrown
     * @return the success value
     * @throws X if the Result is not success
     * @throws NullPointerException if Result is not success and {@code exceptionSupplier} is null
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)
            throws X {
        if (isSuccess()) {
            return data;
        } else {
            throw exceptionSupplier.get();
        }
    }

}

