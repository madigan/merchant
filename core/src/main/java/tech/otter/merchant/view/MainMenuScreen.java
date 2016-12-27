package tech.otter.merchant.view;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;

public class MainMenuScreen extends View {
    private VisTextButton btnContinue;
    private final VisTextButton btnLoadGame;
    private final VisTextButton btnSaveGame;
    private final VisTextButton btnOptions;
    private final VisTextButton btnExit;
    private final VisTextButton btnNewGame;

    public MainMenuScreen(Controller controller, Model model) {
		super(controller, model);

		btnContinue = makeNavButton("Continue Game", StationScreen.class);
        btnNewGame = makeNavButton("New Game", IntroScreen.class);
        btnLoadGame = makeNavButton("Load Game", null);
        btnSaveGame = makeNavButton("Save Game", null);
        btnOptions = makeNavButton("Options", null);
        btnExit = makeButton("Exit", () -> Gdx.app.exit());
	}

	@Override
    public void init() {
        // Create the layout
        VisTable tblButtons = new VisTable();
        tblButtons.setFillParent(true);
        tblButtons.columnDefaults(0).pad(2f).width(300f);

        // Only give the option to continue if a game is active
        if(model.isActive()) tblButtons.add(btnContinue).row();
        tblButtons.add(btnNewGame).row();
        if(Gdx.app.getType().equals(ApplicationType.Desktop)) {
            tblButtons.add(btnLoadGame).row();
            tblButtons.add(btnSaveGame).row();
        }
        tblButtons.add(btnOptions);

        // Web and mobile devices don't need no exit buttons!
        if(Gdx.app.getType().equals(ApplicationType.Desktop)) {
            tblButtons.row();
            tblButtons.add(btnExit);
        }

        ui.addActor(tblButtons);
    }
}
