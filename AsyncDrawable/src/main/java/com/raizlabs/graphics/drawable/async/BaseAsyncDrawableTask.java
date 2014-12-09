package com.raizlabs.graphics.drawable.async;

import java.lang.ref.WeakReference;
import java.util.EventListener;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

/**
 * Base class which can be used for easy implementation of an
 * {@link AsyncDrawableTask}. Allows for different {@link Drawable}s
 * to be shown for the cancelled and loading states.
 * 
 * @param <T> The type of the key used to identify an {@link AsyncDrawableTask}
 * and whether it is trying to accomplish the same thing as another instance.
 */
public abstract class BaseAsyncDrawableTask<T> implements AsyncDrawableTask<T> {
	
	private AsyncDrawableLoadedListener<T> drawableLoadedListener;
	/**
	 * Sets an {@link EventListener} to be called when the execution of this task
	 * is complete.
	 * @param listener The listener to call
	 */
	public void setLoadedListener(AsyncDrawableLoadedListener<T> listener) {
		this.drawableLoadedListener = listener;
	}
	
	private boolean cancelled = false;
	@Override
	public boolean isCancelled() {
		synchronized (this) {
			return cancelled;
		}
	}

	private boolean completed = false;
	@Override
	public boolean isCompleted() {
		synchronized (this) {
			return completed;
		}
	}
	
	@Override
	public void cancel() {
		synchronized (this) {
			cancelled = true;
			// If this task isn't finished, update the drawable
			// to the cancelled drawable
			if (!completed && wrapper.getView() != null) {
				wrapper.update(getCancelledDrawable(), true);
			}
		}
		onCancel();
	}
	
	/**
	 * Wrapper for our current view to abstract the type
	 */
	private ViewWrapper wrapper;
	protected View getView() { return wrapper.getView(); }
	
	/**
	 * Constructs a {@link BaseAsyncDrawableTask} which binds to the given
	 * {@link ImageView}.
	 * @param imageView The image view to bind to.
	 */
	public BaseAsyncDrawableTask(ImageView imageView) {
		// Wrap the view up with a wrapper which sets the image
		wrapper = new ImageWrapper(imageView);
	}
	
	/**
	 * Constructs a {@link BaseAsyncDrawableTask} which binds to the given
	 * {@link View}
	 * @param view The view to bind to.
	 */
	public BaseAsyncDrawableTask(View view) {
		// Wrap the view up with a wrapper which sets the background
		wrapper = new BackgroundWrapper(view);
	}
	
	@Override
	public boolean bind() {
		// Attempt to cancel the existing work
		boolean cancelled = wrapper.cancelExistingWork();
		// If we cancelled the work, set the loading drawable
		if (cancelled) {
			// Default to the bind drawable
			Drawable drawable = onBind();
			// If no drawable was given, fall back to the loading drawable
			// and we are not done
			if (drawable == null) {
				drawable = getLoadingDrawable();
				completed = false;
			} else {
				// We're done early!
				completed = true;
			}
			// Update the view
			wrapper.update(drawable, false);
		}
		return cancelled;
	}
	
	@Override
	public void execute() {
		// If we're already completed, break early
		if (isCompleted()) return;
		// If our view is emptied, we can't do anything, abort
		if (wrapper.getView() == null) return;
		Drawable drawable = null;
		// If we aren't bound to the view, abort
		if (!wrapper.isBoundToView()) return;
		// If we aren't cancelled, start up the work 
		if (!isCancelled()) {
			drawable = doExecute();
			// Work has completed...
			synchronized (this) {
				// If we've been cancelled during the work, set the cancelled
				// drawable
				if (cancelled) {
					drawable = getCancelledDrawable();
				} else {
					// Otherwise we completed successfully
					completed = true;
				}
			}
		} else {
			// We were cancelled, set the cancelled drawable
			drawable = getCancelledDrawable();
		}
		
		// Set our new drawable
		setDrawable(drawable);
		
		if (drawableLoadedListener != null) drawableLoadedListener.onDrawableLoaded(this, drawable);
	}

	/**
	 * Call this when a {@link Drawable} is loaded as the target image. This will set
	 * the {@link Drawable} if we are still bound and haven't been cancelled yet.
	 * @param drawable The {@link Drawable} to set.
	 */
	protected void onDrawableLoaded(Drawable drawable) {
		synchronized (this) {
			if (!isCancelled()) {
				completed = true;
				setDrawable(drawable);
				
				if (drawableLoadedListener != null) drawableLoadedListener.onDrawableLoaded(this, drawable);
			}
		}
	}
	
	/**
	 * Sets the current image to the given {@link Drawable} if we are still bound
	 * @param drawable The {@link Drawable} to set
	 */
	protected void setDrawable(Drawable drawable) {
		// Push our new drawable through the wrapper, and ensure we are still bound
		wrapper.update(drawable, true);
	}
	
	/**
	 * Interface which allows us to update different views polymorphically.
	 * Wraps views of different types and interfaces with them appropriately.
	 */
	private interface ViewWrapper {
		/**
		 * Updates the wrapped view to the given {@link Drawable}.
		 * @param drawable The Drawable to update the view to show.
		 * @param checkIfBound True to double check that the view is still
		 * bound to our task
		 */
		void update(Drawable drawable, boolean checkIfBound);
		/**
		 * Attempts to cancel the existing work associated with the view
		 * @see AsyncDrawableUtils#cancelExistingWork(AsyncDrawableTask, ImageView)
		 * @see AsyncDrawableUtils#cancelExistingWork(AsyncDrawableTask, View)
		 * @return True if there was no work or the work was cancelled, false if
		 * the work is already assigned to another task
		 */
		boolean cancelExistingWork();
		/**
		 * @return The view being wrapped by this wrapper.
		 */
		View getView();
		/**
		 * @return True if this task is still associated with the wrapped view.
		 */
		boolean isBoundToView();
	}
	
