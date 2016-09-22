package tech.otter.merchant.data;

import java.util.List;

/**
 * Created by john on 9/19/16.
 */

public class Merchant {
	private String name;
	private String portrait;
	private List<Item> inventory;

	public Merchant(String name, String portrait, List<Item> inventory) {

		this.name = name;
		this.portrait = portrait;
		this.inventory = inventory;
	}

	public String getName() {
		return name;
	}

	public String getPortrait() {
		return portrait;
	}

	public List<Item> getInventory() {
		return inventory;
	}
}
