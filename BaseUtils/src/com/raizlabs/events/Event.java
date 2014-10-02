package com.raizlabs.events;

import com.raizlabs.functions.Delegate;
import com.raizlabs.functions.DelegateSet;

/**
 * A class which represents an event which notifies a set of listeners when the
 * event is raised.
 *
 * @param <T> The parameter type of the event.
 */
public class Event<T> {
	private DelegateSet<T> listeners;
	
	public Event() {
		listeners = new DelegateSet<T>();
	}
	
	/**
	 * Subscribes the given listener to the event so it is called when the event
	 * is raised.
	 * @param listener The listener to subscribe.
	 */
	public void addListener(Delegate<T> listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unsubscribes the given listener from the event so it is no longer called
	 * when the event is raised.
	 * @param listener The listener to unsubscribe.
	 * @return True if the listener was unsubscribed, false if it wasn't
	 * subscribed.
	 */
	public boolean removeListener(Delegate<T> listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Raises this event with the given parameters, notifying all subscribed
	 * listeners.
	 * @param params The parameters to send to each listener.
	 */
	public void raiseEvent(T params) {
		performRaiseEvent(listeners, params);
	}
	
	/**
	 * Called to actually raise the event. Subclasses may override this in order
	 * to perform custom logic, but should be sure to execute the event across
	 * the given listeners.
	 * @param listeners A set of listeners which need to be notified.
	 * @param params The parameters to be sent to each listener.
	 */
	protected void performRaiseEvent(DelegateSet<T> listeners, T params) {
		listeners.execute(params);
	}
}
