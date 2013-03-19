package com.raizlabs.util.observable;

import com.raizlabs.events.Event;

/**
 * Interface which indicates that a class provides an {@link Event} which is
 * raised when its data changes.
 * 
 * @author Dylan James
 *
 * @param <T> The type of data that will be sent as the arguments to the data
 * change event. In many cases, this may just be the class itself.
 */
public interface ObservableData<T> {
	/**
	 * @return The {@link Event} which will be raised when the data in this
	 * object changes.
	 */
	public Event<T> getDataChangedEvent();
}
