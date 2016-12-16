package tech.otter.merchant.screens;

import tech.otter.merchant.GameController;

public class NewGameScreen extends AbstractScreen {

	public NewGameScreen(GameController parent) {
		super(parent);
	}
	
	@Override
	public void show() {
		super.show();
		parent.newGame();
        parent.changeScreen(StationScreen.class);
	}

}
