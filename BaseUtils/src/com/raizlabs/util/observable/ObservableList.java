package com.raizlabs.util.observable;

import java.util.List;

/**
 * Interface for a {@link List} implementation which is also observable via the
 * {@link ObservableData} interface.
 * 
 * @author Dylan James
 *
 * @param <T> The type of data in the list.
 */
public interface ObservableList<T> extends List<T>, ObservableData<ObservableList<T>> { }
