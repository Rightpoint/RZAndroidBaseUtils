package com.raizlabs.collections;

import java.util.Collection;
import java.util.HashSet;

/**
 * Extension of a {@link HashSet} which allows performing transactions which
 * are not committed until the transaction is ended. This can be used around
 * an iteration so that the iteration may modify the collection and commit
 * the changes after the iteration completes.
 * <br/><br/>
 * Note that this implementation makes liberal use of synchronization and extra
 * operations, so the performance will likely be worse than a standard
 * {@link HashSet}.
 * @param <T> The type of items in the {@link HashSet}
 */
public class TransactionalHashSet<T> extends HashSet<T> {
	
	private static final long serialVersionUID = -7093089056781106409L;

	
	private HashSet<T> toAdd, toRemove;

	private volatile boolean inTransaction;

	
	public TransactionalHashSet() {
		super();
		init();
	}

	public TransactionalHashSet(Collection<? extends T> collection) {
		super(collection);
		init();
	}

	private void init() {
		inTransaction = false;
		toAdd = new HashSet<T>();
		toRemove = new HashSet<T>();
	}


	/**
	 * Starts a transaction. This causes additions and removals to be stored,
	 * but they will not be performed until a call to {@link #endTransaction()}
	 */
	public void beginTransaction() {
		synchronized (this) {
			inTransaction = true;
		}
	}

	/**
	 * Commits all pending operations and returns the {@link HashSet} to a
	 * normal state where operations are committed immediately.
	 */
	public void endTransaction() {
		synchronized (this) {
			// Commit the transaction if we're in one
			if (inTransaction) {
				// Lower the flag so we actually commit operations
				inTransaction = false;

				// Add all the items we need to add
				for (T item : toAdd) {
					add(item);
				}

				// Remove all the items we need to remove
				for (T item : toRemove) {
					remove(item);
				}
				
				toAdd.clear();
				toRemove.clear();
			}
		}
	}

	@Override
	public boolean add(T object) {
		synchronized (this) {
			if (inTransaction) {
				// If we're in a transaction, add it to the toAdd list
				toAdd.add(object);
				// The item already exists if it's in this set or the add set
				boolean exists = (contains(object) || toAdd.contains(object));
				// Since this is happening later in the transaction, undo any
				// removal
				boolean wasToBeRemoved = toRemove.remove(object);

				// If it was to be removed, we just added it back
				if (wasToBeRemoved) return true;
				// Otherwise it was added if it didn't already exist
				else return !exists;
			} else {
				// Not in a transaction, proceed as normal
				return super.add(object);
			}
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		// Take the lock here so we're not acquiring/releasing it repeatedly
		synchronized (this) {
			return super.addAll(collection);
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object object) {
		synchronized (this) {
			if (inTransaction) {
				// If the remove list already contains it, we aren't doing
				// anything
				if (toRemove.contains(object)) {
					return false;
				} else {
					// If it's in either set, we're doing a removal
					if (toAdd.remove(object) || contains(object)) {
						toRemove.add((T) object);
						return true;
					} else {
						return false;
					}
				}
			} else {
				// Not in a transaction, proceed as normal
				return super.remove(object);
			}
		}
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		// Take the lock here so we're not acquiring/releasing it repeatedly
		synchronized (this) {
			return super.removeAll(collection);
		}
	}
	
	@Override
	public void clear() {
		removeAll(this);
	}
}
