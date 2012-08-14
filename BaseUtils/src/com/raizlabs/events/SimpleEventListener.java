package com.raizlabs.events;

/**
 * Interface for an extremely simple event listener which only contains
 * an {@link #onEvent()} method.
 * 
 * @author Dylan James
 */
public interface SimpleEventListener {
	/**
	 * Called when the event occurs.
	 */
	void onEvent();
}
