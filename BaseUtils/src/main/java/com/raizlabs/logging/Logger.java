package com.raizlabs.logging;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

/**
 * Class which logs to the built in Android {@link Log}, but provides a
 * mechanism to disable logging at different levels.
 */
public class Logger {
	
	private static int logFlags = LogLevel.WARNINGS | LogLevel.ERRORS;
	/**
	 * Sets the log levels which will be logged to the Android {@link Log}.
	 * @param logLevel A bitmask of the desired levels, using values defined in
	 * {@link LogLevel}.
	 */
	public static void setLogLevel(int logLevel) { logFlags = logLevel; }
	
	public static void v(String tag, String msg) {
		if ((logFlags & LogLevel.VERBOSE) > 0) {
			Log.v(tag, msg);
		}
	}
	
	public static void v(String tag, String msg, Throwable tr) {
		if ((logFlags & LogLevel.VERBOSE) > 0) {
			Log.v(tag, msg, tr);
		}
	}
	
	public static void d(String tag, String msg) {
		if ((logFlags & LogLevel.DEBUG) > 0) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String tag, String msg, Throwable tr) {
		if ((logFlags & LogLevel.DEBUG) > 0) {
			Log.d(tag, msg, tr);
		}
	}
	
	public static void i(String tag, String msg) {
		if ((logFlags & LogLevel.INFO) > 0) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String tag, String msg, Throwable tr) {
		if ((logFlags & LogLevel.INFO) > 0) {
			Log.i(tag, msg, tr);
		}
	}
	
	public static void w(String tag, String msg) {
		if ((logFlags & LogLevel.WARNINGS) > 0) {
			Log.w(tag, msg);
		}
	}
	
	public static void w(String tag, String msg, Throwable tr) {
		if ((logFlags & LogLevel.WARNINGS) > 0) {
			Log.w(tag, msg, tr);
		}
	}
	
	public static void e(String tag, String msg) {
		if ((logFlags & LogLevel.ERRORS) > 0) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		if ((logFlags & LogLevel.ERRORS) > 0) {
			Log.e(tag, msg, tr);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static void wtf(String tag, String msg) {
		if ((logFlags & LogLevel.WTF) > 0) {
			Log.wtf(tag, msg);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static void wtf(String tag, String msg, Throwable tr) {
		if ((logFlags & LogLevel.WTF) > 0) {
			Log.wtf(tag, msg, tr);
		}
	}
	
	

	public static class LogLevel {
		public static final int VERBOSE = Integer.parseInt("000001", 2);
	    public static final int DEBUG = Integer.parseInt("000010", 2);
	    public static final int INFO = Integer.parseInt("000100");
	    public static final int WARNINGS = Integer.parseInt("001000", 2);
	    public static final int ERRORS = Integer.parseInt("010000", 2);
	    public static final int WTF = Integer.parseInt("100000", 2);
	    public static final int ALL = VERBOSE | DEBUG | INFO | WARNINGS | ERRORS | WTF;
		public static final int NONE = 0;
	}	
}