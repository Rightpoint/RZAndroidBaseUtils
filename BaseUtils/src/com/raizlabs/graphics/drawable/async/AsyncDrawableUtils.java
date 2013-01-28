package com.raizlabs.graphics.drawable.async;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Class of helper functions for {@link AsyncDrawable}s.
 * 
 * @author Dylan James
 */
public class AsyncDrawableUtils {
	/**
	 * Attempts to cancel any work associated with a given {@link ImageView},
	 * yielding if the existing work already assigned to the view contains the
	 * same key as the given task.
	 * @param task The task whose work is to later be associated with the view.
	 * @param imageView The {@link ImageView} whose work to clear.
	 * @return True if the work was empty or cancelled, false if the given tasks
	 * key was already associated with the given view.
	 */
	public static boolean cancelExistingWork(AsyncDrawableTask<?> task, ImageView imageView) {
		final AsyncDrawableTask<?> existingTask = getTask(imageView);
		return cancelExistingWork(existingTask, task);
	}
	
	/**
	 * Attempts to cancel any work associated with a given {@link View},
	 * yielding if the existing work already assigned to the view contains the
	 * same key as the given task.
	 * @param task The task whose work is to later be associated with the view.
	 * @param view The {@link View} whose work to clear.
	 * @return True if the work was empty or cancelled, false if the given tasks
	 * key was already associated with the given view.
	 */
	public static boolean cancelExistingWork(AsyncDrawableTask<?> task, View view) {
		final AsyncDrawableTask<?> existingTask = getTask(view);
		return cancelExistingWork(existingTask, task);
	}
	
	/**
	 * Attempts to cancel the work of the existing task, but yields if the existing
	 * task is already operating on the same key as the new task.
	 * @param existingTask
	 * @param newTask
	 * @return True if the work was empty or cancelled, false if the given existing
	 * tasks key matched the new tasks key.
	 */
	private static boolean cancelExistingWork(AsyncDrawableTask<?> existingTask, AsyncDrawableTask<?> newTask) {
		if (existingTask != null) {
			if (newTask != null) {
				final Object key = newTask.getKey();
				if (key != null && key.equals(existingTask.getKey())) {
					// Both tasks have the same key
					if (!existingTask.isCancelled()) 
						return false;
				}
			}
			existingTask.cancel();
		}
		// No task associated or the task was cancelled
		return true;
	}
	
	/**
	 * Retrieves the task associated with the given {@link ImageView}.
	 * @param imageView The view to get the task from.
	 * @return The task associated with the view or null if none exists.
	 */
	public static AsyncDrawableTask<?> getTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable<?>) {
				return ((AsyncDrawable<?>) drawable).getTask();
			}
		}
		return null;
	}
	
	/**
	 * Retrieves the task associated with the given {@link View}.
	 * @param view The view to get the task from.
	 * @return The task associated with the view or null if none exists.
	 */
	public static AsyncDrawableTask<?> getTask(View view) {
		if (view != null) {
			final Drawable drawable = view.getBackground();
			if (drawable instanceof AsyncDrawable<?>) {
				return ((AsyncDrawable<?>) drawable).getTask();
			}
		}
		return null;
	}
}
