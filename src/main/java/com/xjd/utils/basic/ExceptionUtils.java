package com.xjd.utils.basic;

import java.io.IOException;

import com.xjd.utils.basic.exception.RuntimeIOException;

/**
 * @author elvis.xu
 * @since 2017-11-14 15:39
 */
public abstract class ExceptionUtils {
	public static RuntimeException runtime(Exception e) {
		if (e == null) return null;

		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}

		if (e instanceof IOException) {
			return new RuntimeIOException((IOException) e);
		}

		return new RuntimeException(e.getMessage(), e);
	}

	public static void throwRuntime(Throwable t) {
		if (t == null) return;

		if (t instanceof Error) {
			throw (Error) t;
		}

		if (t instanceof Exception) {
			throw runtime((Exception) t);
		}

		throw new RuntimeException(t.getMessage(), t);
	}
}
