package tech.otter.merchant.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;

public class MainMenuScreen extends AbstractScreen {

	public MainMenuScreen(final MerchantGame parent) {
		super(parent);

		VisTextButton btnContinue = new VisTextButton("Continue Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Continue Game Clicked");
			}
		});

		VisTextButton btnNewGame = new VisTextButton("New Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("New Game Clicked");
				changeScreen(new IntroScreen(parent));
			}
		});

		VisTextButton btnLoadGame = new VisTextButton("Load Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Load Game Clicked");
			}
		});

		VisTextButton btnSaveGame = new VisTextButton("Save Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Save Game Clicked");
			}
		});

		VisTextButton btnOptions = new VisTextButton("Options", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Options Clicked");
			}
		});

		VisTextButton btnExit = new VisTextButton("Exit", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Exit Clicked");
				Gdx.app.exit();
			}
		});

		VisTable tblButtons = new VisTable();
		tblButtons.setFillParent(true);
		tblButtons.columnDefaults(0).pad(2f).width(300f);
		
		// Only give the option to continue if a game is active
		if( parent.isGameActive() ) {
			tblButtons.add(btnContinue);
			tblButtons.row();
		}
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

}
