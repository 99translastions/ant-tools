import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Downloads file from 99translations.com.
 */
public class DownloadTask extends TransTask
{
	/**
	 * Locale.
	 */
	private String locale;

	/**
	 * File to upload.
	 */
	private File file;

	public void setFile(File file)
	{
		this.file = file;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	/**
	 * Generic HTTP download method.
	 *
	 * @param urlString URL string
	 * @param filePath destination path
	 * @throws IOException if I/O operatios fail
	 */
	protected void download(String urlString, File filePath) throws IOException
	{
		URL url = new URL(urlString);
		HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
		if (connection.getResponseCode() != 200)
		{
			throw new BuildException("Download error " + connection.getResponseCode() + ": " + connection.getResponseMessage());
		}
		InputStream stream = null;
		FileOutputStream file = null;
		try
		{
			stream = connection.getInputStream();
			file = new FileOutputStream(filePath);

			byte[] buffer = new byte[100 * 1024];
			int i;
			while ((i = stream.read(buffer)) != -1)
			{
				file.write(buffer, 0, i);
			}
			file.flush();
		} finally
		{
			Util.close(file);
			Util.close(stream);
		}
	}

	/**
	 * Generates download URL.
	 *
	 * @return URL string
	 */
	private String getUrl()
	{
		return super.getUrl("download") + "/" + this.locale;
	}

	/**
	 * Taks action.
	 *
	 * @throws BuildException in case of errors
	 */
	public void execute() throws BuildException
	{
		try
		{
			download(getUrl(), this.file);
		} catch (IOException e)
		{
			throw new BuildException("Unable to download locale " + this.locale, e);
		}
	}
}
