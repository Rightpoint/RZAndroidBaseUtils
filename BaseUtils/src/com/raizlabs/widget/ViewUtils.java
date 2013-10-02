package com.raizlabs.widget;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class ViewUtils {
	
	/**
	 * Returns the text for a given {@link textView} or else null if
	 * the textView is null or has no text within.
	 * @param textView The textView whose text should be returned
	 * @return the text within the textView or null
	 */
	public static CharSequence getTextOrNull(TextView textView) {
		if (textView == null) return null;
		
	    CharSequence text = textView.getText();
	    return !TextUtils.isEmpty(text) ? text : null;
	}
	
	/**
	 * Returns the a String with the text for a given {@link textView} 
	 * or else null if the textView is null or has no text within.
	 * <br/><br/>
	 * Overload for {@link #getTextOrNull(TextView)} which returns a {@link String}
	 * <br/><br/>
	 * @param textView The textView whose text should be returned
	 * @return the a String of the text within the textView or null
	 */
	public static String getStringTextOrNull(TextView textView) {
		CharSequence text = getTextOrNull(textView); 
		return (text != null) ? text.toString() : null; 
	}

	/**
	 * Converts the given value in density-independent pixels into raw pixels
	 * with respect to the metrics of the given {@link View}
	 * @param dips The value in density-independent pixels
	 * @param view The {@link View} whose metrics to use to do the conversion
	 * @return The value in raw pixels
	 */
	public static float dipsToPixels(float dips, View view) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				dips, view.getResources().getDisplayMetrics());
	}
	
	/**
	 * Converts the given value in density-independent pixels into raw pixels
	 * with respect to the metrics of the given {@link Resources}
	 * @param dips The value in density-independent pixels
	 * @param resources The {@link Resources} whose metrics to use to do the
	 * conversion
	 * @return The value in raw pixels
	 */
	public static float dipsToPixels(float dips, Resources resources) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				dips, resources.getDisplayMetrics()); 
	}
	
	/**
	 * Converts the given value in density-independent pixels into raw pixels
	 * with respect to the given {@link DisplayMetrics}
	 * @param dips The value in density-independent pixels
	 * @param metrics The {@link DisplayMetrics} to use to do the conversion
	 * @return The value in raw pixels
	 */
	public static float dipsToPixels(float dips, DisplayMetrics metrics) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dips, metrics);
	}
	
	/**
	 * Sets the given {@link View}s visibility to {@link View#VISIBLE} or
	 * {@link View#GONE} depending on the specified value.
	 * @param view The {@link View} to change the visibility of.
	 * @param visible True to make the view VISIBLE, false to make it GONE.
	 */
	public static void setVisibleOrGone(View view, boolean visible) {
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	/**
	 * Sets the given {@link View}s visibility to {@link View#VISIBLE} or
	 * {@link View#INVISIBLE} depending on the specified value.
	 * @param view The {@link View} to change the visibility of.
	 * @param visible True to make the view VISIBLE, false to make it INVISIBLE.
	 */
	public static void setVisibleOrInvisible(View view, boolean visible) {
		view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}
	
	/**
	 * Returns true if the given {@link View}s visibility is set to
	 * {@link View#VISIBLE}.
	 * @param view The {@link View} to check.
	 * @return True if the view is set to VISIBLE.
	 */
	public static boolean isVisible(View view) {
		return view.getVisibility() == View.VISIBLE;
	}
	
	/**
	 * Returns true if the given {@link View}s visibility is set to
	 * {@link View#INVISIBLE}.
	 * @param view The {@link View} to check.
	 * @return True if the view is set to INVISIBLE.
	 */
	public static boolean isInvisible(View view) {
		return view.getVisibility() == View.INVISIBLE;
	}
	
	/**
	 * Returns true if the given {@link View}s visibility is set to
	 * {@link View#GONE}.
	 * @param view The {@link View} to check.
	 * @return True if the view is set to GONE.
	 */
	public static boolean isGone(View view) {
		return view.getVisibility() == View.GONE;
	}
}
