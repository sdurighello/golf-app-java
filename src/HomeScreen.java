import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/*This class creates 5 bars, one for each day, providing weather information and expected quality of the golf game.
 * Weather conditions for each day are shown via an icon which is set based on the weather conditions,
 * (e.i. a cloud with rain icon for rainy conditions).
 * Expected quality of the golf game is shown through a 1 to 5 indicator providing 1 golden ball for expected poor game and 5 golden balls
 * for very good game conditions. The game indicator is set based on weather conditions.
 * The class also provides the location information that was determined by the IpInforDbClient class.
 */

public class HomeScreen extends JPanel{
	
	DayBar day1Bar;
	DayBar day2Bar;
	DayBar day3Bar;
	DayBar day4Bar;
	DayBar day5Bar;
	
	CardLayout cards = new CardLayout();
	JPanel containerPanel = new JPanel();
	
	JLabel backgroundLabel = new JLabel();
	JPanel topPanel = new JPanel();
	Settings settings;
	
	String screenName = "";
	
	IpInfoDbClient ipInfo;
	
	WeatherAPI weatherAPI;
	static int count = 0;
	
	public HomeScreen(IpInfoDbClient ipInfo, WeatherAPI weatherAPI, String screenName){
		
		this.ipInfo = ipInfo;
		this.weatherAPI = weatherAPI;
		
		this.screenName = screenName;
		
		BufferedImage backgroundPicture = null;
		try {
			backgroundPicture = ImageIO.read(this.getClass().getResource("golfLandscape2.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		backgroundLabel = new JLabel(new ImageIcon(backgroundPicture));
		backgroundLabel.setPreferredSize(new Dimension(320, 480));
		backgroundLabel.setAlignmentX(0.5f);
		backgroundLabel.setAlignmentY(0.5f);
		
		
		TopButtons topButtons = new TopButtons();
		LocationPanel locationPanel = new LocationPanel();
		
		day1Bar = new DayBar(0);
		day2Bar = new DayBar(1);
		day3Bar = new DayBar(2);
		day4Bar = new DayBar(3);
		day5Bar = new DayBar(4);
		
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(320, 480));
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		topPanel.add(topButtons);
		topPanel.add(locationPanel);
		topPanel.add(day1Bar);
		topPanel.add(day2Bar);
		topPanel.add(day3Bar);
		topPanel.add(day4Bar);
		topPanel.add(day5Bar);
		
		setLayout(new OverlayLayout(this));
		setPreferredSize(new Dimension(320, 480));
		add(topPanel);
		add(backgroundLabel);
		
		
		day1Bar.dayScreenButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	cards.show(containerPanel, "dayScreen1");
            }
        });
		day2Bar.dayScreenButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	cards.show(containerPanel, "dayScreen2");
            }
        });
		day3Bar.dayScreenButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	cards.show(containerPanel, "dayScreen3");
            }
        });
		day4Bar.dayScreenButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	cards.show(containerPanel, "dayScreen4");
            }
        });
		day5Bar.dayScreenButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	cards.show(containerPanel, "dayScreen5");
            }
        });
		
	}
	
	private class LocationPanel extends JPanel{
		
		String city;
		JLabel cityLabel;
		JLabel cityName;
		
		private LocationPanel(){
			
			city = ipInfo.woeidInput; 			
			cityLabel = new JLabel("Location: ", JLabel.LEFT);
			cityName = new JLabel(city, JLabel.RIGHT);
			cityLabel.setForeground(Color.WHITE);
			cityLabel.setOpaque(true);
			cityLabel.setBackground(new Color(0.1f, 0.2f, 0.5f, 0.5f));
			cityLabel.setPreferredSize(new Dimension(60, 30));
//			cityLabel.setBorder(BorderFactory.createLineBorder(Color.white));
			
			cityName.setForeground(Color.WHITE);
			cityName.setOpaque(true);
			cityName.setBackground(new Color(0.1f, 0.2f, 0.5f, 0.5f));
			cityName.setPreferredSize(new Dimension(60, 30));
//			cityName.setBorder(BorderFactory.createLineBorder(Color.white));
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setOpaque(false);
			add(cityLabel);
			add(cityName);
//			setBorder(BorderFactory.createLineBorder(Color.black));
			
		}
		
		
	}
	
	
	public class DayBar extends JPanel{
		
		JLabel weatherIconLabel;
		JLabel dateLabel;
		JLabel gameIndicatorLabel;
		JButton dayScreenButton;
		
		String date;
		ImageIcon weatherIcon;
		ImageIcon gameIndicator;
		

		public DayBar(int i){
			
			updateDayBars(i);
			
			weatherIconLabel = new JLabel(weatherIcon);
			dateLabel = new JLabel(date, JLabel.CENTER);
			gameIndicatorLabel = new JLabel(gameIndicator, JLabel.CENTER);
			dayScreenButton = new JButton(new ImageIcon("images/button.jpg"));
			dayScreenButton.setContentAreaFilled(false);
		
			weatherIconLabel.setPreferredSize(new Dimension(40, 40));
			weatherIconLabel.setBorder(BorderFactory.createLineBorder(Color.white));
			
			dateLabel.setForeground(new Color(255,215,0));
			dateLabel.setOpaque(true);
			dateLabel.setBackground(new Color(0.0f, 0.0f, 0.0f, 1.0f));
			dateLabel.setPreferredSize(new Dimension(200, 20));
			dateLabel.setBorder(BorderFactory.createLineBorder(Color.white));
			
			gameIndicatorLabel.setPreferredSize(new Dimension(200, 20));
			gameIndicatorLabel.setForeground(new Color(255,215,0));
			gameIndicatorLabel.setOpaque(true);
			gameIndicatorLabel.setBackground(new Color(0.0f, 0.0f, 0.0f, 1.0f));
			gameIndicatorLabel.setBorder(BorderFactory.createLineBorder(Color.white));
			
			dayScreenButton.setPreferredSize(new Dimension(40, 40));
			dayScreenButton.setOpaque(true);
			dayScreenButton.setBackground(new Color(0.0f, 0.0f, 0.0f, 1.0f));
			dayScreenButton.setBorder(BorderFactory.createLineBorder(Color.white));
	
			setLayout(new GridBagLayout());
			setOpaque(false);
			
			GridBagConstraints cIcon = new GridBagConstraints();
			cIcon.gridx = 0;
			cIcon.gridy = 0;
			cIcon.gridwidth = 1;
			cIcon.gridheight = 2;
			
			GridBagConstraints cDate = new GridBagConstraints();
			cDate.gridx = 1;
			cDate.gridy = 0;
			cDate.gridwidth = 10;
			cDate.gridheight = 1;
			
			GridBagConstraints cIndicator = new GridBagConstraints();
			cIndicator.gridx = 1;
			cIndicator.gridy = 1;
			cIndicator.gridwidth = 10;
			cIndicator.gridheight = 1;
			
			GridBagConstraints cButton = new GridBagConstraints();
			cButton.gridx = 12;
			cButton.gridy = 0;
			cButton.gridwidth = 1;
			cButton.gridheight = 2;
			
			add(weatherIconLabel, cIcon);
			add(dateLabel, cDate);
			add(gameIndicatorLabel, cIndicator);
			add(dayScreenButton, cButton);
			
			setBorder(BorderFactory.createLineBorder(Color.black));
		}
		
		public void updateDayBars(int i){
			this.date = weatherAPI.weatherForecastList.get(i).date;
			this.weatherIcon = getWeatherImageIcon(i);
			this.gameIndicator = getGameIndicator(i);
		}

		private ImageIcon getWeatherImageIcon(int i) {
			ImageIcon weatherImageIcon = null;
			int code = Integer.parseInt(weatherAPI.weatherForecastList.get(i).codeInt);
			
			switch(code){
			
			// Thunder
					case 0: //textCode = "tornado"; break;
					case 2: //textCode = "tropical storm"; break;
					case 3: //textCode = "severe thunderstorms"; break;
					case 4: //textCode = "thunderstorms"; break;
					case 37: //textCode = "isolated thunderstorms"; break;
					case 38: //textCode = "scattered thunderstorms"; break;
					case 39: //textCode = "scattered thunderstorms"; break;
					case 45: //textCode = "thundershowers"; break;
					case 47: //textCode = "isolated thundershowers"; break;
						weatherImageIcon = new ImageIcon("images/iconThunder.jpg"); break;
					
			// Rain
					case 9: //textCode = "drizzle"; break;
					case 11: //textCode = "showers"; break;
					case 12: //textCode = "showers"; break;
					case 40: //textCode = "scattered showers"; break;
						weatherImageIcon = new ImageIcon("images/iconRain.jpg"); break;
					
			// Snow
					case 5: //textCode = "mixed rain and snow"; break;
					case 6: //textCode = "mixed rain and sleet"; break;
					case 7: //textCode = "mixed snow and sleet"; break;
					case 8: //textCode = "freezing drizzle"; break;
					case 10: //textCode = "freezing rain"; break;
					case 13: //textCode = "snow flurries"; break;
					case 14: //textCode = "light snow showers"; break;
					case 15: //textCode = "blowing snow"; break;
					case 16: //textCode = "snow"; break;
					case 17: //textCode = "hail"; break;
					case 18: //textCode = "sleet"; break;
					case 35: //textCode = "mixed rain and hail"; break;
					case 41: //textCode = "heavy snow"; break;
					case 42: //textCode = "scattered snow showers"; break;
					case 43: //textCode = "heavy snow"; break;
					case 46: //textCode = "snow showers"; break;
						weatherImageIcon = new ImageIcon("images/iconSnow.jpg"); break;
					
			// Cloudy
					case 19: //textCode = "dust"; break;
					case 20: //textCode = "foggy"; break;
					case 21: //textCode = "haze"; break;
					case 22: //textCode = "smoky"; break;
					case 26: //textCode = "cloudy"; break;
					case 27: //textCode = "mostly cloudy (night)"; break;
					case 28: //textCode = "mostly cloudy (day)"; break;
					case 29: //textCode = "partly cloudy (night)"; break;
					case 30: //textCode = "partly cloudy (day)"; break;
					case 44: //textCode = "partly cloudy"; break;
						weatherImageIcon = new ImageIcon("images/iconCloudy.jpg"); break;
					
			// Sunny
					case 25: //textCode = "cold"; break;
					case 31: //textCode = "clear (night)"; break;
					case 32: //textCode = "sunny"; break;
					case 33: //textCode = "fair (night)"; break;
					case 34: //textCode = "fair (day)"; break;
					case 36: //textCode = "hot"; break;
						weatherImageIcon = new ImageIcon("images/iconSun.jpg"); break;
					
			// Windy
					case 23: //textCode = "blustery"; break;
					case 24: //textCode = "windy"; break;
						weatherImageIcon = new ImageIcon("images/iconWind.jpg"); break;
					
			// Other
					case 3200: //textCode = "not available"; break;
					default: //textCode = "invalid code";	break;
					}
						
			return weatherImageIcon;
		}
		
		private ImageIcon getGameIndicator(int i){
			ImageIcon weatherImageIcon = null;
			int code = Integer.parseInt(weatherAPI.weatherForecastList.get(i).codeInt);
			
			switch(code){
			
			// Thunder
					case 0: //textCode = "tornado"; break;
					case 2: //textCode = "tropical storm"; break;
					case 3: //textCode = "severe thunderstorms"; break;
					case 4: //textCode = "thunderstorms"; break;
					case 37: //textCode = "isolated thunderstorms"; break;
					case 38: //textCode = "scattered thunderstorms"; break;
					case 39: //textCode = "scattered thunderstorms"; break;
					case 45: //textCode = "thundershowers"; break;
					case 47: //textCode = "isolated thundershowers"; break;
						weatherImageIcon = new ImageIcon("images/goldBall1.jpg"); break;
					
			// Rain
					case 9: //textCode = "drizzle"; break;
					case 11: //textCode = "showers"; break;
					case 12: //textCode = "showers"; break;
					case 40: //textCode = "scattered showers"; break;
						weatherImageIcon = new ImageIcon("images/goldBall2.jpg"); break;
					
			// Snow
					case 5: //textCode = "mixed rain and snow"; break;
					case 6: //textCode = "mixed rain and sleet"; break;
					case 7: //textCode = "mixed snow and sleet"; break;
					case 8: //textCode = "freezing drizzle"; break;
					case 10: //textCode = "freezing rain"; break;
					case 13: //textCode = "snow flurries"; break;
					case 14: //textCode = "light snow showers"; break;
					case 15: //textCode = "blowing snow"; break;
					case 16: //textCode = "snow"; break;
					case 17: //textCode = "hail"; break;
					case 18: //textCode = "sleet"; break;
					case 35: //textCode = "mixed rain and hail"; break;
					case 41: //textCode = "heavy snow"; break;
					case 42: //textCode = "scattered snow showers"; break;
					case 43: //textCode = "heavy snow"; break;
					case 46: //textCode = "snow showers"; break;
						weatherImageIcon = new ImageIcon("images/goldBall1.jpg"); break;
					
			// Cloudy
					case 19: //textCode = "dust"; break;
					case 20: //textCode = "foggy"; break;
					case 21: //textCode = "haze"; break;
					case 22: //textCode = "smoky"; break;
					case 26: //textCode = "cloudy"; break;
					case 27: //textCode = "mostly cloudy (night)"; break;
					case 28: //textCode = "mostly cloudy (day)"; break;
					case 29: //textCode = "partly cloudy (night)"; break;
					case 30: //textCode = "partly cloudy (day)"; break;
					case 44: //textCode = "partly cloudy"; break;
						weatherImageIcon = new ImageIcon("images/goldBall4.jpg"); break;
					
			// Sunny
					case 25: //textCode = "cold"; break;
					case 31: //textCode = "clear (night)"; break;
					case 32: //textCode = "sunny"; break;
					case 33: //textCode = "fair (night)"; break;
					case 34: //textCode = "fair (day)"; break;
					case 36: //textCode = "hot"; break;
						weatherImageIcon = new ImageIcon("images/goldBall5.jpg"); break;
					
			// Windy
					case 23: //textCode = "blustery"; break;
					case 24: //textCode = "windy"; break;
						weatherImageIcon = new ImageIcon("images/goldBall3.jpg"); break;
					
			// Other
					case 3200: //textCode = "not available"; break;
					default: //textCode = "invalid code";	break;
					}
						
			return weatherImageIcon;
		}

	}

private class TopButtons extends JPanel{
	JButton exitButton;
    JLabel borderTitle;
    JButton settingsButton;
	
    private TopButtons(){
    	setBackground(Color.black);
		setBorder(BorderFactory.createLineBorder(Color.orange, 2));
        setPreferredSize(new Dimension(320, 40));
        
        exitButton = new JButton("Exit");
        add(exitButton);
        
        borderTitle = new JLabel("Golf Weather", JLabel.CENTER);
        borderTitle.setForeground(Color.orange);
        add(borderTitle);
        
        settingsButton = new JButton("Settings");
        add(settingsButton);
        
	    exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	ipInfo.HTTP_CLIENT.getConnectionManager().shutdown();
                System.exit(0);
            }
        });
	    
	    settingsButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	settings.setCallingScreen(screenName);
            	cards.show(containerPanel, "settings");
            }
        });
	}
}

public void setScreenInFrame(CardLayout cards, JPanel containerPanel, Settings settings){
	this.cards = cards;
	this.containerPanel = containerPanel;
	this.settings = settings;
}

}
