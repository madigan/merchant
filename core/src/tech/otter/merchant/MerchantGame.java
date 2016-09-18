package tech.otter.merchant;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.github.czyzby.kiwi.log.LoggerService;
import com.kotcrab.vis.ui.VisUI;

import tech.otter.merchant.screens.IntroScreen;
import tech.otter.merchant.screens.MainMenuScreen;

public class MerchantGame extends Game {
	Screen mainMenu;
	private boolean debugOn = false;
	// libGDX Game Methods //
	@Override
	public void create () {
		VisUI.load();
		this.setDebugOn(false);

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

	public MerchantGame setDebugOn(boolean on) {
		this.debugOn = on;
		LoggerService.debug(debugOn);
		return this;
	}

	public boolean isDebugOn() {
		return debugOn;
	}
}