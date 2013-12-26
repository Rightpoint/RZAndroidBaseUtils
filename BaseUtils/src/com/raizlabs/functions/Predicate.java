package com.raizlabs.functions;

/**
 * Interface for a predicate which evaluates a true or false value for a given
 * item.
 * @author Dylan James
 *
 * @param <T> The type of item to evaluate.
 */
public interface Predicate<T> {
	/**
	 * Evaluates the given item.
	 * @param item The item to evaluate.
	 * @return The true or false value for the given item.
	 */
	public boolean evaluate(T item);
}
