package tech.otter.merchant.view;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.controller.GameController;

public class MainMenuScreen extends GameScreen {
    private VisTextButton btnContinue;
    private final VisTextButton btnLoadGame;
    private final VisTextButton btnSaveGame;
    private final VisTextButton btnOptions;
    private final VisTextButton btnExit;
    private final VisTextButton btnNewGame;

    public MainMenuScreen(final GameController parent) {
		super(parent);

		btnContinue = makeNavButton("Continue Game", StationScreen.class);
        btnNewGame = makeNavButton("New Game", IntroScreen.class);
        btnLoadGame = makeNavButton("Load Game", null);
        btnSaveGame = makeNavButton("Save Game", null);
        btnOptions = makeNavButton("Options", null);
        btnExit = makeButton("Exit", () -> Gdx.app.exit());

        // Create the layout
        VisTable tblButtons = new VisTable();
        tblButtons.setFillParent(true);
        tblButtons.columnDefaults(0).pad(2f).width(300f);

        // Only give the option to continue if a game is active
        tblButtons.add(btnContinue);
        tblButtons.row();
        tblButtons.add(btnNewGame);
        tblButtons.row();
        tblButtons.add(btnLoadGame);
        tblButtons.row();
        tblButtons.add(btnSaveGame);
        tblButtons.row();
        tblButtons.add(btnOptions);

        // Web and mobile devices don't need no exit buttons!
        if(Gdx.app.getType().equals(ApplicationType.Desktop)) {
            tblButtons.row();
            tblButtons.add(btnExit);
        }

        ui.addActor(tblButtons);
	}

	@Override
    public void show() {
        super.show();

        // Only show the continue button if there is a game to continue
        btnContinue.setVisible(controller.getWorld().isActive());

        // Only show save/load on platforms that support it
        btnLoadGame.setVisible(!Gdx.app.getType().equals(ApplicationType.WebGL));
        btnSaveGame.setVisible(!Gdx.app.getType().equals(ApplicationType.WebGL));
        btnExit.setVisible(!Gdx.app.getType().equals(ApplicationType.WebGL) && !Gdx.app.getType().equals(ApplicationType.Android));
    }
}
