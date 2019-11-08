import java.awt.Color;

import CS2114.Shape;
import CS2114.Window;

public class IntersectionWindow {
	// fields
	private Intersection intersection;
	private Window window;
	private final int SEGMENT_SIZE = 50;
	private int height, length; // height of vertical sides and length of horizontal sides
	private Side flippedSouth, flippedWest;
	private final int ARROW_LENGTH = 20;
	private final int ARROW_THICKNESS = 4;
	private final int HEAD_SIZE = 10;

	public IntersectionWindow(Intersection intersection) {
		this.intersection = intersection;
		window = new Window("Intersection");
		int heightFactor = ((Side) intersection.getEast().getData()).getSize();
		if (heightFactor < ((Side) intersection.getWest().getData()).getSize()) {
			heightFactor = ((Side) intersection.getWest().getData()).getSize();
		}
		height = SEGMENT_SIZE * heightFactor - heightFactor + 1;
		int widthFactor = ((Side) intersection.getNorth().getData()).getSize();
		if (heightFactor < ((Side) intersection.getSouth().getData()).getSize()) {
			heightFactor = ((Side) intersection.getSouth().getData()).getSize();
		}
		length = SEGMENT_SIZE * heightFactor - heightFactor;

		flipSides();

		drawSides();

	}

	private void drawSides() {
		drawHorizontalSide((Side) intersection.getNorth().getData());
		drawVerticalSide((Side) intersection.getEast().getData());
		drawVerticalSide(flippedWest);
		drawHorizontalSide(flippedSouth);
	}

	private void drawHorizontalSide(Side side) {
		int y = 1;
		if (side == flippedSouth) {
			y = height + SEGMENT_SIZE - 1;
		}
		for (int i = 0; i < side.getSize(); i++) {
			drawRoadSegment(side.get(i), i * SEGMENT_SIZE + SEGMENT_SIZE - i, y);
		}
	}

	private void drawVerticalSide(Side side) {
		int x = 1;
		if (side == (Side) intersection.getEast().getData()) {
			x = length + SEGMENT_SIZE - 1;
		}
		for (int i = 0; i < side.getSize(); i++) {
			drawRoadSegment(side.get(i), x, i * SEGMENT_SIZE + SEGMENT_SIZE - i);
		}
	}

	/**
	 * draws the RoadSegment at a coordinate
	 * 
	 * @param road
	 * @param x
	 * @param y
	 */
	private void drawRoadSegment(RoadSegment road, int x, int y) {
		// draw outline for the road
		Shape outline = new Shape(x, y, SEGMENT_SIZE, Color.BLACK);
		Shape fill = new Shape(x + 1, y + 1, SEGMENT_SIZE - 2, Color.WHITE);

		Shape arrowLine = null;
		Shape arrowHead = null;

		if (!(road.toString().equals("P.H."))) {
			if (road.getDirection() == CardinalDirection.NORTH || road.getDirection() == CardinalDirection.SOUTH) {
				int lineX = x + SEGMENT_SIZE / 2 - ARROW_THICKNESS / 2;
				int lineY = y + SEGMENT_SIZE / 2 - ARROW_LENGTH / 2 + HEAD_SIZE / 2;
				int headX = lineX - (HEAD_SIZE - ARROW_THICKNESS) / 2;
				if ((road.getDirection() == CardinalDirection.SOUTH)) {
					lineY -= HEAD_SIZE / 2;
					arrowLine = new Shape(lineX, lineY, ARROW_THICKNESS, ARROW_LENGTH, Color.BLACK);
					arrowHead = new Shape(headX, y + SEGMENT_SIZE / 2 + ARROW_LENGTH / 2, HEAD_SIZE, Color.BLACK);
				} else {
					lineY += HEAD_SIZE / 2;
					arrowLine = new Shape(lineX, lineY, ARROW_THICKNESS, ARROW_LENGTH, Color.BLACK);
					arrowHead = new Shape(headX, y + SEGMENT_SIZE / 2 - ARROW_LENGTH / 2, HEAD_SIZE, Color.BLACK);
				}
			} else if (road.getDirection() == CardinalDirection.EAST || road.getDirection() == CardinalDirection.WEST) {
				int lineY = y + SEGMENT_SIZE / 2 - ARROW_THICKNESS / 2;
				int lineX = x + SEGMENT_SIZE / 2 - ARROW_LENGTH / 2 + HEAD_SIZE / 2;
				int headY = lineY - (HEAD_SIZE - ARROW_THICKNESS) / 2;

				if ((road.getDirection() == CardinalDirection.EAST)) {
					lineX -= HEAD_SIZE / 2;
					arrowLine = new Shape(lineX, lineY, ARROW_LENGTH, ARROW_THICKNESS, Color.BLACK);
					arrowHead = new Shape(x + SEGMENT_SIZE / 2 + ARROW_LENGTH / 2, headY, HEAD_SIZE, Color.BLACK);
				} else {
					lineX += HEAD_SIZE / 2;
					arrowLine = new Shape(lineX, lineY, ARROW_LENGTH, ARROW_THICKNESS, Color.BLACK);
					arrowHead = new Shape(x + SEGMENT_SIZE / 2 - ARROW_LENGTH / 2, headY, HEAD_SIZE, Color.BLACK);
				}
			}

			if (arrowHead != null) {
				window.addShape(arrowHead);
				window.addShape(arrowLine);
			}
			window.addShape(fill);
			window.addShape(outline);

		}
	}

	private void flipSides() {

		flippedSouth = ((Side) intersection.getSouth().getData()).getFliped();
		flippedWest = ((Side) intersection.getWest().getData()).getFliped();

	}

}
