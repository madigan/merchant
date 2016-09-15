package tech.otter.merchant.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import tech.otter.merchant.MerchantGame;

public abstract class AbstractScreen implements Screen {
	protected Logger logger = LoggerService.forClass(getClass());
	protected MerchantGame parent;
	protected Stage ui;
	
	public AbstractScreen(MerchantGame parent) {
		this.parent = parent;
		
		ui = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(ui);
	}

	@Override
	public void show() { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ui.act(delta);
		ui.draw();
	}

	@Override
	public void resize(int width, int height) {
		ui.getViewport().update(width, height);
	}
	
	@Override
	public void dispose() {
		if(ui != null) ui.dispose();
	}
	
	/**
	 * Convenience method to change screens.
	 * @param next The next screen.
	 */
	public void changeScreen(Screen next) {
		logger.debug("Changing screen to '{0}'", next.getClass().getSimpleName());
		this.parent.setScreen(next);
	}
}
