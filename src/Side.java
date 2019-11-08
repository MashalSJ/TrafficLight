import java.util.ArrayList;

/**
 * Each Side represents one side of an intersection
 * 
 * @author Mashal
 *
 */
public class Side {

	private Side across;
	private Side right;
	private Side left;
	private ArrayList<RoadSegment> roadSegments;
	private int size;
	private String name;
	private Node node;
	private int numRightTurns, numLeftTurns, numStraight;
	private int numExitsNeeded, numExits;

	public Side() {
		roadSegments = new ArrayList<RoadSegment>();
		numExitsNeeded = 0;
		numExits = 0;
		numRightTurns = 0;
		numLeftTurns = 0;
		numStraight = 0;
	}

	public int numEntrances() {
		int numEntrances = 0;
		for (int i = 0; i < getSize(); i++) {
			if (get(i).getClass() == new Entrance(0).getClass()) {
				numEntrances++;
			}
		}
		return numEntrances;
	}

	public int instancesOf(RoadSegment road) {
		int instances = 0;

		for (int i = 0; i < roadSegments.size(); i++) {
			if (road.equals(roadSegments.get(i))) {
				instances++;
			}
		}

		return instances;
	}

	public void addRoad(RoadSegment newRoad) {
		roadSegments.add(newRoad);
	}

	public void addRoad(int index, RoadSegment newRoad) {
		roadSegments.add(index, newRoad);
	}

