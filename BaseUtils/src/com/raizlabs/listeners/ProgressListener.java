package com.raizlabs.listeners;

/**
 * A listener which is called on progress updates.
 */
public interface ProgressListener {
	/**
	 * Called when the progress is updated.
	 * 
	 * @param currentProgress How far along we are.
	 * @param maxProgress What the max value of progress will be, or negative if unknown
	 */
	public void onProgressUpdate(long currentProgress, long maxProgress);
}
