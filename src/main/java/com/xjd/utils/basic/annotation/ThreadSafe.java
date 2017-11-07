package com.xjd.utils.basic.annotation;

import java.lang.annotation.*;

/**
 * The class to which this annotation is applied is thread-safe.
 * @author elvis.xu
 * @since 2017-11-07 10:40
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafe {
}

