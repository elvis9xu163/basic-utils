package com.xjd.utils.basic;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 内容摘要工具类
 * @author elvis.xu
 * @since 2017-08-16 10:38
 */
public abstract class DigestUtils {

	public static final String ALG_MD5 = "MD5";
	public static final String ALG_SHA1 = "SHA1";
	public static final String ALG_SHA256 = "SHA-256";

	public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


	public static String digest(String text, String algorithm) {
		return digest(text.getBytes(DEFAULT_CHARSET), algorithm);
	}

	public static String digest(byte[] bs, String algorithm) {
		return digest(bs, 0, bs.length, algorithm);
	}

	public static String digest(byte[] bs, int offset, int count, String algorithm) {
		try (ByteArrayInputStream in = new ByteArrayInputStream(bs, offset, count)) {
			return digest(in, algorithm);
		} catch (IOException e) {
			throw new DigestException(e);
		}
	}

	public static String digest(File file, String algorithm) {
		try (FileInputStream in = new FileInputStream(file)) {
			return digest(in, algorithm);
		} catch (IOException e) {
			throw new DigestException(e);
		}
	}

	public static String digest(InputStream in, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] buf = new byte[1024 * 2];
			int c;
			while ((c = in.read(buf)) != -1) {
				if (c > 0) {
					md.update(buf, 0, c);
				}
			}
			byte[] digest = md.digest();
			return toHexString(digest);
		} catch (Exception e) {
			throw new DigestException(e);
		}
	}

	/**
	 * 将字节数组转成16进制字符串
	 * @param bs
	 * @return
	 */
	public static String toHexString(byte[] bs) {
		StringBuilder sb = new StringBuilder(bs.length * 2);
		int b;
		for (int i = 0; i < bs.length; i++) {
			b = bs[i];
			if (b < 0) {
				b += 256;
			}
			if (b < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(b));
		}
		return sb.toString();
	}

	public static class DigestException extends RuntimeException {
		public DigestException() {
		}

		public DigestException(String message) {
			super(message);
		}

		public DigestException(String message, Throwable cause) {
			super(message, cause);
		}

		public DigestException(Throwable cause) {
			super(cause);
		}

		public DigestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
}
