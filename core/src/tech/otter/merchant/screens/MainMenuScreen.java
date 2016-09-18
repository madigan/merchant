package tech.otter.merchant.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;

public class MainMenuScreen extends AbstractScreen {
	private VisTextButton btnContinue;
	private VisTextButton btnNewGame;
	private VisTextButton btnLoadGame;
	private VisTextButton btnSaveGame;
	private VisTextButton btnOptions;
	private VisTextButton btnExit;
	private VisTable tblButtons;
	
	public MainMenuScreen(final MerchantGame parent) {
		super(parent);

		btnContinue = new VisTextButton("Continue Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Continue Game Clicked");
			}
		});
		
		btnNewGame = new VisTextButton("New Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("New Game Clicked");
				changeScreen(new IntroScreen(parent));
			}
		});
		
		btnLoadGame  = new VisTextButton("Load Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Load Game Clicked");
			}
		});
		
		btnSaveGame = new VisTextButton("Save Game", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Save Game Clicked");
			}
		});
		
		btnOptions = new VisTextButton("Options", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Options Clicked");
			}
		});
		
		btnExit = new VisTextButton("Exit", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				logger.debug("Exit Clicked");
				Gdx.app.exit();
			}
		});
		
		tblButtons = new VisTable();
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
