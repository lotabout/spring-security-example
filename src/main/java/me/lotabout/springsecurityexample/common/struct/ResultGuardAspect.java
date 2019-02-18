package me.lotabout.springsecurityexample.common.struct;

import me.lotabout.springsecurityexample.common.MyError;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Intercept all exceptions of a method that returns {@link Result} and wrap exceptions to Result.
 */

@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 200)
public class ResultGuardAspect {

    @Around(value = "(@within(me.lotabout.springsecurityexample.common.struct.ResultGuard) || @annotation(me.lotabout.springsecurityexample.common.struct.ResultGuard)) && execution(me.lotabout.springsecurityexample.common.struct.Result *(..))")
    public Object wrapException2Result(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (ResultException ex) {
            return Result.of(ex);
        } catch (Exception ex) {
            return Result.error(MyError.ERROR_UNKNOWN)
                    .description(ex.getMessage())
                    .errorData(ex.getCause());
        }
    }
}

