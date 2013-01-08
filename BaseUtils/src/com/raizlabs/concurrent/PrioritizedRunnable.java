package com.raizlabs.concurrent;

import java.util.Comparator;

/**
 * Interface which declares a {@link Runnable} which is {@link Prioritized}.
 * @author Dylan James
 */
public interface PrioritizedRunnable extends Runnable, Prioritized {
	
	/**
	 * {@link Comparator} implementation which will put the higher priority
	 * objects first.
	 */
	public static final Comparator<PrioritizedRunnable> COMPARATOR_HIGH_FIRST = new Comparator<PrioritizedRunnable>() {
		public int compare(PrioritizedRunnable lhs, PrioritizedRunnable rhs) {
			final int lhp = lhs.getPriority();
			final int rhp = rhs.getPriority();
			
			if (lhp == rhp) return 0;
			if (lhp > rhp) return -1;
			else return 1;
		}
	};
	
	/**
	 * {@link Comparator} implementation which will put the lower priority
	 * objects first.
	 */
	public static final Comparator<PrioritizedRunnable> COMPARATOR_LOW_FIRST = new Comparator<PrioritizedRunnable>() {
		public int compare(PrioritizedRunnable lhs, PrioritizedRunnable rhs) {
			return - COMPARATOR_HIGH_FIRST.compare(lhs, rhs);
		}
	};
}
