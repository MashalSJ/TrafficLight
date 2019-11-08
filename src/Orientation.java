public enum Orientation {
	STRAIGHT, RIGHT, LEFT, BACKWARDS;


public Orientation rotateClockwise() {
	if(this == Orientation.STRAIGHT) {
		return Orientation.RIGHT;
	}
	else if(this == Orientation.RIGHT) {
		return Orientation.BACKWARDS;
	}
	else if(this == Orientation.BACKWARDS) {
		return Orientation.LEFT;
	}
	else if(this == Orientation.LEFT) {
		return Orientation.STRAIGHT;
	}
	throw new IllegalStateException("If this comes up something is very bad");
}

public CardinalDirection orientationToDirection() {
	if (this == Orientation.STRAIGHT) {
		return CardinalDirection.NORTH;
	}
	if (this == Orientation.RIGHT) {
		return CardinalDirection.EAST;
	}
	if (this == Orientation.LEFT) {
		return CardinalDirection.WEST;
	}
	if (this == Orientation.BACKWARDS) {
		return CardinalDirection.SOUTH;
	}
	throw new IllegalStateException("If this comes up something is very bad");
}
}