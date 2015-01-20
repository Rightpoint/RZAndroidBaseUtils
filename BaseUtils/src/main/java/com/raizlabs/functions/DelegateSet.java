package com.raizlabs.functions;

import com.raizlabs.collections.TransactionalHashSet;

/**
 * A class which contains a set of {@link Delegate}s and can easily call
 * {@link Delegate#execute(Object)} across all of them with given parameters.
 * It is also a {@link TransactionalHashSet} so it can block addition/removal
 * of delegates during it's execution or manually. 
 * 
 * @param <T> The parameter type of the delegates.
 */
public class DelegateSet<T> extends TransactionalHashSet<Delegate<T>>{
	
	private static final long serialVersionUID = 8162745188199411480L;

	/**
	 * Calls {@link Delegate#execute(Object)} with the given parameters on all
	 * of the contained delegates.
	 * @param params The parameters to pass to each delegate.
	 */
	public void execute(T params) {
		beginTransaction();
		for (Delegate<T> delegate : this) {
			delegate.execute(params);
		}
		endTransaction();
	}
}
