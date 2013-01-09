package com.raizlabs.view.animation;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.raizlabs.baseutils.Math;

/**
 * View Animation class which animates the size of a given {@link View}
 * from one set of values to another. This class alters the
 * {@link LayoutParams}, modifies the width and height, and calls
 * {@link View#requestLayout()}.
 * 
 * @author Dylan James
 */
public class ResizeAnimation extends Animation {
	private View view;
	private int fromWidth, fromHeight, toWidth, toHeight;
	private boolean finished;
	/**
	 * @return True if this animation has run to completion.
	 */
	public boolean isFinished() { return finished; }
	
	/**
	 * Creates a {@link ResizeAnimation} for the given {@link View} between the
	 * given sizes.
	 * @param view The {@link View} to animate.
	 * @param fromWidth The initial width to animate from.
	 * @param fromHeight The initial height to animate from.
	 * @param toWidth The target width to animate to.
	 * @param toHeight The target height to animate to.
	 */
	public ResizeAnimation(View view, int fromWidth, int fromHeight, int toWidth, int toHeight) {
		this.view = view;
		this.fromWidth = fromWidth;
		this.fromHeight = fromHeight;
		this.toWidth = toWidth;
		this.toHeight = toHeight;
		finished = false;
	}
	
	/**
	 * Creates a {@link ResizeAnimation} for the given {@link View} between the
	 * given sizes over the given duration
	 * @param view The {@link View} to animate.
	 * @param fromWidth The initial width to animate from.
	 * @param fromHeight The initial height to animate from.
	 * @param toWidth The target width to animate to.
	 * @param toHeight The target height to animate to.
	 * @param durationMillis The duration of the animation in milliseconds.
	 */
	public ResizeAnimation(View view, int fromWidth, int fromHeight, int toWidth, int toHeight, long durationMillis) {
		this(view, fromWidth, fromHeight, toWidth, toHeight);
		setDuration(durationMillis);
	}

	@Override
	public void reset() {
		this.finished = false;
		super.reset();
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// Interpolate to get the values for the current time step
		int newWidth = (int) (Math.lerp(fromWidth, toWidth, interpolatedTime));
		int newHeight = (int) (Math.lerp(fromHeight, toHeight, interpolatedTime));

		// If the time is >= 1, we're done
		if (interpolatedTime >= 1) {
			// Clamp the sizes
			newWidth = toWidth;
			newHeight = toHeight;
			// Indicate we've finished
			finished = true;
		} else {
			finished = false;
		}

		if (view == null)
			return;
		
		// Update the Views layout params
		LayoutParams params = view.getLayoutParams();
		params.height = newHeight;
		params.width = newWidth;
		// Set the params and request a layout pass to size the view
		view.setLayoutParams(params);
		view.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
