import java.util.ArrayList;

public class Axis {

	private Side side1;
	private Side side2;

	public Axis(Side side1, Side side2) {
		this.side1 = side1;
		this.side2 = side2;
	}

	public Side getSide1() {
		return side1;
	}

	public Side getSide2() {
		return side2;
	}

	public int getNumStraights() {
		int numStraights = 0;
		numStraights += side1.instancesOf(new Entrance(0));
		numStraights += side2.instancesOf(new Entrance(0));
		return numStraights;
	}

	public int getNumRights() {
		int numRights = 0;
		numRights += side1.instancesOf(new Entrance(1));
		numRights += side2.instancesOf(new Entrance(1));
		return numRights;
	}

	public int getNumLefts() {
		int numLefts = 0;

		numLefts += side1.instancesOf(new Entrance(2));

		numLefts += side2.instancesOf(new Entrance(2));
		return numLefts;
	}

	public ArrayList<Entrance> getStraights() {
		return combineLists(side1.getEntrancePointers(new Entrance(0)), side2.getEntrancePointers(new Entrance(0)));
	}

	public ArrayList<Entrance> getRights() {
		return combineLists(side1.getEntrancePointers(new Entrance(1)), side2.getEntrancePointers(new Entrance(1)));
	}

	public ArrayList<Entrance> getLefts() {
		return combineLists(side1.getEntrancePointers(new Entrance(2)), side2.getEntrancePointers(new Entrance(2)));
	}

	public ArrayList<Entrance> getTurns() {
		ArrayList<Entrance> entranceList = getLefts();
		return combineLists(entranceList, getRights());
	}

	public ArrayList<Entrance> getEntrances() {
		ArrayList<Entrance> entranceList = getStraights();
		entranceList = combineLists(entranceList, getRights());
		return combineLists(entranceList, getLefts());
	}

	private ArrayList<Entrance> combineLists(ArrayList<Entrance> list1, ArrayList<Entrance> list2) {
		ArrayList<Entrance> combined = new ArrayList<Entrance>();

		for (int i = 0; i < list1.size(); i++) {
			combined.add(list1.get(i));
		}
		for (int i = 0; i < list2.size(); i++) {
			combined.add(list2.get(i));
		}
		return combined;
	}
}
