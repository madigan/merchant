package tech.otter.merchant.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.Station;

public class StationScreen extends AbstractScreen {

	public StationScreen(MerchantGame parent) {
		super(parent);
	}
	
	@Override
	public void show() {
		super.show();
		Station station = parent.getPlayer().getCurrentStation();

		VisTable tblLayout = new VisTable();
		tblLayout.setFillParent(true);
		if(station.getBackground() != null) tblLayout.setBackground(station.getBackground());

		VisLabel lblStationName = new VisLabel(station.getName());
		// TODO: Make this lblTitle bigger


		VisLabel lblStationDescription = new VisLabel(station.getDescription());
		lblStationDescription.setWrap(true);
		lblStationDescription.setWidth(100f);
		VisScrollPane spStationDescription = new VisScrollPane(lblStationDescription);

		VisTable tblButtons = new VisTable();
		tblButtons.columnDefaults(0).pad(2f).width(300f);

		if(parent.getPlayer().isAtHomeWorld()) {
			// TODO: Add Clan Screen
			VisTextButton btnClan = new VisTextButton("Visit Clan");
			tblButtons.add(btnClan);
			tblButtons.row();
		}

		VisTextButton btnTrader = new VisTextButton("Visit Trader", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(new TradeScreen(parent, station.getMerchant(), StationScreen.this));
			}
		});
		// TODO: Add Bar Screen
		VisTextButton btnBar = new VisTextButton("Visit Bar");

		VisTextButton btnCargo = new VisTextButton("View Cargo", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(new CargoScreen(parent));
			}
		});

		VisTextButton btnLeave = new VisTextButton("Leave Station", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(new DepartureScreen(parent, StationScreen.this));
			}
		});

		VisTextButton btnMenu = new VisTextButton("Quit to Main Menu", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(new MainMenuScreen(parent));
			}
		});

		tblButtons.add(btnTrader);
		tblButtons.row();
		tblButtons.add(btnBar);
		tblButtons.row();
		tblButtons.add(btnCargo);
		tblButtons.row();
		tblButtons.add(btnLeave);
		tblButtons.row();
		tblButtons.addSeparator();
		tblButtons.row();
		tblButtons.add(btnMenu);

		// === Add elements to the screen === //
		tblLayout.add(lblStationName).colspan(2).pad(10f);
		tblLayout.row();
		tblLayout.add(spStationDescription).width(200f).height(141f).top();
		tblLayout.add(tblButtons);
		ui.addActor(tblLayout);
	}
}
