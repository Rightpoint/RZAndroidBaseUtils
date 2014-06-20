package com.raizlabs.graphics;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.raizlabs.view.ViewCompatibility;

/**
 * Class of helper methods which assist with the loading of images into memory
 * and views. Similar to {@link BitmapFactory}.
 * @author Dylan James
 */
public class ImageFactory {
	
	/**
	 * Sets the background of the given {@link View} to the image in the given
	 * {@link File}, opening the image only as large as necessary to fill
	 * the {@link View}. This will perform scaling to match the screen density.
	 * If a dimension of the {@link View}  is 0, it will be ignored, and the
	 * image may be opened at full size.
	 * @param file The {@link File} to open.
	 * @param view The {@link View} to set the background on.
	 * @param context The {@link Context} to use to get the screen density and
	 * properties.
	 * @return True if the background was set or false if the image could not
	 * be loaded. 
	 */
	public static boolean setBackground(File file, View view, Context context) {
		return setBackground(file.getAbsolutePath(), view, context);
	}
	
	/**
	 * Sets the background of the given {@link View} to the image at the given
	 * path, opening the image only as large as necessary to fill
	 * the {@link View}. This will perform scaling to match the screen density.
	 * If a dimension of the {@link View}  is 0, it will be ignored, and the
	 * image may be opened at full size.
	 * @param pathName The path to the file to open.
	 * @param view The {@link View} to set the background on.
	 * @param context The {@link Context} to use to get the screen density and
	 * properties.
	 * @return True if the background was set or false if the image could not
	 * be loaded. 
	 */
	public static boolean setBackground(String pathName, View view, Context context) {
		Bitmap bitmap = decodeFile(pathName, view, false);
		if (bitmap != null) {
			ViewCompatibility.setViewBackground(view, new BitmapDrawable(context.getResources(), bitmap));
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the background of the given {@link View} to the image in the given
	 * {@link File}, opening the image only as large as necessary to fill
	 * the {@link View}. This does not perform scaling. If a dimension of the
	 * {@link View}  is 0, it will be ignored, and the image may be opened at
	 * full size.
	 * @param file The {@link File} to decode.
	 * @param view The {@link View} to set the background on.
	 * @return True if the background was set or false if the image could not
	 * be loaded. 
	 */
	public static boolean setBackground(File file, View view) {
		return setBackground(file.getAbsolutePath(), view);
	}
	
	/**
	 * Sets the background of the given {@link View} to the image at
	 * the given path, opening the image only as large as necessary to fill
	 * the {@link View}. This does not perform scaling. If a dimension of the
	 * {@link View}  is 0, it will be ignored, and the image may be opened at
	 * full size.
	 * @param pathName The path to the file to decode.
	 * @param view The {@link View} to set the background on.
	 * @return True if the background was set or false if the image could not
	 * be loaded. 
	 */
	public static boolean setBackground(String pathName, View view) {
		Bitmap bitmap = decodeFile(pathName, view, true);
		if (bitmap != null) {
			ViewCompatibility.setViewBackground(view, new BitmapDrawable(bitmap));
			return true;
		}
		return false;
	}

	/**
	 * Creates a {@link Drawable} for the given {@link Bitmap} which will be
	 * scaled to match the screen properties.
	 * @param bitmap The {@link Bitmap} to create a {@link Drawable} for.
	 * @param context The {@link Context} to use to get the screen density and
	 * properties.
	 * @return The scaled {@link Drawable}.
	 */
	public static Drawable getDrawableScaled(Bitmap bitmap, Context context) {
		return new BitmapDrawable(context.getResources(), bitmap);
	}
	
	/**
	 * Creates a {@link Drawable} for the given {@link Bitmap} which will not be
	 * scaled.
	 * @param bitmap The {@link Bitmap} to create a {@link Drawable} for.
	 * @return The unscaled {@link Drawable}.
	 */
	public static Drawable getDrawableUnscaled(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the given {@link File}, sized
	 * within the dimensions of the given {@link View}. If the image is
	 * larger than the {@link View}'s dimensions, it will be opened to fit
	 * within these dimensions. If a dimension is 0, it will be ignored,
	 * and the image may be opened at full size.
	 * @param file The {@link File} to decode.
	 * @param view The {@link View} to constrain the image within the bounds
	 * of. If a dimension is 0, it will be ignored, and the image may be
	 * opened at full size.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(File file, View view, boolean dontScale) {
		return decodeFile(file.getAbsolutePath(), view, dontScale);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the file at the given path, sized
	 * within the dimensions of the given {@link View}. If the image is
	 * larger than the {@link View}'s dimensions, it will be opened to fit
	 * within these dimensions.
	 * @param pathName The path to the file to decode.
	 * @param view The {@link View} to constrain the image within the bounds
	 * of. If a dimension is 0, it will be ignored.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(String pathName, View view, boolean dontScale) {
		int maxWidth = view.getWidth();
		int maxHeight = view.getHeight();
		
		// If we don't get a size, we may be between measure() and layout()
		// Attempt to grab the measured size
		if (maxWidth == 0) maxWidth = view.getMeasuredWidth();
		if (maxHeight == 0) maxHeight = view.getMeasuredHeight();
		
		if (maxWidth == 0) maxWidth = Integer.MAX_VALUE;
		if (maxHeight == 0) maxHeight = Integer.MAX_VALUE;
		
		return decodeFile(pathName, maxWidth, maxHeight, dontScale);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the given {@link File}, sized
	 * within the given dimensions and the dimensions of the given {@link View}.
	 * If the image is larger than the given dimensions, it will be opened to
	 * fit within these dimensions.
	 * @param file The {@link File} to decode.
	 * @param view The {@link View} to constrain the image within the bounds of.
	 * If a dimensions is 0, it will be ignored, and the given max value will be
	 * used.
	 * @param maxWidth The maximum width allowed for the {@link Bitmap}.
	 * @param maxHeight The maximum height allowed for the {@link Bitmap}.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(File file, View view, int maxWidth, int maxHeight, boolean dontScale) {
		return decodeFile(file.getAbsolutePath(), view, maxWidth, maxHeight, dontScale);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the file at the given path, sized
	 * within the given dimensions and the dimensions of the given {@link View}.
	 * If the image is larger than the given dimensions, it will be opened to
	 * fit within these dimensions.
	 * @param pathName The path to the file to decode.
	 * @param view The {@link View} to constrain the image within the bounds of.
	 * If a dimensions is 0, they will be ignored.
	 * @param maxWidth The maximum width allowed for the {@link Bitmap}.
	 * @param maxHeight The maximum height allowed for the {@link Bitmap}.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(String pathName, View view, int maxWidth, int maxHeight, boolean dontScale) { 
		final int viewWidth = view.getWidth();
		final int viewHeight = view.getHeight();
		if (viewWidth > 0)
			maxWidth = java.lang.Math.min(viewWidth, maxWidth);
		
		if (viewHeight > 0)
			maxHeight = java.lang.Math.min(viewHeight, maxHeight);
		
		return decodeFile(pathName, maxWidth, maxHeight, dontScale);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the given {@link File}, sized
	 * within the given dimensions. If the image is larger than the max
	 * dimensions, it will be opened to fit within these dimensions.
	 * @param file The {@link File} to decode.
	 * @param maxWidth The maximum width allowed for the {@link Bitmap}.
	 * @param maxHeight The maximum height allowed for the {@link Bitmap}.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(File file, int maxWidth, int maxHeight, boolean dontScale) {
		return decodeFile(file.getAbsolutePath(), maxWidth, maxHeight, dontScale);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the file at the given path, sized
	 * within the given dimensions. If the image is larger than the max
	 * dimensions, it will be opened to fit within these dimensions.
	 * @param pathName The path to the file to decode.
	 * @param maxWidth The maximum width allowed for the {@link Bitmap}.
	 * @param maxHeight The maximum height allowed for the {@link Bitmap}.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(String pathName, int maxWidth, int maxHeight, boolean dontScale) {
		return decodeFile(pathName, maxWidth, maxHeight, dontScale, null);
	}
	
	/**
	 * Gets a {@link Bitmap} by decoding the file at the given path, sized
	 * within the given dimensions. If the image is larger than the max
	 * dimensions, it will be opened to fit within these dimensions.
	 * @param pathName The path to the file to decode.
	 * @param maxWidth The maximum width allowed for the {@link Bitmap}.
	 * @param maxHeight The maximum height allowed for the {@link Bitmap}.
	 * @param dontScale If set, this sets the {@link Bitmap}'s density to
	 * {@link Bitmap#DENSITY_NONE} to avoid scaling.
	 * @param options Options to use to decode the file.
	 * @return The decoded {@link Bitmap}.
	 */
	public static Bitmap decodeFile(String pathName, int maxWidth, int maxHeight, boolean dontScale, Options options) {
		Bitmap bitmap;
		if (options == null) options = new Options();
		if (maxWidth < Integer.MAX_VALUE || maxHeight < Integer.MAX_VALUE) {
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathName, options);
			
			options.inSampleSize = getSampleSize(options, maxWidth, maxHeight);
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(pathName, options);
		} else {
			bitmap = BitmapFactory.decodeFile(pathName, options);
		}
		if (dontScale && bitmap != null) {
			bitmap.setDensity(Bitmap.DENSITY_NONE);
		}
		return bitmap;
	}
	
	/**
	 * Gets the sample size to use to constrain the image to the given maximum
	 * dimensions.
	 * @param options The decoded {@link Options} from decoding the file.
	 * @param maxWidth The maximum width allowed.
	 * @param maxHeight The maximum height allowed.
	 * @return The sample size to use to keep within the bounds. This is rounded
	 * so it may not be exactly within the bounds.
	 */
	public static int getSampleSize(Options options, int maxWidth, int maxHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > maxHeight || width > maxWidth) {
			final int heightMult = height / maxHeight;
			final int widthMult = width / maxWidth;
			if (widthMult < heightMult) {
				inSampleSize = java.lang.Math.round((float) height / (float) maxHeight);
			} else {
				inSampleSize = java.lang.Math.round((float) width / (float) maxWidth);
			}
		}
		return inSampleSize;
	}
	
	/**
	 * Decodes the given file, obtaining only the image size instead of decoding
	 * the whole image.
	 * @param pathName The path to the file to decode.
	 * @return The populated {@link Options} containing the size in
	 * {@link Options#outWidth} and {@link Options#outHeight}.
	 */
	public static Options decodeFileSize(String pathName) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		return options;
	}
}
