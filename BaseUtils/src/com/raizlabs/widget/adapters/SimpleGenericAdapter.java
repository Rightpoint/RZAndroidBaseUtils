package com.raizlabs.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

/**
 * Simple {@link Adapter} class that handles views for a specified type of item.
 * This implementation utilizes recycled views, but only supports one view layout.
 * @author Dylan James
 *
 * @param <T> The type of item that views will represent.
 */
public abstract class SimpleGenericAdapter<T> extends ListBasedAdapter<T> {
	private LayoutInflater inflater;
	private int layoutResID;
	
	/**
	 * Constructs a {@link SimpleGenericAdapter} that uses the given
	 * resource for its views.
	 * @param context A {@link LayoutInflater} to use to inflate new views.
	 * @param layoutResID The resource ID of the layout to use for
	 * item views.
	 */
	public SimpleGenericAdapter(LayoutInflater inflater, int layoutResID) {
		super();
		this.inflater = inflater;
		this.layoutResID = layoutResID;
	}
	
	/**
	 * Constructs a {@link SimpleGenericAdapter} that uses the given
	 * resource for its views.
	 * @param context A {@link Context} to use to inflate new views.
	 * @param layoutResID The resource ID of the layout to use for
	 * item views.
	 */
	public SimpleGenericAdapter(Context context, int layoutResID) {
		this(LayoutInflater.from(context), layoutResID);
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layoutResID, null);
		}
		
		populateView(get(position), convertView);
		return convertView;
	}
	
	/**
	 * Called to populate the given item into the given view. Note that this
	 * may be a fresh view, or a recycled one which previously represented a
	 * different item. This is guaranteed to be the view specified by the
	 * layout resource ID.
	 * @param item The item whose data to populate.
	 * @param view The {@link View} to populate the data into.
	 */
	protected abstract void populateView(T item, View view);
}
