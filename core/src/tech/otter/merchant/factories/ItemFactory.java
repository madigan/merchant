package tech.otter.merchant.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tech.otter.merchant.data.Item;
import tech.otter.merchant.data.ItemType;

/**
 * Created by john on 9/20/16.
 */

public class ItemFactory {
	private Logger logger = LoggerService.forClass(getClass());
	private static ItemFactory INSTANCE;
	private List<ItemType> types;
	private HashMap<String, List<ItemType>> typesByTag;

	private ItemFactory() {
		types = new ArrayList<ItemType>();
		typesByTag = new HashMap<String, List<ItemType>>();


		Json json = new Json();
		types = json.fromJson(ArrayList.class, ItemType.class, Gdx.files.internal("item-types.json") );

		// Create lists of types by tag to make lookups faster
		for(ItemType type : types) {
			for(String tag : type.getTags()) {
				if(!typesByTag.containsKey(tag)) typesByTag.put(tag, new ArrayList<ItemType>());
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
		return new Item(new ItemType("Sneaky Droids", "This is not the item you're looking for.", null, 1.0f, 1, null));
	}

	/**
	 * Returns a list of items that contain one or more of the tags listed.
	 * @param tags
	 * @return
	 */
	public List<Item> make(List<String> tags) {
		List<Item> list = new ArrayList<Item>();
		for(String tag : tags) {
			if(!typesByTag.containsKey(tag)) continue;
			for(ItemType type : typesByTag.get(tag)) {
				// TODO: Add rarity index check
				// FIXME: Remove duplicate values
				list.add(new Item(type, type.getBaseQuantity() + MathUtils.ceil(type.getBaseQuantity() * MathUtils.random(-0.5f, 0.5f))));
			}
		}
		return list;
	}
}
