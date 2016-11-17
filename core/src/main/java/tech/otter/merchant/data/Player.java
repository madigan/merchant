package tech.otter.merchant.data;

import com.badlogic.gdx.utils.ObjectIntMap;

public class Player extends AbstractTrader {
	private Station currentStation;
	private Station homeworld;

	public Player() {
		super();
	}

	public Player(ObjectIntMap<Item> inventory, Station currentStation, Station homeworld) {
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
}
