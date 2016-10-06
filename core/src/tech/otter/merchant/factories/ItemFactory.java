package tech.otter.merchant.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import java.util.HashMap;

import tech.otter.merchant.data.Item;
import tech.otter.merchant.data.ItemType;

public class ItemFactory {
	private Logger logger = LoggerService.forClass(getClass());
	private static ItemFactory INSTANCE;
	private Array<ItemType> types;
	private HashMap<String, Array<ItemType>> typesByTag;

	private ItemFactory() {
		types = new Array<>();
		typesByTag = new HashMap<>();


		Json json = new Json();
		types = json.fromJson(Array.class, ItemType.class, Gdx.files.internal("item-types.json") );

		// Create lists of types by tag to make lookups faster
		for(ItemType type : types) {
			for(String tag : type.getTags()) {
				if(!typesByTag.containsKey(tag)) typesByTag.put(tag, new Array<>());
				typesByTag.get(tag).add(type);
			}
		}
		for(ItemType i : types) logger.debug("Loaded ItemType: '{0}'", i.getName());
	}

	public static ItemFactory get() {
		if(INSTANCE == null) INSTANCE = new ItemFactory();
		return INSTANCE;
	}

	public Item make(String name) {
		for(ItemType type : types) {
			if(type.getName().equals(name)) {
				return new Item(type);
			}
		}
		logger.error("Couldn't find an item named '{0}'", name);
		return new Item(new ItemType("Sneaky Droids", "This is not the item you're looking for.", null, 1.0f, 1, 1.0f, null));
	}

	/**
	 * Generate a list of random items
	 * @param count The number of items to generate
	 * @return The generated items.
	 */
	public Array<Item> make(int count) {
		Array<Item> items = new Array<>();
		for(int i = 0; i < count; i++) {
			ItemType type = types.random();
			logger.debug("Random type is: {0}", type.getName());
			if(contains(items, type)) {
				logger.debug(" ... but we already have that one in {0}", items);
				i--;
				continue;
			} else {
				logger.debug(" ... we don't have that one yet!");
				items.add(generateItem(type));
			}
		}
		return items;
	}

	/**
	 * Returns a list of items that contain one or more of the tags listed.
	 * @param tags
	 * @return
	 */
	public Array<Item> make(Array<String> tags) {
		Array<Item> items = new Array<>();
		for(String tag : tags) {
			if(!typesByTag.containsKey(tag)) continue;
			for(ItemType type : typesByTag.get(tag)) {
				// Only add the item if it falls below the rarity index (make some items less common)
				if(MathUtils.random(0, 1) <= type.getRarityIndex()) {
					// Don't add duplicate items
					if (!contains(items, type)) {
						items.add(generateItem(type));
					}
				}
			}
		}
		return items;
	}

	/**
	 * Helper method that encapsulates the algorithm used to determine how many items to return.
	 * @param type The type of item to create
	 * @return The generated item.
	 */
	private Item generateItem(ItemType type) {
		return new Item(
				type,
				type.getBaseQuantity() + MathUtils.ceil(type.getBaseQuantity() * MathUtils.random(-0.5f, 0.5f)));
	}

	private boolean contains(Array<Item> items, ItemType type) {
		for(Item item : items) {
			if(item.getType().equals(type)) return true;
		}
		return false;
	}
}
