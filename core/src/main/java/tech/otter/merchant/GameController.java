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

import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.VisUI;
import tech.otter.merchant.data.GameWorld;
import tech.otter.merchant.screens.*;
import tech.otter.merchant.util.GameLogger;
import tech.otter.merchant.util.MultiLogger;

public class GameController extends Game {
	private static final String SKIN_FILE = "skin/neon-ui.json";
	private static final String ATLAS_NAME = "images/merchant.atlas";

	private ObjectMap<Class<? extends AbstractScreen>, AbstractScreen> screens = new ObjectMap<>();
	private boolean debugOn = false;
	private AssetManager assetManager;
	private GameLogger logger;
	private GameWorld world;

	// libGDX Game Methods //
	@Override
	public void create () {
		// Inject the game logger
		logger = new GameLogger();
		Gdx.app.setApplicationLogger(
				new MultiLogger()
						.addLogger(Gdx.app.getApplicationLogger())
						.addLogger(logger));

        // Load the UI first
        VisUI.load();

		// Initialize the asset manager
        // TODO: Make a snazzy loading bar
		assetManager = new AssetManager();
		assetManager.load(ATLAS_NAME, TextureAtlas.class);
		assetManager.finishLoading();

        // Handle any debugging settings
        this.setDebugOn(false);

        // TODO: Make this file-driven
        world = new GameWorld();

        // Load the screens
        loadScreens();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		// Dispose of the view
		setScreen(null);
		screens.forEach((e) -> e.value.dispose());
		screens.clear();
        if(VisUI.isLoaded()) VisUI.dispose();

		// Dispose of the model
		world.dispose();

		// Dispose of any other resources
		assetManager.dispose();
	}

	/**
	 * Convenience method to safely load textures. If the texture isn't found, a blank one is created and the error is logged.
	 * @param imageName The name of the image that is being looked up.
	 * @return
	 */
	public TextureRegionDrawable getManagedTexture(String imageName) {
		try {
			return new TextureRegionDrawable(assetManager.get(ATLAS_NAME, TextureAtlas.class).findRegion(imageName));
		} catch(Exception e) {
			Gdx.app.error(getClass().getCanonicalName(), "Couldn't get managed texture.", e);
			return getEmptyTexture();
		}
	}
	public TextureRegionDrawable getEmptyTexture() {
        return new TextureRegionDrawable(new TextureRegion(new Texture(new Pixmap(1,1, Pixmap.Format.RGBA8888))));
    }

	// Real Game Methods //
	/**
	 * Create a new game, starting with the story screen
	 */
	public void newGame() {
		world.startNewGame();

		this.setScreen(new IntroScreen(this));
	}

    public GameWorld getWorld() {
        return world;
    }

	// === Debug Logic === //
    public boolean isDebugOn() {
        return debugOn;
    }
	public GameController setDebugOn(boolean on) {
		this.debugOn = on;
		Gdx.app.setLogLevel(on ? Application.LOG_DEBUG : Application.LOG_INFO);
		return this;
	}

	// === Screen Management === //
	/**
	 * Eventually this will just be a state machine that takes some sort of event
	 * @param key
	 */
	public void changeScreen(Class<? extends AbstractScreen> key) {
		this.setScreen(screens.get(key));
	}

	public void loadScreens() {
        screens.put(CargoScreen.class, new CargoScreen(this));
        screens.put(DepartureScreen.class, new DepartureScreen(this));
        screens.put(IntroScreen.class, new IntroScreen(this));
        screens.put(MainMenuScreen.class, new MainMenuScreen(this));
        screens.put(NewGameScreen.class, new NewGameScreen(this));
        screens.put(StationScreen.class, new StationScreen(this));
        screens.put(TradeScreen.class, new TradeScreen(this));
    }
}