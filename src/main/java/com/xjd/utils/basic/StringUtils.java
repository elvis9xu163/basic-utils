package com.xjd.utils.basic;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

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

	public static String[] split(String txt, String regex) {
		if (txt == null) return new String[0];
		return txt.split(regex, -1); // 默认使用-1
	}

	public static String[] trimElement(String[] strArray) {
		return map(strArray, StringUtils::trim);
	}

	public static String[] trimElementToNull(String[] strArray) {
		return map(strArray, StringUtils::trimToNull);
	}

	public static String[] trimElementToEmpty(String[] strArray) {
		return map(strArray, StringUtils::trimToEmpty);
	}

	public static String[] toLowerCase(String[] strArray) {
		return map(strArray, StringUtils::toLowerCase);
	}

	public static String[] toUpperCase(String[] strArray) {
		return map(strArray, StringUtils::toUpperCase);
	}

	public static String[] trimNull(String[] strArray) {
		return filter(strArray, s -> s != null);
	}

	public static String[] trimEmpty(String[] strArray) {
		return filter(strArray, s -> !isEmpty(s));
	}

	public static String[] trimBlank(String[] strArray) {
		return filter(strArray, s -> !isBlank(s));
	}

	public static String[] map(String[] strArray, Function<String, String> mapper) {
		if (strArray != null && strArray.length > 0) {
			return Arrays.stream(strArray).map(mapper).toArray(String[]::new);
		}
		return new String[0];
	}

	public static String[] filter(String[] strArray, Predicate<String> predicate) {
		if (strArray != null && strArray.length > 0) {
			return Arrays.stream(strArray).filter(predicate).toArray(String[]::new);
		}
		return new String[0];
	}

}
