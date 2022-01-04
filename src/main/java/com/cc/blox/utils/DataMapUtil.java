package com.cc.blox.utils;

import java.util.HashMap;
import java.util.Map;

public class DataMapUtil {
	public static Map<String, Object> createDataMap(String key, Object value) {
		Map<String, Object> dataMap = new HashMap<String, Object>(1);
		dataMap.put(key, value);
		return dataMap;
	}
}
