package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;

public class AbstractTrader implements iTrader {
	private Array<Item> inventory;

	public AbstractTrader() {
		this(new Array<>());
	}
	public AbstractTrader(Array<Item> inventory) {
		this.inventory = inventory;
	}

	@Override
	public Array<Item> getInventory() {
		return inventory;
	}

	@Override
	public void remove(ItemType type, int quantity) {
		for(Item item : inventory) {
			if(item.getType().equals(type)) {
				item.setCount(item.getCount() - quantity);
				if(item.getCount() <= 0) {
					inventory.removeValue(item, true);
				}
				return;
			}
		}
	}

	@Override
	public void addItem(ItemType type, int quantity) {
		for(Item item : inventory) {
			if(item.getType().equals(type)) {
				item.setCount(item.getCount() + quantity);
				return;
			}
		}
		inventory.add(new Item(type, quantity));
	}

	@Override
	public void addItem(Item item) {
		addItem(item.getType(), item.getCount());
	}
}
