package com.raizlabs.baseutils;

/**
 * 
 * A class of math utility functions
 * 
 * @author Dylan James
 *
 */
public class Math {
	/**
	 * Returns a linearly interpolated value t percent of the way between the
	 * given start and end values.
	 * @param start The start value
	 * @param end The end value
	 * @param t The percentage along the way to end (0 being the start, 1 being
	 * the end)
	 * @return The linearly interpolated value
	 */
	public static int lerp(int start, int end, float t) {
		return (int) (start + (end - start) * t); 
	}
	
	/**
	 * Returns a linearly interpolated value t percent of the way between the
	 * given start and end values.
	 * @param start The start value
	 * @param end The end value
	 * @param t The percentage along the way to end (0 being the start, 1 being
	 * the end)
	 * @return The linearly interpolated value
	 */
	public static float lerp(float start, float end, float t) {
		return start + (end - start) * t;
	}
}
