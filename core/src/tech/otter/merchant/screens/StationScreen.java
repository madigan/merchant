package tech.otter.merchant.screens;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.Station;

public class StationScreen extends AbstractScreen {

	public StationScreen(MerchantGame parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		Station station = parent.getCurrentStation();

		VisTable tblLayout = new VisTable();
		tblLayout.setFillParent(true);
		if(station.getBackground() != null) tblLayout.setBackground(station.getBackground());

		VisLabel lblStationName = new VisLabel(station.getName());
		// TODO: Make this label bigger


		VisLabel lblStationDescription = new VisLabel(station.getDescription());
		lblStationDescription.setWrap(true);
		lblStationDescription.setWidth(100f);
		VisScrollPane spStationDescription = new VisScrollPane(lblStationDescription);

		VisTable tblButtons = new VisTable();
		tblButtons.columnDefaults(0).pad(2f).width(300f);

		if(parent.isHomeWorld(station)) {
			// TODO: Add Clan Screen
			VisTextButton btnClan = new VisTextButton("Visit Clan");
			tblButtons.add(btnClan);
			tblButtons.row();
		}

		// TODO: Add Trader Screen
		VisTextButton btnTrader = new VisTextButton("Visit Trader");
		// TODO: Add Bar Screen
		VisTextButton btnBar = new VisTextButton("Visit Bar");
		// TODO: Add Cargo Screen
		VisTextButton btnCargo = new VisTextButton("View Cargo");
		// TODO: Add Travel Screen
		VisTextButton btnLeave = new VisTextButton("Leave Station");

		tblButtons.add(btnTrader);
		tblButtons.row();
		tblButtons.add(btnBar);
		tblButtons.row();
		tblButtons.add(btnCargo);
		tblButtons.row();
		tblButtons.add(btnLeave);

		// === Add elements to the screen === //
		tblLayout.add(lblStationName).colspan(2).pad(10f);
		tblLayout.row();
		tblLayout.add(spStationDescription).width(200f).height(141f).top();
		tblLayout.add(tblButtons);
		ui.addActor(tblLayout);
	}
}
