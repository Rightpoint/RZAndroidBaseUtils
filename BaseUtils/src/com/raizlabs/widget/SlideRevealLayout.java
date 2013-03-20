package com.raizlabs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.raizlabs.baseutils.Math;
import com.raizlabs.baseutils.ThreadingUtils;

/**
 * 
 * {@link RelativeLayout} that slides its content to the right to reveal content
 * behind it. Other content should just be placed below this layout. The slide
 * can be opened or closed via {@link #open()} and {@link #clone()} or their 
 * animated counterparts. The layout can also be edge-dragged opened (which can
 * be disabled via {@link #setEdgeThreshold(float)}.
 * 
 * @author Dylan James
 */
public class SlideRevealLayout extends RelativeLayout {
	private static final long DEFAULT_DURATION = 400;
	
	/**
	 * Interface for a class which is called when the layout state changes.	 *
	 */
	public interface StateChangedListener {
		/**
		 * Called when the state of the layout changes.
		 * @param open True if the state is now open, false if it is closed.
		 */
		public void onStateChanged(boolean open);
	}
	
	int overlap;
	long duration;
	boolean open;
	public boolean isOpen() { return open; }
	StateChangedListener stateChangedListener;
	
	private float touchSlop;
	boolean isTouching;
	boolean isDragging;
	private float dragOffset;
	
	boolean animating;
	/**
	 * @return True if the layout is currently animating.
	 */
	public boolean isAnimating() { return animating; }
	
	private float edgeThreshold;
	/**
	 * Sets how far from the edge is considered and bevel-drag and opens the layout.
	 * @param threshold The threshold distance in pixels
	 */
	public void setEdgeThreshold(float threshold) { this.edgeThreshold = threshold; }
	
	boolean enabled;
	/**
	 * @return True if revealing is enabled
	 */
	public boolean isRevealEnabled() { return enabled; }
	/**
	 * Sets whether revealing is enabled
	 * @param enabled Whether revealing should be enabled
	 */
	public void setRevealEnabled(boolean enabled) { this.enabled = enabled; }
	
	/**
	 * Child layout which we actually put our contents inside. This layout is
	 * then slid via layout params and animations.
	 */
	RelativeLayout childLayout;
	
	public SlideRevealLayout(Context context) {
		super(context);
		init(context);
	}
	
	public SlideRevealLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public SlideRevealLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		setAnimationDuration(DEFAULT_DURATION);
		open = false;
		animating = false;
		enabled = true;
		isTouching = false;
		isDragging = false;
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		edgeThreshold = touchSlop * 3;
		setClipChildren(false);
		setStaticTransformationsEnabled(true);
		
