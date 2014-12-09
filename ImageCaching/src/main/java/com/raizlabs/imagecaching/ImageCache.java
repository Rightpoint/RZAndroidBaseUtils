package com.raizlabs.imagecaching;

import android.graphics.Bitmap;

/**
 * Interface which declares a cache of bitmaps keyed by names.
 */
public interface ImageCache {
	/**
	 * Adds the given {@link Bitmap} to this cache under the given name (key).
	 * @param imageName The name to store the {@link Bitmap} under.
	 * @param bitmap The {@link Bitmap} to store
	 */
	public void addImage(String imageName, Bitmap bitmap);
	
	/**
	 * Gets the image currently mapped to the given name, if one exists.
	 * @param imageName The name of the image to get.
	 * @return The current {@link Bitmap} or null if none exists
	 */
	public Bitmap getImage(String imageName);
	
	/**
	 * Removes any image currently mapped to the given name.
	 * @param imageName The name of the image to remove
	 * @return The {@link Bitmap} which was removed, or null
	 */
	public Bitmap remove(String imageName);
	
	/**
	 * Removes all images from this cache.
	 */
	public void purge();
}
