package com.xjd.utils.basic;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author elvis.xu
 * @since 2017-09-12 11:51
 */
public class StringUtilsExample {

	public static void main(String[] args) {
		stringArray();
	}

	public static void stringArray() {
		String s = ",,b ,,, c,,";

		System.out.println("======StringUtils.split=======");
		String[] splits = StringUtils.split(s, ",");
		System.out.println(Arrays.toString(splits));

		System.out.println("======Optional=======");
		String[] ss1 = Optional.ofNullable(splits).map(StringUtils::trimElement).map(StringUtils::trimEmpty).get();
		System.out.println(Arrays.toString(ss1));

		System.out.println("======StringUtils.mapFilter=======");
		String[] ss2 = StringUtils.mapFilter(splits, (Function<String, String>)StringUtils::trim, (Predicate<String>)StringUtils::isNotEmpty);
		System.out.println(Arrays.toString(ss2));

		System.out.println("======StringUtils.mapFilter2=======");
		String[] ss22 = StringUtils.mapFilter(splits, (StringUtils.StringMapper)StringUtils::trim, (StringUtils.StringFilter)StringUtils::isNotEmpty);
		System.out.println(Arrays.toString(ss22));

		System.out.println("======StringUtils.trim...=======");
		String[] ss3 = StringUtils.trimNull(StringUtils.trimElementToNull(splits));
		System.out.println(Arrays.toString(ss3));
	}
}