	/**
	 * {@link ViewWrapper} implementation which interfaces with the background
	 * of a given view.
	 */
	private class BackgroundWrapper implements ViewWrapper {
		WeakReference<View> viewRef;
		/**
		 * Constructs a {@link BackgroundWrapper} which wraps the given View.
		 * @param view The View to wrap.
		 */
		public BackgroundWrapper(View view) {
			viewRef = new WeakReference<View>(view);
		}
		
		@Override
		public View getView() {
			return viewRef.get();
		}
		
		@Override
		public boolean cancelExistingWork() {
			final View view = getView();
			if (view == null) return false;
			return AsyncDrawableUtils.cancelExistingWork(BaseAsyncDrawableTask.this, view);
		}
		
		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		public void update(Drawable drawable, final boolean checkIfBound) {
			final View view = viewRef.get();
			if (view != null) {
				// If we were told to check, only proceed if we are bound to the view
				if (!checkIfBound || isBoundToView(view)) {
					// Wrap the desired drawable in a wrapper that contains the information
					// about this task
					final AsyncDrawableWrapper<T> newDrawable = 
							new AsyncDrawableWrapper<T>(drawable, BaseAsyncDrawableTask.this);
					// Change the drawable on the UI thread
					ThreadingHelper.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// Double check that we're still the bound task
							if (!checkIfBound || isBoundToView(view)) {
								// JellyBean+ deprecates setBackgroundDrawable
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
									view.setBackground(newDrawable);
								} else {
									view.setBackgroundDrawable(newDrawable);
								}
								onDrawableUpdated(newDrawable.getDrawble());
							}
						}
					});
				}
			}
		}

		@Override
		public boolean isBoundToView() {
			final View view = getView();
			if (view == null) return false;
			return isBoundToView(view);
		}
		
		boolean isBoundToView(View view) {
			// We're bound if our task is the associated task
			return BaseAsyncDrawableTask.this.equals(AsyncDrawableUtils.getTask(view));
		}
	}
	
	/**
	 * {@link ViewWrapper} implementation which interfaces with the image
	 * drawable of a given ImageView.
	 */
	private class ImageWrapper implements ViewWrapper {
		WeakReference<ImageView> viewRef;
		/**
		 * Constructs a {@link BackgroundWrapper} which wraps the given ImageView.
		 * @param imageView The ImageView to wrap.
		 */
		public ImageWrapper(ImageView imageView) {
			viewRef = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		public View getView() {
			return viewRef.get();
		}
		
		@Override
		public boolean cancelExistingWork() {
			ImageView view = viewRef.get();
			if (view == null) return false;
			return AsyncDrawableUtils.cancelExistingWork(BaseAsyncDrawableTask.this, view);
		}
		
		@Override
		public void update(Drawable drawable, final boolean checkIfBound) {
			final ImageView view = viewRef.get();
			if (view != null) {
				// If we were told to check, only proceed if we are bound to the view
				if (!checkIfBound || isBoundToView(view)) {
					// Wrap the desired drawable in a wrapper that contains the information
					// about this task
					final AsyncDrawableWrapper<T> newDrawable = 
							new AsyncDrawableWrapper<T>(drawable, BaseAsyncDrawableTask.this);
					// Change the drawable on the UI thread
					ThreadingHelper.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// Double check that we're still the bound task
							if (!checkIfBound ||isBoundToView(view)) {
								view.setImageDrawable(newDrawable);
								onDrawableUpdated(newDrawable.getDrawble());
							}
						}
					});
				}
			}
		}
		
		@Override
		public boolean isBoundToView() {
			ImageView view = viewRef.get();
			if (view == null) return false;
			return isBoundToView(view);
		}
		
		boolean isBoundToView(ImageView imageView) {
			// We're bound if our task is the associated task
			return BaseAsyncDrawableTask.this.equals(AsyncDrawableUtils.getTask(imageView));
		}
	}
	
	/**
	 * Called when the drawable for the view is updated. This will be called on the UI thread.
	 * @param drawable The drawable which was committed.
	 */
	protected void onDrawableUpdated(Drawable drawable) { }
	
	/**
	 * Called when this task is bound to a view. You may pass a Drawable back
	 * to complete early, though this is run from the UI thread. A good case
	 * for this is if an image is already cached in memory or something simliar.
	 * @return The completed drawable if it can be obtained quickly, otherwise
	 * null.
	 */
	protected abstract Drawable onBind();
	/**
	 * Called to do long running work and obtain the desired Drawable.
	 * @return The resulting Drawable.
	 */
	protected abstract Drawable doExecute();
	/**
	 * Called to get a Drawable to display while the task is running.
	 * @return The Drawable to display.
	 */
	protected abstract Drawable getLoadingDrawable();
	/**
	 * Called to get a Drawable to display when the task is cancelled.
	 * @return The Drawable to display.
	 */
	protected abstract Drawable getCancelledDrawable();
	/**
	 * Called when the task is cancelled.
	 */
	protected abstract void onCancel();
}
