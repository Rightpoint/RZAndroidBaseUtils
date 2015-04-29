package com.raizlabs.util.observable.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Class which adapts an existing {@link List} implementation into an
 * {@link ObservableList}
 * 
 * @param <T> The type of item that the list contains.
 */
public class ObservableListWrapper<T> implements ObservableList<T> {

	/**
	 * The list which is currently backing this adapter
	 */
	protected List<T> underlyingList;
	protected SimpleListObserver<T> listObserver;
	private boolean runningTransaction;
	private boolean transactionModified;

	@Override
	public ListObserver<T> getListObserver() { return listObserver; }
	
	/**
	 * Constructs a new {@link ObservableListWrapper} which contains an empty
	 * list of data.
	 */
	public ObservableListWrapper() {
		this(new LinkedList<T>());
	}
	
	/**
	 * Constructs a new {@link ObservableListWrapper} which points to the given
	 * list of data. Note that modifying the passed list will modify the contents
	 * of this {@link ObservableListWrapper}, but changes will go unnoticed and
	 * the events will not be raised. Perform modifications through this
	 * {@link ObservableListWrapper} if the events need to be raised.
	 * 
	 * @param underlyingList The list which will back this adapter
	 */
	public ObservableListWrapper(List<T> underlyingList) {
		if (underlyingList == null) underlyingList = new LinkedList<T>();
		this.underlyingList = underlyingList;
		this.listObserver = new SimpleListObserver<T>();
	}

	/**
	 * Sets the underlying list to be used as the contents of this adapter.
	 * Note that modifying the passed list will modify the contents
	 * of this {@link ObservableListWrapper}, but changes will go unnoticed and
	 * the events will not be raised. Perform modifications through this
	 * {@link ObservableListWrapper} if the events need to be raised.
	 * @param list The list which will back this adapter
	 */
	public void setList(List<T> list) {
		if (list == null) list = new LinkedList<T>();
		this.underlyingList = list;
		onGenericChange();
	}
	

	/**
	 * Updates the underlying list to reflect the contents of the given {@code list} 
	 * by replacing elements which already exist and appending those that do not.
	 * 
	 * @param list the data to update the underlying list with.
	 */
	public void updateFromList(List<T> list) {
		ArrayList<T>updateFromList = new ArrayList<T>(list);
		
		// Loop through the current list and find duplicate entries.
		// Replace them in the current list while removing them from
		// the list that is being loaded.
		ListIterator<T> it = this.listIterator();
		while (it.hasNext()) {
			int nextIndex = it.nextIndex();
			int indexOfObjectInLoadList = updateFromList.indexOf(it.next());
			if (indexOfObjectInLoadList != -1) {
				set(nextIndex, updateFromList.remove(indexOfObjectInLoadList));
			}
		}
		
		// Add all remaining, non-duplicate, items
		addAll(updateFromList);
	}
	
	/**
	* Replaces any existing instances of the given item (as defined by {@link Object#equals()})
	* or appends the item to the end of the list if not found.
	* @param item The item to add
	* @param replaceAll True to replace all instances of the item, false to only replace the first instance in the list.
	*/
	public void addToListOrReplace(T item, boolean replaceAll) {
		boolean foundItem = false;
		
		if (item == null) {
			return;
		} else { 
			ListIterator<T> it = this.listIterator();
			while (it.hasNext()) {
				int nextIndex = it.nextIndex();
				if (it.next().equals(item)) {
					set(nextIndex, item);
					
					if (replaceAll) {
						foundItem = true;
					} else {
						return;
					}
				}
			}
		}

		if (!foundItem) { add(item); }
	}	
	
	/**
	 * Removes all items from this list and adds all the given items,
	 * effectively replacing the entire list.
	 * @param contents The items to set as the new contents.
	 */
	public void replaceContents(Collection<? extends T> contents) {
		this.underlyingList.clear();
		this.underlyingList.addAll(contents);
		this.listObserver.notifyGenericChange();
		
	}
	
