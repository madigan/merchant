package tech.otter.merchant.data;

/**
 * Created by john on 9/17/16.
 */
public class Station {

	private String name;
	private String description;
	private String background;

	public Station(String name, String description, String background) {
		this.name = name;
		this.description = description;
		this.background = background;
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

	public static Station mock() {
		return new Station("Homeworld", "This is the place where you used to live. It has a population of 3 Billion and it is known for its Cubic Yak farms.", null);
	}
}
