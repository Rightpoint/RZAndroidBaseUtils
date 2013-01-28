package com.raizlabs.baseutils.examples.asyncdrawable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.raizlabs.baseutils.R;
import com.raizlabs.graphics.drawable.async.AsyncDrawableTask;
import com.raizlabs.graphics.drawable.async.AsyncDrawableUtils;
import com.raizlabs.graphics.drawable.async.BaseAsyncDrawableTask;

public class AsyncDrawableExampleActivity extends Activity {
	ImageView img1, img2, img3, img4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asyncdrawable);
		
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);
		
		img1.setOnClickListener(imageViewClickListener);
		img2.setOnClickListener(imageViewClickListener);
		img3.setOnClickListener(imageViewClickListener);
		img4.setOnClickListener(imageViewClickListener);
		
		findViewById(R.id.asyncdrawable_load).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load();
			}
		});
		
		findViewById(R.id.asyncdrawable_reset).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
			}
		});
	}
	
	private OnClickListener imageViewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// On click, try to grab the async drawable and cancel it
			if (v instanceof ImageView) {
				AsyncDrawableTask<?> task = AsyncDrawableUtils.getTask((ImageView)v);
				if (task != null) {
					task.cancel();
				}
			}
		}
	};

	private void reset() {
		// Load a standard drawable into each image
		Drawable defaultDrawable = new ColorDrawable(Color.WHITE);
		img1.setImageDrawable(defaultDrawable);
		img2.setImageDrawable(defaultDrawable);
		img3.setImageDrawable(defaultDrawable);
		img4.setImageDrawable(defaultDrawable);
	}
	
	private void load() {
		// For each view, bind a new DelayedDrawable
		final AsyncDrawableTask<?> draw1 = new DelayedDrawable(img1, R.drawable.ic_launcher, 2000);
		draw1.bind();
		final AsyncDrawableTask<?> draw2 = new DelayedDrawable(img2, R.drawable.ic_launcher, 3000);
		draw2.bind();
		final AsyncDrawableTask<?> draw3 = new DelayedDrawable(img3, R.drawable.ic_launcher, 4000);
		draw3.bind();
		final AsyncDrawableTask<?> draw4 = new DelayedDrawable(img4, R.drawable.ic_launcher, 5000);
		draw4.bind();

		// Execute each one on a new thread
		new Thread(new Runnable() {
			@Override
			public void run() {	
				draw1.execute();
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {	
				draw2.execute();
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {	
				draw3.execute();
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {	
				draw4.execute();
			}
		}).start();
	}
	
	/**
	 * Implementation of an {@link AsyncDrawableTask} which just pauses for a given
	 * period of time to simulate work and then loads a given image resource
	 */
	private static class DelayedDrawable extends BaseAsyncDrawableTask<Integer> {
		private int resID;
		private long delay;

		public DelayedDrawable(ImageView view, int resourceID, long delayMillis) {
			super(view);
			resID = resourceID;
			delay = delayMillis;
		}

		@Override
		public Integer getKey() {
			// Use the resource ID as our key
			return resID;
		}

		@Override
		protected Drawable onBind() { 
			return null;
		}

		@Override
		protected Drawable doExecute() {
			// Sleep to simulate work
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) { }
			Log.d(getClass().getName(), "Sleep complete");
			// Use the view to gain access to the resources.
			// If it's null, we won't be setting any drawable anyway
			View view = getView();
			if (view != null) {
				Log.d(getClass().getName(), "View non-null");
				return view.getResources().getDrawable(resID);
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
		protected void onCancel() { }

	}
}
