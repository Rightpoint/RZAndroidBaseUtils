package com.raizlabs.baseutils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

public class IOUtils {

	/**
	 * Utility method for pulling plain text from an InputStream object
	 * @param in InputStream object retrieved from an HttpResponse
	 * @return String contents of stream
	 */
	public static String readStream(InputStream in)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try
		{
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		}
		catch(Exception ex) { }
		finally
		{
			safeClose(in);
			safeClose(reader);
		}
		return sb.toString();
	}
	
	/**
	 * Reads the given input stream into a byte array. Note that this is done
	 * entirely in memory, so the input should not be very large.
	 * @param input The stream to read.
	 * @return The byte array containing all the data from the input stream or
	 * null if there was a problem.
	 */
	public static byte[] readStreamBytes(InputStream input) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		if (copyStream(input, outputStream)) {
			return outputStream.toByteArray();
		}
		
		return null;
	}
	
	/**
	 * Feeds the entire input stream into the output stream.
	 * @param input The stream to copy from.
	 * @param output The stream to copy into.
	 * @return True if the copy succeeded, false if it failed.
	 */
	public static boolean copyStream(InputStream input, OutputStream output) {
		return copyStream(input, output, 4096);
	}
	
	/**
	 * Feeds the entire input stream into the output stream.
	 * @param input The stream to copy from.
	 * @param output The stream to copy into.
	 * @param bufferSize The size of the buffer to use.
	 * @return True if the copy succeeded, false if it failed.
	 */
	public static boolean copyStream(InputStream input, OutputStream output, int bufferSize) {
		byte[] buffer = new byte[bufferSize];
		
		try {
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			
			return true;
		} catch (IOException e) {
			Log.w(IOUtils.class.getSimpleName(), "Error copying stream", e);
			return false;
		}
	}

	/**
	 * Safely closes the given {@link Closeable} without throwing
	 * any exceptions due to null pointers or {@link IOException}s.
	 * @param closeable The {@link Closeable} to close.
	 */
	public static void safeClose(Closeable closeable)
	{
		if(closeable != null)
		{
			try
			{
				closeable.close();
			}
			catch (IOException e) { }
		}
	}
	
	/**
	 * Creates an {@link InputStream} from the data in the given string.
	 * @param str The string to set as the contents of the stream.
	 * @return The created {@link InputStream}
	 */
	public static InputStream getInputStream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
	
	
	/**
	 * Deletes the file or directory at the given path using the rm shell
	 * command.
	 * @param path Path to the file to delete.
	 * @param recursive True to do the delete recursively, else false.
	 * @return True if the file existed and was deleted, or false if it didn't
	 * exist.
	 * @throws IOException if the shell execution fails.
	 */
	public static boolean deleteViaShell(String path, boolean recursive) 
			throws IOException {
		File file = new File(path);
		if (file.exists()) {
			String deleleteCommand = (recursive ? "rm -r " : "rm ") + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(deleleteCommand);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Deletes the file or directory at the given path using the rm shell
	 * command.
	 * @param file The {@link File} to delete.
	 * @param recursive True to do the delete recursively, else false.
	 * @return True if the file existed and was deleted, or false if it didn't
	 * exist.
	 * @throws IOException if the shell execution fails.
	 */
	public static boolean deleteViaShell(File file, boolean recursive)
			throws IOException {
		if (file.exists()) {
			String deleleteCommand = (recursive ? "rm -r " : "rm ") 
					+ file.getAbsolutePath();
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(deleleteCommand);
			return true;
		}
		return false;
	}
	
}
