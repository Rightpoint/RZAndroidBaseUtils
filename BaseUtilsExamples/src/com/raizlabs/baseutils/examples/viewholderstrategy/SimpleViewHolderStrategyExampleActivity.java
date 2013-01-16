package com.raizlabs.baseutils.examples.viewholderstrategy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.raizlabs.baseutils.examples.R;
import com.raizlabs.widget.adapters.ViewHolderStrategyAdapter;
import com.raizlabs.widget.utils.SimpleViewHolderStrategy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SimpleViewHolderStrategyExampleActivity extends Activity {
	/**
	 * Class which represents the data for one of our rows
	 */
	public static class RowData {
		public int Color;
		public String Title;
		public String SubTitle;
		
		public RowData(int color, String title, String subTitle) {
			this.Color = color;
			this.Title = title;
			this.SubTitle = subTitle;
		}
	}

	private int currentCount = 0;
	/**
	 * The adapter that contains our data
	 */
	ViewHolderStrategyAdapter<RowData, RowViewHolder> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simpleviewholderstrategy);

		// Grab the list view we will be populating
		ListView listView = (ListView) findViewById(R.id.simpleViewHolderStrategy_listView);
		// Construct our strategy implementation
		StrategyImplementation strategy = new StrategyImplementation();
		// Create an adapter based on our strategy
		adapter = new ViewHolderStrategyAdapter<RowData, RowViewHolder>(strategy);
		// Attach our adapter to the list view
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
				adapter.add(getNextRow());
			}
		});

		// Handle the add 5 button
		findViewById(R.id.add5Button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<RowData> rows = new LinkedList<RowData>();
				for (int i = 0; i < 5; ++i) {
					rows.add(getNextRow());
				}
				adapter.addAll(rows);
			}
		});

		// Handle the clear button
		findViewById(R.id.clearButton).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				adapter.clear();
			}
		});
		
		for (int i = 0; i < 10; ++i) {
			adapter.add(getNextRow());
		}
	}
	
	static final Random rand = new Random();
	static final int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA,
		Color.RED, Color.CYAN, Color.YELLOW };
	static final DateFormat timeFormat = new SimpleDateFormat("h:m:s a");
	/**
	 * @return The next row to display
	 */
	RowData getNextRow() {
		int color = colors[rand.nextInt(colors.length)];
		String title = String.format("Row %s", getNextString()); 
		String subTitle = timeFormat.format(new Date());
		return new RowData(color, title, subTitle);
	}
	
	/**
	 * @return The next string to display
	 */
	private String getNextString() {
		return Integer.toString(++currentCount);
	}
	
	/**
	 * Class which holds references to all the views we need to update.
	 */
	public static class RowViewHolder {
		public View colorView;
		public TextView titleView;
		public TextView subTitleView;
	}
	
	/**
	 * Implementation of the SimpleViewHolderStrategy for our example functionality.
	 */
	private static class StrategyImplementation extends SimpleViewHolderStrategy<RowData, RowViewHolder>  {
		@Override
		public RowViewHolder createHolder(RowData item) {
			return new RowViewHolder();
		}

		@Override
		public void populateHolder(View view, RowViewHolder holder) {
			// Find each view we need
			holder.colorView = view.findViewById(R.id.simpleStrategy_row_color);
			holder.titleView = (TextView) view.findViewById(R.id.simpleStrategy_row_title);
			holder.subTitleView = (TextView) view.findViewById(R.id.simpleStrategy_row_subTitle);
		}

		@Override
		public void updateView(RowData item, RowViewHolder holder) {
			// Update each view via the holder
			holder.colorView.setBackgroundColor(item.Color);
			holder.titleView.setText(item.Title);
			holder.subTitleView.setText(item.SubTitle);
		}

		@Override
		protected int getLayoutResID(RowData item) {
			return R.layout.simpleviewholderstrategy_row;
		}
	}
}
