
public class Exit extends RoadSegment {
	private int numCarsRecieved;

	public Exit() {
		super();
		numCarsRecieved = 0;
		setOrientation(Orientation.BACKWARDS);
	}

	@Override
	public String toString() {
		return "exit";
	}

	public void recieveCar() {
		numCarsRecieved++;
	}
	
	public String carDataString() {
		return "Cars Recieved: " + numCarsRecieved;
	}
	
	public String formattedData() {
		return " recived " + numCarsRecieved;
	}
}
