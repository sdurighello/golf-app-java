import java.net.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.io.*;

/*Based on the WOEID passed by the class CreateWOEID, this class queries the Yahoo weather API and saves the forecasted data in the
 * Forecast arraylist and today's data in the Today object. Instances of the objects Forecast and Today are then used in the class
 * HomeScreen and DayScreen to retrieve and display specific weather data for each day and in the classes Settings and Sounds to regulate
 * audio output based on user choices and weather conditions.
 * 
 */

public class WeatherAPI {
	String theWeatherRSS;
	String woeid;
	ArrayList<Forecast> weatherForecastList;
	CreateWOEID createWoeid;
	Today today;
	String timeToLive;
	
	public class Forecast
	{
		String day;
		String date;
		String lowTemp;
		String highTemp;
		String text;
		String code;
		String codeInt;
	}
	
	public class Today{
		String windSpeed;
		String airPressure;
		String codeThunder;
	}

	public WeatherAPI(CreateWOEID createWOEID) {
		this.createWoeid = createWOEID;
		
		woeid = createWoeid.woeid;
		System.out.println("woeid: "+woeid);
		
		runWeatherAPI();
//		theWeatherRSS = getWeatherAsRSS(woeid);
//		parseWeather(theWeatherRSS);
	}
	
	public void runWeatherAPI(){
		theWeatherRSS = getWeatherAsRSS(woeid);
		parseWeather(theWeatherRSS);
//		System.out.println(theWeatherRSS);
	}

	void parseWeather(String weatherHTML){
		
		weatherForecastList = new ArrayList<Forecast>();
		int startIndex = 0;
		
		while(startIndex != -1) {
			
			startIndex = weatherHTML.indexOf("<yweather:forecast", startIndex);
			
			if(startIndex != -1) {
				
				// found a weather forecast
				int endIndex = weatherHTML.indexOf(">", startIndex);
				String weatherForecast = weatherHTML.substring(startIndex, endIndex+1);

				// get forecast				
				String dayString = getValueForKey(weatherForecast, "day");
				String dateString = getValueForKey(weatherForecast, "date");
				String lowString = getValueForKey(weatherForecast, "low");
				String highString = getValueForKey(weatherForecast, "high");
				String textString = getValueForKey(weatherForecast, "text");
				String codeString = getValueForKey(weatherForecast, "code");
				
				Forecast fore = new Forecast();
				fore.date = dayString+" "+dateString;
				fore.lowTemp = lowString;
				fore.highTemp = highString;
				fore.text = textString;
				fore.code = getTextCode(codeString);
				fore.codeInt = codeString;
				weatherForecastList.add(fore);
				
				// move to end of this forecast
				startIndex = endIndex;
			}
		}
		
		for(int i = 0; i < weatherForecastList.size(); i++)
		{
			System.out.println(
			"day: "+ weatherForecastList.get(i).day + " " +
			"date: "+ weatherForecastList.get(i).date + " " +
			"lowTemp: "+ weatherForecastList.get(i).lowTemp + " " +
			"text: "+ weatherForecastList.get(i).text + " " +
			"code: "+ weatherForecastList.get(i).code + " " +
			"codeInt: "+ weatherForecastList.get(i).codeInt
					);
		}
		
		today = new Today();
		
		startIndex = weatherHTML.indexOf("<yweather:wind");
		int endIndex = weatherHTML.indexOf(">", startIndex);
		String weatherToday = weatherHTML.substring(startIndex, endIndex+1);
		today.windSpeed = getValueForKey(weatherToday, "speed");
		System.out.println("today.windSpeed: "+today.windSpeed);
		
		startIndex = weatherHTML.indexOf("<yweather:atmosphere");
		endIndex = weatherHTML.indexOf(">", startIndex);
		weatherToday = weatherHTML.substring(startIndex, endIndex+1);
		today.airPressure = getValueForKey(weatherToday, "pressure");
		System.out.println("today.airPressur: "+today.airPressure);
		
		startIndex = weatherHTML.indexOf("<yweather:condition");
		endIndex = weatherHTML.indexOf(">", startIndex);
		weatherToday = weatherHTML.substring(startIndex, endIndex+1);
		today.codeThunder = getValueForKey(weatherToday, "code");
		System.out.println("today.codeThunder: "+today.codeThunder);
		
		timeToLive = weatherHTML.substring(weatherHTML.indexOf("<ttl>")+5, weatherHTML.indexOf("</ttl>"));
		System.out.println("timeToLive: "+timeToLive);
		
	}

