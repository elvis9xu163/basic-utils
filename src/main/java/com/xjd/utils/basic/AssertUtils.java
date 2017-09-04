package com.xjd.utils.basic;

import java.util.Objects;

/**
 * @author elvis.xu
 * @since 2017-08-29 19:19
 */
public abstract class AssertUtils {

	public static <T> void assertNonNull(T obj, String msg) {
		Objects.requireNonNull(obj, msg);
	}

	public static <T> void assertNonNull(T obj) {
		Objects.requireNonNull(obj);
	}

	public static <T> void assertArgumentNonNull(T obj, String msg) {
		if (obj == null) {
			throw new IllegalArgumentException(msg);
		}
	}

	public static <T> void assertArgumentNonNull(T obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
	}

	public static void assertArgumentGreaterEqualThan(Integer number, int refer, String msg) {
		assertArgumentNonNull(number, msg);
		if (number < refer) {
			throw new IllegalArgumentException(msg);
		}
	}

	public static void assertArgumentGreaterEqualThan(Long number, long refer, String msg) {
		assertArgumentNonNull(number, msg);
		if (number < refer) {
			throw new IllegalArgumentException(msg);
		}
	}

	public static void assertArgumentLessEqualThan(Integer number, int refer, String msg) {
		assertArgumentNonNull(number, msg);
		if (number > refer) {
			throw new IllegalArgumentException(msg);
		}
	}

	public static void assertArgumentLessEqualThan(Long number, long refer, String msg) {
		assertArgumentNonNull(number, msg);
		if (number > refer) {
			throw new IllegalArgumentException(msg);
		}
	}

	public static void assertArgumentNonBlank(CharSequence cs, String msg) {
		if (StringUtils.isBlank(cs)) {
			throw new IllegalArgumentException(msg);
		}
	}
}
