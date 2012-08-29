package com.raizlabs.tasks;

import java.util.LinkedList;
import java.util.List;

/**
 * Class which represents Events for {@link RZAsyncTask}s.
 * @author Dylan James
 *
 * @param <Result> The Result type of the {@link RZAsyncTask}.
 */
public class RZAsyncTaskEvent<Progress, Result> {
	private List<RZAsyncTaskListener<Progress, Result>> listeners;
	
	/**
	 * Creates a new RZAsyncTaskEvent.
	 */
	public RZAsyncTaskEvent() {
		listeners = new LinkedList<RZAsyncTaskListener<Progress, Result>>();
	}
	
	/**
	 * Adds an RZAsyncTaskListener to be notified when AsyncTaskEvents happen.
	 * @param listener The listener to be notified.
	 */
	public void addListener(RZAsyncTaskListener<Progress, Result> listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}
	
	/**
	 * Removes an RZEventListener so it will no longer be notified of these
	 * events.
	 * @param listener The RZAsyncTaskListener to be removed.
	 * @return True if the listener was removed, false if it wasn't found.
	 */
	public boolean removeListener(RZAsyncTaskListener<Progress, Result> listener) {
		return listeners.remove(listener);
	}
	
	public void raiseCancelled(Result result) {
		for (RZAsyncTaskListener<Progress, Result> listener : listeners) {
			listener.onCancelled(result);
		}
	}
	
	/**
	 * Raises the PreExecute event on all listeners.
	 * @see RZAsyncTask#doPreExecute()
	 * @param isInstant true if the task will be fast.
	 */
	public void raisePreExecute(boolean isInstant) {
		for (RZAsyncTaskListener<Progress, Result> listener : listeners) {
			listener.onPreExecute(isInstant);
		}
	}
	
	/**
	 * Raises the PostExecute event on all listeners.
	 * @param result The result arguments to be passed to the listeners.
	 */
	public void raisePostExecute(Result result) {
		for (RZAsyncTaskListener<Progress, Result> listener : listeners) {
			listener.onPostExecute(result);
		}
	}
	
	/**
	 * Publishes progress to all listeners.
	 * @param progress The progress to be passed to the listeners.
	 */
	public void publishProgress(Progress... progress) {
		for (RZAsyncTaskListener<Progress, Result> listener: listeners) {
			listener.onUpdateProgress(progress);
		}
	}
}
