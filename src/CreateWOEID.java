import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This class takes the "woeidInput" string from the IpInfoDbClient class and passes that to the Yahoo API to get the WOEID.
//The returned WOEID will be stored in the string "woeid" and then accessed by the WeatherAPI class to retrieve the weather data.

public class CreateWOEID {
	static String theWeatherRSS;
	static String woeidInput;
	String woeid;
	IpInfoDbClient ipInfo;
	
	public CreateWOEID(IpInfoDbClient ipInfo) {
	
		this.ipInfo = ipInfo;
		
		woeidInput = ipInfo.woeidInput;
		theWeatherRSS = getWeatherAsRSS(woeidInput);
		parseWeather(theWeatherRSS);
	}

	void parseWeather(String weatherHTML){
		woeid = weatherHTML.substring(weatherHTML.indexOf("<woeid>")+7, weatherHTML.indexOf("</woeid>"));
	}


	String getWeatherAsRSS(String woeidInput)
	{
		try{
			/*
			Adapted from: http://stackoverflow.com/questions/1381617/simplest-way-to-correctly-load-html-from-web-page-into-a-string-in-java
			Answer provided by: erickson
			*/
			URL url = new URL("http://where.yahooapis.com/v1/places.q('"+woeidInput+"')?appid=[NKU7oDjV34ENW2gu3aoHlK0rWD7XCDjVb1SBFihfH8rhqeYYpTgh6gu32zpT3pexLQHxkXH8nA--]");
			URLConnection con = url.openConnection();
			Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
			Matcher m = p.matcher(con.getContentType());
			/* If Content-Type doesn't match this pre-conception, choose default and 
			 * hope for the best. */
			String charset = m.matches() ? m.group(1) : "UTF-8";
			Reader r = new InputStreamReader(con.getInputStream(), charset);
			StringBuilder buf = new StringBuilder();
			while (true) {
			  int ch = r.read();
			  if (ch < 0)
				break;
			  buf.append((char) ch);
			}
			String str = buf.toString();
			return(str);
		}
		catch(Exception e) {System.err.println("Create WOEID Exception: "+e);}
		//return null;
		return "<woeid>44418</woeid>";
	}

}