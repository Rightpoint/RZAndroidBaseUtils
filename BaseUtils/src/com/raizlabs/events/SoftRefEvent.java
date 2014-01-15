package com.raizlabs.events;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;

/**
 * Class that extends {@link Event} to use SoftReferences to
 * hold any listeners.
 * @author joe.mahon
 *
 * @param <T>
 */
public class SoftRefEvent<T> {
	
	private class KeyedSoftReference<K, X> extends SoftReference<X> {
		private K key;
		
		public KeyedSoftReference(K k, X r) {
			super(r);
			this.key = k;
		}

		public KeyedSoftReference(K k, X r, ReferenceQueue<? super X> q) {
			super(r, q);
			this.key = k;
		}
	}
	
	private SparseArray<KeyedSoftReference<Integer, EventListener<T>>> listeners;
	private HashSet<EventListener<T>> listenersToAdd;
	private HashSet<EventListener<T>> listenersToRemove;
	private boolean raisingEvent;
	
	/**
	 * Creates a new RZEvent
	 */
	public SoftRefEvent() {
		listeners = new SparseArray<KeyedSoftReference<Integer, EventListener<T>>>();
		listenersToAdd = new HashSet<EventListener<T>>();
		listenersToRemove = new HashSet<EventListener<T>>();
		raisingEvent = false;
	}
	
	/**
	 * Adds an RZEventListener to be notified when this event happens.
	 * @param listener The listener to be notified.
	 */
	public void addListener(EventListener<T> listener) {
		if (listener != null) {
			synchronized (this) {
				// If the event is currently being raised, we can't modify the collection
				// Add it to the toAdd collection
				if (raisingEvent) {
					listenersToAdd.add(listener);
				} else {
					listeners.put(listener.hashCode(), new KeyedSoftReference<Integer, EventListener<T>>(listener.hashCode(), listener));
				}
			}
		}
	}
	
	/**
	 * Removes an RZEventListener so it will no longer be notified of
	 * this event.
	 * @param listener The RZEventListener to be removed.
	 * @return True if the listener was removed, false if it wasn't found.
	 */
	public boolean removeListener(EventListener<T> listener) {
		synchronized (this) {
			int key = listener.hashCode();
			
			KeyedSoftReference<Integer, EventListener<T>> listenerRef = listeners.get(key);
			
			// If the event is currently being raised, we can't modify the collection
			// Add it to the toRemove collection
			if (raisingEvent) {
				listenersToRemove.add(listener);
			} else {
				listeners.remove(key);
			}
			
			if (listenerRef == null) {
				return false;
			} else {
				return listenerRef.get() != null;
			}
		}
	}
	
	/**
	 * Raises this event and notifies its listeners.
	 * @param sender The object raising the even.
	 * @param args The arguments to the event which will be passed to the listeners.
	 */
	public void raiseEvent(Object sender, T args) {
		synchronized (this) {
			raisingEvent = true;
			listenersToAdd.clear();
			listenersToRemove.clear();
			
			List<Integer> deadRefIndices = new ArrayList<Integer>();
			for (int i = 0; i < listeners.size(); i++) {
				KeyedSoftReference<Integer, EventListener<T>> listenerRef = listeners.get(i);
				if (listenerRef != null) {
					EventListener<T> listener = listenerRef.get();
					if (listener != null) {
						if (!listenersToRemove.contains(listener)) {
							listener.onEvent(sender, args);
						}
					} else {
						deadRefIndices.add(listenerRef.key);
					}
				}
			}
			
			// remove dead references:
			for (Integer index : deadRefIndices) {
				Log.d("testdebug", "Removing dead reference for key "+index);
				listeners.remove(index);
			}
			
			for (EventListener<T> listener : listenersToAdd) {
				int key = listener.hashCode();
				listeners.put(key, new KeyedSoftReference<Integer, EventListener<T>>(key, listener));
			}
			
			for (EventListener<T> listener : listenersToRemove) {
				listeners.remove(listener.hashCode());
			}
			raisingEvent = false;
		}
	}
	
	/**
	 * Clears all listeners from this event.
	 */
	public void clear() {
		synchronized (this) {
			// If the event is currently being raised, we can't modify the collection
			// Add all of the current listeners to the toRemove collection
			if (raisingEvent) {
				for (int i = 0; i < listeners.size(); i++) {
					KeyedSoftReference<Integer, EventListener<T>> listenerRef = listeners.get(i);
					if (listenerRef != null) {
						EventListener<T> listener = listenerRef.get();
						if (listener != null) {
							listenersToRemove.add(listener);
						}
					}
				}
			} else {
				listeners.clear();
			}
		}
	}
}
