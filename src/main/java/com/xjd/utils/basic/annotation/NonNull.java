package com.xjd.utils.basic.annotation;

import java.lang.annotation.*;

/**
 * @author elvis.xu
 * @since 2017-11-23 10:51
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonNull {
}
