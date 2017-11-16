package com.xjd.utils.basic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elvis.xu
 * @since 2017-11-16 10:05
 */
public abstract class CollectionUtils {
	public static Map asMap(Object... keyOrVal) {
		if (keyOrVal == null) return null;
		if (keyOrVal.length % 2 != 0) throw new IllegalArgumentException("the length of params must be even.");
		Map map = new HashMap(keyOrVal.length / 2);
		for (int i = 0; i < keyOrVal.length; i += 2) {
			map.put(keyOrVal[i], keyOrVal[i + 1]);
		}
		return map;
	}
}
