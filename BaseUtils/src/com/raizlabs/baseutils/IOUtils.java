package com.raizlabs.baseutils;

import java.io.BufferedReader;
import java.io.Closeable;
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
	
}
