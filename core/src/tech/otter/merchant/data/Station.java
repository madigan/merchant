package tech.otter.merchant.data;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by john on 9/17/16.
 */
public class Station {
	private String name;
	private String description;
	private Texture background;

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

	public Texture getBackground() {
		return background;
	}

	public void setBackground(Texture background) {
		this.background = background;
	}
}
