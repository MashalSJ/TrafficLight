import java.util.Scanner;

public class Intersection<T> extends CircularDoublyLinkedList<T> {

	private Node<T> north;
	private Node<T> east;
	private Node<T> south;
	private Node<T> west;

	private Axis northSouth;
	private Axis eastWest;
	private Axis main;
	private Axis other;

	private Side excluded; // Reference to the side that does not exist in a 3-way intersection
	private int numSides;

	private Scanner kb = new Scanner(System.in);

	public Intersection(T north, T east, T south, T west) {
		super();

		if (north.getClass() != (new Side()).getClass()) {
			throw new IllegalArgumentException("Parameters of Intersection must be of the Side class");
		}

		this.north = add(north);
		this.east = add(east);
		this.south = add(south);
		this.west = add(west);

		northSouth = new Axis((Side) this.north.getData(), (Side) this.south.getData());
		eastWest = new Axis((Side) this.east.getData(), (Side) this.west.getData());

		construct();
	}

	private void construct() {
		configureSides();

		assignSidePointers();

		promptNumSides();

		defineExcluded();

		promptEntrances();

		calculateNumExitsNeeded();

		placeEntrances();

		placeExits();

		assignRoadPointers();

		determineCardinalDirections();

		determineMainAxis();
	}

	private void configureSides() {
		((Side) this.north.getData()).setNode(this.north);
		((Side) this.north.getData()).setName(sideToString((Side) this.north.getData()));

		((Side) this.east.getData()).setNode(this.east);
		((Side) this.east.getData()).setName(sideToString((Side) this.east.getData()));

		((Side) this.south.getData()).setNode(this.south);
		((Side) this.south.getData()).setName(sideToString((Side) this.south.getData()));

		((Side) this.west.getData()).setNode(this.west);
		((Side) this.west.getData()).setName(sideToString((Side) this.west.getData()));
	}

	/**
	 * asks if its a 3 or 4 way intersection
	 */
	private void promptNumSides() {
		System.out.println("Is it a 3-way or 4-way intersection?");
		int input = kb.nextInt();

		if (input < 3 || input > 4) {
			throw new IllegalArgumentException("Input must be 3 or 4.");
		}

		numSides = input;
	}

	/**
	 * does assignSidePointers(Side) for each side of the intersection
	 */
	private void assignSidePointers() {
		assignSidePointers((Side) north.getData());
		assignSidePointers((Side) east.getData());
		assignSidePointers((Side) south.getData());
		assignSidePointers((Side) west.getData());
	}

	/**
	 * Defines the across, right, and left pointers for a given side
	 * 
	 * @param side
	 */
	private void assignSidePointers(Side side) {
		side.setAcross((Side) side.getNode().getNext().getNext().getData());
		side.setRight((Side) side.getNode().getPrev().getData());
		side.setLeft((Side) side.getNode().getNext().getData());
	}

	/**
	 * Sets the value of exclude, null if 4 way
	 * 
	 */
	private void defineExcluded() {
		if (numSides == 4) {
			excluded = null;
		} else {
			System.out.println("Enter the integer coressponding to the side that does not exist in the intersection");
			int input = kb.nextInt();
			if (input < 0 || input > 3) {
				throw new IllegalArgumentException(
						"Input has to be integer 0-3\n 0 = North | 1 = East | 2 = South | 3 = West");
			}
			if (input == 0)
				excluded = (Side) north.getData();
			else if (input == 1)
				excluded = (Side) east.getData();
			else if (input == 2)
				excluded = (Side) south.getData();
			else if (input == 3)
				excluded = (Side) west.getData();
		}
	}

	private void promptEntrances() {
		if (excluded != (Side) north.getData()) {
			promptEntrances((Side) north.getData());
		}

		if (excluded != (Side) east.getData()) {
			promptEntrances((Side) east.getData());
		}

		if (excluded != (Side) south.getData()) {
			promptEntrances((Side) south.getData());
		}

		if (excluded != (Side) west.getData()) {
			promptEntrances((Side) west.getData());
		}
	}

