package com.raizlabs.tasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

/**
 * Base class for RZAsyncTasks which provides features such as RZAsyncTaskEvents.
 * Subclasses should override doPreExecute(), doProgressUpdate, and doPostExecute() instead of
 * onPreExecute(), onProgressUpdate, and onPostExecute(). These "do" functions will be called
 * inside the "on" functions.
 * @author Dylan James
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class RZAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	/**
	 * RZAsyncTaskEvents
	 */
	public RZAsyncTaskEvent<Progress, Result> AsyncTaskEvent = new RZAsyncTaskEvent<Progress, Result>();
	/**
	 * Adds a given RZAsyncTaskListener to be notified of AsyncTaskEvents
	 * @param listener
	 */
	public void addRZAsyncTaskListener(RZAsyncTaskListener<Progress, Result> listener) {
		AsyncTaskEvent.addListener(listener);
	}
	/**
	 * Removes a given RZAsyncTaskListener so it will no longer receive these events.
	 * @param listener
	 * @return True if the listener was removed, false if it wasn't found.
	 */
	public boolean removeRZAsyncTaksListner(RZAsyncTaskListener<Progress, Result> listener) {
		return AsyncTaskEvent.removeListener(listener);
	}
	
	protected void onCancelled(Result result) {
		AsyncTaskEvent.raiseCancelled(result);
	}
	
	/**
	 * Gets whether this task should be executed on the
	 * {@link AsyncTask#THREAD_POOL_EXECUTOR} when
	 * {@link #executeInParallel()} is called.
	 * <br><br>
	 * By default, this will return true if the version is >= Honeycomb.
	 * @return True to execute on the {@link AsyncTask#THREAD_POOL_EXECUTOR},
	 * otherwise false.
	 */
	protected boolean executeInParallelOnThreadPoolExecutor() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	}
	
	/**
	 * Executes this {@link RZAsyncTask} in parallel.
	 * <br><br>
	 * Uses {@link #executeOnExecutor(java.util.concurrent.Executor, Object...)}
	 * if {@link #executeInParallelOnThreadPoolExecutor()} returns true, else it will
	 * execute using {@link #execute(Object...)}.
	 * @return This {@link RZAsyncTask}.
	 */
	public RZAsyncTask<Params, Progress, Result> executeInParallel() {
		return executeAsync((Params[])null);
	}
	
	/**
	 * Executes this {@link RZAsyncTask} in parallel.
	 * <br><br>
	 * Uses {@link #executeOnExecutor(java.util.concurrent.Executor, Object...)}
	 * if {@link #executeInParallelOnThreadPoolExecutor()} returns true, else it will
	 * execute using {@link #execute(Object...)}.
	 * @param values The values to be passed to the task.
	 * @return This {@link RZAsyncTask}.
	 */
	@SuppressLint("NewApi")
	public RZAsyncTask<Params, Progress, Result> executeAsync(Params...values) {
		if (executeInParallelOnThreadPoolExecutor()) {
			executeOnExecutor(THREAD_POOL_EXECUTOR, values);
			return this;
		} else {
			execute(values);
			return this;
		}
	}
	
	@Override
	protected final void onPreExecute() {
		super.onPreExecute();
		AsyncTaskEvent.raisePreExecute(doPreExecute());
	}
	
	/**
	 * Called on the UI thread to do pre-execution logic.
	 * @return True if the task will be instant. Return true ONLY IF the task
	 * will run fast enough that we won't need to display a dialog or anything to the
	 * user. If unknown, return false by default.
	 */
	protected boolean doPreExecute() { return false; }

	protected final void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		doProgressUpdate(values);
		AsyncTaskEvent.publishProgress(values);
	}
	
	protected void doProgressUpdate(Progress... values) { }
	
	protected final void onPostExecute(Result result) {
		super.onPostExecute(result);
		doPostExecute(result);
		AsyncTaskEvent.raisePostExecute(result);
	}
	
	protected void doPostExecute(Result result) { }
}
