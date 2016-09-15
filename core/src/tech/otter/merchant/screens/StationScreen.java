package tech.otter.merchant.screens;

import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;

public class StationScreen extends AbstractScreen {

	public StationScreen(MerchantGame parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		ui.addActor(new VisTextButton("Hello!"));
	}
}