	/**
	 * Asks user how many entrances of each type are in a side
	 */
	private void promptEntrances(Side side) {
		int input;
		System.out.println("Beginning " + sideToString(side) + " construction.");
		// straight entrances
		if (side.across() != excluded) {
			System.out.println("How many straight lanes?");
			input = kb.nextInt();
			if (input < 1) {
				throw new IllegalArgumentException("Input must be greater than zero");
			}
			side.setNumStraight(input);
		}

		if (side.right() != excluded) {
			// right turn entrances
			System.out.println("How many right turn lanes?");
			input = kb.nextInt();
			if (input < 0) {
				throw new IllegalArgumentException("Input must be greater or equal to zero");
			}
			side.setNumRightTurns(input);
		}

		if (side.left() != excluded) {
			// left turn entrances
			System.out.println("How many left turn lanes?");
			input = kb.nextInt();
			if (input < 0) {
				throw new IllegalArgumentException("Input must be greater or equal to zero");
			}
			side.setNumLeftTurns(input);
		}
	}

	private void placeEntrances() {
		placeEntrances((Side) north.getData());
		placeEntrances((Side) east.getData());
		placeEntrances((Side) south.getData());
		placeEntrances((Side) west.getData());
	}

	/**
	 * Places the entrances in the sides array and their corresponding exit or null
	 * road in the opposite sides array
	 */
	private void placeEntrances(Side side) {
		// exitIndex is the next available index to place an exit on the
		// opposite side, counting backwards from the end of the list
		int entranceIndex = 0;
		int exitIndex = -1;

		// right turn entrances and null roads in opposite side
		for (int i = 0; i < side.getNumRightTurns(); i++) {
			side.addRoad(entranceIndex, new Entrance(1));
			entranceIndex++;

			side.across().addRoad(side.across().getSize() - 1 - exitIndex, new PlaceHolder());
			exitIndex++;
		}

		// straight entrances and exits in other side
		for (int i = 0; i < side.getNumStraight(); i++) {
			side.addRoad(entranceIndex, new Entrance(0));
			entranceIndex++;

			side.across().addRoad(side.across().getSize() - 1 - exitIndex, new Exit());
			exitIndex++;
		}

		// left turn entrances and null roads in oposite side
		for (int i = 0; i < side.getNumLeftTurns(); i++) {
			side.addRoad(entranceIndex, new Entrance(2));
			entranceIndex++;

			side.across().addRoad(side.across().getSize() - 1 - exitIndex, new PlaceHolder());
			exitIndex++;
		}
	}

	/**
	 * Does placeExits(Side) for each side
	 */
	private void placeExits() {
		placeExits((Side) north.getData());
		placeExits((Side) east.getData());
		placeExits((Side) south.getData());
		placeExits((Side) west.getData());
	}

	/**
	 * Places the rest of the exits needed if there are any
	 * 
	 * @param side
	 */
	private void placeExits(Side side) {

		while (side.getNumExits() < side.getNumExitsNeeded()) {
			// if there are no more blank spaces, expand
			if (side.getSize() == 0 || side.getRoadSegments().get(side.getSize() - 1).getClass() == new Entrance(0).getClass()) {
				side.addRoad(new Exit());
				if (!(side.across() == excluded)) {
					side.across().addRoad(0, new PlaceHolder());
				}
			}

			// special case for last index
			if (side.getRoadSegments().get(side.getSize() - 1).equals(new PlaceHolder())
					&& side.getRoadSegments().get(side.getSize() - 2).equals(new Exit())) {
				side.setRoad(side.getSize() - 1, new Exit());
			}

			// all other cases
			else {
				for (int i = side.getSize() - 2; i > 0; i--) {
					if (side.getRoadSegments().get(i).equals(new PlaceHolder())) {
						if (side.getRoadSegments().get(i + 1).equals(new Exit())) {
							side.setRoad(i, new Exit());
						} else if (side.getRoadSegments().get(i - 1).equals(new Exit())) {
							side.setRoad(i, new Exit());
						}
					}
				}
			}
		}

	}

	/**
	 * does caculateNumExits(Side) for each side
	 */
	private void calculateNumExitsNeeded() {
		calculateNumExitsNeeded((Side) north.getData());
		calculateNumExitsNeeded((Side) east.getData());
		calculateNumExitsNeeded((Side) south.getData());
		calculateNumExitsNeeded((Side) west.getData());
	}

