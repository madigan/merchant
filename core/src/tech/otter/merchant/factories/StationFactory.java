package tech.otter.merchant.factories;

import com.badlogic.gdx.utils.Array;

import tech.otter.merchant.data.Station;

/**
 * Created by john on 10/2/16.
 */

public class StationFactory {
	private static StationFactory INSTANCE = new StationFactory();

	public static StationFactory get() {
		return INSTANCE;
	}

	private StationFactory() {

	}

	public Station mock() {
		return new Station("Homeworld", 100f, 100f, "This is the place where you used to live. It has a population of 3 Billion and it is known for its Cubic Yak farms.", null, MerchantFactory.get().make());
	}

	public Array<Station> mockMultiple() {
		Array<Station> stations = new Array<>();
		stations.add(new Station("Homeworld", 200f, 200f, "This is the place where you used to live. It has a population of 3 Billion and it is known for its Cubic Yak farms.", null, MerchantFactory.get().make()));
		stations.add(new Station("Neverland", 50f, 300f, "Contains a very youthful population.", null, MerchantFactory.get().make()));
		stations.add(new Station("Narnia", 350f, 135f, "This is also a place", null, MerchantFactory.get().make()));
		stations.add(new Station("Albuquerque", 100f, 350f, "Did you mean to take a right?", null, MerchantFactory.get().make()));
		stations.add(new Station("New New York", 100f, 267f, "This is also a place", null, MerchantFactory.get().make()));
		return stations;
	}
}
