package com.raizlabs.util.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.raizlabs.events.Event;

/**
 * Class which adapts an existing {@link List} implementation into an
 * {@link ObservableList}
 * 
 * @author Dylan James
 *
 * @param <T> The type of item that the list contains.
 */
public class ObservableListAdapter<T> implements ObservableList<T> {

	/**
	 * The list which is currently backing this adapter
	 */
	protected List<T> underlyingList;
	private Event<ObservableList<T>> dataChangedEvent;
	private boolean runningTransaction;
	/**
	 * True to indicate that the list has been modified during the current transaction.
	 */
	protected boolean modified;
	
	/**
	 * Constructs a new {@link ObservableListAdapter} which contains an empty
	 * list of data.
	 */
	public ObservableListAdapter() {
		this(new LinkedList<T>());
	}
	
	/**
	 * Constructs a new {@link ObservableListAdapter} which points to the given
	 * list of data. Note that modifying the passed list will modify the contents
	 * of this {@link ObservableListAdapter}, but changes will go unnoticed and
	 * the events will not be raised. Perform modifications through this
	 * {@link ObservableListAdapter} if the events need to be raised.
	 * 
	 * @param underlyingList The list which will back this adapter
	 */
	public ObservableListAdapter(List<T> underlyingList) {
		if (underlyingList == null) underlyingList = new LinkedList<T>();
		this.underlyingList = underlyingList;
		runningTransaction = false;
		modified = false;
		dataChangedEvent = new Event<ObservableList<T>>();
	}
	
	/**
	 * Raises the data changed event, notifying observers that the data has
	 * changed, only if no transaction is currently being run.
	 */
	public void notifyDataSetChanged() {
		if (modified && !runningTransaction) {
			dataChangedEvent.raiseEvent(this, this);
		}
		modified = false;
	}
	
	/**
	 * Begins a transaction. Changes will be visible immediately, but the data
	 * changed event will not be raised until a call is made to
	 * {@link #endTransaction()}.
	 * @see ObservableListAdapter#endTransaction()
	 * @throws IllegalStateException if a transaction is already running.
	 */
	public void beginTransaction() {
		if (!runningTransaction) {
			runningTransaction = true;
		} else {
			throw new IllegalStateException("Tried to begin a transaction when one was already running!");
		}
	}
	
	/**
	 * Ends the current transaction. This will raise the data changed event
	 * if any modifications have been made.
	 * @see ObservableListAdapter#beginTransaction()
	 * @throws IllegalStateException if no transaction is currently running.
	 */
	public void endTransaction() {
		if (runningTransaction) {
			runningTransaction = false;
			notifyDataSetChanged();
		} else {
			throw new IllegalStateException("Tried to end a transaction when no transaction was running!");
		}
	}
	
	@Override
	public Event<ObservableList<T>> getDataChangedEvent() {
		return dataChangedEvent;
	}

	/**
	 * Sets the underlying list to be used as the contents of this adapter.
	 * Note that modifying the passed list will modify the contents
	 * of this {@link ObservableListAdapter}, but changes will go unnoticed and
	 * the events will not be raised. Perform modifications through this
	 * {@link ObservableListAdapter} if the events need to be raised.
	 * @param list The list which will back this adapter
	 */
	public void setList(List<T> list) {
		this.underlyingList = list;
		modified = true;
		if (!runningTransaction) notifyDataSetChanged();
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
		beginTransaction();
		clear();
		addAll(contents);
		endTransaction();
	}
	
	@Override
	public boolean add(T object) {
		boolean result = underlyingList.add(object);
		if (result) {
			modified = true;
			if (!runningTransaction) notifyDataSetChanged();
		}
		return result;
	}

	@Override
	public void add(int location, T object) {
		underlyingList.add(location, object);
		modified = true;
		if (!runningTransaction) notifyDataSetChanged();
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		boolean result = underlyingList.addAll(arg0);
		if (result) {
			modified = true;
			if (!runningTransaction) notifyDataSetChanged();
		}
		return result;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		boolean result = underlyingList.addAll(arg0, arg1);
		if (result) {
			modified = true;
			if (!runningTransaction) notifyDataSetChanged();
		}
		return result;
	}

	@Override
	public void clear() {
		underlyingList.clear();
		modified = true;
		if (!runningTransaction) notifyDataSetChanged();
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
		modified = true;
		if (!runningTransaction) notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean remove(Object object) {
		boolean result = underlyingList.remove(object);
		if (result) {
			modified = true;
			if (result && !runningTransaction) notifyDataSetChanged();
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean result = underlyingList.removeAll(arg0);
		if (result) {
			modified = true;
			if (!runningTransaction) notifyDataSetChanged();
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		boolean result = underlyingList.retainAll(arg0);
		if (result) {
			modified = true;
			if (!runningTransaction) notifyDataSetChanged();
		}
		return result;
	}

	@Override
	public T set(int location, T object) {
		T result = underlyingList.set(location, object);
		modified = true;
		if (!runningTransaction) notifyDataSetChanged();
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
	
}
