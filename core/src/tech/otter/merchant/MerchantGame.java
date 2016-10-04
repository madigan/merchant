package tech.otter.merchant;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.kotcrab.vis.ui.VisUI;

import tech.otter.merchant.data.Galaxy;
import tech.otter.merchant.data.Player;
import tech.otter.merchant.factories.GalaxyFactory;
import tech.otter.merchant.screens.IntroScreen;
import tech.otter.merchant.screens.MainMenuScreen;

public class MerchantGame extends Game {
	private Logger logger;
	private Screen mainMenu;
	private boolean debugOn = false;
	private AssetManager assetManager;

	private Galaxy galaxy;
	private Player player;

	// libGDX Game Methods //
	@Override
	public void create () {
		logger = LoggerService.forClass(getClass());
		assetManager = new AssetManager();
		VisUI.load();
		this.setDebugOn(false);

		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;
		TexturePacker.process(settings, "images", "images", "game");

		mainMenu = new MainMenuScreen(this);
		this.setScreen(mainMenu);
	}

	@Override
	public void dispose () {
		mainMenu.dispose(); // TODO: Make sure the other screens are disposing correctly.
		VisUI.dispose();
		assetManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public Player getPlayer() {
		return player;
	}

	public Galaxy getGalaxy() { return galaxy; }

	// Real Game Methods //
	private boolean gameActive = false;
	/**
	 * Create a new game, starting with the story screen
	 */
	public void newGame() {
		gameActive = true;
		this.player = Player.mock();
		this.galaxy = GalaxyFactory.get().mock();
		this.setScreen(new IntroScreen(this));
	}

	public boolean isGameActive() {
		return gameActive;
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