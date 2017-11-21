package com.xjd.utils.basic;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
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

	public static boolean isNotBlank(CharSequence str) {
		return !isBlank(str);
	}

	public static String toLowerCase(String str) {
		return str == null ? str : str.toLowerCase();
	}

	public static String toUpperCase(String str) {
		return str == null ? str : str.toUpperCase();
	}

	public static boolean isEqual(String str1, String str2) {
		if (str1 == str2) return true;
		if (str1 == null || str2 == null) return false;
		return str1.equals(str2);
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

	public static String[] map(String[] strArray, Function<String, String>... mappers) {
		if (strArray != null && strArray.length > 0) {
			if (mappers == null || mappers.length == 0) {
				String[] newArray = new String[strArray.length];
				System.arraycopy(strArray, 0, newArray, 0, strArray.length);
				return newArray;
			} else {
				Function<String, String> mapper = mappers[0];
				for (int i = 1; i < mappers.length; i++) {
					mapper.andThen(mappers[i]);
				}
				return Arrays.stream(strArray).map(mapper).toArray(String[]::new);
			}
		}
		return new String[0];
	}

	public static String[] filter(String[] strArray, Predicate<String>... predicates) {
		if (strArray != null && strArray.length > 0) {
			if (predicates == null || predicates.length == 0) {
				String[] newArray = new String[strArray.length];
				System.arraycopy(strArray, 0, newArray, 0, strArray.length);
				return newArray;
			} else {
				Predicate<String> predicate = predicates[0];
				for (int i = 1; i < predicates.length; i++) {
					predicate.and(predicates[i]);
				}
				return Arrays.stream(strArray).filter(predicate).toArray(String[]::new);
			}
		}
		return new String[0];
	}

	public static String[] mapFilter(String[] strArray, Object... mapperOrFilters) {
		if (strArray == null || strArray.length == 0) {
			return new String[0];
		}

		if (mapperOrFilters == null || mapperOrFilters.length == 0) {
			String[] newArray = new String[strArray.length];
			System.arraycopy(strArray, 0, newArray, 0, strArray.length);
			return newArray;
		}

		try {
			for (Object mapperOrFilter : mapperOrFilters) {
				if (mapperOrFilter instanceof Function) {
					Function<String, String> f = (Function<String, String>) mapperOrFilter;

				} else if (mapperOrFilter instanceof Predicate) {
					Predicate<String> p = (Predicate<String>) mapperOrFilter;

				} else {
					throw new IllegalArgumentException("Not supported class " + mapperOrFilter.getClass());
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("All elements in mapperOrFilters must be instanceof Function<String,String> or Predicate<String>.", e);
		}

		Stream<String> stream = Arrays.stream(strArray);
		for (Object mapperOrFilter : mapperOrFilters) {
			if (mapperOrFilter instanceof Function) {
				stream = stream.map((Function<String, String>) mapperOrFilter);

			} else if (mapperOrFilter instanceof Predicate){
				stream = stream.filter((Predicate<String>) mapperOrFilter);
			}
		}
		return stream.toArray(String[]::new);
	}

	public static interface StringMapper extends Function<String, String> {}
	public static interface StringFilter extends Predicate<String> {}

	public static Long toLong(String str) {
		return toNumber(str, Long.class);
	}

	public static Long[] toLong(String[] strArray) {
		return toNumber(strArray, Long.class);
	}

	public static Integer toInteger(String str) {
		return toNumber(str, Integer.class);
	}

	public static Integer[] toInteger(String[] strArray) {
		return toNumber(strArray, Integer.class);
	}

	public static <T extends Number> T toNumber(String str, Class<T> clazz) {
		str = trimToNull(str);
		if (str == null) {
			return null;
		}

		try {
			// Float, Double, Long, Integer, Short, Byte, BigDecimal, BigInteger
			Constructor<T> constructor = clazz.getConstructor(String.class);
			return constructor.newInstance(str);
		} catch (NoSuchMethodException e) {
			// do-nothing

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// AtomicLong, AtomicInteger
		if (AtomicLong.class.equals(clazz)) {
			return (T) (new AtomicLong(Long.parseLong(str)));

		} else if (AtomicInteger.class.equals(clazz)) {
			return (T) (new AtomicInteger(Integer.parseInt(str)));

		}
		throw new IllegalArgumentException("not supported class '" + clazz + "'");
	}

	public static <T extends Number> T[] toNumber(String[] strArray, Class<T> clazz) {
		if (strArray == null) {
			return null;
		}
		T[] array = (T[]) Array.newInstance(clazz, strArray.length);
		for (int i = 0; i < strArray.length; i++) {
			array[i] = toNumber(strArray[i], clazz);
		}
		return array;
	}

	/**
	 * <pre>
	 * StringUtils.substr(null, *, *)    = null
	 * StringUtils.substr("", * ,  *)    = "";
	 * StringUtils.substr("abc", 0, 2)   = "ab"
	 * StringUtils.substr("abc", 2, 0)   = ""
	 * StringUtils.substr("abc", 2, 1)   = "c"
	 * StringUtils.substr("abc", 2, 4)   = "c"
	 * StringUtils.substr("abc", 4, 6)   = ""
	 * StringUtils.substr("abc", 0, -1)   = ""
	 * StringUtils.substr("abc", 2, -1)   = "b"
	 * StringUtils.substr("abc", 2, -3)   = "ab"
	 * StringUtils.substr("abc", 3, -1)   = "c"
	 * StringUtils.substr("abc", 4, -1)   = ""
	 * StringUtils.substr("abc", 4, -2)   = "c"
	 * StringUtils.substr("abc", -2, -1) = "a"
	 * StringUtils.substr("abc", -2, 1) = "b"
	 * StringUtils.substr("abc", -4, 2)  = "a"
	 * </pre>
	 * @param str
	 * @param start
	 * @param len
	 * @return
	 */
	public static String substr(String str, int start, int len) {
		if (str == null || str.length() == 0) return str;
		int end = start + len;
		return substring(str, start, end);
	}

	/**
	 * <pre>
	 * StringUtils.substring(null, *, *)    = null
	 * StringUtils.substring("", * ,  *)    = "";
	 * StringUtils.substring("abc", 0, 2)   = "ab"
	 * StringUtils.substring("abc", 2, 0)   = ""
	 * StringUtils.substring("abc", 2, 4)   = "c"
	 * StringUtils.substring("abc", 4, 6)   = ""
	 * StringUtils.substring("abc", 2, 2)   = ""
	 * StringUtils.substring("abc", -2, -1) = "b"
	 * StringUtils.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String substring(String str, int start, int end) {
		if (str == null || str.length() == 0) return str;

		// 负数处理
		if (start < 0) start += str.length();
		if (end < 0) end += str.length();

		// 范围处理
		if (start < 0) start = 0;
		if (start > str.length()) start = str.length();
		if (end < 0) end = 0;
		if (end > str.length()) end = str.length();

		// 先后处理
		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		return str.substring(start, end);
	}
}
