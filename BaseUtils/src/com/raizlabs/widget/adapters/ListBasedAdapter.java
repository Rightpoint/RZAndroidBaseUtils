package com.raizlabs.widget.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.widget.Adapter;
import android.widget.BaseAdapter;

/**
 * Common base class implementation of a {@link Adapter} that is backed
 * by a {@link List}. This class also implements {@link List} to support the
 * same functionality, while still functioning as an {@link Adapter}.
 * @author Dylan James
 *
 * @param <T> The type of item that views will represent.
 */
public abstract class ListBasedAdapter<T> extends BaseAdapter implements List<T> {
	private List<T> mList;
	/**
	 * @return The {@link List} of items in this adapter.
	 */
	protected List<T> getItemsList() { return mList; }
	/**
	 * Sets the {@link List} of items in this adapter.
	 * @param list The {@link List} of items to use.
	 */
	protected void setItemsList(List<T> list) {
		if (list == null) list = new LinkedList<T>();
		mList = list;
		notifyDataSetChanged();
	}
	
	/**
	 * Constructs an empty {@link ListBasedAdapter}.
	 */
	protected ListBasedAdapter() {
		setItemsList(null);
	}
	
	/**
	 * Constructs a {@link ListBasedAdapter} which contains the given list.
	 * @param list The list of items to use.
	 */
	protected ListBasedAdapter(List<T> list) {
		setItemsList(list);
	}
	
	/**
	 * Loads the given {@link List} into this adapter. This will use the same
	 * reference, so any changes to the source list will be reflected by the
	 * adapter whenever the data is repopulated. See
	 * {@link #notifyDataSetChanged()}. 
	 * @param list The {@link List} to load.
	 */
	public void loadItemList(List<T> list) {
		setItemsList(list);
	}
	
	/**
	 * Loads the given items as the contents of this adapter.
	 * @param items The {@link Collection} of items to load.
	 */
	public void loadItems(Collection<T> items) {
		List<T> data = new ArrayList<T>(items.size());
		for (T item : items) {
			data.add(item);
		}
		setItemsList(data);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public void add(int location, T object) {
		mList.add(location, object);
		notifyDataSetChanged();
	}

	@Override
	public boolean add(T object) {
		final boolean result = mList.add(object);
		notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean addAll(int location, Collection<? extends T> collection) {
		final boolean result = mList.addAll(location, collection);
		if (result) notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		final boolean result = mList.addAll(collection);
		if (result) notifyDataSetChanged();
		return result;
	}

	@Override
	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	@Override
	public boolean contains(Object object) {
		return mList.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return mList.containsAll(collection);
	}

	@Override
	public T get(int location) {
		return mList.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return mList.indexOf(object);
	}

	@Override
	public Iterator<T> iterator() {
		return mList.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return mList.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return mList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		return mList.listIterator();
	}

	@Override
	public T remove(int location) {
		T result = mList.remove(location);
		notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean remove(Object object) {
		boolean result = mList.remove(object);
		if (result) notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = mList.removeAll(collection);
		if (result) notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean result = mList.retainAll(collection);
		if (result) notifyDataSetChanged();
		return result;
	}

	@Override
	public T set(int location, T object) {
		T result = mList.set(location, object);
		if (!result.equals(object)) notifyDataSetChanged();
		return result;
	}

	@Override
	public int size() {
		return mList.size();
	}

	@Override
	public List<T> subList(int start, int end) {
		return mList.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return mList.toArray();
	}

	@Override
	public <S> S[] toArray(S[] array) {
		return mList.toArray(array);
	}

}
