/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tq.geodb.tool;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.geodb.tool.jackson.JacksonObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Jackson工具类
 *
 * @author Chill
 */
@Slf4j
public class JsonUtil {

	/**
	 * 将对象序列化成json字符串
	 *
	 * @param value javaBean
	 * @param <T>   T 泛型标记
	 * @return jsonString json字符串
	 */
	public static <T> String toJson(T value) {
		try {
			return getInstance().writeValueAsString(value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将对象序列化成 json byte 数组
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	public static byte[] toJsonAsBytes(Object object) {
		try {
			return getInstance().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content   content
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(String content, Class<T> valueType) {
		try {
			return getInstance().readValue(content, valueType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content       content
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(String content, TypeReference<T> typeReference) {
		try {
			return getInstance().readValue(content, typeReference);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json byte 数组反序列化成对象
	 *
	 * @param bytes     json bytes
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(byte[] bytes, Class<T> valueType) {
		try {
			return getInstance().readValue(bytes, valueType);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}


	/**
	 * 将json反序列化成对象
	 *
	 * @param bytes         bytes
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
		try {
			return getInstance().readValue(bytes, typeReference);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in        InputStream
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(InputStream in, Class<T> valueType) {
		try {
			return getInstance().readValue(in, valueType);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in            InputStream
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
		try {
			return getInstance().readValue(in, typeReference);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}



	public static Map<String, Object> toMap(String content) {
		try {
			return getInstance().readValue(content, Map.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
		try {
			Map<String, Map<String, Object>> map = getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
			});
			Map<String, T> result = new HashMap<>(16);
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
			}
			return result;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
		return getInstance().convertValue(fromValue, toValueType);
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonString jsonString
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(String jsonString) {
		try {
			return getInstance().readTree(jsonString);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param in InputStream
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(InputStream in) {
		try {
			return getInstance().readTree(in);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param content content
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(byte[] content) {
		try {
			return getInstance().readTree(content);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonParser JsonParser
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(JsonParser jsonParser) {
		try {
			return getInstance().readTree(jsonParser);
		} catch (IOException e) {
			throw unchecked(e);
		}
	}

	public static ObjectMapper getInstance() {
		return JacksonHolder.INSTANCE;
	}

	private static class JacksonHolder {
		private static ObjectMapper INSTANCE = new JacksonObjectMapper();
	}
	
	/**
	 * 将CheckedException转换为UncheckedException.
	 *
	 * @param e Throwable
	 * @return {RuntimeException}
	 */
	public static RuntimeException unchecked(Throwable e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
			|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}
}
