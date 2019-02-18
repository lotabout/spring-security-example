package me.lotabout.springsecurityexample.common.struct;

import java.lang.annotation.*;

/**
 * Together with {@link ResultGuardAspect}, will turn all the exception into Result
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ResultGuard {

}
