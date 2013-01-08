package com.raizlabs.content.sharing;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * Utility class which contains utilities for sharing.
 * 
 * @author Dylan James
 *
 */
public class SharingUtils {
	
	/**
	 * Gets an {@link Intent} to share the given {@link File} using the mime type
	 * specified by the {@link File}. This may limit the activities which can handle
	 * the {@link Intent} or may fail if the extension isn't recognized.
	 * @see MimeTypeMap#getMimeTypeFromExtension(String)
	 * @see #getShareFileIntentUsingGenericMimeType(File)
	 * @see #getShareFileIntent(File, String)
	 * @param file The {@link File} to share.
	 * @return The {@link Intent} to share the given file.
	 */
	public static Intent getShareFileIntentUsingFileMimeType(File file) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		return getShareFileIntent(file, mimeType);
	}
	
	/**
	 * Gets an {@link Intent} to share the given {@link File} using a generic
	 * mime type which can be handled by many activities.
	 * @see #getShareFileIntent(File, String)
	 * @see #getShareFileIntentUsingFileMimeType(File)
	 * @param file The {@link File} to share.
	 * @return The {@link Intent} to share the given file.
	 */
	public static Intent getShareFileIntentUsingGenericMimeType(File file) {
		return getShareFileIntent(file, "text/plain");
	}
	
	/**
	 * Gets an {@link Intent} to share the given {@link File} using the given mime type.
	 * @see #getShareFileIntentUsingFileMimeType(File)
	 * @see #getShareFileIntentUsingGenericMimeType(File)
	 * @param file The {@link File} to share.
	 * @param mimeType The Mime Type to set as the type for the {@link Intent}.
	 * @return The {@link Intent} to share the given file.
	 */
	public static Intent getShareFileIntent(File file, String mimeType) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		intent.setType(mimeType);
		return intent;
	}
}
