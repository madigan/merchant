package tech.otter.merchant.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;

public class Player extends AbstractTrader {
	private Station currentStation;
	private Station homeworld;
    private boolean canLeave;
    private Array<Quest> quests;

	public Player() {
		super();
	}

	public Player(ObjectIntMap<Item> inventory, Station currentStation, Station homeworld) {
		super(inventory);
		this.currentStation = currentStation;
		this.homeworld = homeworld;
        this.canLeave = true;
        this.quests = new Array<>();
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

    public boolean canLeave() {
        return canLeave;
    }

    public Player canLeave(boolean canLeave) {
        this.canLeave = canLeave;
        return this;
    }

    public Array<Quest> getQuests() {
        return quests;
    }
}
