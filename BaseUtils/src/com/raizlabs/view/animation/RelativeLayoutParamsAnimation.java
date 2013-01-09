package com.raizlabs.view.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.raizlabs.baseutils.Math;

/**
 * View Animation class which animates the numeric parameters of the
 * {@link RelativeLayout} {@link LayoutParams} of a given {@link View} from one
 * set of {@link LayoutParams} to another.
 * @author Dylan James
 *
 */
public class RelativeLayoutParamsAnimation extends Animation {

	private View view;
	
	private LayoutParams from, to;
	/**
	 * @param params The {@link LayoutParams} to animate from.
	 */
	public void setFrom(LayoutParams params) { from = params; }
	/**
	 * @param params The {@link LayoutParams} to animate to.
	 */
	public void setTo(LayoutParams params) { to = params; }
	
	/**
	 * Creates a {@link RelativeLayoutParamsAnimation} that operates on the
	 * given {@link View}
	 * @param v The {@link View} which will be animated.
	 */
	public RelativeLayoutParamsAnimation(View v) {
		this.view = v;
	}
	
	/**
	 * Creates a {@link RelativeLayoutParamsAnimation} that operates on the
	 * given {@link View}, animating from the views current parameters to
	 * the given parameters.
	 * @param v The {@link View} which will be animated.
	 * @param to The {@link LayoutParams} to animate to.
	 */
	public RelativeLayoutParamsAnimation(View v, LayoutParams to) {
		this.view = v;
		this.from = new LayoutParams(v.getLayoutParams());
		this.to = to;
	}
	
	/**
	 * Creates a {@link RelativeLayoutParamsAnimation} that operates on the
	 * given {@link View}, animating between the given parameters.
	 * @param v The {@link View} which will be animated. 
	 * @param from The {@link LayoutParams} to animate from.
	 * @param to The {@link LayoutParams} to animate to.
	 */
	public RelativeLayoutParamsAnimation(View v, LayoutParams from, LayoutParams to) {
		this.view = v;
		this.from = from;
		this.to = to;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		ViewGroup.LayoutParams existingParams = view.getLayoutParams();
		// Reuse the existing parameters if we can, otherwise create new ones.
		final LayoutParams viewParams =
				(existingParams instanceof LayoutParams) ?
						(LayoutParams) existingParams : new LayoutParams(from);
						
		viewParams.bottomMargin = Math.lerp(from.bottomMargin, to.bottomMargin, interpolatedTime);
		viewParams.height = Math.lerp(from.height, to.height, interpolatedTime);
		viewParams.leftMargin = Math.lerp(from.leftMargin, to.leftMargin, interpolatedTime);
		viewParams.rightMargin = Math.lerp(from.rightMargin, to.rightMargin, interpolatedTime);
		viewParams.topMargin = Math.lerp(from.topMargin, to.topMargin, interpolatedTime);
		viewParams.width = Math.lerp(from.width, to.width, interpolatedTime);
		view.setLayoutParams(viewParams);
		view.invalidate();
	}
}
