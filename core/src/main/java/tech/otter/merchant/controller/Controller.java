package tech.otter.merchant.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.VisUI;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.util.GameLogger;
import tech.otter.merchant.util.MultiLogger;
import tech.otter.merchant.view.*;

public class Controller extends Game {
	private static final String SKIN_FILE = "skin/neon-ui.json";
	private static final String ATLAS_NAME = "images/merchant.atlas";

	private ObjectMap<Class<? extends View>, View> screens = new ObjectMap<>();
	private boolean debugOn = false;
	private AssetManager assetManager;
	private GameLogger logger;
	private Model model;

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
        model = new Model();

        // Load the screens
        loadScreens();
		this.changeScreen(MainMenuScreen.class);
	}

	@Override
	public void dispose () {
		// Dispose of the view
		setScreen(null);
		screens.forEach((e) -> e.value.dispose());
		screens.clear();
        if(VisUI.isLoaded()) VisUI.dispose();

		// Dispose of the model
		model.dispose();

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
			Gdx.app.error(getClass().getSimpleName(), "Couldn't get managed texture.", e);
			return getEmptyTexture();
		}
	}
	public TextureRegionDrawable getEmptyTexture() {
        return new TextureRegionDrawable(new TextureRegion(new Texture(new Pixmap(1,1, Pixmap.Format.RGBA8888))));
    }

    // Event Bus Methods //
    private ObjectMap<String, Array<Subscriber>> subscriptions = new ObjectMap<>();

    /**
     * Allow subscribers to subscribe to certain types of GameEvents. Right now it's just based on type, but this could
     * be expanded to include additional criteria.
     * @param subscriber The subscriber to add.
     * @param type A string representation of the GameEvent type; the subscriber will receive all events of this type.
     */
    public void subscribe(Subscriber subscriber, String type) {
        if(!subscriptions.containsKey(type)) subscriptions.put(type, new Array<>());
        if(!subscriptions.get(type).contains(subscriber, true)) {
            subscriptions.get(type).add(subscriber);
        }
    }

    /**
     * Allow subscribers to end their subscriptions.
     * @param subscriber The subscriber to be removed.
     * @param type The event to unsubscribe from.
     */
    public void unsubscribe(Subscriber subscriber, String type) {
        if(subscriptions.containsKey(type)) {
            subscriptions.get(type).removeValue(subscriber, true);
        }
    }

    /**
     * Publishes an event to all appropriate subscribers.
     * @param event The event to publish.
     */
    public void handle(GameEvent event) {
        if(subscriptions.containsKey(event.type)) {
            subscriptions.get(event.type).forEach(subscriber -> subscriber.handle(event));
        }
    }

	// Real Game Methods //
	/**
	 * Create a new game, starting with the story screen
	 */
	public void newGame() {
        this.subscriptions.clear(); // Just in case
		model.startNewGame(this);
		this.changeScreen(IntroScreen.class);
	}

    public Model getModel() {
        return model;
    }

	// === Debug Logic === //
    public boolean isDebugOn() {
        return debugOn;
    }
	public Controller setDebugOn(boolean on) {
		this.debugOn = on;
		Gdx.app.setLogLevel(on ? Application.LOG_DEBUG : Application.LOG_INFO);
		return this;
	}

	// === Screen Management === //
	/**
	 * Eventually this will just be a state machine that takes some sort of event
	 * @param key
	 */
	public void changeScreen(Class<? extends View> key) {
		this.setScreen(screens.get(key));
        handle(new GameEvent("SCREEN_CHANGE").set("SCREEN", screens.get(key)));
	}

	public void loadScreens() {
        screens.put(CargoScreen.class, new CargoScreen(this, model));
        screens.put(DepartureScreen.class, new DepartureScreen(this, model));
        screens.put(IntroScreen.class, new IntroScreen(this, model));
        screens.put(MainMenuScreen.class, new MainMenuScreen(this, model));
        screens.put(NewGameScreen.class, new NewGameScreen(this, model));
        screens.put(StationScreen.class, new StationScreen(this, model));
        screens.put(TradeScreen.class, new TradeScreen(this, model));
        screens.put(ClanScreen.class, new ClanScreen(this, model));
    }
}