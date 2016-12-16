package tech.otter.merchant.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import java.util.HashMap;

import tech.otter.merchant.data.Item;

public class ItemFactory {
    private static final String FILE_NAME = "items.json";

	private Logger logger = LoggerService.forClass(getClass());
	private static ItemFactory INSTANCE;
	private Array<Item> types;
	private HashMap<String, Array<Item>> typesByTag;

	private ItemFactory() {
		types = new Array<>();
		typesByTag = new HashMap<>();


		Json json = new Json();
		types = json.fromJson(Array.class, Item.class, Gdx.files.internal(FILE_NAME) );

		// Create lists of types by tag to make lookups faster
		for(Item type : types) {
			for(String tag : type.getTags()) {
				if(!typesByTag.containsKey(tag)) typesByTag.put(tag, new Array<Item>());
				typesByTag.get(tag).add(type);
			}
		}
		for(Item i : types) Gdx.app.debug(getClass().getCanonicalName(), String.format("Loaded Item: '%s'", i.getName()));
	}

	public static ItemFactory get() {
		if(INSTANCE == null) INSTANCE = new ItemFactory();
		return INSTANCE;
	}

	public ObjectIntMap<Item> make(String name) {
		ObjectIntMap<Item> map = new ObjectIntMap<>();
		for(Item type : types) {
			if(type.getName().equals(name)) {
				map.put(type, 0);
				return map;
			}
		}
		Gdx.app.error(getClass().getCanonicalName(), String.format("Couldn't find an item named '%s'", name));
		map.put(new Item("Sneaky Droids", "This is not the item you're looking for.", null, 1.0f, 1, 1.0f, null), 0);
		return map;
	}

	/**
	 * Generate a list of random items
	 * @param count The number of items to generate
	 * @return The generated items.
	 */
	public ObjectIntMap<Item> make(int count) {
		ObjectIntMap<Item> items = new ObjectIntMap<>();
		types.shuffle();
		for(int i = 0; i < count; i++) {
			items.putAll(generateItem(types.get(i)));
		}
		return items;
	}

	/**
	 * Returns a list of items that contain one or more of the tags listed.
	 * @param tags
	 * @return
	 */
	public ObjectIntMap<Item> make(Array<String> tags) {
		ObjectIntMap<Item> items = new ObjectIntMap<>();
		for(String tag : tags) {
			if(!typesByTag.containsKey(tag)) continue;
			for(Item type : typesByTag.get(tag)) {
				// Only add the item if it falls below the rarity index (make some items less common)
				if(MathUtils.random(0, 1) <= type.getRarityIndex()) {
					items.putAll(generateItem(type));
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
	private ObjectIntMap<Item> generateItem(Item type) {
		ObjectIntMap<Item> item = new ObjectIntMap<>();
		item.put(type, type.getBaseQuantity() + MathUtils.ceil(type.getBaseQuantity() * MathUtils.random(-0.5f, 0.5f)));
		return item;
	}
}
