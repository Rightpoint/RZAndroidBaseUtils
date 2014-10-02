package com.raizlabs.math;


/**
 * A class of math utility functions
 */
public class MathUtils {
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
	
	/**
	 * Returns a value between 0 and 1 which represents how far along the start
	 * and end values the given value is. For example, normalize(0, 100, 50) would
	 * return 0.5 as 50 is halfway between 0 and 100.
	 * <br/><br/>
	 * Note that values larger than end will produce numbers larger than 1 and
	 * values smaller than start will produce negative numbers.
	 * <br/><br/>
	 * Inverse of {@link #lerp(float, float, float)}
	 * @param start The start value
	 * @param end The end value
	 * @param value The value to normalize
	 * @return The value normalized between start and end
	 */
	public static float normalize(float start, float end, float value) {
		return (value - start) / (end - start);
	}
	
	/**
	 * Clamps the given value between the two end points.
	 * @param value The value to clamp.
	 * @param min The minimum allowed value.
	 * @param max The maximum allowed value.
	 * @return The clamped value.
	 */
	public static float clamp(float value, float min, float max) {
		return java.lang.Math.min(max, java.lang.Math.max(min, value));
	}
	
	public static float distanceSquared(float x1, float x2, float y1, float y2) {
		return (float) (java.lang.Math.pow(x2 - x1, 2) + java.lang.Math.pow(y2 - y1, 2));
	}
	
	public static float distance(float x1, float x2, float y1, float y2) {
		return (float) java.lang.Math.sqrt(
				java.lang.Math.pow(x2 - x1, 2)  + java.lang.Math.pow(y2 - y1, 2));
	}
}
