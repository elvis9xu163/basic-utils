package com.xjd.utils.basic.annotation;

import java.lang.annotation.*;

/**
 * @author elvis.xu
 * @since 2017-11-23 17:31
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Describes {
	Describe[] value() default {};
}