	public void setRoad(int index, RoadSegment newRoad) {
		roadSegments.set(index, newRoad);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Side across() {
		return across;
	}

	public void setAcross(Side across) {
		this.across = across;
	}

	public Side right() {
		return right;
	}

	public void setRight(Side right) {
		this.right = right;
	}

	public Side left() {
		return left;
	}

	public void setLeft(Side left) {
		this.left = left;
	}

	public ArrayList<RoadSegment> getRoadSegments() {
		return roadSegments;
	}

	public void setRoadSegments(ArrayList<RoadSegment> roadSegments) {
		this.roadSegments = roadSegments;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public int getNumRightTurns() {
		return numRightTurns;
	}

	public void setNumRightTurns(int numRightTurns) {
		this.numRightTurns = numRightTurns;
	}

	public int getNumLeftTurns() {
		return numLeftTurns;
	}

	public void setNumLeftTurns(int numLeftTurns) {
		this.numLeftTurns = numLeftTurns;
	}

	public int getNumStraight() {
		return numStraight;
	}

	public void setNumStraight(int numStraight) {
		this.numStraight = numStraight;
	}

	public int getNumExitsNeeded() {
		return numExitsNeeded;
	}

	public void setNumExitsNeeded(int numExitsNeeded) {
		this.numExitsNeeded = numExitsNeeded;
	}

	public int getSize() {
		return roadSegments.size();
	}

	public int getNumExits() {
		return instancesOf(new Exit());
	}

	public Side getClockwise() {
		Side rotated = new Side();
		for (int i = 0; i < getSize(); i++) {
			RoadSegment road = roadSegments.get(i);
			rotated.addRoad(road);
			if (road != null) {
				rotated.get(i).rotateClockWise();
			}
		}
		return rotated;
	}

	public Side getFliped() {
		Side flipped = new Side();

		for (int i = getSize() - 1; i >= 0; i--) {
			flipped.addRoad(roadSegments.get(i));
		}

		return flipped;
	}

	public RoadSegment get(int index) {
		return roadSegments.get(index);
	}

	@Override
	public String toString() {
		String output = "[";
		output += get(0).toString();
		for (int i = 1; i < getSize(); i++) {
			output += ", " + get(i).toString();
		}
		output += "]";
		return output;
	}

	public String directionString() {
		String output = "[";
		if (roadSegments.size() != 0) {
			PlaceHolder tester = new PlaceHolder();
			if (!(get(0).getClass() == tester.getClass())) {
				output += get(0).directionString();
			}
			for (int i = 1; i < getSize(); i++) {
				if (!(get(i).getClass() == tester.getClass())) {
					output += ", " + get(i).directionString();
				}
			}
		}
		output += "]";
		return output;
	}

	public String statusString() {
		String output = "[";

		if ((get(0).getClass() == new Entrance(0).getClass())) {
			output += ((Entrance) get(0)).status();
		}
		for (int i = 1; i < getSize(); i++) {
			if (get(i).getClass() == new Entrance(0).getClass()) {
				output += ", " + ((Entrance) get(i)).status();
			}
		}
		output += "]";
		return output;
	}

	public String carDataString() {
		String output = "[";

		PlaceHolder tester = new PlaceHolder();
		if (!(get(0).getClass() == tester.getClass())) {
			output += get(0).carDataString();
		}
		for (int i = 1; i < getSize(); i++) {
			if (!(get(i).getClass() == tester.getClass())) {
				output += ", " + get(i).carDataString();
			}
		}
		output += "]";
		return output;
	}

	public String generationDataString() {
		String output = "[";

		if ((get(0).getClass() == new Entrance(0).getClass())) {
			output += ((Entrance) get(0)).generationDataString();
		}
		for (int i = 1; i < getSize(); i++) {
			if (get(i).getClass() == new Entrance(0).getClass()) {
				output += ",\t" + ((Entrance) get(i)).generationDataString();
			}
		}
		output += "]";
		return output;
	}

	public String assignedExitString() {
		String output = "[";

		if (roadSegments.size() != 0) {
			if ((get(0).getClass() == new Entrance(0).getClass())) {
				output += ((Entrance) get(0)).assignedExit();
			}
			for (int i = 1; i < getSize(); i++) {
				if (get(i).getClass() == new Entrance(0).getClass()) {
					output += ",\t" + ((Entrance) get(i)).assignedExit();
				}
			}
		}
		output += "]";
		return output;
	}

	public String formattedData() {
		String output = "";
		int numStraightsPrinted = 1;
		int numRightsPrinted = 1;
		int numLeftsPrinted = 1;
		int numExitsPrinted = 1;

		ArrayList<Entrance> straights = getEntrancePointers(new Entrance(0));
		ArrayList<Entrance> rights = getEntrancePointers(new Entrance(1));
		ArrayList<Entrance> lefts = getEntrancePointers(new Entrance(2));
		ArrayList<Exit> exits = getExitPointers();

		output += "\n\tEntrances";
		if (lefts.size() > 0) {
			for (int i = 0; i < lefts.size(); i++) {
				output += "\n\t\tLeft #" + numLeftsPrinted++ + lefts.get(i).formattedData();
			}
		}
		if (straights.size() > 0) {
			for (int i = 0; i < straights.size(); i++) {
				output += "\n\t\tStraight #" + numStraightsPrinted++ + straights.get(i).formattedData();
			}
		}
		if (rights.size() > 0) {
			for (int i = 0; i < rights.size(); i++) {
				output += "\n\t\tRight #" + numRightsPrinted++ + rights.get(i).formattedData();
			}
		}

		output += "\n\tExits";
		if (exits.size() > 0) {
			for (int i = 0; i < exits.size(); i++) {
				output += "\n\t\tExit #" + numExitsPrinted++ + exits.get(i).formattedData();
			}
		}

		return output;
	}

	public ArrayList<Entrance> getEntrancePointers(Entrance type) {
		ArrayList<Entrance> pointers = new ArrayList<Entrance>();

		for (int i = 0; i < getSize(); i++) {
			if (getRoadSegments().get(i).equals(type)) {
				pointers.add((Entrance) getRoadSegments().get(i));
			}
		}
		return pointers;
	}

	public ArrayList<Exit> getExitPointers() {
		ArrayList<Exit> pointers = new ArrayList<Exit>();

		for (int i = 0; i < getSize(); i++) {
			if (getRoadSegments().get(i).getClass() == new Exit().getClass()) {
				pointers.add((Exit) getRoadSegments().get(i));
			}
		}
		return pointers;
	}

}
