package tech.otter.merchant.screens;

import tech.otter.merchant.MerchantGame;

public class NewGameScreen extends AbstractScreen {

	public NewGameScreen(MerchantGame parent) {
		super(parent);
	}
	
	@Override
	public void show() {
		super.show();
		parent.newGame();
		// TODO: Add the new game screen stuff
		changeScreen(new StationScreen(parent));
	}

}
