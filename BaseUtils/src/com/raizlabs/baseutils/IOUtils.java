package com.raizlabs.baseutils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
