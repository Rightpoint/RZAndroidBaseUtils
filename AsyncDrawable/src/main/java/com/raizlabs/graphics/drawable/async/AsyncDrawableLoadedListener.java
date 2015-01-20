package com.raizlabs.graphics.drawable.async;

import android.graphics.drawable.Drawable;

/**
 * An interface which listens to drawable loads from an {@link AsyncDrawableTask}.
 * 
 * @param <T> The type of the key used to identify the {@link AsyncDrawableTask}.
 */
public interface AsyncDrawableLoadedListener<T> {

	public void onDrawableLoaded(AsyncDrawableTask<T> task, Drawable drawable);
}
