package com.xjd.utils.basic;

import java.util.Arrays;
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
}
