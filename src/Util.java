import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility methods.
 */
public class Util
{

	public static void close(OutputStream stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			} catch (IOException e)
			{
			}
		}
	}

	public static void close(InputStream stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			} catch (IOException e)
			{
			}
		}
	}
}
