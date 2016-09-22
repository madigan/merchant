package tech.otter.merchant.data;

/**
 * Created by john on 9/21/16.
 */

public class Item {
	private ItemType type;
	private int count;

	public Item(ItemType type) {
		this(type, 0);
	}
	public Item(ItemType type, int count) {
		this.type = type;
		this.count = count;
	}


	public ItemType getType() {
		return type;
	}

	public Item setType(ItemType type) {
		this.type = type;
		return this;
	}

	public int getCount() {
		return count;
	}

	public Item setCount(int count) {
		this.count = count;
		return this;
	}
}
