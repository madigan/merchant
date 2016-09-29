package tech.otter.merchant.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tech.otter.merchant.data.Item;
import tech.otter.merchant.data.ItemType;

public class ItemFactory {
	private Logger logger = LoggerService.forClass(getClass());
	private static ItemFactory INSTANCE;
	private Array<ItemType> types;
	private HashMap<String, List<ItemType>> typesByTag;

	private ItemFactory() {
		types = new Array<>();
		typesByTag = new HashMap<>();


		Json json = new Json();
		types = json.fromJson(Array.class, ItemType.class, Gdx.files.internal("item-types.json") );

		// Create lists of types by tag to make lookups faster
		for(ItemType type : types) {
			for(String tag : type.getTags()) {
				if(!typesByTag.containsKey(tag)) typesByTag.put(tag, new ArrayList<>());
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
						items.add(
								new Item(
										type,
										type.getBaseQuantity() + MathUtils.ceil(type.getBaseQuantity() * MathUtils.random(-0.5f, 0.5f))));
					}
				}
			}
		}
		return items;
	}

	private boolean contains(Array<Item> items, ItemType type) {
		for(Item item : items) {
			if(item.getType().equals(type)) return false;
		}
		return true;
	}
}
