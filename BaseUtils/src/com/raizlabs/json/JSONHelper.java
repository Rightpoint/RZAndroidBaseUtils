package com.raizlabs.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

/**
 * Class with common utilities for JSON parsing
 * 
 * @author Dylan James
 */
public class JSONHelper {
	/**
	 * Iterates over the {@link JSONArray} defined at the given key in the
	 * given JSON object, if one exists, and parses each element with the given
	 * {@link JSONArrayParserDelegate}. If the delegate returns an item, it
	 * will be added to the result list. If it does not, that index will be
	 * skipped - no null will be added to the list.
	 * @param json The {@link JSONObject} to get the array from
	 * @param key The key to look for the array under
	 * @param delegate The {@link JSONArrayParserDelegate} to call to parse
	 * each object
	 * @return A {@link List} containing all parsed objects or null if the key
	 * didn't map to a {@link JSONArray}
	 */
	public static <T> List<T> parseJSONArray(JSONObject json, String key,
			JSONArrayParserDelegate<T> delegate) {
		JSONArray array = json.optJSONArray(key);
		if (array == null) return null;
		
		return parseJSONArray(array, delegate);
	}
	
	/**
	 * Iterates over the given {@link JSONArray} and parses each element with
	 * the given {@link JSONArrayParserDelegate}. If the delegate returns an
	 * item, it will be added to the result list. If it does not, that index
	 * will be skipped - no null will be added to the list.
	 * @param array The {@link JSONArray} to parse
	 * @param delegate The {@link JSONArrayParserDelegate} to call to parse
	 * each object
	 * @return A {@link List} containing all parsed objects
	 */
	public static <T> List<T> parseJSONArray(JSONArray array, 
			JSONArrayParserDelegate<T> delegate) {
		
		final int numItems = (array == null) ? 0 : array.length();
	
		if (numItems == 0) {
			return new ArrayList<T>(0);
		} else {
			List<T> items = new ArrayList<T>(numItems);
			
			for (int i = 0; i < numItems; i++) {
				try {
					JSONObject objectJSON = array.getJSONObject(i);
					
					T obj = delegate.parseObject(objectJSON);
					if (obj != null) items.add(obj);
				} catch (JSONException e) {
					// This shouldn't really ever happen since we are checking
					// the length
					Log.d(JSONHelper.class.getName(), "Error parsing object", e);
				}
			}
			
			return items;
		}
	}
	
	/**
	 * Parses all strings out of the data at the given key inside the given
	 * {@link JSONObject} if it exists
	 * @param json The {@link JSONObject} to look for the key in
	 * @param key The key to look for the strings under
	 * @return The list of strings, or null if they couldn't be parsed or the
	 * key wasn't defined
	 */
	public static List<String> parseStrings(JSONObject json, String key) {
		// Attempt to grab the messages as an array of messages
		List<String> strings = parseStrings(json.optJSONArray(key));
		if (strings == null) {
			// If there is only one string, the field will be a simple string
			// mapping instead of an array of size 1 :(
			String string = json.optString(key);
			if (!TextUtils.isEmpty(string)) {
				strings = new ArrayList<String>(1);
				strings.add(string);
			}
		}

		return strings;
	}
	
	/**
	 * Parses all strings out of the given {@link JSONArray} into a list.
	 * @param json The array to parse.
	 * @return A list containing all the strings or null if the input array
	 * was null.
	 */
	public static List<String> parseStrings(JSONArray json) {
		if (json != null) {

			final int numStrings = json.length();

			List<String> strings = new ArrayList<String>(numStrings);

			for (int i = 0; i < numStrings; i++) {
				String string = json.optString(i);
				if (!TextUtils.isEmpty(string)) strings.add(string);
			}

			return strings;
		}
		return null;
	}
}
