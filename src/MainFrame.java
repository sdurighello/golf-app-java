import java.awt.CardLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/*This is the CONTROLLER class for the whole application. From this class, all view and model classes are instantiated and passed.
 *The class has a timer that is used to refresh the displayed weather data every 10sec.
 */

public class MainFrame extends JFrame implements ActionListener{
	
	Point point = new Point();
    JPanel containerPanel = new JPanel();
    CardLayout cards = new CardLayout();
    String currentScreen = "";
    
    IpInfoDbClient ipInfo;
    CreateWOEID createWOEID;
    WeatherAPI weatherAPI;
    
	HomeScreen homeScreen;
	Sounds sounds;
	Settings settings;
    
	DayScreen dayScreen1;
	DayScreen dayScreen2;
	DayScreen dayScreen3;
	DayScreen dayScreen4;
	DayScreen dayScreen5;
	
	Timer timer;
	
	public MainFrame(){
		try {
			ipInfo = new IpInfoDbClient();
		} catch (Exception e) {
			System.err.println(e);
		}
		
		createWOEID = new CreateWOEID(ipInfo);
		
	    weatherAPI = new WeatherAPI(createWOEID);
	    
		homeScreen = new HomeScreen(ipInfo, weatherAPI, "homeScreen");
		sounds = new Sounds(weatherAPI);
		settings = new Settings(sounds);
	    
		dayScreen1 = new DayScreen(weatherAPI, "dayScreen1");
		dayScreen2 = new DayScreen(weatherAPI, "dayScreen2");
		dayScreen3 = new DayScreen(weatherAPI, "dayScreen3");
		dayScreen4 = new DayScreen(weatherAPI, "dayScreen4");
		dayScreen5 = new DayScreen(weatherAPI, "dayScreen5");
		
		timer = new Timer(10000, this);
		timer.start();
		
		
		
		setUndecorated(true);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x,
                        p.y + e.getY() - point.y);
            }
        });

        setSize(320, 480);
        setLocation(200, 200);
        
//*******************************  
        homeScreen.setName("homeScreen");
        
        
        containerPanel.setLayout(cards);
        containerPanel.add(homeScreen, "homeScreen");
        containerPanel.add(settings, "settings");
        containerPanel.add(dayScreen1, "dayScreen1");
        containerPanel.add(dayScreen2, "dayScreen2");
        containerPanel.add(dayScreen3, "dayScreen3");
        containerPanel.add(dayScreen4, "dayScreen4");
        containerPanel.add(dayScreen5, "dayScreen5");
        
        cards.show(containerPanel, "homeScreen");
        
        getContentPane().add(containerPanel);
        pack();
        setVisible(true);
        
        homeScreen.setScreenInFrame(cards, containerPanel, settings);
        settings.setScreenInFrame(cards, containerPanel);
        dayScreen1.setScreenInFrame(cards, containerPanel, settings);
        dayScreen2.setScreenInFrame(cards, containerPanel, settings);
        dayScreen3.setScreenInFrame(cards, containerPanel, settings);
        dayScreen4.setScreenInFrame(cards, containerPanel, settings);
        dayScreen5.setScreenInFrame(cards, containerPanel, settings);
        
    }
	public void actionPerformed(ActionEvent e) {
		weatherAPI.runWeatherAPI();
		homeScreen.day1Bar.updateDayBars(0);
		homeScreen.day1Bar.updateDayBars(1);
		homeScreen.day1Bar.updateDayBars(2);
		homeScreen.day1Bar.updateDayBars(3);
		homeScreen.day1Bar.updateDayBars(4);
		dayScreen1.updateDayScreen(0);
		dayScreen2.updateDayScreen(1);
		dayScreen3.updateDayScreen(2);
		dayScreen4.updateDayScreen(3);
		dayScreen5.updateDayScreen(4);
		repaint();
		
	}
	
}
