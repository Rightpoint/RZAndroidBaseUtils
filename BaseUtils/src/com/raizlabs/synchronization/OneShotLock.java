package com.raizlabs.synchronization;

/**
 * A class which is locked until {@link #unlock()} is called.
 * Calling {@link #waitUntilUnlocked()} will block until this has
 * happened. You may synchronize on this object to lock its state.
 * 
 * @author Dylan James
 *
 */
public class OneShotLock {
	private boolean unlocked;
	/**
	 * @return True if {@link #unlock()} has been called
	 */
	public boolean isUnlocked() { return unlocked; }
	
	public OneShotLock() {
		unlocked = false;
	}
	
	/**
	 * Unlocks this lock
	 */
	public void unlock() {
		synchronized (this) {
			unlocked = true;
			notifyAll();
		}
	}
	
	/**
	 * Blocks until this {@link OneShotLock} is unlocked via a call
	 * to {@link #unlock()}. If this has already been unlocked, it
	 * will return immediately.
	 */
	public void waitUntilUnlocked() {
		synchronized (this) {
			while (!unlocked) {
				try {
					wait();
				} catch (InterruptedException e) { }
			}
		}
	}
}