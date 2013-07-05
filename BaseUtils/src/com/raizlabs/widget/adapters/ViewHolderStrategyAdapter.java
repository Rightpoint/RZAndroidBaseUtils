package com.raizlabs.widget.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.widget.utils.ViewHolderStrategy;
import com.raizlabs.widget.utils.ViewHolderStrategyUtils;

/**
 * Adapter class which leverages an {@link ViewHolderStrategy} to populate
 * its views.
 * @author Dylan James
 *
 * @param <Item> The type of item that views will represent.
 * @param <Holder> The type of the view holder the {@link ViewHolderStrategy} uses.
 */
public class ViewHolderStrategyAdapter<Item> extends ListBasedAdapter<Item>{
	private ViewHolderStrategy<Item, ?> strategy;
	
	/**
	 * Creates a {@link ViewHolderStrategyAdapter} that utilizes the given
	 * {@link ViewHolderStrategy}.
	 * @param strategy The strategy to use to create and populate views.
	 */
	public ViewHolderStrategyAdapter(ViewHolderStrategy<Item, ?> strategy) {
		this.strategy = strategy;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return strategy.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		Item item = get(position);
		return strategy.isEnabled(item);
	}

	@Override
	public int getItemViewType(int position) {
		Item item = get(position);
		return strategy.getItemViewType(item);
	}

	@Override
	public int getViewTypeCount() {
		return strategy.getViewTypeCount();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item item = get(position);
		if (convertView == null) {
			// If we didn't get a recycled view, create a new one
			return ViewHolderStrategyUtils.createAndPopulateView(strategy, item, parent);
		} else {
			// We have a recycled view, so just repopulate it
			ViewHolderStrategyUtils.populateView(strategy, convertView, item);
			return convertView;
		}
	}
	
	public ViewHolderStrategy<Item, ?> getStrategy() {
		return this.strategy;
	}
}
