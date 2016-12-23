package tech.otter.merchant.model;

public class Station {

	private String name;
	private String description;
	private String background;
	private float x;
	private float y;
	private Merchant merchant;

	public Station() {}

	public Station(String name, float x, float y, String description, String background, Merchant merchant) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.description = description;
		this.background = background;
		this.merchant = merchant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Station station = (Station) o;

		if (name != null ? !name.equals(station.name) : station.name != null) return false;
		return description != null ? description.equals(station.description) : station.description == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}
}
