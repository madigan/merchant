package tech.otter.merchant.data;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import tech.otter.merchant.factories.ItemFactory;

public class Merchant extends AbstractTrader {

	private transient Logger logger = LoggerService.forClass(getClass());
	private final static float VARIANCE = 0.1f; // 10%

	private String name;
	private String portrait;

	public Merchant() {
	}

	public Merchant(String name, String portrait, Array<Item> inventory) {
		super(inventory);
		this.name = name;
		this.portrait = portrait;
	}

	/**
	 * Offer a "fair" deal based on the player offer. A "fair" deal is currently 10% lower than the
	 * actual value, although more parameters will be added in the future.
	 * @param playerItem The item the player is offering.
	 * @param playerQty The quantity that the player is offering.
	 * @return A "fair" deal, or null if no deal is possible.
	 */
	public Item getOffer(Item playerItem, int playerQty) {
		// "Fair value" is the base value * number offered - 10% profit margin
		return getOfferHelper(playerItem, playerQty, getInventory(), 1-VARIANCE);
	}
	public Item getOffer(Item merchantItem, int merchantQty, Array<Item> playerItems) {
		// "Fair value" is the base value * number offered + 10% profit margin
		return getOfferHelper(merchantItem, merchantQty, playerItems, 1+VARIANCE);
	}

	/**
	 * Helper method
	 * @param item
	 * @param qty
	 * @param alternatives
	 * @param variance
	 * @return
	 */
	private Item getOfferHelper(Item item, int qty, Array<Item> alternatives, float variance) {
		// "Fair value" is the base value * number offered * variance for profit margin
		float targetValue = item.getType().getBaseValue() * qty * variance;
		Array<Item> candidates = new Array<>();
		for(Item alternative : alternatives) {
			if(alternative.getType().equals(item.getType())) continue;

			int count = MathUtils.floor(targetValue / alternative.getType().getBaseValue());
			if( count < alternative.getCount() && count > 0) {
				candidates.add(new Item(alternative.getType(), count));
				logger.debug(
						"Potential Offer: {0} {1} [{2} for {3}]; merchant wants {4}",
						count,
						alternative.getType().getName(),
						item.getType().getBaseValue() * qty,
						alternative.getType().getBaseValue() * count,
						targetValue);
			}
		}
		return candidates.random();
	}

	/**
	 * Evaluates an offer where an item and quantity have already been provided by both parties.
	 * @param playerItem The item the player is offering (and how many they have)
	 * @param playerQty The quantity the player is offering
	 * @param merchantItem The item the merchant is offering (and how many they have)
	 * @param merchantQty The quantity the merchant is offering
	 * @return A fair trade (same as "playerItem" if the trade is "fair").
	 */
	public Item getOffer(Item playerItem, int playerQty, Item merchantItem, int merchantQty) {
		float playerValue = playerItem.getType().getBaseValue() * playerQty;
		float merchantValue = merchantItem.getType().getBaseValue() * merchantQty;
		float lowerLimit = merchantValue * (1-VARIANCE);
		logger.debug("Offer: [{0} for {1}]; merchant wants at least {2}", playerValue, merchantValue, lowerLimit);
		// If the player is offering more than the merchant's lower limit, accept the trade.
		if(playerValue >= lowerLimit) {
			logger.debug("{0} >= {1}", playerValue, lowerLimit);
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

	public void restock() {
		logger.debug("Clearing inventory ... ");
		this.getInventory().clear();
		logger.debug("Adding items ... ");
		this.getInventory().addAll(ItemFactory.get().make(5));
	}

	// == Getters / Setters == //
	public String getName() {
		return name;
	}

	public String getPortrait() {
		return portrait;
	}

	// == Convenience Methods == //
	public static Merchant mock() {
		Array<Item> inventory = Array.with(
				ItemFactory.get().make("ASTRO(C) Nano-Beans").setCount(152)
		);
		return new Merchant("Freddy's General Store", "merchant1", inventory);
	}
}
