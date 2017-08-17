package com.xjd.utils.basic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json工具类, 依赖Jackson
 * @author elvis.xu
 * @since 2017-08-16 10:25
 */
public abstract class JsonUtils {

	protected static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 序列化对象为JSON字符串
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JsonException("Cannot serialize object to json: " + object, e);
		}
	}

	/**
	 * 将JSON字符串转换成对象
	 * @param json
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new JsonException("Cannot parse json to object: " + json, e);
		}
	}


	public static class JsonException extends RuntimeException {
		public JsonException() {
		}

		public JsonException(String message) {
			super(message);
		}

		public JsonException(String message, Throwable cause) {
			super(message, cause);
		}

		public JsonException(Throwable cause) {
			super(cause);
		}

		public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
}
