package tech.otter.merchant.view;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.model.Station;

public class StationScreen extends View {
    private VisTextButton btnClan;
    private VisTextButton btnTrader;
    private VisTextButton btnBar;
    private VisTextButton btnCargo;
    private VisTextButton btnLeave;
    private VisTextButton btnMenu;
    private VisLabel lblStationDescription;
    private VisLabel lblStationName;

    public StationScreen(Controller controller, Model model) {
        super(controller, model);
        // Create the UI elements
        lblStationName = new VisLabel();
        lblStationDescription = new VisLabel();
        lblStationDescription.setWrap(true);
        lblStationDescription.setWidth(100f);

        // Create the buttons
        btnClan = makeNavButton("Visit Clan", ClanScreen.class);
        btnTrader = makeNavButton("Visit Trader", TradeScreen.class);
        btnBar = makeNavButton("Visit Bar", null);  // TODO: Add Bar
        btnCargo = makeNavButton("View Cargo", CargoScreen.class);
        btnLeave = makeNavButton("Leave Station", DepartureScreen.class);
        btnMenu = makeNavButton("Quit to Main Menu", MainMenuScreen.class);
	}
	
	@Override
	public void init() {
        ui.clear();

        // Configure the dynamic UI elements
        Station station = controller.getModel().getPlayer().getCurrentStation();
        lblStationName.setText(station.getName());
        lblStationDescription.setText(station.getDescription());

        VisImage background = new VisImage();
        background.setDrawable(controller.getManagedTexture(station.getBackground()));
        background.setSize(ui.getWidth(), ui.getWidth());
        background.setPosition(0, -background.getHeight());
        background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0);
        background.addAction(Actions.fadeIn(2.0f));
        background.addAction(Actions.moveBy(0, ui.getHeight(), 2.0f));

        // Create the table layout
        VisTable tblLayout = new VisTable();
        tblLayout.setFillParent(true);

        VisTable tblMenu = new VisTable();
        tblMenu.setBackground("window");

        // === Add buttons to the screen === //
        VisTable tblButtons = new VisTable();
        tblButtons.columnDefaults(0).pad(2f).width(300f);

        if(model.getPlayer().isAtHomeWorld() && model.getPlayer().getQuests().size > 0) {
            tblButtons.add(btnClan).row();
        }
        if(model.getPlayer().getCurrentStation().getMerchant().isOpen()) {
            tblButtons.add(btnTrader).row();
        }
        //tblButtons.add(btnBar).row();
        tblButtons.add(btnCargo).row();
        if(controller.getModel().getPlayer().canLeave()) {
            tblButtons.add(btnLeave).row();
        }
        tblButtons.addSeparator().row();
        tblButtons.add(btnMenu);

        tblMenu.add(lblStationName).colspan(2).pad(10f).row();
        tblMenu.add(new VisScrollPane(lblStationDescription)).width(200f).height(141f).top();
        tblMenu.add(tblButtons);

        tblLayout.add(tblMenu);

        ui.addActor(background);
        ui.addActor(tblLayout);
	}
}
