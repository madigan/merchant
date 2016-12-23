package tech.otter.merchant.model;

import com.badlogic.gdx.utils.ObjectIntMap;

public class AbstractTrader {
	private ObjectIntMap<Item> inventory;

	public AbstractTrader() {
		this(new ObjectIntMap<>());
	}
	public AbstractTrader(ObjectIntMap<Item> inventory) {
		this.inventory = inventory;
	}

	/**
	 *
	 * @param type
	 * @param quantity
	 */
	public void remove(Item type, int quantity) {
		int delta = inventory.get(type, 0) - quantity;
		if(delta > 0) {
			inventory.put(type, delta);
		} else {
			inventory.remove(type, 0);
		}
	}

	public void addItem(Item type, int quantity) {
		inventory.getAndIncrement(type, 0, quantity);
	}

    public void setInventory(ObjectIntMap<Item> inventory) {
        this.inventory = inventory;
    }

	public ObjectIntMap<Item> getInventory() {
		return inventory;
	}
}
