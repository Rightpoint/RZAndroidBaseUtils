package com.raizlabs.collections;

import com.raizlabs.functions.Delegate;


/**
 * A class which contains a set of items and can perform an actions across all
 * items in the set.
 * 
 * @param <T> The type of item stored in the set.
 */
public class MappableSet<T> {
	
	private TransactionalHashSet<T> members;
	protected Iterable<T> getMembers() { return members; }
	
	/**
	 * Creates a new empty {@link MappableSet}.
	 */
	public MappableSet() {
		members = new TransactionalHashSet<T>();
	}
	
	/**
	 * Adds the given item to the set.
	 * @param item The item to add.
	 */
	public <E extends T> void add(E item) {
		members.add(item);
	}
	
	/**
	 * Removes the given item from the set.
	 * @param item The item to remove.
	 * @return True if the item was removed, false if it wasn't found in the
	 * set.
	 */
	public <E extends T> boolean remove(E item) {
		return members.remove(item);
	}
	
	/**
	 * Removes all items from this set.
	 */
	public void clear() {
		members.clear();
	}
	
	/**
	 * Returns true if the given item is found in the set.
	 * @param item The item to look for.
	 * @return True if the item was found, false if it is not.
	 */
	public <E extends T> boolean contains(E item) {
		return members.contains(item);
	}
	
	/**
	 * @return The number of items in this set.
	 */
	public int size() {
		return members.size();
	}
	
	/**
	 * Calls the given {@link Delegate} on all items in the set. Items may be
	 * added or removed during this call, but changes may not be reflected
	 * until this completes.
	 * @param function The {@link Delegate} to call for each item.
	 */
	public void map(Delegate<T> function) {
		beginTransaction();
		for (T member : members) {
			function.execute(member);
		}
		endTransaction();
	}

	public void beginTransaction() {
		members.beginTransaction();
	}
	
	public void endTransaction() {
		members.endTransaction();
	}
}
