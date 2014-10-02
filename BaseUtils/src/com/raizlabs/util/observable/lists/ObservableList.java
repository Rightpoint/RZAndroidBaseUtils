package com.raizlabs.util.observable.lists;

import java.util.List;

/**
 * Interface for a {@link List} implementation which is also observable via a
 * {@link ListObserver}.
 * 
 * @param <T> The type of data in the list.
 */
public interface ObservableList<T> extends List<T> {
	/**
	 * Gets a {@link ListObserver} which can be used to be notified of changes
	 * to the list data.
	 * @return The {@link ListObserver}.
	 */
	public ListObserver<T> getListObserver();
}
