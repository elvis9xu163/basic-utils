package com.xjd.utils.basic.exception;

import java.io.IOException;

/**
 * IOException runtime wrapper
 * @author elvis.xu
 * @since 2017-11-14 15:36
 */
public class RuntimeIOException extends RuntimeException {
	public RuntimeIOException(IOException cause) {
		super(cause.getMessage(), cause);
	}
}
