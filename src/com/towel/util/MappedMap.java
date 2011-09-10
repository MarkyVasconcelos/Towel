package com.towel.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MappedMap<U, T, K> implements Serializable {
	private Map<U, Map<T, K>> map = new HashMap<U, Map<T, K>>();
	private K defaultValue;

	public MappedMap() {

	}

	public MappedMap(K defaultObject) {
		this.defaultValue = defaultObject;
	}

	public void put(U firstKey, T secondKey, K value) {
		Map<T, K> uMap = map.get(firstKey);
		if (uMap == null) {
			uMap = new HashMap<T, K>();
			map.put(firstKey, uMap);
		}
		uMap.put(secondKey, value);
	}

	public K get(U firstKey, T secondKey) {
		Map<T, K> uMap = map.get(firstKey);
		if (uMap == null)
			return defaultValue;
		K k = uMap.get(secondKey);
		if (k == null)
			return defaultValue;
		return k;
	}

	public Map<T, K> getMap(U key) {
		return map.get(key);
	}

	public List<U> getKeys() {
		List<U> list = new ArrayList<U>();
		for (Entry<U, Map<T, K>> ent : map.entrySet())
			list.add(ent.getKey());
		return list;
	}

	public K getDefaultValue() {
		return defaultValue;
	}
}
