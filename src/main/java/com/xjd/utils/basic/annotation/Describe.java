package com.xjd.utils.basic.annotation;

import java.lang.annotation.*;

/**
 * @author elvis.xu
 * @since 2017-11-23 17:10
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
@Repeatable(value = Describes.class)
public @interface Describe {
	String value() default "";
}
