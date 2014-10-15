package com.raizlabs.events;

import android.os.Handler;
import android.os.Looper;

import com.raizlabs.functions.DelegateSet;
import com.raizlabs.threading.ThreadingUtils;

/**
 * An {@link Event} which calls all listeners on a {@link Handler} when it is
 * raised instead of doing so on the calling thread. If the event is raised on
 * the thread it is bound to, it will be called inline.
 *
 * @param <T> The parameter type of the event.
 */
public class HandlerEvent<T> extends Event<T> {

	private Handler handler;
	
	/**
	 * Constructs a {@link HandlerEvent} which will be dispatched on the
	 * current thread. Throws an exception if this thread is not a looper
	 * thread.
	 * @throws RuntimeException if called from a non-looper thread.
	 */
	public HandlerEvent() {
		super();
		Looper looper = Looper.myLooper();
		if (looper != null) {
			this.handler = new Handler(looper);
		} else {
			throw new RuntimeException("Can't create a default " + getClass().getSimpleName() + " constructor from a non-looper thread.");
		}
	}
	
	/**
	 * Constructs a {@link HandlerEvent} which will be dispatched via the given
	 * {@link Handler}.
	 * @param handler The handler to dispatch the event on.
	 */
	public HandlerEvent(Handler handler) {
		super();
		this.handler = handler;
	}
	
	@Override
	protected void performRaiseEvent(final DelegateSet<T> listeners, final T params) { 
		ThreadingUtils.runOnHandler(new Runnable() {
			@Override
			public void run() {
				listeners.execute(params);
			}
		}, handler);
	}
}
