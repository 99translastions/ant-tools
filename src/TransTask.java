import org.apache.tools.ant.Task;

/**
 * Common methods.
 */
public class TransTask extends Task
{
	/**
	 * API key.
	 */
	protected String api;

	/**
	 * Translation file.
	 */
	protected String translationFile;

	public void setTranslationFile(String translationFile)
	{
		this.translationFile = translationFile;
	}

	public void setApi(String api)
	{
		this.api = api;
	}

	/**
	 * Generates URL.
	 *
	 * @param action API action
	 * @return URL string
	 */
	public String getUrl(String action)
	{
		return "http://99translations.com/" + action + "/" + this.api + "/" + translationFile;
	}
}
