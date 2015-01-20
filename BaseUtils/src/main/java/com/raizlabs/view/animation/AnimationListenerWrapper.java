package com.raizlabs.view.animation;

import java.util.HashSet;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * {@link AnimationListener} implementation which manages a set of
 * other {@link AnimationListener}s. When events are called on this
 * {@link AnimationListenerWrapper}, each of the managed
 * {@link AnimationListener}s are called appropriately.
 * 
 */
public class AnimationListenerWrapper implements AnimationListener {
	private HashSet<AnimationListener> listeners;
	
	/**
	 * Constructs an empty {@link AnimationListenerWrapper}.
	 */
	public AnimationListenerWrapper() {
		listeners = new HashSet<AnimationListener>();
	}
	
	/**
	 * Constructs an {@link AnimationListenerWrapper} and subscribes the
	 * given {@link AnimationListener}.
	 * @param listener An {@link AnimationListener} to subscribe to events.
	 */
	public AnimationListenerWrapper(AnimationListener listener) {
		this();
		addListener(listener);
	}
	
	/**
	 * Constructs an {@link AnimationListenerWrapper} and subscribes all
	 * the given {@link AnimationListener}s to events.
	 * @param listeners
	 */
	public AnimationListenerWrapper(Iterable<AnimationListener> listeners) {
		this();
		synchronized (listeners) {			
			for (AnimationListener listener : listeners) {
				this.listeners.add(listener);
			}
		}
	}
	
	/**
	 * Adds an {@link AnimationListener} to be called on events.
	 * @param listener The {@link AnimationListener} to subscribe.
	 */
	public void addListener(AnimationListener listener) {
		if (listener != null) {
			synchronized (listeners) {				
				listeners.add(listener);
			}
		}
	}
	
	/**
	 * Removes an {@link AnimationListener} so it will not receive
	 * calls on future events.
	 * @param listener The {@link AnimationListener} to remove.
	 * @return True if the listener was removed, false if it wasn't
	 * found or was null.
	 */
	public boolean removeListener(AnimationListener listener) {
		if (listener != null) {
			synchronized (listeners) {
				return listeners.remove(listener);
			}
		}
		return false;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		synchronized (listeners) {
			for (AnimationListener listener : listeners) {
				listener.onAnimationEnd(animation);
			}
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		synchronized (listeners) {
			for (AnimationListener listener : listeners) {
				listener.onAnimationRepeat(animation);
			}
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		synchronized (listeners) {
			for (AnimationListener listener : listeners) {
				listener.onAnimationStart(animation);
			}
		}
	}
}
