package com.xjd.utils.basic;

import java.util.Objects;

/**
 * @author elvis.xu
 * @since 2017-08-25 10:33
 */
public abstract class ObjectUtils {

	@Deprecated
	public static <T> T requireNonNull(T obj, String msg) {
		return Objects.requireNonNull(obj, msg);
	}
	@Deprecated
	public static <T> T requireNonNull(T obj) {
		return Objects.requireNonNull(obj);
	}
	@Deprecated
	public static <T> T  requireArgumentNonNull(T obj, String msg) {
		if (obj == null)
			throw new IllegalArgumentException(msg);
		return obj;
	}
	@Deprecated
	public static <T> T  requireArgumentNonNull(T obj) {
		if (obj == null)
			throw new IllegalArgumentException();
		return obj;
	}
}
