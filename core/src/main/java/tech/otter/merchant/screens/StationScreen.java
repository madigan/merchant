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

    private VisImage background;
    private VisTextButton btnClan;
    private VisTextButton btnTrader;
    private VisTextButton btnBar;
    private VisTextButton btnCargo;
    private VisTextButton btnLeave;
    private VisTextButton btnMenu;
    private VisLabel lblStationDescription;
    private VisLabel lblStationName;

    public StationScreen(GameController parent) {
		super(parent);

        // Create the table layout
        VisTable tblLayout = new VisTable();
        tblLayout.setFillParent(true);

        // Create the UI elements
        lblStationName = new VisLabel();
        lblStationDescription = new VisLabel();
        lblStationDescription.setWrap(true);
        lblStationDescription.setWidth(100f);
        VisScrollPane spStationDescription = new VisScrollPane(lblStationDescription);

        // Create the buttons
        VisTable tblButtons = new VisTable();
        tblButtons.columnDefaults(0).pad(2f).width(300f);
        btnClan = makeNavButton("Visit Clan", ClanScreen.class);
        btnTrader = makeNavButton("Visit Trader", TradeScreen.class);
        btnBar = makeNavButton("Visit Bar", null);  // TODO: Add Bar
        btnCargo = makeNavButton("View Cargo", CargoScreen.class);
        btnLeave = makeNavButton("Leave Station", DepartureScreen.class);
        btnMenu = makeNavButton("Quit to Main Menu", MainMenuScreen.class);

        // Create a holder for the background image
        background = new VisImage();

        // === Add elements to the screen === //
        tblButtons.add(btnClan).row();
        tblButtons.add(btnTrader).row();
        tblButtons.add(btnBar).row();
        tblButtons.add(btnCargo).row();
        tblButtons.add(btnLeave).row();
        tblButtons.addSeparator().row();
        tblButtons.add(btnMenu);

        tblLayout.add(lblStationName).colspan(2).pad(10f).row();
        tblLayout.add(spStationDescription).width(200f).height(141f).top();
        tblLayout.add(tblButtons);

        ui.addActor(background);
        ui.addActor(tblLayout);
	}
	
	@Override
	public void show() {
		super.show();

        Station station = parent.getWorld().getPlayer().getCurrentStation();
        lblStationName.setText(station.getName());
        lblStationDescription.setText(station.getDescription());
        btnClan.setVisible(parent.getWorld().getPlayer().isAtHomeWorld());

        background.setDrawable(parent.getManagedTexture(station.getBackground()));
        background.setSize(ui.getWidth(), ui.getWidth());
        background.setPosition(0, -background.getHeight());
        background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0);
        background.addAction(Actions.fadeIn(2.0f));
        background.addAction(Actions.moveBy(0, ui.getHeight(), 2.0f));
	}
}
