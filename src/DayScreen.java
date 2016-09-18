import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*This class shows weather information displayed in 3 main sub-panels: tee, fairway, green.
 *The class provides the method updateDayScreen(int i) where i indicates one of the five days. This update method can be used to
 *update the class weather data periodically in alignment with the periodic refresh of the weather data from the class WeatherAPI.
 */

public class DayScreen extends JPanel{
	
	static int count = 0;
	int i = 0;
	
	JLabel backgroundLabel = new JLabel();
	JPanel topPanel = new JPanel();
	JPanel greenPanel = new JPanel();
	JPanel fairwayPanel = new JPanel();
	JPanel teePanel = new JPanel();
	
	CardLayout cards = new CardLayout();
	JPanel containerPanel = new JPanel();
	Settings settings;
	
	String screenName = "";
	
	String day;
	
	WeatherAPI weatherAPI;
	
	String greenMessageText;
	String greenDataText;
	String fairwayMessageText;
	String fairwayDataText;
	String teeMessageText;
	String teeDataText;

	public DayScreen(WeatherAPI weatherAPI, String screenName){
		
		this.weatherAPI = weatherAPI;
		count = count + 1;
		i = count - 1;
		
		this.day = weatherAPI.weatherForecastList.get(i).date;
		updateDayScreen(i);
		
		this.screenName = screenName;
		
		BufferedImage backgroundPicture = null;
		try {
			backgroundPicture = ImageIO.read(this.getClass().getResource("fairway3.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		backgroundLabel = new JLabel(new ImageIcon(backgroundPicture));
		backgroundLabel.setPreferredSize(new Dimension(320, 480));
		backgroundLabel.setAlignmentX(0.5f);
		backgroundLabel.setAlignmentY(0.5f);
		
		TopButtons topButtons = new TopButtons();
		
		greenPanel.setPreferredSize(new Dimension(320, 100));
		greenPanel.setMaximumSize(greenPanel.getPreferredSize());
		greenPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		greenPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		greenPanel.setLayout(new GridBagLayout());
		BoxTitle greenTitle = new BoxTitle("Green");
		BoxMessage greenMessage = new BoxMessage(greenMessageText);
		BoxData greenData = new BoxData(greenDataText);
		
		greenPanel.add(greenTitle, greenTitle.cBoxTitle);
		greenPanel.add(greenMessage, greenMessage.cBoxMessage);
		greenPanel.add(greenData, greenData.cBoxData);
		
		fairwayPanel.setPreferredSize(new Dimension(300, 100));
		fairwayPanel.setMaximumSize(greenPanel.getPreferredSize());
		fairwayPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		fairwayPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		fairwayPanel.setLayout(new GridBagLayout());
		BoxTitle fairwayTitle = new BoxTitle("Fairway");
		BoxMessage fairwayMessage = new BoxMessage(fairwayMessageText);
		BoxData fairwayData = new BoxData(fairwayDataText);
		
		fairwayPanel.add(fairwayTitle, fairwayTitle.cBoxTitle);
		fairwayPanel.add(fairwayMessage, fairwayMessage.cBoxMessage);
		fairwayPanel.add(fairwayData, fairwayData.cBoxData);
		
		teePanel.setPreferredSize(new Dimension(300, 100));
		teePanel.setMaximumSize(greenPanel.getPreferredSize());
		teePanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		teePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		teePanel.setLayout(new GridBagLayout());
		BoxTitle teeTitle = new BoxTitle("Tee");
		BoxMessage teeMessage = new BoxMessage(teeMessageText);
		BoxData teeData = new BoxData(teeDataText);
		
		teePanel.add(teeTitle, teeTitle.cBoxTitle);
		teePanel.add(teeMessage, teeMessage.cBoxMessage);
		teePanel.add(teeData, teeData.cBoxData);
		
		BottomButtons bottomButtons = new BottomButtons();
		
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(320, 480));
		topPanel.setOpaque(false);
		topPanel.setAlignmentX(0.5f);
		topPanel.setAlignmentY(0.5f);
		topPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		topPanel.add(topButtons);
		topPanel.add(Box.createRigidArea(new Dimension(320, 35)));
		topPanel.add(greenPanel);
		topPanel.add(Box.createRigidArea(new Dimension(320, 15)));
		topPanel.add(fairwayPanel);
		topPanel.add(Box.createRigidArea(new Dimension(320, 15)));
		topPanel.add(teePanel);
		topPanel.add(Box.createRigidArea(new Dimension(320, 15)));
		topPanel.add(bottomButtons);
		
		setLayout(new OverlayLayout(this));
		setPreferredSize(new Dimension(320, 480));
		add(topPanel);
		add(backgroundLabel);
		
	}
	public void updateDayScreen(int i){
		greenMessageText = weatherAPI.weatherForecastList.get(i).code;
		greenDataText = "<html>Precipitation: 10mm<br>Humidity: 80%<br>Pressure: 196 mbar<br></html>";
		fairwayMessageText = weatherAPI.weatherForecastList.get(i).text;
		fairwayDataText = "<html>Wind speed: 60Km/h<br>Wind direction: South-East<br>Visibility: 20km<br></html>";
		teeMessageText = "Cloudy but sunglasses required";
		teeDataText = "<html>Sunrine - Sunset: 07:20 - 18:00<br>Temperature: 20C<br>Min - Max: "+ 
				weatherAPI.weatherForecastList.get(i).lowTemp+" - "+weatherAPI.weatherForecastList.get(i).highTemp
				+" <br></html>";
	}
	
	
	private class BoxTitle extends JLabel{
		GridBagConstraints cBoxTitle = new GridBagConstraints();
		private BoxTitle(String text){
			setText(text);
			setForeground(new Color(255,215,0));
			setHorizontalAlignment(JLabel.LEFT);
			setOpaque(true);
			setBackground(new Color(0.0f, 0.0f, 0.0f, 1.0f));
			setBorder(BorderFactory.createLineBorder(Color.black));
			cBoxTitle.fill = GridBagConstraints.HORIZONTAL;
			cBoxTitle.gridx = 0;
			cBoxTitle.gridy = 0;
			cBoxTitle.weightx = 1;
			cBoxTitle.weighty = 1;

		}
	}
	private class BoxMessage extends JLabel{
		GridBagConstraints cBoxMessage;		
		private BoxMessage(String text){
			setText(text);
			setForeground(Color.WHITE);
			setHorizontalAlignment(JLabel.CENTER);
			setBorder(BorderFactory.createLineBorder(Color.black));
			cBoxMessage = new GridBagConstraints();
			cBoxMessage.fill = GridBagConstraints.HORIZONTAL;
			cBoxMessage.gridx = 1;
			cBoxMessage.gridy = 0;
			cBoxMessage.weightx = 1;
			cBoxMessage.weighty = 1;

		}
		
	}
	private class BoxData extends JLabel{
		GridBagConstraints cBoxData = new GridBagConstraints();
		private BoxData(String text){
			setText(text);
			setForeground(Color.WHITE);
			setHorizontalAlignment(JLabel.LEFT);
			setBorder(BorderFactory.createLineBorder(Color.black));
			cBoxData.fill = GridBagConstraints.NONE;
			cBoxData.anchor = GridBagConstraints.FIRST_LINE_START;
			cBoxData.gridx = 1;
			cBoxData.gridy = 1;
			cBoxData.weightx = 1;
			cBoxData.weighty = 1;

		}
	}
	
	private class TopButtons extends JPanel{
		JButton exitButton = new JButton("Exit");
        JLabel borderTitle = new JLabel(day, JLabel.CENTER);
        JButton settingsButton = new JButton("Settings");
		
		private TopButtons(){
			setBackground(Color.black);
			setBorder(BorderFactory.createLineBorder(Color.orange, 2));
	        setPreferredSize(new Dimension(320, 10));
	        borderTitle.setForeground(Color.orange);
	        
	        add(exitButton);
	        add(borderTitle);
	        add(settingsButton);
	        
	        exitButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
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
	
	private class BottomButtons extends JPanel{
		JButton homeButton = new JButton("Home");
		
		private BottomButtons(){
			setBorder(BorderFactory.createLineBorder(Color.black));
			setOpaque(false);
			add(homeButton);
			setPreferredSize(new Dimension(320, 30));
			
			homeButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	cards.show(containerPanel, "homeScreen");
	            }
	        });

		}
	}
	public void setScreenInFrame(CardLayout cards, JPanel containerPanel, Settings settings){
		this.settings = settings;
		this.cards = cards;
		this.containerPanel = containerPanel;
		
	}
}
