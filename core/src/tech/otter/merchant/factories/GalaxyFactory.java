package tech.otter.merchant.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import tech.otter.merchant.data.Galaxy;
import tech.otter.merchant.data.StarLane;
import tech.otter.merchant.data.Station;

/**
 * Created by john on 10/2/16.
 */
public class GalaxyFactory {
	private static GalaxyFactory INSTANCE = new GalaxyFactory();

	public static GalaxyFactory get() {
		return INSTANCE;
	}

	private GalaxyFactory() {

	}

	public Galaxy mock() {
		Galaxy galaxy = new Galaxy();
		Array<Station> stations = StationFactory.get().mockMultiple();

		Array<StarLane> lanes = new Array<>();
		for(int i = 1; i < stations.size; i++) {
			lanes.add(new StarLane(stations.get(i-1), stations.get(i), MathUtils.random(10)));
		}
		for(int i = 0; i < stations.size / 2; i++) {
			StarLane lane = new StarLane(stations.random(), stations.random(), MathUtils.random(10));
			if(lane.getTo() != lane.getFrom() && !lanes.contains(lane, false)) lanes.add(lane);
		}

		galaxy.setStations(stations);
		galaxy.setLanes(lanes);
		return galaxy;
	}
}
