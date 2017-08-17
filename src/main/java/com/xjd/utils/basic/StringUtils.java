package com.xjd.utils.basic;

/**
 * @author elvis.xu
 * @since 2017-08-16 16:07
 */
public abstract class StringUtils {

	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	public static String trimToNull(String str) {
		String trimStr = trim(str);
		return trimStr == null || trimStr.length() == 0 ? null : trimStr;
	}

	public static String trimToEmpty(String str) {
		String trimStr = trim(str);
		return trimStr == null || trimStr.length() == 0 ? null : trimStr;
	}
}
