package com.raizlabs.util.observable.lists;

/**
 * An interface which observes a list and maintains a set of listeners to be
 * notified of changes to the list.
 *
 * @param <T> The type of items stored in the observed list.
 */
public interface ListObserver<T> {
	/**
	 * Adds a listener to be notified of changes to the list.
	 * @param listener The listener to add.
	 */
	public void addListener(ListObserverListener<T> listener);
	/**
	 * Removes a listener from being notified of changes to the list.
	 * @param listener The listener to remove.
	 * @return True if the listener was removed, false if it hadn't been added.
	 */
	public boolean removeListener(ListObserverListener<T> listener);
}
