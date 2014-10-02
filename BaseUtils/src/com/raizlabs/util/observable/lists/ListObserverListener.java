package com.raizlabs.util.observable.lists;

/**
 * Interface which listens to {@link ListObserver} changes.
 *
 * @param <T> The type of items stored in the observed list.
 */
public interface ListObserverListener<T> {

	/**
	 * Called to indicate that the items in a given range were changed.
	 * @param observer The {@link ListObserver} which notified of this change.
	 * @param startPosition The position of the first element that changed.
	 * @param itemCount How many sequential items were changed.
	 */
	public void onItemRangeChanged(ListObserver<T> observer, int startPosition, int itemCount);
	
	/**
	 * Called to indicate that the items were inserted at a given range.
	 * @param observer The {@link ListObserver} which notified of this change.
	 * @param startPosition The position of the first element that was inserted.
	 * @param itemCount How many sequential items were inserted.
	 */
	public void onItemRangeInserted(ListObserver<T> observer, int startPosition, int itemCount);
	
	/**
	 * Called to indicate that the items were removed at a given range.
	 * @param observer The {@link ListObserver} which notified of this change.
	 * @param startPosition The position of the first element that was removed.
	 * @param itemCount How many sequential items were inserted.
	 */
	public void onItemRangeRemoved(ListObserver<T> observer, int startPosition, int itemCount);
	
	/**
	 * Called to indicate that a generic change happened which changed the list
	 * contents.
	 * @param observer The {@link ListObserver} which notified of this change. 
	 */
	public void onGenericChange(ListObserver<T> observer);
}
