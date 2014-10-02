package com.raizlabs.collections;

import java.util.List;
import java.util.ListIterator;

import com.raizlabs.functions.Predicate;

/**
 * Collection of utilities for use with {@link List}s.
 */
public class ListUtils {
	/**
	 * Returns true if the given list is null or empty.
	 * @param list The list to check.
	 * @return True if the list is null or empty.
	 */
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}
	
	/**
	 * Removes all items from the given list for which the given
	 * {@link Predicate} returns false.
	 * @param list The list to filter.
	 * @param predicate The predicate to run against list items.
	 */
	public static <T> void filter(List<? extends T> list, Predicate<T> predicate) {
		ListIterator<? extends T> iter = list.listIterator();
		T currItem = null;
		
		while (iter.hasNext()) {
			currItem = iter.next();
			if (!predicate.evaluate(currItem)) {
				iter.remove();
			}
		}
	}
}
