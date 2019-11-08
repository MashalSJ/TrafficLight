
/**
 * Road segments that face into the intersection
 * 
 * @author Mashal
 *
 */
public class Entrance extends RoadSegment {

	private Exit exit; // exit segment that this entrance corresponds to
	private int numCarsWaiting; // number of cars currently stopped here
	private int numCarsSent; // number of cars that passed through this Entrance
	private int numCarsGenerated; // number of cars that were generated at this Entrance
	private boolean isGreen; // green light or not
	private boolean isRed; // red light or not

	/**
	 * 0 = STRAIGHT | 1 = RIGHT | 2 = LEFT
	 *
	 */
	public Entrance(int turn) {
		super();
		numCarsWaiting = 0;
		numCarsSent = 0;
		exit = null;
		isGreen = false;
		isRed = true;

		if (turn < 0 || turn > 2) {
			throw new IllegalArgumentException("Direction must be integer 0-2");
		}

		if (turn == 0)
			this.setOrientation(Orientation.STRAIGHT);
		if (turn == 1)
			this.setOrientation(Orientation.RIGHT);
		if (turn == 2)
			this.setOrientation(Orientation.LEFT);

	}

	public void assignExit(Exit exit) {
		this.exit = exit;
	}

	public void sendCar() {
		if (numCarsWaiting > 0) {
			if(exit == null) {
				throw new IllegalStateException("This Entrance does not have a defined Exit");
			}
			exit.recieveCar();
			numCarsWaiting--;
			numCarsSent++;
		}
	}

	public void generateCar() {
		numCarsWaiting++;
		numCarsGenerated++;
	}

	public Exit getExit() {
		return exit;
	}

	public int getNumCarsWaiting() {
		return numCarsWaiting;
	}

	@Override
	public String toString() {
		if (getOrientation() == Orientation.STRAIGHT) {
			return "straight";
		} else if (getOrientation() == Orientation.RIGHT) {
			return "right";
		} else if (getOrientation() == Orientation.LEFT) {
			return "left";
		} else if (getOrientation() == Orientation.BACKWARDS) {
			return "back";
		}
		return "";
	}

	public boolean isGreen() {
		return isGreen;
	}

	public void setGreen() {
		isGreen = true;
		isRed = false;
	}

	public boolean isRed() {
		return isRed;
	}

	public void setRed() {
		isRed = true;
		isGreen = false;
	}

	public String status() {
		if (isGreen) {
			return "green";
		} else if (isRed) {
			return "red";
		}
		throw new IllegalStateException("Light is neither green nor red");
	}

	public String carDataString() {
		return "Cars Waiting: " + numCarsWaiting + "  Cars sent: " + numCarsSent;
	}

	public String generationDataString() {
		return "Cars generated: " + numCarsGenerated;
	}
	
	public String assignedExit() {
		if(exit == null) {
			return "null";
		}
		return "defined";
	}
	
	public String formattedData() {
		return " generated " + numCarsGenerated + ", and sent " + numCarsSent + ". Currently " + numCarsWaiting + " waiting.";
	}

}
