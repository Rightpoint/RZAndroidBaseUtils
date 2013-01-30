package com.raizlabs.baseutils.examples.asyncdrawable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.raizlabs.baseutils.examples.R;
import com.raizlabs.graphics.drawable.async.AsyncDrawableTask;
import com.raizlabs.graphics.drawable.async.AsyncDrawableUtils;
import com.raizlabs.graphics.drawable.async.BaseAsyncDrawableTask;
import com.raizlabs.widget.adapters.SimpleGenericAdapter;

public class AsyncDrawableListExampleActivity extends Activity {
	private static final int LIST_SIZE = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asyncdrawable_list);
		
		// Populate the list view with delayed drawables for each item
		ListView listView = (ListView) findViewById(R.id.asyncdrawable_list_list);
		// Attach an adapter that loads cells with a delayed image in them
		// See the comments on DelayedDrawableAdapter and DelayedDrawable below for more info
		DelayedDrawableAdapter adapter = new DelayedDrawableAdapter(this, R.layout.simpleviewholderstrategy_row);
		listView.setAdapter(adapter);
		
		// Populate a list of 200 integers, which will be the ID of each item
		List<Integer> items = new ArrayList<Integer>(LIST_SIZE);
		for (int i = 0; i < LIST_SIZE; ++i) {
			items.add(i);
		}
		
		adapter.loadItemList(items);
	}	
	
	// Pool to run our background execution/fetching
	ExecutorService threadPool = Executors.newFixedThreadPool(5);
	
	/**
	 * Adapter implementation which binds rows to a DelayedDrawable as declared below
	 */
	private class DelayedDrawableAdapter extends SimpleGenericAdapter<Integer> {

		public DelayedDrawableAdapter(Context context, int layoutResID) {
			super(context, layoutResID);
		}

		@Override
		protected void populateView(final Integer item, View view) {
			// Grab the view that we'll be putting the image in
			View imgView = view.findViewById(R.id.simpleStrategy_row_color);
			// Create a drawable for that view, and bind it to the 
			final DelayedDrawable drawable = new DelayedDrawable(imgView, item, 2000);
			// If there is work to do, execute the task on our thread pool
			if (drawable.bind()) {
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						drawable.execute();
					}
				});
			}
			// Bind our click listener so clicking the image cancels the task
			imgView.setOnClickListener(imageViewClickListener);
			
			((TextView) view.findViewById(R.id.simpleStrategy_row_title)).setText(String.format("Item %s", item));
			((TextView) view.findViewById(R.id.simpleStrategy_row_subTitle)).setText("");
		}
		
		private OnClickListener imageViewClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// On click, try to grab the async drawable and cancel it
				AsyncDrawableTask<?> task = AsyncDrawableUtils.getTask(v);
				if (task != null) {
					task.cancel();
				}
			}
		};
	}

	/**
	 * A cache for the DelayedDrawables to know which items are already loaded 
	 */
	HashSet<Integer> loadedCache = new HashSet<Integer>();
	
	/**
	 * Implementation of an {@link AsyncDrawableTask} which just pauses for a given
	 * period of time to simulate work and then loads a given image resource, but is 
	 * unique by some ID. Also checks against the local cache for items which are
	 * already loaded, and returns their drawable immediately.
	 * 
	 * Note that in cases where we are reassigning the Drawable to the same view we
	 * will automatically see that the existing task is already set up to do the work.
	 * In this example though, the views are being recycled by the ListView. This means
	 * when we scroll away from Item X, when we scroll back to it we may get a different
	 * view which was bound to X+1 or X+2 etc. and the work will not match. Because of
	 * this, we are caching which IDs have been loaded already.
	 */
	private class DelayedDrawable extends BaseAsyncDrawableTask<Integer> {
		private Integer id;
		private long delay;

		/**
		 * Constructs a DelayedDrawable for the given parameters
		 * @param view The view to bind to
		 * @param id The unique identifier of the item, to be used by the cache
		 * @param delayMillis
		 */
		public DelayedDrawable(View view, Integer id, long delayMillis) {
			super(view);
			this.id = id;
			delay = delayMillis;
		}

		@Override
		public Integer getKey() {
			// Use the resource ID as our key
			return id;
		}

		@Override
		protected Drawable onBind() {
			// If our ID is already in the cache, this row has already been
			// loaded, return the image instantly
			if (loadedCache.contains(id)) {
				return getResources().getDrawable(R.drawable.ic_launcher);
			}
			return null;
		}

		@Override
		protected Drawable doExecute() {
			// If our ID still isn't in the cache, simulate work -> sleep
			if (!loadedCache.contains(id)) {
				// We will be done at the current time + the delay
				final long endTime = SystemClock.elapsedRealtime() + delay;
				// Sleep until the end time to simulate work
				while (SystemClock.elapsedRealtime() < endTime) {
					try {
						synchronized (this) {
							// Wait on this for the remaining time
							// Do this through a wait to allow ourselves to be
							// woken up via a notify
							this.wait(endTime - SystemClock.elapsedRealtime());
						}
					} catch (InterruptedException e) { }

					// If we get woken up because of a cancel, return early
					if (isCancelled()) return null;
				}
			}
			// Add the id to the cache of loaded ids
			loadedCache.add(id);
			// Use the view to gain access to the resources.
			// If it's null, we won't be setting any drawable anyway
			View view = getView();
			if (view != null) {
				return view.getResources().getDrawable(R.drawable.ic_launcher);
			}
			return null;
		}

		@Override
		protected Drawable getLoadingDrawable() {
			// Use BLUE to represent loading
			return new ColorDrawable(Color.BLUE);
		}

		@Override
		protected Drawable getCancelledDrawable() {
			// Use RED to represent cancelled
			return new ColorDrawable(Color.RED);
		}

		@Override
		protected void onCancel() {
			// Notify ourselves to allow us to break early
			synchronized (this) {
				notify();
			}
		}

	}
}
