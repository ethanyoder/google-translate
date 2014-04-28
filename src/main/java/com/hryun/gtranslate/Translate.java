/**
 * @author Vinícius Egidio (vegidio@gmail.com)
 * @since Apr 28th 2014
 * v1.0
 */

package com.hryun.gtranslate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate
{
	private String sourceLang;
	private String destLang;
	private String sourceText;
	private String destText;
	
	public Translate()
	{
		initialize();
	}
	
	private void initialize()
	{
		sourceLang = "";
		destLang = "";
		sourceText = "";
		destText = "";
	}
	
	public String execute()
	{
		destText = "";
		
		// Check if we have all variables set
		if(sourceLang.isEmpty() || sourceText.isEmpty() || destLang.isEmpty())
		{
			System.err.println("Missing parameters; please set the Source Language, Destination Language and the " +
					"Source Text first");
		}
		else
		{
			execute(sourceText, sourceLang, destLang);
		}
		
		return destText;
	}
	
	public String execute(String text, String sl, String dl)
	{
		// We initialize the variables first
		initialize();
		
		sourceLang = sl;
		destLang = dl;
		sourceText = text;
		
		// Creating the URL
		text = text.replace(" ", "%20");
		String url = "http://translate.google.com/translate_a/t?client=t&sl=" + sourceLang +
				"&tl=" + destLang + "&sc=2&ie=UTF-8&oe=UTF-8&oc=1&otf=2&ssel=0&tsel=0" +
				"&q=" + text;
		
		// Get the JS
		String js = fetchJs(url);
		
		// Parse the JS
		Matcher matcher;
		final String regex = "\\[\"(.*?)\",";
		Pattern pattern = Pattern.compile(regex);
		matcher = pattern.matcher(js);
		if(matcher.find()) destText = matcher.group(1);
		
		return destText;
	}
	
	private String fetchJs(String urlString)
	{
		String line;
		StringBuffer html = new StringBuffer();
		
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			// Fake the User-Agent
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 " +
					"(KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
			
			// Check the HTTP response code
			if(conn.getResponseCode() == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				
				// Reading the HTML
				while((line = in.readLine()) != null)
					html.append(line.trim());
				
				in.close();
			}
			
			// Close the connection
			conn.disconnect();
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return html.toString();
	}

	/**
	 * @return the sourceLang
	 */
	public String getSourceLang()
	{
		return sourceLang;
	}

	/**
	 * @param sourceLang the sourceLang to set
	 */
	public void setSourceLang(String sourceLang)
	{
		this.sourceLang = sourceLang;
	}

	/**
	 * @return the destLang
	 */
	public String getDestLang()
	{
		return destLang;
	}

	/**
	 * @param destLang the destLang to set
	 */
	public void setDestLang(String destLang)
	{
		this.destLang = destLang;
	}

	/**
	 * @return the sourceText
	 */
	public String getSourceText()
	{
		return sourceText;
	}

	/**
	 * @param sourceText the sourceText to set
	 */
	public void setSourceText(String sourceText)
	{
		this.sourceText = sourceText;
	}

	/**
	 * @return the destText
	 */
	public String getDestText()
	{
		return destText;
	}
}