package com.actram.wordattainer.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * 
 * @author Peter André Johansen
 */
public class BiMap<T, R> {
	private final Map<T, R> map1 = new HashMap<>();
	private final Map<R, T> map2 = new HashMap<>();

	public T getPrimary(R key) {
		Objects.requireNonNull(key, "the key cannot be null");
		return map2.get(key);
	}

	public R getSecondary(T key) {
		Objects.requireNonNull(key, "the key cannot be null");
		return map1.get(key);
	}

	public void put(T key1, R key2) {
		Objects.requireNonNull(key1, "key 1 cannot be null");
		Objects.requireNonNull(key2, "key 2 cannot be null");

		this.map1.put(key1, key2);
		this.map2.put(key2, key1);
	}
}