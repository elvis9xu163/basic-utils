package com.xjd.utils.basic;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author elvis.xu
 * @since 2017-11-17 10:11
 */
public abstract class IOUtils {
	public static final int BUFFER_SIZE = 4096;

	public static InputStream asStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	public static InputStream asStream(byte[] bytes, int offset, int length) {
		return bytes == null ? null : new ByteArrayInputStream(bytes, offset, length);
	}

	public static InputStream asStream(CharSequence chars, Charset charset) {
		return chars == null ? null : asStream(chars.toString().getBytes(charset));
	}

	public static InputStream asStream(CharSequence chars, int offset, int length, Charset charset) {
		if (chars == null) return null;
		String s = chars.subSequence(offset, Math.min(offset + length, chars.length())).toString();
		return asStream(s.getBytes(charset));
	}

	public static byte[] asBytes(InputStream in, long offset, int length) {
		if (in == null) return null;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream(in.available());
		} catch (IOException e) {
			ExceptionUtils.throwRuntime(e);
		}
		copy(in, out, offset, length);
		return out.toByteArray();
	}

	public static byte[] asBytes(InputStream in) {
		if (in == null) return null;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream(in.available());
		} catch (IOException e) {
			ExceptionUtils.throwRuntime(e);
		}
		copy(in, out);
		return out.toByteArray();
	}

	public static String asString(InputStream in, Charset charset) {
		return in == null ? null : new String(asBytes(in), charset);
	}

	public static long copy(InputStream in, OutputStream out) {
		if (in == null || out == null) return -1;
		long count = 0;
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				if (bytesRead == 0) continue; // 加速
				out.write(buffer, 0, bytesRead);
				count += bytesRead;
			}
			out.flush();
		} catch (IOException e) {
			ExceptionUtils.throwRuntime(e);
		}
		return count;
	}

	public static long copy(InputStream in, OutputStream out, long offset, long length) {
		if (in == null || out == null) return -1;
		try {
			long skipped = in.skip(offset);
			if (skipped < offset) {
				throw new IOException("skipped only " + skipped + " bytes out of " + offset + " required.");
			}
			long bytesToCopy = length;
			byte[] buffer = new byte[BUFFER_SIZE];
			while (bytesToCopy > 0) {
				int bytesRead = in.read(buffer);
				if (bytesRead == -1) {
					break;
				}
				if (bytesRead == 0) {
					continue; // 加速
				}
				if (bytesRead <= bytesToCopy) {
					out.write(buffer, 0, bytesRead);
					bytesToCopy -= bytesRead;
				} else {
					out.write(buffer, 0, (int) bytesToCopy);
					bytesToCopy = 0;
				}
			}
			return length - bytesToCopy;
		} catch (IOException e) {
			ExceptionUtils.throwRuntime(e);
		}
		return -1;
	}

	public static void copy(byte[] in, OutputStream out, int offset, int length) {
		if (in == null || out == null) return;
		try {
			out.write(in, offset, length);
		} catch (IOException e) {
			ExceptionUtils.throwRuntime(e);
		}
	}

	public static void copy(byte[] in, OutputStream out) {
		if (in == null || out == null) return;
		try {
			out.write(in);
		} catch (IOException e) {
			ExceptionUtils.throwRuntime(e);
		}
	}

	public static int copy(CharSequence in, OutputStream out, int offset, int length, Charset charset) {
		if (in == null || out == null) return -1;
		byte[] bytes = in.subSequence(offset, Math.min(offset + length, in.length())).toString().getBytes(charset);
		copy(bytes, out);
		return bytes.length;
	}

	public static int copy(CharSequence in, OutputStream out, Charset charset) {
		if (in == null || out == null) return -1;
		byte[] bytes = in.toString().getBytes(charset);
		copy(bytes, out);
		return bytes.length;
	}
}
