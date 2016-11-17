package tech.otter.merchant;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.kotcrab.vis.ui.VisUI;

import tech.otter.merchant.data.Galaxy;
import tech.otter.merchant.data.Player;
import tech.otter.merchant.factories.GalaxyFactory;
import tech.otter.merchant.factories.ItemFactory;
import tech.otter.merchant.screens.IntroScreen;
import tech.otter.merchant.screens.MainMenuScreen;
import tech.otter.merchant.util.GameLogger;
import tech.otter.merchant.util.MultiLogger;

public class MerchantGame extends Game {
	private boolean debugOn = false;
	private AssetManager assetManager;
	private GameLogger logger;

	private Galaxy galaxy;
	private Player player;

	// libGDX Game Methods //
	@Override
	public void create () {
		// Inject the game logger
		logger = new GameLogger();
		Gdx.app.setApplicationLogger(
				new MultiLogger()
						.addLogger(Gdx.app.getApplicationLogger())
						.addLogger(logger));
		// Initialize the asset manager
		assetManager = new AssetManager();
		VisUI.load();
		this.setDebugOn(false);

		// TODO: Make a snazzy loading bar
		assetManager.load("images/goods.atlas", TextureAtlas.class);
		assetManager.load("images/merchants.atlas", TextureAtlas.class);
		assetManager.load("images/ui.atlas", TextureAtlas.class);
		assetManager.finishLoading();

		// TODO: Create a manager for the screens
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		this.setScreen(null);  // TODO: Make sure all the screens are disposing correctly
		VisUI.dispose();
		assetManager.dispose();
	}

	public TextureRegionDrawable getManagedTexture(String atlas, String image) {
		try {
			return new TextureRegionDrawable(assetManager.get(atlas, TextureAtlas.class).findRegion(image));
		} catch(Exception e) {
			Gdx.app.error(getClass().getCanonicalName(), "Couldn't get managed texture.", e);
			return new TextureRegionDrawable(new TextureRegion(new Texture(new Pixmap(1,1, Pixmap.Format.RGBA8888))));
		}
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
		this.galaxy = GalaxyFactory.get().make();
		this.player = new Player(ItemFactory.get().make(5), galaxy.getStations().get("Homeworld"), galaxy.getStations().get("Homeworld"));
		this.setScreen(new IntroScreen(this));
	}

	public boolean isGameActive() {
		return gameActive;
	}

	public MerchantGame setDebugOn(boolean on) {
		this.debugOn = on;
		Gdx.app.setLogLevel(on ? Application.LOG_DEBUG : Application.LOG_INFO);
		return this;
	}

	public boolean isDebugOn() {
		return debugOn;
	}
}