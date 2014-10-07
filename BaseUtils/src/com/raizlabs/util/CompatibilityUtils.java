package com.raizlabs.util;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

public class CompatibilityUtils {
	/**
	 * @return True if the current device supports the Honeycomb+ Animation APIs
	 */
	public static boolean supportsHoneycombAnimation() {
		return VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
	}
}