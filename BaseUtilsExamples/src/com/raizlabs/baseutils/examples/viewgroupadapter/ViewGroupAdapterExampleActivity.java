package com.raizlabs.baseutils.examples.viewgroupadapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.baseutils.examples.R;
import com.raizlabs.widget.adapters.ViewGroupAdapter;
import com.raizlabs.widget.adapters.ViewGroupAdapter.ItemClickedListener;

/**
 * Simple example of a {@link ViewGroupAdapter} that displays incrementing
 * strings.
 * @author Dylan James
 */
public class ViewGroupAdapterExampleActivity extends Activity {
	private int currentCount = 0;
	private ViewGroupAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewgroupadapter);
		// Grab the view to bind the adapter to
		ViewGroup adapterGroup = (ViewGroup) findViewById(R.id.viewGroupAdapter_adapterLayout);

		// Create and bind an adapter which displays views representing strings
		adapter = new ViewGroupAdapter<String>(adapterGroup) {
			@Override
			protected View createView(String item, LayoutInflater inflater, ViewGroup root) {
				// Inflate a new view
				TextView text = (TextView) inflater.inflate(
						R.layout.viewgroupadapaterexample_itemlayout, root, false);
				// Simply set the text in a label
				text.setText(item);
				return text;
			}
		};
		
		// When an item is clicked in the adapter, remove it.
		adapter.setItemClickedListener(new ItemClickedListener<String>() {
			@Override
			public void onItemClicked(ViewGroupAdapter<String> adapter,
					String item, int index) {
				adapter.removeAt(index);
			}
		});
		
		// Handle the add 1 button
		findViewById(R.id.viewGroupAdapter_addButton).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				adapter.add(getNextString());
			}
		});
		
		// Handle the add 5 button
		findViewById(R.id.viewGroupAdapter_add5Button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> strings = new LinkedList<String>();
				for (int i = 0; i < 5; ++i) {
					strings.add(getNextString());
				}
				adapter.add(strings);
			}
		});
		
		// Handle the clear button
		findViewById(R.id.viewGroupAdapter_clearButton).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				adapter.clear();
			}
		});
	}
	
	/**
	 * @return The next string to display
	 */
	private String getNextString() {
		return Integer.toString(++currentCount);
	}
}
