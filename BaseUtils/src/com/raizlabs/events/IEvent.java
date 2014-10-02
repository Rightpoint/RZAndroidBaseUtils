package com.raizlabs.events;

import com.raizlabs.functions.Delegate;

/**
 * Interface for a class which represents an event which notifies a set of
 * listeners when the event is raised.
 *
 * @param <T> The parameter type of the event.
 */
public interface IEvent<T> {

	/**
	 * Subscribes the given listener to the event so it is called when the event
	 * is raised.
	 * @param listener The listener to subscribe.
	 */
	public void addListener(Delegate<T> listener);
	
	/**
	 * Unsubscribes the given listener from the event so it is no longer called
	 * when the event is raised.
	 * @param listener The listener to unsubscribe.
	 * @return True if the listener was unsubscribed, false if it wasn't
	 * subscribed.
	 */
	public boolean removeListener(Delegate<T> listener);
	
	/**
	 * Raises this event with the given parameters, notifying all subscribed
	 * listeners.
	 * @param params The parameters to send to each listener.
	 */
	public void raiseEvent(T params);
}
