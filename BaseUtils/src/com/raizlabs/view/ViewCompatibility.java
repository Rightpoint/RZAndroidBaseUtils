package com.raizlabs.view;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * Compatibility helpers for view related tasks. Handles using API level
 * appropriate methods based on the run time SDK.
 * 
 * @author Dylan James
 */
public class ViewCompatibility {
	
	/**
	 * Removes the given {@link OnGlobalLayoutListener} from the given
	 * {@link ViewTreeObserver} using the most appropriate method in the
	 * current SDK.
	 * @param observer The view tree observer to remove the listener from.
	 * @param listener The listener to remove.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void removeGlobalOnLayoutListener(ViewTreeObserver observer, OnGlobalLayoutListener listener) {
		if (Build.VERSION.SDK_INT >= 16) {
			observer.removeOnGlobalLayoutListener(listener);
		} else {
			observer.removeGlobalOnLayoutListener(listener);
		}
	}
	
	/**
	 * Sets the given {@link Drawable} as the background of the given
	 * {@link View}.
	 * @param view The view to set the background of.
	 * @param background The drawable to set as the background.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setViewBackground(View view, Drawable background) {
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(background);
		} else {
			view.setBackgroundDrawable(background);
		}
	}
}
