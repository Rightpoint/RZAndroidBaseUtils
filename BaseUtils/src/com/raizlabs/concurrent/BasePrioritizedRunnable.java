package com.raizlabs.concurrent;


/**
 * Simple base class implementation for a {@link PrioritizedRunnable}.
 * 
 * @author Dylan James
 */
public abstract class BasePrioritizedRunnable implements PrioritizedRunnable {
	
	private int priority;
	/**
	 * {@inheritDoc}
	 */
	public int getPriority() { return priority; }
	/**
	 * Sets the priority of this object.
	 * @param priority The priority to set.
	 */
	public void setPriority(int priority) { this.priority = priority; }
	
	/**
	 * Creates a {@link BasePrioritizedRunnable} with default priority.
	 */
	public BasePrioritizedRunnable() {
		this(Priority.NORMAL);
	}
	
	/**
	 * Creates a {@link BasePrioritizedRunnable} with the given priority.
	 * @param priority The priority to set for this {@link BasePrioritizedRunnable}.
	 */
	public BasePrioritizedRunnable(int priority) {
		setPriority(priority);
	}
}
