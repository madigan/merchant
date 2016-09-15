package tech.otter.merchant;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;

import tech.otter.merchant.screens.IntroScreen;
import tech.otter.merchant.screens.MainMenuScreen;

public class MerchantGame extends Game {
	Screen mainMenu;
	// libGDX Game Methods //
	@Override
	public void create () {
		VisUI.load();

		mainMenu = new MainMenuScreen(this);
		this.setScreen(mainMenu);
	}

	@Override
	public void dispose () {
		mainMenu.dispose();
		VisUI.dispose();
	}

	// Real Game Methods //
	private boolean gameActive = false;
	/**
	 * Create a new game, starting with the story screen
	 */
	public void newGame() {
		gameActive = true;

		this.setScreen(new IntroScreen(this));
	}

	public boolean isGameActive() {
		return gameActive;
	}

	public void loadGame() {

	}

	public void saveGame() {

	}
}