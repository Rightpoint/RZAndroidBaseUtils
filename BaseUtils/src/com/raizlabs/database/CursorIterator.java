package com.raizlabs.database;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import android.database.Cursor;

/**
 * {@link Iterator} implementation which iterates over the items in a {@link Cursor}.
 * 
 * @author Dylan James
 *
 * @param <T> The type of object stored in the {@link Cursor}.
 */
public abstract class CursorIterator<T> implements Iterator<T>, Closeable {

	private Cursor mCursor;

	/**
	 * Creates a {@link CursorIterator} that iterates over the rows of the given
	 * {@link Cursor}.
	 * @param cursor The {@link Cursor} to iterate over.
	 */
	public CursorIterator(Cursor cursor) {
		this.mCursor = cursor;
	}
	
	public void close() throws IOException {
		if (mCursor != null) { 
			mCursor.close();
			mCursor = null;
		}
	}

	@Override
	public boolean hasNext() {
		if (mCursor == null) {
			throw new IllegalStateException("Cannot use a CursorIteator after close() has been called or with a null cursor!");
		}
		return mCursor.getPosition() < mCursor.getCount() - 1;
	}

	@Override
	public T next() {
		if (mCursor == null) {
			throw new IllegalStateException("Cannot use an CursorIterator after close() has been called or with a null cursor!");
		}
		if (mCursor.moveToNext()) {
			return getCurrentItem(mCursor);
		} else {
			return null;
		}
	}

	/**
	 * Gets the item at the current row in the cursor. Implementation should only
	 * read the current columns, and should be sure not to move the cursor to the
	 * next row, as this will cause items to be skipped.
	 * @param cursor The {@link Cursor} to read values from.
	 * @return The object at the current row in the cursor.
	 */
	protected abstract T getCurrentItem(Cursor cursor);

	@Override
	public void remove() {
		throw new UnsupportedOperationException("CursorIterators cannot remove items by default.");
	}

}
