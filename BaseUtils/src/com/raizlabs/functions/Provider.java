package com.raizlabs.functions;

/**
 * Interface which provides an object of a specific type.
 *
 * @param <T> The type of object this interface provides.
 */
public interface Provider<T>  {

	/**
	 * Obtains the provided object.
	 * @return The provided object.
	 */
	public T obtainProvided();
}
