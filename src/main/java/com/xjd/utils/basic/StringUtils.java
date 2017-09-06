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
		return trimStr == null ? "" : trimStr;
	}

	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	public static boolean isBlank(CharSequence str) {
		if (isEmpty(str)) return true;

		for (int i = 0, len = str.length(); i < len; i++) {
			if (str.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	public static String toLowerCase(String str) {
		return str == null ? str : str.toLowerCase();
	}

	public static String toUpperCase(String str) {
		return str == null ? str : str.toUpperCase();
	}
}
