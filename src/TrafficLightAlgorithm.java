import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLightAlgorithm extends TimerTask {
	private TrafficLight light;
	private Intersection<Side> intersection;
	private Timer timer;
	private final double CHANCE = 3; // % chance a car will generate at a road segment
	private int duration; // duration of simulation in seconds

	public TrafficLightAlgorithm(TrafficLight light, int duration) {
		this.light = light;
		intersection = light.getIntersection();
		this.duration = duration;
	}

	@Override
	public void run() {
		long second = javax.management.timer.Timer.ONE_SECOND;
		timer = new Timer();

		CheckStatus checkStatus = new CheckStatus();
		CheckCarData checkCarData = new CheckCarData();
		CheckGenerationData checkGenerationData = new CheckGenerationData();
		PrintFormattedData printFormattedData = new PrintFormattedData();

		GenerateCars generateCars = new GenerateCars();
		MoveCars moveCars = new MoveCars();

		StepA stepA = new StepA();
		StepB stepB = new StepB();
		StepC stepC = new StepC();
		StepD stepD = new StepD();
		StepE stepE = new StepE();
		Stop stop = new Stop();

		final int STRAIGHT_STEP_TIME = 30;
		final int OTHER_STEP_TIME = 10;
		final int CYCLE_TIME = 1 + 3 * STRAIGHT_STEP_TIME + OTHER_STEP_TIME;

		timer.scheduleAtFixedRate(generateCars, 0, 2);
		timer.scheduleAtFixedRate(moveCars, 0, OTHER_STEP_TIME);

		// timer.scheduleAtFixedRate(checkCarData, 0, 100);

		timer.scheduleAtFixedRate(stepA, 1, CYCLE_TIME);
		timer.scheduleAtFixedRate(stepB, 1 + STRAIGHT_STEP_TIME, CYCLE_TIME);
		timer.scheduleAtFixedRate(stepC, 1 + 2 * STRAIGHT_STEP_TIME, CYCLE_TIME);
		timer.scheduleAtFixedRate(stepD, 1 + 3 * STRAIGHT_STEP_TIME, CYCLE_TIME);
		timer.scheduleAtFixedRate(stepE, 1 + 3 * STRAIGHT_STEP_TIME + OTHER_STEP_TIME, CYCLE_TIME);

		timer.schedule(printFormattedData, duration * second - 1);
		timer.schedule(stop, duration * second);
	}

	private class StepA extends TimerTask {
		public void run() {
			// sets other's lefts to red
			if (intersection.getNumSides() == 4) {
				ArrayList<Entrance> otherLefts = intersection.getOtherAxis().getLefts();
				for (int i = 0; i < otherLefts.size(); i++) {
					otherLefts.get(i).setRed();
				}
			}

			// sets main's straights and rights to green
			ArrayList<Entrance> mainStraights = intersection.getMainAxis().getStraights();
			for (int i = 0; i < mainStraights.size(); i++) {
				mainStraights.get(i).setGreen();
			}
			ArrayList<Entrance> mainRights = intersection.getMainAxis().getRights();
			for (int i = 0; i < mainRights.size(); i++) {
				mainRights.get(i).setGreen();
			}
		}
	}

	private class StepB extends TimerTask {
		public void run() {
			// sets main's straights and rights to red
			ArrayList<Entrance> mainStraights = intersection.getMainAxis().getStraights();
			for (int i = 0; i < mainStraights.size(); i++) {
				mainStraights.get(i).setRed();
			}
			ArrayList<Entrance> mainRights = intersection.getMainAxis().getRights();
			for (int i = 0; i < mainRights.size(); i++) {
				mainRights.get(i).setRed();
			}

			// sets other's straights and rights to green
			if (intersection.getNumSides() == 4) {
				ArrayList<Entrance> otherStraights = intersection.getOtherAxis().getStraights();
				for (int i = 0; i < otherStraights.size(); i++) {
					otherStraights.get(i).setGreen();
				}
				ArrayList<Entrance> otherRights = intersection.getOtherAxis().getRights();
				for (int i = 0; i < otherRights.size(); i++) {
					otherRights.get(i).setGreen();
				}
			}

		}
	}

	private class StepC extends TimerTask {
		public void run() {
			// sets other's straights to red
			if (intersection.getNumSides() == 4) {
				ArrayList<Entrance> otherStraights = intersection.getOtherAxis().getStraights();
				for (int i = 0; i < otherStraights.size(); i++) {
					otherStraights.get(i).setRed();
				}
				ArrayList<Entrance> otherRights = intersection.getOtherAxis().getRights();
				for (int i = 0; i < otherRights.size(); i++) {
					otherRights.get(i).setRed();
				}
			}

			// sets 3-way's lanes to green
			if (intersection.getNumSides() == 3) {
				ArrayList<Entrance> lefts = intersection.getOtherAxis().getLefts();
				for (int i = 0; i < lefts.size(); i++) {
					lefts.get(i).setGreen();
				}
				ArrayList<Entrance> otherRights = intersection.getOtherAxis().getRights();
				for (int i = 0; i < otherRights.size(); i++) {
					otherRights.get(i).setGreen();
				}
			}
		}
	}

	private class StepD extends TimerTask {
		public void run() {
			// sets 3-way's lanes to red
			if (intersection.getNumSides() == 3) {
				ArrayList<Entrance> lefts = intersection.getOtherAxis().getLefts();
				for (int i = 0; i < lefts.size(); i++) {
					lefts.get(i).setRed();
				}
				ArrayList<Entrance> otherRights = intersection.getOtherAxis().getRights();
				for (int i = 0; i < otherRights.size(); i++) {
					otherRights.get(i).setRed();
				}
			}

			// sets main's lefts to green
			ArrayList<Entrance> mainLefts = intersection.getMainAxis().getLefts();
			for (int i = 0; i < mainLefts.size(); i++) {
				mainLefts.get(i).setGreen();
			}
		}
	}

	private class StepE extends TimerTask {
		public void run() {
			// sets main's lefts to red
			ArrayList<Entrance> mainLefts = intersection.getMainAxis().getLefts();
			for (int i = 0; i < mainLefts.size(); i++) {
				mainLefts.get(i).setRed();
			}

			// sets other's lefts to green
			if (intersection.getNumSides() == 4) {
				ArrayList<Entrance> otherLefts = intersection.getOtherAxis().getLefts();
				for (int i = 0; i < otherLefts.size(); i++) {
					otherLefts.get(i).setGreen();
				}
			}
		}
	}

	private class GenerateCars extends TimerTask {
		double mainStraightsWeight = .35;
		final double OTHER_STRAIGHTS_WEIGHT = .30;
		final double THREEWAY_TURNS_WEIGHT = .30;
		double mainRightsWeight = .20;
		final double OTHER_RIGHTS_WEIGHT = .15;
		double mainLeftsWeight = .13;
		final double OTHER_LEFTS_WEIGHT = .13;

		public void run() {
			// Randomly generate cars in each entrance
			generateCars(intersection.getMainAxis().getStraights(), mainStraightsWeight);
			generateCars(intersection.getMainAxis().getRights(), mainRightsWeight);
			generateCars(intersection.getMainAxis().getLefts(), mainLeftsWeight);
			if (intersection.getNumSides() == 4) {
				generateCars(intersection.getOtherAxis().getStraights(), OTHER_STRAIGHTS_WEIGHT);
				generateCars(intersection.getOtherAxis().getRights(), OTHER_RIGHTS_WEIGHT);
				generateCars(intersection.getOtherAxis().getLefts(), OTHER_LEFTS_WEIGHT);
			} else if (intersection.getNumSides() == 3) {
				generateCars(intersection.getOtherAxis().getTurns(), THREEWAY_TURNS_WEIGHT);
			}
		}

		private void generateCars(ArrayList<Entrance> entrances, double weight) {
			for (int i = 0; i < entrances.size(); i++) {
				double roll = (Math.random() * 100);
				if (CHANCE * weight > roll) {
					entrances.get(i).generateCar();
					entrances.get(i).generateCar();
					entrances.get(i).generateCar();
					entrances.get(i).generateCar();
					entrances.get(i).generateCar();
				}
			}
		}
	}

	private class MoveCars extends TimerTask {
		public void run() {
			// Move cars from currently green entrances to their exits
			ArrayList<Entrance> entrances = combineLists(intersection.getMainAxis().getEntrances(),
					intersection.getOtherAxis().getEntrances());
			for (int i = 0; i < entrances.size(); i++) {
				if (entrances.get(i).isGreen()) {
					entrances.get(i).sendCar();
				}
			}
		}
	}

	private class CheckStatus extends TimerTask {
		public void run() {
			System.out.println("\n" + intersection.statusString());
		}
	}

	private class CheckCarData extends TimerTask {
		public void run() {
			System.out.println("\n" + intersection.carDataString());
		}
	}

	private class CheckGenerationData extends TimerTask {
		public void run() {
			System.out.println("\n" + intersection.generationDataString());
		}
	}

	private class PrintFormattedData extends TimerTask {
		public void run() {
			System.out.println("\n" + intersection.formattedData());
		}
	}

	private class Stop extends TimerTask {
		public void run() {
			timer.cancel();
		}
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