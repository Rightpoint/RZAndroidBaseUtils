package com.raizlabs.events;

/**
 * Generic EventListener which takes arguments of type T
 * @author Dylan James
 *
 * @param <T> The type of the arguments which will be passed to the onEvent method.
 */
public interface EventListener<T> {
	/**
	 * Called when the event is triggered.
	 * @param sender The object sending the message.
	 * @param args The arguments of the event.
	 */
	void onEvent(Object sender, T args);
}
