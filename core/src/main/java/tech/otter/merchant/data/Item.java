package tech.otter.merchant.data;

import com.badlogic.gdx.utils.Array;

public class Item {
	private String name;
	private String description;
	private String image;
	private float baseValue;
	private int baseQuantity;
	private float rarityIndex;
	private Array<String> tags = new Array<>();

	public Item() {

	}

	public Item(String name, String description, String image, float baseValue, int baseQuantity, float rarityIndex, Array<String> tags) {
		this.name = name;
		this.description = description;
		this.image = image;
		this.baseValue = baseValue;
		this.baseQuantity = baseQuantity;
		this.rarityIndex = rarityIndex;
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public Item setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Item setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getImage() {
		return image;
	}

	public Item setImage(String image) {
		this.image = image;
		return this;
	}

	public float getBaseValue() {
		return baseValue;
	}

	public Item setBaseValue(float baseValue) {
		this.baseValue = baseValue;
		return this;
	}

	public int getBaseQuantity() {
		return baseQuantity;
	}

	public Item setBaseQuantity(int baseQuantity) {
		this.baseQuantity = baseQuantity;
		return this;
	}

	public float getRarityIndex() {
		return rarityIndex;
	}

	public Item setRarityIndex(float rarityIndex) {
		this.rarityIndex = rarityIndex;
		return this;
	}

	public Array<String> getTags() {
		return tags;
	}

	public Item setTags(Array<String> tags) {
		this.tags = tags;
		return this;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Item item = (Item) o;

		if (name != null ? !name.equals(item.name) : item.name != null) return false;
		return description != null ? description.equals(item.description) : item.description == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (image != null ? image.hashCode() : 0);
		return result;
	}
}
