package com.raizlabs.baseutils;

/**
 * Class which is a simple wrapper of an object of another type, allowing this
 * object to be used as a placeholder or property.
 * 
 * @author Dylan James
 *
 * @param <T> The type to be wrapped.
 */
public class Wrapper<T> {
	private T value;
	
	/**
	 * Constructs a new, initially empty, {@link Wrapper}.
	 */
	public Wrapper() { }
	
	/**
	 * Constructs a new {@link Wrapper} whose contents are initially set to the given
	 * value.
	 * @param value The initial contents of the wrapper.
	 */
	public Wrapper(T value) {
		set(value);
	}
	
	/**
	 * Sets the contents of this {@link Wrapper}.
	 * @param value The value to set.
	 */
	public void set(T value) {
		this.value = value;
	}
	
	/**
	 * Gets the contents of this {@link Wrapper}.
	 * @return The contents of this {@link Wrapper}.
	 */
	public T get() {
		return value;
	}
}
