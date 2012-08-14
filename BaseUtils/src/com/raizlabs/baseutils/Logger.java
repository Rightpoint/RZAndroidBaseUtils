package com.raizlabs.baseutils;

import android.util.Log;

public class Logger {
	public static boolean ENABLED = true;
	
	public static void d(String tag, String msg) {
		if (ENABLED) { Log.d(tag, msg); }
	}
	
	public static void e(String tag, String msg) {
		if (ENABLED) { Log.e(tag, msg); }
	}
	
	public static void i(String tag, String msg) {
		if (ENABLED) { Log.i(tag, msg);  }
	}
	
	public static void v(String tag, String msg) {
		if (ENABLED) { Log.v(tag, msg); }
	}
	
	public static void w(String tag, String msg) {
		if (ENABLED) { Log.w(tag, msg); }
	}
}
