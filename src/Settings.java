import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.OverlayLayout;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*This class allows the user to set up to three sounds corresponding to three weather elements he would like to keep under control,
 *that is: wind, rain or thunder.
 *The class provides a check box for each sound and two sliders one to allow the user to set the sound duration and one to set the
 *interval the sound should be played.
 */

public class Settings extends JPanel{
	
	CardLayout cards = new CardLayout();
	JPanel containerPanel = new JPanel();
	
	JLabel backgroundLabel = new JLabel();
	JLabel alertsLabel;
	JLabel controlsLabel;
	
	JPanel alertsPanel = new JPanel();
	JPanel controlsPanel = new JPanel();
	JPanel middlePanel = new JPanel();
	JPanel topPanel = new JPanel();
	
	String callingScreen = "";
	
	Sounds sounds;
	
	public Settings(Sounds sounds){
		
		this.sounds = sounds;
		
		BufferedImage backgroundPicture = null;
		try {
			backgroundPicture = ImageIO.read(this.getClass().getResource("golfLandscape3.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		backgroundLabel = new JLabel(new ImageIcon(backgroundPicture));
		backgroundLabel.setPreferredSize(new Dimension(320, 480));
		backgroundLabel.setAlignmentX(0.5f);
		backgroundLabel.setAlignmentY(0.5f);
		
		TopButtons topButtons = new TopButtons();
		
		alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
		alertsPanel.setPreferredSize(new Dimension(320, 100));
		alertsPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		alertsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		AlertsCheckBox alertsCheckBox = new AlertsCheckBox();
		alertsLabel = new JLabel("Alerts", JLabel.CENTER);
		alertsLabel.setForeground(new Color(255,215,0));
		alertsPanel.add(alertsLabel);
		alertsPanel.add(alertsCheckBox);
		
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setPreferredSize(new Dimension(320, 200));
		controlsPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		controlsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		AlertControls alertsControls = new AlertControls();
		controlsLabel = new JLabel("Controls", JLabel.CENTER);
		controlsLabel.setForeground(new Color(255,215,0));
		controlsPanel.add(controlsLabel);
		controlsPanel.add(Box.createRigidArea(new Dimension(320, 10)));
		controlsPanel.add(alertsControls);
		
		middlePanel.setPreferredSize(new Dimension(320, 310));
		middlePanel.setOpaque(false);
		middlePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(alertsPanel);
		middlePanel.add(Box.createRigidArea(new Dimension(320, 10)));
		middlePanel.add(controlsPanel);
		
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(320, 480));
		topPanel.setOpaque(false);
		topPanel.add(topButtons);
		topPanel.add(Box.createRigidArea(new Dimension(320, 70)));
		topPanel.add(middlePanel);
		topPanel.add(Box.createRigidArea(new Dimension(320, 70)));
		
		setLayout(new OverlayLayout(this));
		setPreferredSize(new Dimension(320, 480));
		add(topPanel);
		add(backgroundLabel);
	
		
	}
	
	private class TopButtons extends JPanel{
		JButton exitButton = new JButton("Exit");
	    JLabel borderTitle = new JLabel("Golf Weather");
	    JButton backButton = new JButton("Back");
		
	    private TopButtons(){
			setBackground(Color.black);
			setBorder(BorderFactory.createLineBorder(Color.orange, 2));
			borderTitle.setForeground(Color.orange);
	        setPreferredSize(new Dimension(320, 40));
	        add(exitButton);
	        add(borderTitle);
	        add(backButton);
	        
		    exitButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	                System.exit(0);
	            }
	        });
		    
		    backButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	cards.show(containerPanel, callingScreen);
	            }
	        });
		}
	}
	
	private class AlertsCheckBox extends JPanel implements ItemListener{
		JCheckBox rainCheckBox;
		JCheckBox windCheckBox;
		JCheckBox thunderCheckBox;
		
		private AlertsCheckBox() {
			rainCheckBox = new JCheckBox("Rain");
			rainCheckBox.setForeground(new Color(255,215,0));
			windCheckBox = new JCheckBox("Wind");
			windCheckBox.setForeground(new Color(255,215,0));
			thunderCheckBox = new JCheckBox("Thunder");
			thunderCheckBox.setForeground(new Color(255,215,0));
			
			rainCheckBox.addItemListener(this);
			windCheckBox.addItemListener(this);
			thunderCheckBox.addItemListener(this);
			
			setOpaque(false);
			setLayout(new GridLayout(3, 1));
			 add(rainCheckBox);
			 add(windCheckBox);
			 add(thunderCheckBox);
			 			 
		}
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
		    if (source == rainCheckBox){
		    	if(e.getStateChange() == ItemEvent.SELECTED) {sounds.setStartStopRain("start"); }
		    	else if(e.getStateChange() == ItemEvent.DESELECTED){sounds.setStartStopRain("stop"); }}
		    else if (source == windCheckBox){
		    	if(e.getStateChange() == ItemEvent.SELECTED) {sounds.setStartStopWind("start");  }
		    	else if(e.getStateChange() == ItemEvent.DESELECTED){ sounds.setStartStopWind("stop"); }}
		    else if (source == thunderCheckBox){
		    	if(e.getStateChange() == ItemEvent.SELECTED) {sounds.setStartStopThunder("start");   }
		    	else if(e.getStateChange() == ItemEvent.DESELECTED){sounds.setStartStopThunder("stop");  }}
		    }
		
	}
	
	private class AlertControls extends JPanel implements ChangeListener{
		JSlider durationSlider = new JSlider(1, 60, 5); //seconds;
		JSlider intervalSlider = new JSlider(1, 60, 10); //minutes
//		JSlider volumeSlider = new JSlider(1, 10, 3);
		
		JLabel durationLabel = new JLabel("Duration", JLabel.CENTER);
		JLabel intervalLabel = new JLabel("Interval", JLabel.CENTER);
//		JLabel volumeLabel = new JLabel("Volume", JLabel.CENTER);
		
		private AlertControls(){
			durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			durationLabel.setForeground(new Color(255,215,0));
			intervalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			intervalLabel.setForeground(new Color(255,215,0));
//			volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//			volumeLabel.setForeground(new Color(255,215,0));
	
			durationSlider.addChangeListener(this);
			intervalSlider.addChangeListener(this);
//			volumeSlider.addChangeListener(this);
			
			setOpaque(false);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(durationLabel);
			add(durationSlider);
			add(intervalLabel);
			add(intervalSlider);
//			add(volumeLabel);
//			add(volumeSlider);
			
		}
		
		public void stateChanged(ChangeEvent e) {
			Object source = e.getSource();
		    if (source == durationSlider){
		    	sounds.setDuration(durationSlider.getValue()); 
		    	}
		    else if (source == intervalSlider){
		    	sounds.setInterval(intervalSlider.getValue());
		    	}
//		    else if (source == volumeSlider){
//		    	sounds.setVolumeLevel(volumeSlider.getValue()*0.1f);
//		    	}
		}
		
		
	}

	public void setCallingScreen(String callingScreen){
		this.callingScreen = callingScreen;
	}
	
	public void setScreenInFrame(CardLayout cards, JPanel containerPanel){
		this.cards = cards;
		this.containerPanel = containerPanel;
	}
	
}


