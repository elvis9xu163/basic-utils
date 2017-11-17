package com.xjd.utils.basic;

import java.io.PrintWriter;
import java.io.StringWriter;

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

		return new WrappedRuntimeException(e);
	}

	public static void throwRuntime(Throwable t) {
		if (t == null) return;

		if (t instanceof Error) {
			throw (Error) t;
		}

		throw runtime((Exception) t);
	}

	public static String printStackTrace(Throwable t) {
		if (t == null) return null;

		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

	public static class WrappedRuntimeException extends RuntimeException {
		public WrappedRuntimeException(Throwable cause) {
			super("wrapped runtime exception for " + cause.getClass(), cause);
		}
	}
}
