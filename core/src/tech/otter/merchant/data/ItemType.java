package tech.otter.merchant.data;

import java.util.List;

/**
 * Created by john on 9/19/16.
 */

public class ItemType {
	private String name;
	private String description;
	private String image;
	private float baseValue;
	private int baseQuantity;
	private List<String> tags;

	public ItemType() {

	}

	public ItemType(String name, String description, String image, float baseValue, int baseQuantity, List<String> tags) {
		this.name = name;
		this.description = description;
		this.baseValue = baseValue;
		this.baseQuantity = baseQuantity;
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public float getBaseValue() {
		return baseValue;
	}

	public int getBaseQuantity() {
		return baseQuantity;
	}

	public List<String> getTags() {
		return tags;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemType itemType = (ItemType) o;

		if (name != null ? !name.equals(itemType.name) : itemType.name != null) return false;
		if (description != null ? !description.equals(itemType.description) : itemType.description != null)
			return false;
		return image != null ? image.equals(itemType.image) : itemType.image == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (image != null ? image.hashCode() : 0);
		return result;
	}
}
