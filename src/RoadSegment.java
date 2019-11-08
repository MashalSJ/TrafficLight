
public abstract class RoadSegment {

	// fields
	private Orientation orientation;
	private CardinalDirection direction;

	// constructor
	public RoadSegment() {
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public CardinalDirection getDirection() {
		return direction;
	}

	public void setDirection(CardinalDirection direction) {
		this.direction = direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == this.getClass()
				&& ((RoadSegment) obj).getOrientation().compareTo(this.getOrientation()) == 0) {
			return true;
		}
		return false;
	}

	public void rotateClockWise() {
		Entrance entranceTester = new Entrance(0);

		if (this.getClass() == entranceTester.getClass()) {
			orientation = orientation.rotateClockwise();
		}
	}

	public String directionString() {
		String output = null;

		if (direction.compareTo(CardinalDirection.NORTH) == 0) {
			output = "north";
		} else if (direction.compareTo(CardinalDirection.EAST) == 0) {
			output = "east";
		} else if (direction.compareTo(CardinalDirection.SOUTH) == 0) {
			output = "south";
		} else if (direction.compareTo(CardinalDirection.WEST) == 0) {
			output = "west";
		}
		if (output != null) {
			return output;
		}
		throw new IllegalStateException("RoadSegment does not have a defined direction");
	}

	public abstract String carDataString();
}