		childLayout = new RelativeLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(childLayout, params);
	}
	
	/**
	 * Sets the number of pixels of the layout that will remain on the screen
	 * when the layout is open.
	 * @param overlap The number of pixels that should overlap
	 */
	public void setOverlap(int overlap) {
		if (this.overlap != overlap) {
			this.overlap = overlap;
			forceUpdate();
		}
	}
	
	/**
	 * Sets the duration of open/close animations
	 * @param duration The duration in milliseconds
	 */
	public void setAnimationDuration(long duration) {
		this.duration = duration;
	}
	
	/**
	 * Sets a listener to be called when the state of the window changes.
	 * @param listener A listener to be called when the state changes.
	 */
	public void setStateChangedListener(StateChangedListener listener) {
		this.stateChangedListener = listener;
	}
	
	private void setOpenState(boolean open) {
		final boolean wasOpen = this.open;
		this.open = open;
		
		if (wasOpen != open && stateChangedListener != null) {
			stateChangedListener.onStateChanged(open);
		}
	}
	
	@Override
	public void addView(View child) {
		if (getChildCount() == 0) {
			super.addView(child);
		} else {
			childLayout.addView(child);
		}
	}
	
	@Override
	public void addView(View child, int index) {
		if (getChildCount() == 0) {
			super.addView(child, index);
		} else {
			childLayout.addView(child, index);
		}
	}
	
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		if (getChildCount() == 0) {
			super.addView(child, index, params);
		} else {
			childLayout.addView(child, index, params);
		}
	}
	
	@Override
	public void addView(View child, int width, int height) {
		if (getChildCount() == 0) {
			super.addView(child, width, height);
		} else {
			childLayout.addView(child, width, height);
		}
	}
	
	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		if (getChildCount() == 0) {
			super.addView(child, params);
		} else {
			childLayout.addView(child, params);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		forceUpdate();
	}
	
	protected void forceUpdate() {
		if (open) {
			post(new Runnable() {
				@Override
				public void run() {				
					open();
				}
			});
		}
	}
	
	/**
	 * Immediately opens this reveal layout
	 */
	public void open() {
		childLayout.clearAnimation();
		RelativeLayout.LayoutParams params = (LayoutParams) childLayout.getLayoutParams();
		params.leftMargin = getWidth() - overlap;
		params.rightMargin = -(getWidth() - overlap);
		childLayout.setLayoutParams(params);
		setOpenState(true);
	}
	
	/**
	 * Opens this reveal layout with an animation and calls the listener when
	 * the animation finishes.
	 * @param completedListener The listener to be called when the animation
	 * finishes 
	 */
	public void openAnimated(final StateChangedListener completedListener) {
		openAnimated(0, duration, completedListener);
	}
	
	/**
	 * Opens this reveal layout starting at the given offset over an animation
	 * lasting the given number of milliseconds and calls he listener when
	 * the animation finishes.
	 * @param startingOffset The starting offset
	 * @param duration The duration of the animation in milliseconds
	 * @param completedListener An optional listener to be called when the
	 * animation finishes 
	 */
	protected void openAnimated(final float startingOffset, final long duration, final StateChangedListener completedListener) {
		animating = true;
		ThreadingUtils.runOnUIThread(this, new Runnable() {
			public void run() {
				final float currPosition = childLayout.getLeft();
				final float destinationOffset = getWidth() - overlap - currPosition;
				
				TranslateAnimation translateAnimation = new TranslateAnimation(startingOffset - currPosition, destinationOffset, 0, 0);
				translateAnimation.setDuration(duration);
				translateAnimation.setFillAfter(false);
				translateAnimation.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) { }
					
					public void onAnimationRepeat(Animation animation) { }
					
					public void onAnimationEnd(Animation animation) {
						animating = false;
						open();
						if (completedListener != null) completedListener.onStateChanged(true);
					}
				});
				childLayout.startAnimation(translateAnimation);
			}
		});
	}
	
	/**
	 * Immediately closes this reveal layout
	 */
	public void close() {
		childLayout.clearAnimation();
		RelativeLayout.LayoutParams params = (LayoutParams) childLayout.getLayoutParams();
		params.leftMargin = 0;
		params.rightMargin = 0;
		childLayout.setLayoutParams(params);	
		setOpenState(false);
	}
	
	/**
	 * Closes this reveal layout with an animation and calls the listener when
	 * the animation finishes.
	 * @param completedListener The listener to be called when the animation
	 * finishes 
	 */
	public void closeAnimated(final StateChangedListener completedListener) {
		closeAnimated(getWidth() - overlap, duration, null);
	}
	
	/**
	 * Closes this reveal layout starting at the given offset over an animation
	 * lasting the given number of milliseconds and calls he listener when
	 * the animation finishes.
	 * @param startingOffset The starting offset
	 * @param duration The duration of the animation in milliseconds
	 * @param completedListener An optional listener to be called when the
	 * animation finishes
	 */
	protected void closeAnimated(final float startingOffset, final long duration, final StateChangedListener completedListener) {
		animating = true;
		ThreadingUtils.runOnUIThread(this, new Runnable() {
			public void run() {
				final float currPosition = childLayout.getLeft();
				final float destinationOffset = -currPosition;
				TranslateAnimation translateAnimation = new TranslateAnimation(startingOffset - currPosition, destinationOffset, 0, 0);
				translateAnimation.setDuration(duration);
				translateAnimation.setFillAfter(false);
				translateAnimation.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) { }

					public void onAnimationRepeat(Animation animation) {						
					}

					public void onAnimationEnd(Animation animation) {
						animating = false;
						close();
						if (completedListener != null) completedListener.onStateChanged(false);			
					}
				});
				childLayout.startAnimation(translateAnimation);
			}
		});
	}	
	
	/**
	 * Toggles whether this reveal layout is opened with an animation and calls
	 * the listener when the animation finishes.
	 * @param completedListener An optional listener to be called when the
	 * animation finishes
	 */
	public void toggleOpenAnimated(StateChangedListener completedListener) {
		if (animating) return;
		
		if (open) {
			closeAnimated(completedListener);
		} else {
			openAnimated(completedListener);
		}
	}
	
	private float touchInitialX, touchInitialY;
	private VelocityTracker touchVelocityTracker;
	
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// If the layout is disabled, don't intercept
		if (!enabled) return false;
		// Intercept the touch if
		//  - The layout is already being touched
		//  - The layout is open and within the overlapping content
		//  - The layout is closed and along the edge
		return animating || (open && ev.getX() >= childLayout.getLeft()) || 
				!open && ev.getX() < edgeThreshold;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// If the layout is disabled, pass on the event
		if (!enabled) return false;
		// If the layout is animating, trap all touches and do nothing
		if (animating) return true;
		
		// Handle the touch if
		//  - The layout is already being touched
		//  - The layout is open and within the overlapping content
		//  - The layout is closed and along the edge
		if (isTouching || (open && event.getX() >= childLayout.getLeft()) ||
				!open && event.getX() < edgeThreshold){
			// Calculate the total distanced the touch has traveled
			final float travelSquared = Math.distanceSquared(
					event.getX(), touchInitialX,
					event.getY(), touchInitialY);
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// Set the inital position
				touchInitialX = event.getX();
				touchInitialY = event.getY();
				// Grab a velocity tracker and add the movement
				touchVelocityTracker = VelocityTracker.obtain();
				touchVelocityTracker.addMovement(event);
				// We are touching, but not yet dragging
				isTouching = true;
				isDragging = false;
				// Reset the offset
				dragOffset = 0;
				// Dispatch the touch to the child until we determine it
				// belongs to us
				childLayout.dispatchTouchEvent(event);
				break;
			case MotionEvent.ACTION_UP:
				// The touch ended
				isTouching = false;
				isDragging = false;
				// Sometimes we miss the DOWN when intercepting events from above
				if (touchVelocityTracker == null) touchVelocityTracker = VelocityTracker.obtain();
				// Add the movement to the velocity tracker
				touchVelocityTracker.addMovement(event);
				
				// If they hadn't passed the "slop" threshold, it was just a click
				if (travelSquared < (touchSlop * touchSlop)) {
					// If we are open, close ourselves
					if (open) {
						closeAnimated(null);
					} else {
						// Otherwise, pass the click along to the child - we don't
						// care about clicks otherwise
						childLayout.dispatchTouchEvent(event);
					}
				} else {
					// Compute and store the velocity
					touchVelocityTracker.computeCurrentVelocity(1);
					float velocity = touchVelocityTracker.getXVelocity();
					// The minimum speed to open/close the view
					float minVelocity = (float)(getWidth() - overlap) / (float)duration;
					// How fast the touch must be traveling to force an open/close
					float escapeVelocity = minVelocity;
					// Clamp to the min velocity
					if (java.lang.Math.abs(velocity) < java.lang.Math.abs(minVelocity)) {
						velocity = minVelocity;
					}
					
					// Open or close the layout respectively
					// The duration is calculated from the current velocity and how far we are
					// from the desired target
					if (open) {
						// If the touch is moving fast enough (far enough left/negative) or is 
						// on the close side, close the layout
						if (velocity < escapeVelocity || event.getX() < getWidth() / 2) {
							long duration = (long) java.lang.Math.abs((overlap + dragOffset) / velocity);
							closeAnimated(getWidth() - overlap + dragOffset, duration, null);
						} else {
							// Otherwise, keep it open
							long duration = (long) java.lang.Math.abs(dragOffset / velocity);
							openAnimated(getWidth() - overlap + dragOffset, duration, null);
						}
					} else {
						// If the touch is moving fast enough (far enough right/positive) or is
						// on the open side, open the layout
						if (velocity > escapeVelocity || event.getX() > getWidth() / 2) {
							long duration = (long) java.lang.Math.abs((getWidth() - dragOffset) / velocity);
							openAnimated(dragOffset, duration, null);
						} else {
							// Otherwise keep it closed
							long duration = (long) java.lang.Math.abs(dragOffset / velocity);
							closeAnimated(dragOffset, duration, null);
						}
					}
				}
				// Free our velocity tracker
				touchVelocityTracker.recycle();
				break;
			case MotionEvent.ACTION_MOVE:
				// Update the offset
				dragOffset = event.getX() - touchInitialX;
				// Clamp the offset to the edges of the screen so they can't drag the window off
				if (open) {
					dragOffset = Math.clamp(dragOffset, -(getWidth() - overlap), 0);
				} else {
					dragOffset = Math.clamp(dragOffset, 0, getWidth() - overlap);
				}
				// Sometimes we miss the DOWN when intercepting events from above
				if (touchVelocityTracker == null) touchVelocityTracker = VelocityTracker.obtain();
				touchVelocityTracker.addMovement(event);
				
				// If we're beyond the threshold for a touch...
				if (travelSquared > (touchSlop * touchSlop)) {
					// We are now dragging
					// If we weren't already, cancel the touch event for the
					// child as we will definitely be handling it now
					if (!isDragging) {
						isDragging = true;
						event.setAction(MotionEvent.ACTION_CANCEL);
						childLayout.dispatchTouchEvent(event);
					}
				} else {
					// Haven't hit the threshold yet, so keep passing the touch along
					childLayout.dispatchTouchEvent(event);
				}
				break;
			}
			// Invalidate so we redraw ourselves
			invalidate();
			// Return that we handled the touch
			return true;
		}
		// If we are closed, trap the touch so the user can't click through
		return !open;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// Draw normally, off setting the view according to the current
		// translation if we are dragging
		if (isDragging) {
			canvas.save();
			canvas.translate(dragOffset, 0);
			super.dispatchDraw(canvas);
			canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
	}
}
