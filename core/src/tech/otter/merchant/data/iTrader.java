package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;

/**
 * Created by john on 9/25/16.
 */

public interface iTrader {
	public Array<Item> getInventory();

	public void remove(ItemType type, int quantity);

	public void addItem(ItemType type, int quantity);
	public void addItem(Item item);
}
