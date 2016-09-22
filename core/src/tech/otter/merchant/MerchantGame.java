package tech.otter.merchant;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.github.czyzby.kiwi.log.LoggerService;
import com.kotcrab.vis.ui.VisUI;

import tech.otter.merchant.data.Station;
import tech.otter.merchant.screens.IntroScreen;
import tech.otter.merchant.screens.MainMenuScreen;

public class MerchantGame extends Game {
	Screen mainMenu;
	private boolean debugOn = false;
	private AssetManager assets;

	// libGDX Game Methods //
	@Override
	public void create () {
		assets = new AssetManager();
		VisUI.load();
		this.setDebugOn(false);


		mainMenu = new MainMenuScreen(this);
		this.setScreen(mainMenu);

	}

	@Override
	public void dispose () {
		mainMenu.dispose();
		VisUI.dispose();
		assets.dispose();
	}

	public AssetManager getAssets() {
		return assets;
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

	// TODO: Track a current station instead of returning a new one every time.
	public Station getCurrentStation() {
		return new Station("Homeworld", "This is the place where you used to live. It has a population of 3 Billion and it is known for its Cubic Yak farms.", null);
	}

	// TODO: Make this intelligent
	public boolean isHomeWorld(Station station) {
		return true;
	}
}