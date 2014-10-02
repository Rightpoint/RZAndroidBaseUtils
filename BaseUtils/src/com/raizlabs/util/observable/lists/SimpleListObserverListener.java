package com.raizlabs.util.observable.lists;

/**
 * A class which redirects all {@link ListObserverListener} calls into
 * {@link #onGenericChange(ListObserver)} so that any change fires one call.
 * This makes it function like the notifyDataSetChanged() pattern.
 * 
 * @param <T> The type of items stored in the observed list.
 */
public abstract class SimpleListObserverListener<T> implements ListObserverListener<T> {

	@Override
	public void onItemRangeChanged(ListObserver<T> observer, int startPosition, int itemCount) {
		onGenericChange(observer);
	}

	@Override
	public void onItemRangeInserted(ListObserver<T> observer, int startPosition, int itemCount) {
		onGenericChange(observer);
	}

	@Override
	public void onItemRangeRemoved(ListObserver<T> observer, int startPosition, int itemCount) {
		onGenericChange(observer);
	}
}
