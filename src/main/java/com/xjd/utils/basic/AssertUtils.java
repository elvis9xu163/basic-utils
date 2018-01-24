package com.xjd.utils.basic;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author elvis.xu
 * @since 2017-08-29 19:19
 */
public abstract class AssertUtils {

	public static <T> void assertNotMeet(T t, Predicate<T> condition, Supplier<? extends RuntimeException> supplier) {
		if (condition.test(t)) {
			throw supplier.get();
		}
	}

	public static <T> void assertAllNotMeet(Collection<T> ct, Predicate<T> condition, Supplier<? extends
			RuntimeException> supplier) {
		if (ct == null) return;
		ct.forEach(t -> assertNotMeet(t, condition, supplier));
	}

	public static <T> void assertAnyNotMeet(Collection<T> ct, Predicate<T> condition, Supplier<? extends
			RuntimeException> supplier) {
		if (ct == null) return;
		boolean anyNotMeet = false;
		for (T t : ct) {
			if (!condition.test(t)) {
				anyNotMeet = true;
				break;
			}
		}
		if (!anyNotMeet) {
			throw supplier.get();
		}
	}

	public static void assertNonNull(Object obj, String msg) {
		Objects.requireNonNull(obj, msg);
	}

	public static void assertNonNull(Object obj) {
		Objects.requireNonNull(obj);
	}

	public static void assertNonNull(Object obj, Supplier<? extends RuntimeException> supplier) {
		assertNotMeet(obj, Objects::isNull, supplier);
	}

	public static void assertAllNonNull(Collection<? extends Object> ct) {
		assertAllNotMeet(ct, Objects::isNull, () -> new NullPointerException());
	}

	public static void assertAllNonNull(Collection<? extends Object> ct, String msg) {
		assertAllNotMeet(ct, Objects::isNull, () -> new NullPointerException(msg));
	}

	public static void assertAllNonNull(Collection<? extends Object> ct, Supplier<? extends RuntimeException> supplier) {
		assertAllNotMeet(ct, Objects::isNull, supplier);
	}

	public static void assertAnyNonNull(Collection<? extends Object> ct) {
		assertAnyNotMeet(ct, Objects::isNull, () -> new NullPointerException());
	}

	public static void assertAnyNonNull(Collection<? extends Object> ct, String msg) {
		assertAnyNotMeet(ct, Objects::isNull, () -> new NullPointerException(msg));
	}

	public static void assertAnyNonNull(Collection<? extends Object> ct, Supplier<? extends RuntimeException> supplier) {
		assertAnyNotMeet(ct, Objects::isNull, supplier);
	}


	public static void assertEqual(Object obj, Object expect, Supplier<? extends RuntimeException> supplier) {
		assertNotMeet(obj, o -> !Objects.equals(o, expect), supplier);
	}

	public static void assertNotEqual(Object obj, Object expect, Supplier<? extends RuntimeException> supplier) {
		assertNotMeet(obj, o -> Objects.equals(o, expect), supplier);
	}

	public static <T extends Comparable<T>> void assertGreaterThan(T obj, T expect, Supplier<? extends RuntimeException>
			supplier) {
		assertNonNull(obj, supplier);
		assertNonNull(expect, supplier);
		assertNotMeet(obj, o -> o.compareTo(expect) <= 0, supplier);
	}

	public static <T extends Comparable<T>> void assertGreaterEqualThan(T obj, T expect, Supplier<? extends
			RuntimeException> supplier) {
		assertNonNull(obj, supplier);
		assertNonNull(expect, supplier);
		assertNotMeet(obj, o -> o.compareTo(expect) < 0, supplier);
	}

	public static <T extends Comparable<T>> void assertLessEqualThan(T obj, T expect, Supplier<? extends
			RuntimeException> supplier) {
		assertNonNull(obj, supplier);
		assertNonNull(expect, supplier);
		assertNotMeet(obj, o -> o.compareTo(expect) > 0, supplier);
	}

	public static <T extends Comparable<T>> void assertLessThan(T obj, T expect, Supplier<? extends
			RuntimeException> supplier) {
		assertNonNull(obj, supplier);
		assertNonNull(expect, supplier);
		assertNotMeet(obj, o -> o.compareTo(expect) >= 0, supplier);
	}

	public static void assertArgumentNonNull(Object obj, String msg) {
		assertNotMeet(obj, Objects::isNull, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentAllNonNull(Collection<? extends Object> ct, String msg) {
		assertAllNotMeet(ct, Objects::isNull, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentAnyNonNull(Collection<? extends Object> ct, String msg) {
		assertAnyNotMeet(ct, Objects::isNull, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentNonBlank(CharSequence cs, String msg) {
		assertNotMeet(cs, StringUtils::isBlank, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentAllNonBlank(Collection<? extends CharSequence> cs, String msg) {
		assertAllNotMeet(cs, StringUtils::isBlank, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentAnyNonBlank(Collection<? extends CharSequence> cs, String msg) {
		assertAnyNotMeet(cs, StringUtils::isBlank, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentEqual(Object obj, Object expect, String msg) {
		assertEqual(obj, expect, ()-> new IllegalArgumentException(msg));
	}

	public static void assertArgumentNotEqual(Object obj, Object expect, String msg) {
		assertNotEqual(obj, expect, ()-> new IllegalArgumentException(msg));
	}

	public static <T extends Comparable<T>>  void assertArgumentGreaterThan(T obj, T expect, String msg) {
		assertGreaterThan(obj, expect, ()-> new IllegalArgumentException(msg));
	}

	public static <T extends Comparable<T>>  void assertArgumentGreaterEqualThan(T obj, T expect, String msg) {
		assertGreaterEqualThan(obj, expect, ()-> new IllegalArgumentException(msg));
	}

	public static <T extends Comparable<T>>  void assertArgumentLessEqualThan(T obj, T expect, String msg) {
		assertLessEqualThan(obj, expect, ()-> new IllegalArgumentException(msg));
	}

	public static <T extends Comparable<T>>  void assertArgumentLessThan(T obj, T expect, String msg) {
		assertLessThan(obj, expect, ()-> new IllegalArgumentException(msg));
	}

}
