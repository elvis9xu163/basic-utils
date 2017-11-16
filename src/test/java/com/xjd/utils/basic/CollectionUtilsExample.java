package com.xjd.utils.basic;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author elvis.xu
 * @since 2017-11-16 10:16
 */
public class CollectionUtilsExample {
	public static void main(String[] args) {
		// asMap
//		Map<String, String> map = CollectionUtils.<String, String>asMap("key1", "val1", "key2", "val2", "key3", 20L);
		Map<String, String> map = CollectionUtils.asMap("key1", "val1", "key2", "val2", "key3", 20L);
		System.out.println(JsonUtils.toJson(map));

		// isEmpty
		System.out.println(CollectionUtils.isEmpty(new ArrayList<String>()));
	}
}