
public class IntersectionRunner {

	public static void main(String[] args) {
		Intersection intersection = new Intersection(new Side(), new Side(), new Side(), new Side());

		@SuppressWarnings("unused")
		IntersectionWindow window = new IntersectionWindow(intersection);

		TrafficLight light = new TrafficLight(intersection);
		System.out.println(intersection.assignedExitSideString());
		System.out.println(intersection.directionString());
		light.runLight(2);
	}
}