	/**
	 * Calculates the number of exits on a Side based off entrances from other sides
	 */
	private void calculateNumExitsNeeded(Side side) {
		// straight entrances from the across side
		int numExits = side.across().getNumStraight();

		// right turns from the left Side
		int rightTFromLeftS = side.right().getNumLeftTurns();
		if (numExits < rightTFromLeftS) {
			numExits = rightTFromLeftS;
		}

		// left turns from the right Side
		int leftTFromRightS = side.left().getNumRightTurns();
		if (numExits < leftTFromRightS) {
			numExits = leftTFromRightS;
		}

		side.setNumExitsNeeded(numExits);
	}

	/**
	 * Takes a side and returns a string representation of it
	 * 
	 * @param side
	 * @return
	 */
	private String sideToString(Side side) {
		String output = null;
		if (side == north.getData()) {
			output = "North";
		} else if (side == east.getData()) {
			output = "East";
		} else if (side == south.getData()) {
			output = "South";
		} else if (side == west.getData()) {
			output = "West";
		}

		return output;
	}

	private String printSideRoadSegmentData(Node<T> node) {
		Side side = (Side) node.getData();
		return ("\tEntrances:\n\t\tStraight: " + side.getNumStraight() + "\n\t\tRight: " + side.getNumRightTurns()
				+ "\n\t\tLeft: " + side.getNumLeftTurns() + "\n\tExits: " + side.getNumExits());
	}

	public String printRoadSegmentData() {
		String output = "";

		if (excluded != (Side) north.getData()) {
			output += "North Side Data:\n" + printSideRoadSegmentData(north);
		}
		if (excluded != (Side) east.getData()) {
			output += "\n\nEast Side Data:\n" + printSideRoadSegmentData(east);
		}
		if (excluded != (Side) south.getData()) {
			output += "\n\nSouth Side Data:\n" + printSideRoadSegmentData(south);
		}
		if (excluded != (Side) west.getData()) {
			output += "\n\nWest Side Data:\n" + printSideRoadSegmentData(west);
		}

		return output;
	}

	/**
	 * Does assignRoadPointers(Side) for each side
	 */
	private void assignRoadPointers() {
		assignRoadPointers((Side) north.getData());
		assignRoadPointers((Side) east.getData());
		assignRoadPointers((Side) south.getData());
		assignRoadPointers((Side) west.getData());
	}

	/**
	 * Determines the exit for each entrance for all segments in a side
	 */
	private void assignRoadPointers(Side side) {
		Side exitSide = null;
		int rightsAssigned = 0;
		int leftsAssigned = 0;

		int releventAssigned = 0;

		for (int i = 0; i < side.numEntrances(); i++) {
			if (side.get(i).getOrientation() == Orientation.STRAIGHT) {
				((Entrance) side.get(i)).assignExit((Exit) side.across().get(side.getSize() - 1 - i));

			} else if (side.get(i).getOrientation() == Orientation.RIGHT) {
				exitSide = side.right();
				releventAssigned = rightsAssigned;
			} else if (side.get(i).getOrientation() == Orientation.LEFT) {
				exitSide = side.left();
				releventAssigned = leftsAssigned;
			}

			if (exitSide != null) {
				for (int j = exitSide.getSize() - 1; j > -1; j--) {
					int exitsFound = 0;
					if (exitSide.get(j).getClass() == new Exit().getClass()) {
						if (exitsFound == releventAssigned) {
							exitsFound++;
							((Entrance) side.get(i)).assignExit((Exit) exitSide.get(j));
							releventAssigned++;
						}
					}
				}
			}
		}
	}

	/**
	 * Does determineCardinalDirections(Side) for each side
	 */
	private void determineCardinalDirections() {
		determineCardinalDirections((Side) north.getData());
		determineCardinalDirections((Side) east.getData());
		determineCardinalDirections((Side) south.getData());
		determineCardinalDirections((Side) west.getData());
	}

