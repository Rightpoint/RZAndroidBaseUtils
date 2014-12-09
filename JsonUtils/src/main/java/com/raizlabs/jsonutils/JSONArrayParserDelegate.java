package com.raizlabs.jsonutils;

import org.json.JSONObject;

/**
 * Delegate interface which parses an object from JSON during an array iteration.
 * 
 * @param <T> The type of object which will be parsed from the JSON
 */
public interface JSONArrayParserDelegate<T> {
	/**
	 * Called to parse an object from the given JSON
	 * @param json The {@link JSONObject} to parse
	 * @return The object, or null if the data could not be parsed
	 */
	public T parseObject(JSONObject json);
}