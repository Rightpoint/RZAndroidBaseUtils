package com.raizlabs.graphics.drawable.async;

import android.os.Handler;
import android.os.Looper;

class ThreadingHelper {
	private static final Handler uiHandler = new Handler(Looper.getMainLooper());

	static void runOnUIThread(Runnable runnable) {
		if (Looper.getMainLooper().equals(Looper.myLooper())) {
			runnable.run();
		} else {
			uiHandler.post(runnable);
		}
	}
}
