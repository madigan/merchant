package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;

import tech.otter.merchant.factories.ItemFactory;
import tech.otter.merchant.factories.StationFactory;

public class Player extends AbstractTrader {
	private Station currentStation;
	private Station homeworld;

	public Player() {
		super();
	}

	public Player(Array<Item> inventory, Station currentStation, Station homeworld) {
		super(inventory);
		this.currentStation = currentStation;
		this.homeworld = homeworld;
	}

	public Station getCurrentStation() {
		return this.currentStation;
	}

	public Player setCurrentStation(Station station) {
		this.currentStation = station;
		return this;
	}

	public boolean isAtHomeWorld() {
		return this.homeworld.equals(currentStation);
	}

	public static Player mock() {
		Array<Item> inventory = Array.with(
				ItemFactory.get().make("Cubic Yaks").setCount(20),
				ItemFactory.get().make("Bindookian Spices").setCount(5),
				ItemFactory.get().make("Cryo-Pod").setCount(2));
		Station world = StationFactory.get().mock();
		return new Player(inventory, world, world);
	}
}
