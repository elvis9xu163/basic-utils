package com.xjd.utils.basic;

import java.util.function.*;

/**
 * @author elvis.xu
 * @since 2018-01-17 10:54
 */
public abstract class TernaryUtils {

	public static <T, R> TernaryOperator<T, R> of(T t) {
		return new TernaryOperator(t);
	}

	public static <T, R> TernaryOption<T, R> $if(boolean condition) {
		return new TernaryOperator(null).$if(condition);
	}

	public static <T, R> TernaryOption<T, R> $if(BooleanSupplier supplier) {
		return new TernaryOperator(null).$if(t1 -> supplier.getAsBoolean());
	}

	public static class TernaryOperator<T, R> {
		protected TernaryOption<T, R> result = new TernaryOption();

		public TernaryOperator(T t) {
			result.t = t;
			result.thenFunction = t1 -> (R) t1;
		}

		public TernaryOption<T, R> $if(Predicate<T> predicate) {
			result.testResult = predicate.test(result.t);
			return result;
		}

		public TernaryOption<T, R> $if(boolean condition) {
			result.testResult = condition;
			return result;
		}

	}

	public static class TernaryOption<T, R> extends TernaryResult<T, R> {

		public TernaryResult<T, R> $then(Function<T, R> function) {
			this.thenFunction = function;
			return this;
		}

		public TernaryResult<T, R> $then(R r) {
			return $then(t1 -> r);
		}

		public TernaryResult<T, R> $then() {
			return $then((R) null);
		}

		public TernaryResult<T, R> $then(Consumer<T> consumer) {
			return $then(t1 -> {consumer.accept(t); return null;});
		}

	}

	public static class TernaryResult<T, R> {
		protected T t;
		protected boolean testResult = true;
		protected Function<T, R> thenFunction;

		public R $else(Function<T, R> function) {
			if (testResult) {
				return thenFunction.apply(t);
			} else {
				return function.apply(t);
			}
		}

		public R $else(R r) {
			return $else(t1 -> r);
		}

		public R $else() {
			return $else((R) null);
		}

		public R $else(Consumer<T> consumer) {
			return $else(t1 -> {consumer.accept(t); return null;});
		}
	}
}
