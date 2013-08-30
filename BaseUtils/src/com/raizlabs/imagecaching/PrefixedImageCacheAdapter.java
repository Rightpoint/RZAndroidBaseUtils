package com.raizlabs.imagecaching;

import java.util.HashSet;

import android.graphics.Bitmap;

/**
 * Adapter {@link ImageCache} which stores its images inside another
 * {@link ImageCache}, prefixed by a given string. Think of this as
 * creating a "folder" inside the other {@link ImageCache}.
 * 
 * @author Dylan James
 *
 */
public class PrefixedImageCacheAdapter implements ImageCache {

	/**
	 * Our outer cache
	 */
	private ImageCache cache;
	
	/**
	 * The prefix we use
	 */
	private String prefix;
	
	private HashSet<String> addedKeys;
	
	/**
	 * Creates a {@link PrefixedImageCacheAdapter} which uses the given
	 * prefix inside the given {@link ImageCache}
	 * @param prefix The prefix to use on each item passed through this cache
	 * @param cache The {@link ImageCache} to store images in
	 */
	public PrefixedImageCacheAdapter(String prefix, ImageCache cache) {
		this.prefix = prefix;
		this.cache = cache;
		this.addedKeys = new HashSet<String>(); 
	}
	
	@Override
	public void addImage(String imageName, Bitmap bitmap) {
		String imageKey = prefix + imageName;
		cache.addImage(imageKey, bitmap);
		addedKeys.add(imageKey);
	}

	@Override
	public Bitmap getImage(String imageName) {
		return cache.getImage(prefix + imageName);
	}

	@Override
	public Bitmap remove(String imageName) {
		String imageKey = prefix + imageName;
		addedKeys.remove(imageKey);
		return cache.remove(imageKey);
	}

	@Override
	public void purge() {
		for (String key : addedKeys) {
			cache.remove(key);
		}
		addedKeys.clear();
	}
}
