package com.raizlabs.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Class with common utilities for JSON parsing
 * 
 * @author Dylan James
 */
public class JSONHelper {

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
}
