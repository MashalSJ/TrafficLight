import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLight {
	
	private Intersection<Side> intersection;
	private Timer timer;

	public TrafficLight(Intersection<Side> intersection) {
		this.intersection = intersection;
		timer = new Timer("Traffic Light");
	}

	public Intersection<Side> getIntersection() {
		return intersection;
	}
	
	public void runLight(int duration) {
	TrafficLightAlgorithm alg = new TrafficLightAlgorithm(this, duration);
	timer.schedule(alg, 0);
	}
	

	

	
	

	
	
	
	

}
