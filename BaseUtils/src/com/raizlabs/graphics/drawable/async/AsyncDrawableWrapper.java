package com.raizlabs.graphics.drawable.async;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Class which wraps an existing {@link Drawable} and acts as a {@link Drawable},
 * but also provides additional information about an {@link AsyncDrawableTask}
 * in order to bind task data to a view. This allows us to assign and obtain
 * tasks via the Drawable object itself.
 * 
 * @author Dylan James
 *
 * @param <T> The type of the key of the {@link AsyncDrawableTask} that will be
 * associated with this Drawable.
 */
public class AsyncDrawableWrapper<T> extends Drawable implements AsyncDrawable<T> {
	private Drawable drawable;
	private AsyncDrawableTask<T> task;
	
	/**
	 * Constructs an {@link AsyncDrawableWrapper} which wraps the given
	 * {@link Drawable} and is tagged with the given {@link AsyncDrawableTask}
	 * @param drawable The Drawable to wrap.
	 * @param task The task to tag the Drawable with.
	 */
	public AsyncDrawableWrapper(Drawable drawable, AsyncDrawableTask<T> task) {
		if (drawable == null) {
			throw new IllegalArgumentException("Drawable may not be null");
		}
		this.drawable = drawable;
		this.task = task;
	}
	
	public Drawable getDrawble() { return drawable; }
	
	@Override
	public AsyncDrawableTask<T> getTask() {
		return task;
	}

	////////////////////////////////////////////
	// Forwarding methods to wrapped drawable //
	////////////////////////////////////////////
	@Override
	public void clearColorFilter() {
		drawable.clearColorFilter();
	}
	
	@Override
	public void draw(Canvas canvas) {
		drawable.draw(canvas);
	}
	
	@TargetApi(11)
	@Override
	public Callback getCallback() {
		return drawable.getCallback();
	}
	
	@Override
	public int getChangingConfigurations() {
		return drawable.getChangingConfigurations();
	}
	
	@Override
	public ConstantState getConstantState() {
		return drawable.getConstantState();
	}
	
	@Override
	public Drawable getCurrent() {
		return drawable.getCurrent();
	}
	
	@Override
	public int getIntrinsicHeight() {
		return drawable.getIntrinsicHeight();
	}
	
	@Override
	public int getIntrinsicWidth() {
		return drawable.getIntrinsicWidth();
	}
	
	@Override
	public int getMinimumHeight() {
		return drawable.getMinimumHeight();
	}
	
	@Override
	public int getMinimumWidth() {
		return drawable.getMinimumWidth();
	}
	
	@Override
	public int getOpacity() {
		return drawable.getOpacity();
	}
	
	@Override
	public boolean getPadding(Rect padding) {
		return drawable.getPadding(padding);
	}
	
	@Override
	public int[] getState() {
		return drawable.getState();
	}
	
	@Override
	public Region getTransparentRegion() {
		return drawable.getTransparentRegion();
	}

	@Override
	public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
			throws XmlPullParserException, IOException {
		drawable.inflate(r, parser, attrs);
	}
	
	@Override
	public void invalidateSelf() {
		drawable.invalidateSelf();
	}
	
	@Override
	public boolean isStateful() {
		return drawable.isStateful();
	}
	
	@TargetApi(11)
	@Override
	public void jumpToCurrentState() {
		drawable.jumpToCurrentState();
	}
	
	@Override
	public Drawable mutate() {
		return drawable.mutate();
	}
	
	@Override
	public void scheduleSelf(Runnable what, long when) {
		drawable.scheduleSelf(what, when);
	}
	
	@Override
	public void setAlpha(int alpha) {
		drawable.setAlpha(alpha);
	}

	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		drawable.setBounds(left, top, right, bottom);
	}
	
	@Override
	public void setBounds(Rect bounds) {
		drawable.setBounds(bounds);
	}
	
	@Override
	public void setChangingConfigurations(int configs) {
		drawable.setChangingConfigurations(configs);
	}
	
	@Override
	public void setColorFilter(ColorFilter cf) {
		drawable.setColorFilter(cf);
	}
	
	@Override
	public void setColorFilter(int color, Mode mode) {
		drawable.setColorFilter(color, mode);
	}
	
	@Override
	public void setDither(boolean dither) {
		drawable.setDither(dither);
	}
	
	@Override
	public void setFilterBitmap(boolean filter) {
		drawable.setFilterBitmap(filter);
	}
	
	@Override
	public boolean setState(int[] stateSet) {
		return drawable.setState(stateSet);
	}
	
	@Override
	public boolean setVisible(boolean visible, boolean restart) {
		return drawable.setVisible(visible, restart);
	}
	
	@Override
	public void unscheduleSelf(Runnable what) {
		drawable.unscheduleSelf(what);
	}
}
