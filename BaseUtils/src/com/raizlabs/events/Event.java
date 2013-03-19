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
	private HashSet<EventListener<T>> listenersToAdd;
	private HashSet<EventListener<T>> listenersToRemove;
	private boolean raisingEvent;
	
	/**
	 * Creates a new RZEvent
	 */
	public Event() {
		listeners = new HashSet<EventListener<T>>();
		listenersToAdd = new HashSet<EventListener<T>>();
		listenersToRemove = new HashSet<EventListener<T>>();
		raisingEvent = false;
	}
	
	/**
	 * Adds an RZEventListener to be notified when this event happens.
	 * @param listener The listener to be notified.
	 */
	public void addListener(EventListener<T> listener) {
		if (listener != null) {
			synchronized (this) {
				// If the event is currently being raised, we can't modify the collection
				// Add it to the toAdd collection
				if (raisingEvent) {
					listenersToAdd.add(listener);
				} else {
					listeners.add(listener);
				}
			}
		}
	}
	
	/**
	 * Removes an RZEventListener so it will no longer be notified of
	 * this event.
	 * @param listener The RZEventListener to be removed.
	 * @return True if the listener was removed, false if it wasn't found.
	 */
	public boolean removeListener(EventListener<T> listener) {
		synchronized (this) {
			// If the event is currently being raised, we can't modify the collection
			// Add it to the toRemove collection
			if (raisingEvent) {
				listenersToRemove.add(listener);
				return listeners.contains(listener);
			} else {
				return listeners.remove(listener);
			}
		}
	}
	
	/**
	 * Raises this event and notifies its listeners.
	 * @param sender The object raising the even.
	 * @param args The arguments to the event which will be passed to the listeners.
	 */
	public void raiseEvent(Object sender, T args) {
		synchronized (this) {
			raisingEvent = true;
			listenersToAdd.clear();
			listenersToRemove.clear();
			for (EventListener<T> listener : listeners) {
				// Don't raise the event if the listener is flagged to be removed.
				if (!listenersToRemove.contains(listener)) {
					listener.onEvent(sender, args);
				}
			}
			
			for (EventListener<T> listener : listenersToAdd) {
				listeners.add(listener);
			}
			
			for (EventListener<T> listener : listenersToRemove) {
				listeners.remove(listener);
			}
			raisingEvent = false;
		}
	}
	
	/**
	 * Clears all listeners from this event.
	 */
	public void clear() {
		synchronized (this) {
			// If the event is currently being raised, we can't modify the collection
			// Add all of the current listeners to the toRemove collection
			if (raisingEvent) {
				for (EventListener<T> listener : listeners) {
					listenersToRemove.add(listener);
				}
			} else {
				listeners.clear();
			}
		}
	}
}