	String getValueForKey(String theString, String keyString)
	{
		int startIndex = theString.indexOf(keyString);
		startIndex = theString.indexOf("\"", startIndex);
		int endIndex = theString.indexOf("\"", startIndex+1);
		String resultString = theString.substring(startIndex+1, endIndex);
		return resultString;
	}

	String getWeatherAsRSS(String city)
	{
		try{
			/*
			Adapted from: http://stackoverflow.com/questions/1381617/simplest-way-to-correctly-load-html-from-web-page-into-a-string-in-java
			Answer provided by: erickson
			*/
			URL url = new URL("http://weather.yahooapis.com/forecastrss?w="+city+"&u=c");
			URLConnection con = url.openConnection();
			Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
			Matcher m = p.matcher(con.getContentType());
			/* If Content-Type doesn't match this pre-conception, choose default and 
			 * hope for the best. */
			String charset = m.matches() ? m.group(1) : "ISO-8859-1";
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
		catch(Exception e) {System.err.println("Weather API Exception: "+e);}
		return null;
	}
	
	String getTextCode(String code){
		String textCode = null;
		int intCode = Integer.parseInt(code);
		switch (intCode) {
		
		// Thunder
		case 0: textCode = "tornado"; break;
		case 2: textCode = "tropical storm"; break;
		case 3: textCode = "severe thunderstorms"; break;
		case 4: textCode = "thunderstorms"; break;
		case 37: textCode = "isolated thunderstorms"; break;
		case 38: textCode = "scattered thunderstorms"; break;
		case 39: textCode = "scattered thunderstorms"; break;
		case 45: textCode = "thundershowers"; break;
		case 47: textCode = "isolated thundershowers"; break;
		
		// Rain
		case 9: textCode = "drizzle"; break;
		case 11: textCode = "showers"; break;
		case 12: textCode = "showers"; break;
		case 40: textCode = "scattered showers"; break;
		
		// Snow
		case 5: textCode = "mixed rain and snow"; break;
		case 6: textCode = "mixed rain and sleet"; break;
		case 7: textCode = "mixed snow and sleet"; break;
		case 8: textCode = "freezing drizzle"; break;
		case 10: textCode = "freezing rain"; break;
		case 13: textCode = "snow flurries"; break;
		case 14: textCode = "light snow showers"; break;
		case 15: textCode = "blowing snow"; break;
		case 16: textCode = "snow"; break;
		case 17: textCode = "hail"; break;
		case 18: textCode = "sleet"; break;
		case 35: textCode = "mixed rain and hail"; break;
		case 41: textCode = "heavy snow"; break;
		case 42: textCode = "scattered snow showers"; break;
		case 43: textCode = "heavy snow"; break;
		case 46: textCode = "snow showers"; break;
		
		// Cloudy
		case 19: textCode = "dust"; break;
		case 20: textCode = "foggy"; break;
		case 21: textCode = "haze"; break;
		case 22: textCode = "smoky"; break;
		case 26: textCode = "cloudy"; break;
		case 27: textCode = "mostly cloudy (night)"; break;
		case 28: textCode = "mostly cloudy (day)"; break;
		case 29: textCode = "partly cloudy (night)"; break;
		case 30: textCode = "partly cloudy (day)"; break;
		case 44: textCode = "partly cloudy"; break;
		
		// Sunny
		case 25: textCode = "cold"; break;
		case 31: textCode = "clear (night)"; break;
		case 32: textCode = "sunny"; break;
		case 33: textCode = "fair (night)"; break;
		case 34: textCode = "fair (day)"; break;
		case 36: textCode = "hot"; break;
		
		// Windy
		case 23: textCode = "blustery"; break;
		case 24: textCode = "windy"; break;
		
		// Other
		case 3200: textCode = "not available"; break;
		default: textCode = "invalid code";	break;
		}
			
		return textCode;
	}

}