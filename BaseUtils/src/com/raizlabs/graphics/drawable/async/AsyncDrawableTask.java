package com.raizlabs.graphics.drawable.async;

/**
 * Interface for a task which does the work to load data for an 
 * {@link AsyncDrawable}.
 *  
 * @author Dylan James
 *
 * @param <T> The type of the key used to identify an {@link AsyncDrawableTask}
 * and whether it is trying to accomplish the same thing as another instance.
 */
public interface AsyncDrawableTask<T> {
	/**
	 * @return The key for the work associated with this task.
	 */
	public T getKey();
	
	/**
	 * Cancels this task.
	 */
	public void cancel();
	/**
	 * @return True if this task is cancelled. 
	 */
	public boolean isCancelled();
	
	/**
	 * @return True if this task has completed its work.
	 */
	public boolean isCompleted();
	
	/**
	 * Binds this task to its designated view.
	 * @return True if this task was bound, false if there was already a task
	 * bound to the view to do the same work.
	 */
	public boolean bind();
	
	/**
	 * Synchronously executes the work for this task. This should be called on
	 * a background thread.
	 */
	public void execute();
}
