package com.raizlabs.graphics.drawable.async;

import android.graphics.drawable.Drawable;

/**
 * Interface which represents a {@link Drawable} that is loaded asynchronously.
 *  
 * @param <T> The type of the key of the {@link AsyncDrawableTask} that does
 * the work for this drawable. 
 */
public interface AsyncDrawable<T> {
	public AsyncDrawableTask<T> getTask();
}
