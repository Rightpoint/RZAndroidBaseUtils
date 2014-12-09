package com.raizlabs.util.observable.lists;

import com.raizlabs.collections.MappableSet;
import com.raizlabs.functions.Delegate;

/**
 * Simple implementation of a {@link ListObserver}.
 */
public class SimpleListObserver<T> implements ListObserver<T> {

	private MappableSet<ListObserverListener<T>> listeners;
	
	public SimpleListObserver() {
		listeners = new MappableSet<ListObserverListener<T>>();
	}
	
	@Override
	public void addListener(ListObserverListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public boolean removeListener(ListObserverListener<T> listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Call to notify listeners that the items in a given range were changed.
	 * @param startPosition The position of the first element that changed.
	 * @param itemCount How many sequential items were changed.
	 */
	public void notifyItemRangeChanged(final int startPosition, final int itemCount) {
		mapListeners(new Delegate<ListObserverListener<T>>() {
			@Override
			public void execute(ListObserverListener<T> listener) {
				listener.onItemRangeChanged(SimpleListObserver.this, startPosition, itemCount);
			}
		});
	}
	
	/**
	 * Call to notify listeners that items were inserted at a given range.
	 * @param startPosition The position of the first element that was inserted.
	 * @param itemCount How many sequential items were inserted.
	 */
	public void notifyItemRangeInserted(final int startPosition, final int itemCount) {
		mapListeners(new Delegate<ListObserverListener<T>>() {
			@Override
			public void execute(ListObserverListener<T> listener) {
				listener.onItemRangeInserted(SimpleListObserver.this, startPosition, itemCount);
			}
		});
	}
	
	/**
	 * Call to notify listeners that items were removed at a given range.
	 * @param startPosition The position of the first element that was removed.
	 * @param itemCount How many sequential items were removed.
	 */
	public void notifyItemRangeRemoved(final int startPosition, final int itemCount) {
		mapListeners(new Delegate<ListObserverListener<T>>() {
			@Override
			public void execute(ListObserverListener<T> listener) {
				listener.onItemRangeRemoved(SimpleListObserver.this, startPosition, itemCount);
			}
		});
	}
	
	/**
	 * Call to notify listeners that a generic change happened which changed
	 * the list contents.
	 */
	public void notifyGenericChange() {
		mapListeners(genericChangeDelegate);
	}
	
	private void mapListeners(Delegate<ListObserverListener<T>> function) {
		listeners.beginTransaction();
		listeners.map(function);
		listeners.endTransaction();
	}
	
	private final Delegate<ListObserverListener<T>> genericChangeDelegate = new Delegate<ListObserverListener<T>>() {
		@Override
		public void execute(ListObserverListener<T> listener) {
			listener.onGenericChange(SimpleListObserver.this);
		}
	};
}
