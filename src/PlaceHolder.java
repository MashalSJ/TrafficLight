
public class PlaceHolder extends RoadSegment {

	public PlaceHolder() {
		setOrientation(null);
	}

	@Override
	public String toString() {
		return "P.H.";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == this.getClass()) {
			return true;
		}
		return false;
	}

	@Override
	public String carDataString() {
		throw new UnsupportedOperationException("Can only get road data from exits and entrances");
	}
	
	
}