	/**
	 * Determines the cardinal direction of each Direction arrow for display
	 * purposes
	 * 
	 * @param side
	 */
	private void determineCardinalDirections(Side side) {
		int clockwiseRotations = 0;

		if (side == (Side) south.getData()) {
			clockwiseRotations = 0;
		} else if (side == (Side) west.getData()) {
			clockwiseRotations = 1;
		} else if (side == (Side) north.getData()) {
			clockwiseRotations = 2;
		} else if (side == (Side) east.getData()) {
			clockwiseRotations = 3;
		}

		for (int i = 0; i < side.getSize(); i++) {
			PlaceHolder test = new PlaceHolder();
			if (!(side.get(i).getClass() == test.getClass())) {
				Orientation temp = side.get(i).getOrientation();
				for (int j = 0; j < clockwiseRotations; j++) {
					temp = temp.rotateClockwise();
				}
				side.get(i).setDirection(temp.orientationToDirection());
			}
		}
	}

	private void determineMainAxis() {
		int verticalStraightEntrances = 0;
		int horizontalStraightEntrances = 0;

		verticalStraightEntrances += ((Side) getNorth().getData()).instancesOf(new Entrance(0));
		verticalStraightEntrances += ((Side) getSouth().getData()).instancesOf(new Entrance(0));

		horizontalStraightEntrances += ((Side) getEast().getData()).instancesOf(new Entrance(0));
		horizontalStraightEntrances += ((Side) getWest().getData()).instancesOf(new Entrance(0));

		if (horizontalStraightEntrances >= verticalStraightEntrances) {
			main = eastWest;
			other = northSouth;
		} else {
			main = northSouth;
			other = eastWest;
		}
	}

	public Node<T> getNorth() {
		return north;
	}

	public Node<T> getEast() {
		return east;
	}

	public Node<T> getSouth() {
		return south;
	}

	public Node<T> getWest() {
		return west;
	}

	public Side getExcluded() {
		return excluded;
	}

	public int getNumSides() {
		return numSides;
	}

	public Axis getNSAxis() {
		return northSouth;
	}

	public Axis getEWAxis() {
		return eastWest;
	}

	public Axis getMainAxis() {
		return main;
	}

	public Axis getOtherAxis() {
		return other;
	}

	@Override
	public String toString() {
		return "North: " + north.getData().toString() + "\nEast: " + east.getData().toString() + "\nSouth: "
				+ south.getData().toString() + "\nWest: " + west.getData().toString();
	}

	public String directionString() {
		return "North: " + ((Side) north.getData()).directionString() + "\nEast: "
				+ ((Side) east.getData()).directionString() + "\nSouth: " + ((Side) south.getData()).directionString()
				+ "\nWest: " + ((Side) west.getData()).directionString();
	}

	public String statusString() {
		return "North: " + ((Side) north.getData()).statusString() + "\nEast: " + ((Side) east.getData()).statusString()
				+ "\nSouth: " + ((Side) south.getData()).statusString() + "\nWest: "
				+ ((Side) west.getData()).statusString();
	}

	public String carDataString() {
		return "North: " + ((Side) north.getData()).carDataString() + "\nEast: "
				+ ((Side) east.getData()).carDataString() + "\nSouth: " + ((Side) south.getData()).carDataString()
				+ "\nWest: " + ((Side) west.getData()).carDataString();
	}

	public String generationDataString() {
		return "North: " + ((Side) north.getData()).generationDataString() + "\nEast: \t"
				+ ((Side) east.getData()).generationDataString() + "\nSouth: "
				+ ((Side) south.getData()).generationDataString() + "\nWest: \t"
				+ ((Side) west.getData()).generationDataString();
	}

	public String assignedExitSideString() {
		return "North: " + ((Side) north.getData()).assignedExitString() + "\nEast: \t"
				+ ((Side) east.getData()).assignedExitString() + "\nSouth: "
				+ ((Side) south.getData()).assignedExitString() + "\nWest: \t"
				+ ((Side) west.getData()).assignedExitString();
	}

	public String formattedData() {
		return "North" + ((Side) north.getData()).formattedData() + "\n\nEast"
				+ ((Side) east.getData()).formattedData() + "\n\nSouth"
				+ ((Side) south.getData()).formattedData() + "\n\nWest"
				+ ((Side) west.getData()).formattedData();
	}
}
