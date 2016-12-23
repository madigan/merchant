package tech.otter.merchant.view;

import tech.otter.merchant.controller.GameController;

public class NewGameScreen extends GameScreen {

	public NewGameScreen(GameController parent) {
		super(parent);
	}
	
	@Override
	public void show() {
		super.show();
		controller.newGame();
        controller.changeScreen(tech.otter.merchant.view.StationScreen.class);
	}

}
