package com.raizlabs.concurrent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing some helper methods related to concurrency.
 */
public class ConcurrencyUtils {
	/**
	 * Puts the given value in the given map for the given key if no mapping exists,
	 * and returns the new value.
	 * @param map The map to store the value in.
	 * @param key The key in the map to store the value in.
	 * @param absentValue The value to put if no mapping exists.
	 * @return The value that is now stored in the map. This will be absentValue if
	 * no mapping existed and the absentValue was stored, or the existing value if
	 * one existed. 
	 */
	public static <K, V> V putIfAbsent(ConcurrentHashMap<K, V> map, K key, V absentValue) {
		// First quickly try to get the value
		V value = map.get(key);
		// If we failed to retrieve one, try to put the absentValue
		if (value == null) {
			value = map.putIfAbsent(key, absentValue);
			// If there was not an old mapping, it is now mapped to absent value
			if (value == null) {
				value = absentValue;
			}
		}
		// Return the current mapping.
		return value;
	}
}
