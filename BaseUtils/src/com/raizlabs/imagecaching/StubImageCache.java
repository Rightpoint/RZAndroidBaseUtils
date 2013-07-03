package com.raizlabs.imagecaching;

import android.graphics.Bitmap;

/**
 * {@link ImageCache} implementation which doesn't actually cache anything
 * and therefore never returns images. This can be used as a place holder
 * which removes {@link ImageCache} functionality without removing or
 * refactoring any code - just stick this in place of your cache implementation.
 * 
 * @author Dylan James
 *
 */
public class StubImageCache implements ImageCache {

	@Override
	public void addImage(String imageName, Bitmap bitmap) { }

	@Override
	public Bitmap getImage(String imageName) { return null; }

	@Override
	public Bitmap remove(String imageName) { return null; }
	
	@Override
	public void purge() { }

}
