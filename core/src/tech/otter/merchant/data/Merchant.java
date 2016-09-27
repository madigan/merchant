package tech.otter.merchant.data;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import tech.otter.merchant.factories.ItemFactory;

public class Merchant extends AbstractTrader {
	private String name;
	private String portrait;

	public Merchant(String name, String portrait, Array<Item> inventory) {
		super(inventory);
		this.name = name;
		this.portrait = portrait;
	}

	private final static float VARIANCE = 0.1f; // 10%
	// FIXME: Returns "0" offers
	/**
	 * Offer a "fair" deal based on the player offer. A "fair" deal is currently 10% lower than the
	 * actual value, although more parameters will be added in the future.
	 * @param playerItem The item the player is offering.
	 * @param playerQty The quantity that the player is offering.
	 * @return A "fair" deal, or null if no deal is possible.
	 */
	public Item getOffer(Item playerItem, int playerQty) {
		// "Fair value" is the base value * number offered - 10% profit margin
		float targetValue = playerItem.getType().getBaseValue() * playerQty * (1-VARIANCE);
		Array<Item> candidates = new Array<Item>();
		for(Item item : getInventory()) {
			if(playerItem.getType().equals(item.getType())) continue;

			int count = MathUtils.floor(targetValue / item.getType().getBaseValue());
			if( count < item.getCount() ) {
				candidates.add(new Item(item.getType(), count));
			}
		}
		return candidates.random();
	}
	public Item getOffer(Item merchantItem, int merchantQty, Array<Item> playerItems) {
		// "Fair value" is the base value * number offered + 10% profit margin
		float targetValue = merchantItem.getType().getBaseValue() * merchantQty * (1+VARIANCE);
		Array<Item> candidates = new Array<Item>();
		for(Item item : playerItems) {
			if(merchantItem.getType().equals(item.getType())) continue;

			int count = MathUtils.floor(targetValue / item.getType().getBaseValue());
			if( count < item.getCount() ) {
				candidates.add(new Item(item.getType(), count));
			}
		}
		return candidates.random();
	}
	public Item getOffer(Item playerItem, int playerQty, Item merchantItem, int merchantQty) {
		float playerValue = playerItem.getType().getBaseValue() * playerQty;
		float merchantValue = merchantItem.getType().getBaseValue() * merchantQty;
		float lowerLimit = merchantValue * (1-VARIANCE);
		// If the player is offering more than the merchant's lower limit, accept the trade.
		if(playerValue > lowerLimit) {
			return new Item(playerItem.getType(), playerQty);
		} else {
			// Otherwise, make a counter offer
			float targetValue = merchantItem.getType().getBaseValue() * merchantQty * (1+VARIANCE);
			int count = MathUtils.floor(targetValue / playerItem.getType().getBaseValue());
			if( count < playerItem.getCount() ) {
				return new Item(playerItem.getType(), count);
			} else {
				// Otherwise, no fair offer is available; Return null
				return null;
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getPortrait() {
		return portrait;
	}

	public static Merchant mock() {
		Array<Item> inventory = Array.with(
				ItemFactory.get().make("ASTRO(C) Nano-Beans").setCount(152)
		);
		return new Merchant("Freddy's General Store", "images/raw/merchant_1.png", inventory);
	}
}
