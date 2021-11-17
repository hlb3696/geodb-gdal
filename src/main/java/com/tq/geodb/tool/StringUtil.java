package com.tq.geodb.tool;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 将数组使用join给定连接符连接，不保留null连接
	 *
	 * @param separator 连接符
	 * @param ss 待连接数组
	 * @return java.lang.String
	 */
	@SafeVarargs
	public static <T> String join(String separator, T... ss) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < ss.length; i++) {
			T t = ss[i];
			if (t == null || StringUtils.isEmpty(t.toString())) {
				continue;
			}
			if (i != 0) {
				sb.append(separator);
			}
			sb.append(t);
		}
		return sb.toString();
	}
}
