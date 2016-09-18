import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.SamplePlayer;

/*This class loads three sounds: rain, wind, thunder. Each sound file is around 5 sec each so their sampleplayer is set to loop to ensure
 * they can be played for whatever time the user will set.
 * The variables "startStopXxx" are set by the users using the checkboxes in the settings class.
 * If a sound is checked, then that sound should be played.
 * The variable "duration" indicates for how long the use wants that sound to be played. This is set by the duration slider in the
 * Settings class.
 * The variable "interval" indicates what is the periodic interval the sound should be played. This is set by the interval slider in the
 * Settings class.
 * If for example the users sets a duration of 5 seconds with an interval of 3 minutes, every 5 minutes the checked sounds should be
 * played by 
 * The level of volume (gain) is set based on the weather conditions through the setVolumeLevel method. The worst the weather condition
 * the higher the volume will be.
 */

public class Sounds {
	AudioContext audioContext;
	
	Sample sampleRain;
	Sample sampleWind;
	Sample sampleThunder;
	
	SamplePlayer samplePlayerRain;
	SamplePlayer samplePlayerWind;
	SamplePlayer samplePlayerThunder;
	
	Clock clock;
	
	int x = 1;
	int interval = 10;
	int duration = 5;
	
	String startStopRain = "stop";
	String startStopWind = "stop";
	String startStopThunder = "stop";
	
	Gain gainVolumeRain;
	Gain gainVolumeWind;
	Gain gainVolumeThunder;
	
	float volumeLevelRain = 0.3f;
	float volumeLevelWind = 0.3f;
	float volumeLevelThunder = 0.3f;
	
	Glide glideVolumeRain;
	Glide glideVolumeWind;
	Glide glideVolumeThunder;
	
	WeatherAPI weatherAPI;
	
 public Sounds(WeatherAPI weatherAPI){
	 
	 	this.weatherAPI = weatherAPI;
	 
		audioContext = new AudioContext();
		
		clock = new Clock(audioContext, 1000);
		audioContext.out.addDependent(clock);
		
		glideVolumeRain = new Glide(audioContext, volumeLevelRain);
		glideVolumeWind = new Glide(audioContext, volumeLevelWind);
		glideVolumeThunder = new Glide(audioContext, volumeLevelThunder);
		
		gainVolumeRain = new Gain(audioContext, 1, glideVolumeRain);
		gainVolumeWind = new Gain(audioContext, 1, glideVolumeWind);
		gainVolumeThunder = new Gain(audioContext, 1, glideVolumeThunder);
		
		sampleRain = SampleManager.sample("audio/rain.wav");
	 	samplePlayerRain = new SamplePlayer(audioContext, sampleRain);
	 	samplePlayerRain.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
	 	audioContext.out.addInput(samplePlayerRain);
	 	gainVolumeRain.addInput(samplePlayerRain);
		
	 	sampleWind = SampleManager.sample("audio/wind.wav");
 	 	samplePlayerWind = new SamplePlayer(audioContext, sampleWind);
 	 	samplePlayerWind.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
 	 	audioContext.out.addInput(samplePlayerWind);
 	 	gainVolumeWind.addInput(samplePlayerWind);
 	 	
 	 	sampleThunder = SampleManager.sample("audio/lightning.wav");
	 	samplePlayerThunder = new SamplePlayer(audioContext, sampleThunder);
	 	samplePlayerThunder.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
	 	audioContext.out.addInput(samplePlayerThunder);
	 	gainVolumeThunder.addInput(samplePlayerThunder);
	 	
	 	audioContext.out.addInput(gainVolumeRain);
	 	audioContext.out.addInput(gainVolumeWind);
	 	audioContext.out.addInput(gainVolumeThunder);
	 	
	 	clock.addMessageListener(
					new Bead() { 
						public void messageReceived(Bead message) {
							if(clock.isBeat()){
														
								if(startStopRain == "stop"){samplePlayerRain.pause(true);}
								if(startStopWind == "stop"){samplePlayerWind.pause(true);}
								if(startStopThunder == "stop"){samplePlayerThunder.pause(true);}
								
								setVolumeLevel();
							
								if(clock.getBeatCount()%x==0){
								//	System.out.println("x: "+x);
								//	System.out.println(startStopRain);
									if(startStopRain == "start"){samplePlayerRain.start();}
									if(startStopWind == "start"){samplePlayerWind.start();}	
									if(startStopThunder == "start"){samplePlayerThunder.start();}
								}
							
								if(clock.getBeatCount()%(x+duration)==0){
								//	System.out.println("x+duration: "+(x+duration));
									samplePlayerRain.pause(true);
									samplePlayerWind.pause(true);
									samplePlayerThunder.pause(true);
									x = interval+(x+duration);
								}
							}
						}
					});
	 	audioContext.start();
	}

public void setInterval(int interval) {
	this.interval = interval;
}

public void setDuration(int duration) {
	this.duration = duration;
}

public void setStartStopRain(String startStopRain) {
	this.startStopRain = startStopRain;
}

public void setStartStopWind(String startStopWind) {
	this.startStopWind = startStopWind;
}

public void setStartStopThunder(String startStopThunder) {
	this.startStopThunder = startStopThunder;
}

public void setVolumeLevel() {
	
	if(weatherAPI.today.airPressure != null){
		if (Float.parseFloat(weatherAPI.today.airPressure)<=995){
			glideVolumeRain.setValue(1.0f);
		} else if(Float.parseFloat(weatherAPI.today.airPressure)>995 && Float.parseFloat(weatherAPI.today.airPressure)<=1005){
			glideVolumeRain.setValue(0.5f);
		} else {
			glideVolumeRain.setValue(0.0f);
		}
	} else {glideVolumeRain.setValue(0.0f);}
	
	if(weatherAPI.today.windSpeed != ""){
		if (Float.parseFloat(weatherAPI.today.windSpeed)<20){
			glideVolumeWind.setValue(0.0f);
		} else if(Float.parseFloat(weatherAPI.today.windSpeed)>=20 && Float.parseFloat(weatherAPI.today.windSpeed)<=40){
			glideVolumeWind.setValue(0.5f);
		} else {
			glideVolumeWind.setValue(1.0f);
		}
	} else {glideVolumeWind.setValue(0.0f);}
	
	int codeThunder = Integer.parseInt(weatherAPI.today.codeThunder);
	switch (codeThunder) {
	case 0: // "tornado"
	case 2: // "tropical storm"
	case 3: // "severe thunderstorms"
	case 4: // "thunderstorms"
	case 37: // "isolated thunderstorms"
	case 38: // "scattered thunderstorms"
	case 39: // "scattered thunderstorms"
	case 45: // "thundershowers"
	case 47: // "isolated thundershowers"
		glideVolumeThunder.setValue(1.0f);
		break;

	default:
		glideVolumeWind.setValue(0.0f);
		break;
	}
	
}

 
}