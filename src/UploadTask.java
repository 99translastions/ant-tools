import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Uploads master translations to the server.
 */
public class UploadTask extends TransTask
{
	/**
	 * CR LF.
	 */
	private static final String CRLF = "\r\n";

	/**
	 * File to upload.
	 */
	private File file;

	public void setFile(File file)
	{
		this.file = file;
	}

	/**
	 * Generic HTTP upload action.
	 *
	 * @param urlString url string
	 * @param filePath source file path
	 * @throws IOException if I/O operations fail
	 */
	protected void upload(String urlString, File filePath) throws IOException
	{
		URL url = new URL(urlString);
		String aBoundary = "------------------------" + System.currentTimeMillis();
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setUseCaches(false);
		http.setRequestProperty("Connection", "Keep-Alive");
		http.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + aBoundary);

		FileInputStream fileStream = null;
		OutputStream stream = null;
		try
		{
			fileStream = new FileInputStream(filePath);
			stream = http.getOutputStream();

			stream.write(("--" + aBoundary + CRLF).getBytes());
			stream.write(("Content-Disposition: form-data; name=\"upload\"; filename=\"" + filePath.getName() + "\"" + CRLF).getBytes());
			stream.write(("Content-Type: application/octet-stream" + CRLF + CRLF).getBytes());

			byte[] buffer = new byte[100 * 1000];
			int len;
			while ((len = fileStream.read(buffer)) >= 0)
			{
				stream.write(buffer, 0, len);
			}

			stream.write(CRLF.getBytes());
			stream.flush();
			if (http.getResponseCode() != 200)
			{
				throw new BuildException("Upload error " + http.getResponseCode() + ": " + http.getResponseMessage());
			}
		} finally
		{
			Util.close(fileStream);
			Util.close(stream);
		}
	}

	/**
	 * Task action.
	 *
	 * @throws BuildException if upload fails.
	 */
	public void execute() throws BuildException
	{
		try
		{
			System.out.println("URL = " + getUrl("upload_master"));
			upload(getUrl("upload_master"), file);
		} catch (IOException e)
		{
			throw new BuildException("Unable to upload the file: " + e.getMessage(), e);
		}
	}
}
