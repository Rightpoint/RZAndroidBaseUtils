package com.raizlabs.events;

import java.util.HashSet;

/**
 * Class which represents an Event with arguments of type T.
 * @author Dylan James
 *
 * @param <T>
 */
public class Event<T> {
	private HashSet<EventListener<T>> listeners;
	
	/**
	 * Creates a new RZEvent
	 */
	public Event() {
		listeners = new HashSet<EventListener<T>>();
	}
	
	/**
	 * Adds an RZEventListener to be notified when this event happens.
	 * @param listener The listener to be notified.
	 */
	public void addListener(EventListener<T> listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes an RZEventListener so it will no longer be notified of
	 * this event.
	 * @param listener The RZEventListener to be removed.
	 * @return True if the listener was removed, false if it wasn't found.
	 */
	public boolean removeListener(EventListener<T> listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Raises this event and notifies its listeners.
	 * @param sender The object raising the even.
	 * @param args The arguments to the event which will be passed to the listeners.
	 */
	public void raiseEvent(Object sender, T args) {
		for (EventListener<T> listener : listeners) {
			listener.onEvent(sender, args);
		}
	}
}
