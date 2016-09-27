package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;

import tech.otter.merchant.factories.ItemFactory;

public class Player extends AbstractTrader {

	public Player() {
		super();
	}

	public Player(Array<Item> inventory) {
		super(inventory);
	}

	public static Player mock() {
		Array<Item> inventory = Array.with(
				ItemFactory.get().make("Cubic Yaks").setCount(20),
				ItemFactory.get().make("Bindookian Spices").setCount(5),
				ItemFactory.get().make("Cryo-Pod").setCount(2));
		return new Player(inventory);
	}
}
