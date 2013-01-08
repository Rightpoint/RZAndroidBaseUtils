package com.raizlabs.database;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import android.database.Cursor;

/**
 * {@link Iterable} implementation which iterates over the items in a {@link Cursor}.
 * @author Dylan James
 *
 * @param <T> The type of object stored in the {@link Cursor}.
 */
public abstract class CursorIterable<T> implements Iterable<T>, Closeable {

	private CursorIterator<T> iterator;
	
	/**
	 * Creates a {@link CursorIterable} that iterates over the rows of the given
	 * {@link Cursor}.
	 * @param cursor The {@link Cursor} to iterate over.
	 */
	public CursorIterable(Cursor cursor) {
		iterator = new CursorIterator<T>(cursor) {
			@Override
			protected T getCurrentItem(Cursor cursor) {
				return CursorIterable.this.getCurrentItem(cursor);
			}
		};
	}
	
	@Override
	public Iterator<T> iterator() {
		return iterator;
	}

	@Override
	public void close() throws IOException {
		if (iterator != null) iterator.close();
	}
	
	/**
	 * Gets the item at the current row in the cursor. Implementation should only
	 * read the current columns, and should be sure not to move the cursor to the
	 * next row, as this will cause items to be skipped.
	 * @param cursor The {@link Cursor} to read values from.
	 * @return The object at the current row in the cursor.
	 */
	protected abstract T getCurrentItem(Cursor cursor);
}