	@Override
	public boolean add(T object) {
		int position = underlyingList.size();
		boolean result = underlyingList.add(object);
		if (result) {
			onItemRangeChanged(position, 1);
		}
		return result;
	}

	@Override
	public void add(int location, T object) {
		underlyingList.add(location, object);
		onItemRangeInserted(location, 1);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean result = false;
		int position = underlyingList.size();
		if (collection != null) {
			result = underlyingList.addAll(collection);
		}
		if (result) {
			onItemRangeInserted(position, collection.size());
		}
		return result;
	}

	@Override
	public boolean addAll(int location, Collection<? extends T> collection) {
		boolean result = false;
		if (underlyingList != null && collection != null) {
			result = underlyingList.addAll(location, collection);
		}
		if (result) {
			onItemRangeInserted(location, collection.size());
		}
		return result;
	}

	@Override
	public void clear() {
		int count = underlyingList.size();
		underlyingList.clear();
		onItemRangeRemoved(0, count);
	}

	@Override
	public boolean contains(Object object) {
		return underlyingList.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return underlyingList.containsAll(arg0);
	}

	@Override
	public T get(int location) {
		return underlyingList.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return underlyingList.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return underlyingList.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return underlyingList.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return underlyingList.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return underlyingList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		return underlyingList.listIterator(location);
	}

	@Override
	public T remove(int location) {
		T result = underlyingList.remove(location);
		onItemRangeRemoved(location, 1);
		return result;
	}

	@Override
	public boolean remove(Object object) {
		int index = underlyingList.indexOf(object);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = underlyingList.removeAll(collection);
		if (result) {
			onGenericChange();
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean result = underlyingList.retainAll(collection);
		if (result) {
			onGenericChange();
		}
		return result;
	}

	@Override
	public T set(int location, T object) {
		T result = underlyingList.set(location, object);
		onItemRangeChanged(location, 1);
		return result;
	}

	@Override
	public int size() {
		return underlyingList.size();
	}

	@Override
	public List<T> subList(int start, int end) {
		return underlyingList.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return underlyingList.toArray();
	}

	@Override
	public <E> E[] toArray(E[] array) {
		return underlyingList.toArray(array);
	}
	
	@Override
	public String toString() {
		return "Observable(" + underlyingList.toString() + ")";
	}
	
	protected void onItemRangeChanged(int startPosition, int itemCount) {
		if (tryTransactionModification()) {
			this.listObserver.notifyItemRangeChanged(startPosition, itemCount);
		}
	}
	
	protected void onItemRangeInserted(int startPosition, int itemCount) {
		if (tryTransactionModification()) {
			this.listObserver.notifyItemRangeInserted(startPosition, itemCount);
		}
	}
	
	protected void onItemRangeRemoved(int startPosition, int itemCount) {
		if (tryTransactionModification()) {
			this.listObserver.notifyItemRangeRemoved(startPosition, itemCount);
		}
	}
	
	protected void onGenericChange() {
		if (tryTransactionModification()) {
			this.listObserver.notifyGenericChange();
		}
	}

	/**
	 * Records a modification attempt to any currently running transaction and
	 * returns whether the change should notify listeners.
	 * @return True if the modification should notify listeners, false if it
	 * should not.
	 */
	private boolean tryTransactionModification() {
		if (runningTransaction) {
			transactionModified = true;
			return false;
		}
		return true;
	}
	
	@Override
	public void beginTransaction() {
		if (!runningTransaction) {
			runningTransaction = true;
			transactionModified = false;
		} else {
			throw new IllegalStateException("Tried to begin a transaction when one was already running!");
		}
	}

	@Override
	public void endTransaction() {
		if (runningTransaction) {
			runningTransaction = false;
			if (transactionModified) {
				onGenericChange();
			}
		} else {
			throw new IllegalStateException("Tried to end a transaction when no transaction was running!");
		}
	}
}
