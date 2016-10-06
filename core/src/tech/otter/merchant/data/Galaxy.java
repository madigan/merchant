package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

public class Galaxy {
	private OrderedMap<String, Station> stations;
	private Array<StarLane> lanes;

	public Galaxy() {
		this.stations = new OrderedMap<>();
		this.lanes = new Array<>();
	}

	public OrderedMap<String, Station> getStations() {
		return stations;
	}

	public Galaxy setStations(OrderedMap<String, Station> stations) {
		this.stations = stations;
		return this;
	}

	public Galaxy setStations(Array<Station> stations) {
		for(Station station : stations) {
			this.stations.put(station.getName(), station);
		}
		return this;
	}

	public Array<StarLane> getLanes() {
		return lanes;
	}

	public Galaxy setLanes(Array<StarLane> lanes) {
		this.lanes = lanes;
		return this;
	}
}
