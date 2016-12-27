package tech.otter.merchant.view;

import com.badlogic.gdx.Gdx;
import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;

public class NewGameScreen extends View {

	public NewGameScreen(Controller controller, Model model) {
        super(controller, model);
	}
	
	@Override
	public void init() {
        Gdx.app.debug("WEB", "Starting new game...");
		controller.newGame();
        Gdx.app.debug("WEB", "... new game loaded!");
        controller.changeScreen(StationScreen.class);
	}

}
