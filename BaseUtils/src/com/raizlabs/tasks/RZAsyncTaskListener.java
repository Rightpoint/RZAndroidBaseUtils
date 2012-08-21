package com.raizlabs.tasks;


/**
 * Abstract class which is used to listen to an {@link RZAsyncTask}
 * @author Dylan James
 *
 * @param <Progress>
 * @param <Result>
 */
public abstract class RZAsyncTaskListener<Progress, Result> {
	/**
	 * Called on the UI thread when the task is cancelled.
	 * @param result
	 */
	public void onCancelled(Result result) { }
	/**
	 * Called on the UI thread after the task does any initialization.
	 * @see RZAsyncTask#doPreExecute()
	 * @param isInstant true if the task will be fast.
	 */
	public void onPreExecute(boolean isInstant) { }
	/**
	 * Called on the UI thread after progress updates.
	 * @param progress
	 */
	public void onUpdateProgress(Progress[] progress) { }
	/**
	 * Called on the UI thread after the task completes.
	 * @param result
	 */
	public void onPostExecute(Result result) { }
}
