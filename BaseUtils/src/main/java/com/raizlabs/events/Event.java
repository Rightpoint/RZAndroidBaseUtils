package com.raizlabs.events;

import com.raizlabs.functions.Delegate;
import com.raizlabs.functions.DelegateSet;

/**
 * Simple implementation of {@link IEvent}.
 *
 * @param <T> The parameter type of the event.
 */
public class Event<T> implements IEvent<T>{
	private DelegateSet<T> listeners;
	
	public Event() {
		listeners = new DelegateSet<T>();
	}
	
	@Override
	public void addListener(Delegate<T> listener) {
		listeners.add(listener);
	}
	
	@Override
	public boolean removeListener(Delegate<T> listener) {
		return listeners.remove(listener);
	}
	
	@Override
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
