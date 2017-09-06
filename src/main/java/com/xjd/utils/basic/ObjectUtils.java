package com.xjd.utils.basic;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author elvis.xu
 * @since 2017-08-25 10:33
 */
public abstract class ObjectUtils {

	@Deprecated
	public static <T> T requireNonNull(T obj, String msg) {
		return Objects.requireNonNull(obj, msg);
	}
	@Deprecated
	public static <T> T requireNonNull(T obj) {
		return Objects.requireNonNull(obj);
	}
	@Deprecated
	public static <T> T  requireArgumentNonNull(T obj, String msg) {
		if (obj == null)
			throw new IllegalArgumentException(msg);
		return obj;
	}
	@Deprecated
	public static <T> T  requireArgumentNonNull(T obj) {
		if (obj == null)
			throw new IllegalArgumentException();
		return obj;
	}

	public static <T extends Comparable<? super T>> int compare(final T c1, final T c2) {
		return compare(c1, c2, false);
	}

	public static <T extends Comparable<? super T>> int compare(final T c1, final T c2, final boolean nullGreater) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return nullGreater ? 1 : -1;
		} else if (c2 == null) {
			return nullGreater ? -1 : 1;
		}
		return c1.compareTo(c2);
	}

	public static <T extends Comparable<? super T>> Comparator<T> comparator() {
		return comparator(false);
	}

	public static <T extends Comparable<? super T>> Comparator<T> comparator(final boolean nullGreater) {
		return (c1, c2) -> {
			return compare(c1, c2, nullGreater);
		};
	}

	public static int compare(ComparePairs comparePairs) {
		return compare(comparePairs, false);
	}

	public static int compare(ComparePairs comparePairs, final boolean nullGreater) {
		for (Comparable[] pair : comparePairs.pairs) {
			int c = compare(pair[0], pair[1], nullGreater);
			if (c != 0) return c;
		}
		return 0;
	}

	public static class ComparePairs {
		List<Comparable[]> pairs = new LinkedList<>();

		public <T extends Comparable<? super T>> ComparePairs addPair(T c1, T c2) {
			pairs.add(new Comparable[]{c1, c2});
			return this;
		}
	}
}
