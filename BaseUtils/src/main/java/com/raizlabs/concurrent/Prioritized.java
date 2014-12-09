package com.raizlabs.concurrent;

import java.util.Comparator;

/**
 * Indicates that a class has a priority.
 */
public interface Prioritized {
	/**
	 * Some predefined priority values.
	 */
	public static final class Priority {
		public static final int EXTREMELY_LOW = -1000;
		public static final int BACKGROUND = -300;
		public static final int LESS_FAVORABLE = -50;
		public static final int NORMAL = 0;
		public static final int MORE_FAVORABLE = 50;
		public static final int FOREGROUND = 300;
		public static final int IMMEDIATE = 1000;
	}
	
	/**
	 * {@link Comparator} implementation which will put the higher priority
	 * {@link Prioritized} objects first.
	 */
	public static final Comparator<Prioritized> COMPARATOR_HIGH_FIRST = new Comparator<Prioritized>() {
		public int compare(Prioritized lhs, Prioritized rhs) {
			final int lhp = lhs.getPriority();
			final int rhp = rhs.getPriority();
			
			if (lhp == rhp) return 0;
			if (lhp > rhp) return -1;
			else return 1;
		}
	};
	
	/**
	 * {@link Comparator} implementation which will put the lower priority
	 * {@link Prioritized} objects first.
	 */
	public static final Comparator<Prioritized> COMPARATOR_LOW_FIRST = new Comparator<Prioritized>() {
		public int compare(Prioritized lhs, Prioritized rhs) {
			return - COMPARATOR_HIGH_FIRST.compare(lhs, rhs);
		}
	};
	
	/**
	 * Gets the priority of this object.
	 * @return The priority of this object.
	 */
	public int getPriority();
}
