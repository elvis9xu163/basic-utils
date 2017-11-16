package com.xjd.utils.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author elvis.xu
 * @since 2017-11-16 10:05
 */
public abstract class CollectionUtils {
	public static <K, V> Map<K, V> asMap(Object... keyOrVal) {
		if (keyOrVal == null) return null;
		if (keyOrVal.length % 2 != 0) throw new IllegalArgumentException("the length of params must be even.");
		Map<K, V> map = new HashMap<K, V>(keyOrVal.length / 2);
		for (int i = 0; i < keyOrVal.length; i += 2) {
			map.put((K) keyOrVal[i], (V) keyOrVal[i + 1]);
		}
		return map;
	}

	public static <K, V> void addMultiValue(Map<K, Collection<V>> map, K key, V value) {
		Collection<V> vs = map.get(key);
		if (vs == null) {
			vs = new ArrayList<V>();
			Collection<V> existVs = map.putIfAbsent(key, vs);
			if (existVs != null) {
				vs = existVs;
			}
		}
		vs.add(value);
	}
}
