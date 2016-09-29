package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;

public interface iTrader {
	Array<Item> getInventory();

	void remove(ItemType type, int quantity);

	void addItem(ItemType type, int quantity);
	void addItem(Item item);
}
