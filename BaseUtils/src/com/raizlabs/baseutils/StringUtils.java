package com.raizlabs.baseutils;

/**
 * Collection of utilities for use with {@link String}s.
 * 
 * @author Dylan James
 *
 */
public class StringUtils {
	
	/**
	 * Joins the given set of strings, placing the given delimiter between each.
	 * @param delimiter The string to place between each item.
	 * @param items The set of strings to join together.
	 * @return The joined {@link String}.
	 */
	public static String join(String delimiter, Iterable<String> items) {
		StringBuilder builder = new StringBuilder();
		
		boolean isFirst = true;
		for (String str : items) {
			if (!isFirst) builder.append(delimiter);
			builder.append(str);
			isFirst = false;
		}
		
		return builder.toString();
	}
	
	/**
	 * Returns true if the given string is null, the empty string, or the
	 * string "null".
	 * @param str The string to check.
	 * @return True if the string is null or empty.
	 */
	public static boolean isNullOrEmpty(String str) {
		return (str == null) || (str.equals("")) || (str.equals("null"));
	}
}
