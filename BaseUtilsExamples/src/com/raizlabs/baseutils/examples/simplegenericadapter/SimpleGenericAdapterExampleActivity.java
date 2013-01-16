package com.raizlabs.baseutils.examples.simplegenericadapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.raizlabs.baseutils.examples.R;
import com.raizlabs.widget.adapters.SimpleGenericAdapter;

public class SimpleGenericAdapterExampleActivity extends Activity {
	private int currentCount = 0;
	private SimpleGenericAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simplegenericadapter);
		// Grab the view to bind the adapter to
		ListView listView = (ListView) findViewById(R.id.simpleGenericAdapter_listView);

		// Create and bind an adapter which displays views representing strings
		adapter = new SimpleGenericAdapter<String>(this, R.layout.simple_text_item_layout) {
			@Override
			protected void populateView(String item, View view) {
				// We know this view is the one in the layout file which is
				// really just a TextView. Alternatively, we could use
				// view.findViewById to find the views we want
				TextView text = (TextView) view;
				// Simply set the text in the label
				text.setText(item);
			}
		};
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.remove(position);
			}
		});
		
		// Handle the add 1 button
		findViewById(R.id.add1Button).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				adapter.add(getNextString());
			}
		});
		
		// Handle the add 5 button
		findViewById(R.id.add5Button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> strings = new LinkedList<String>();
				for (int i = 0; i < 5; ++i) {
					strings.add(getNextString());
				}
				adapter.addAll(strings);
			}
		});
		
		// Handle the clear button
		findViewById(R.id.clearButton).setOnClickListener(new OnClickListener() {	
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
