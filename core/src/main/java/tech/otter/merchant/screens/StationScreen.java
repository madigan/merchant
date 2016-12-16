package tech.otter.merchant.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.GameController;
import tech.otter.merchant.data.Station;

public class StationScreen extends AbstractScreen {

	public StationScreen(GameController parent) {
		super(parent);
	}
	
	@Override
	public void show() {
		super.show();
		final Station station = parent.getWorld().getPlayer().getCurrentStation();

		VisTable tblLayout = new VisTable();
		tblLayout.setFillParent(true);

		VisLabel lblStationName = new VisLabel(station.getName());
		// TODO: Make this lblTitle bigger


		VisLabel lblStationDescription = new VisLabel(station.getDescription());
		lblStationDescription.setWrap(true);
		lblStationDescription.setWidth(100f);
		VisScrollPane spStationDescription = new VisScrollPane(lblStationDescription);

		VisTable tblButtons = new VisTable();
		tblButtons.columnDefaults(0).pad(2f).width(300f);

		if(parent.getWorld().getPlayer().isAtHomeWorld()) {
			// TODO: Add Clan Screen
			VisTextButton btnClan = new VisTextButton("Visit Clan");
			tblButtons.add(btnClan);
			tblButtons.row();
		}

		VisTextButton btnTrader = new VisTextButton("Visit Trader", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(TradeScreen.class);
			}
		});
		// TODO: Add Bar Screen
		VisTextButton btnBar = new VisTextButton("Visit Bar");

		VisTextButton btnCargo = new VisTextButton("View Cargo", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(CargoScreen.class);
			}
		});

		VisTextButton btnLeave = new VisTextButton("Leave Station", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(DepartureScreen.class);
			}
		});

		VisTextButton btnMenu = new VisTextButton("Quit to Main Menu", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MainMenuScreen.class);
			}
		});

		VisImage background = new VisImage(parent.getManagedTexture(station.getBackground()));
		background.setSize(ui.getWidth(), ui.getWidth());
		background.setPosition(0, -background.getHeight());
		background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0);
		background.addAction(Actions.fadeIn(2.0f));
		background.addAction(Actions.moveBy(0, ui.getHeight(), 2.0f));

		tblButtons.add(btnTrader).row();
		tblButtons.add(btnBar).row();
		tblButtons.add(btnCargo).row();
		tblButtons.add(btnLeave).row();
		tblButtons.addSeparator().row();
		tblButtons.add(btnMenu);

		// === Add elements to the screen === //
		tblLayout.add(lblStationName).colspan(2).pad(10f);
		tblLayout.row();
		tblLayout.add(spStationDescription).width(200f).height(141f).top();
		tblLayout.add(tblButtons);

		ui.addActor(background);
		ui.addActor(tblLayout);
	}
}
