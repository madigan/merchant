package tech.otter.merchant.model;

public class StarLane {
	private Station to;
	private Station from;
	private int distance;

	public StarLane(Station to, Station from, int distance) {

		this.to = to;
		this.from = from;
		this.distance = distance;
	}

	public Station getTo() {
		return to;
	}

	public Station getFrom() {
		return from;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StarLane starLane = (StarLane) o;

		if (to != null ? !to.equals(starLane.to) : starLane.to != null) return false;
		return from != null ? from.equals(starLane.from) : starLane.from == null;

	}

	@Override
	public int hashCode() {
		int result = to != null ? to.hashCode() : 0;
		result = 31 * result + (from != null ? from.hashCode() : 0);
		return result;
	}
}
