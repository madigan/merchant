package tech.otter.merchant.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import com.kotcrab.vis.ui.widget.VisTextButton;
import tech.otter.merchant.controller.GameController;
import tech.otter.merchant.model.Dialog;
import tech.otter.merchant.model.GameWorld;

public abstract class GameScreen implements Screen {
	protected Logger logger = LoggerService.forClass(getClass());
	protected GameController controller;
    protected GameWorld world;
	protected Stage ui;
	
	public GameScreen(final GameController controller) {
		this.controller = controller;
        this.world = controller.getWorld();
		
		ui = new Stage(new ScreenViewport());
	}

	// === Lifecycle Methods === //
	@Override
	public void show() {
		// Set Debug Mode
		ui.setDebugAll(controller.isDebugOn());

        // Map the controller
        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(ui);

        // Add an input processor to toggle debug mode via F3.
        input.addProcessor(new DebugProcessor());
        Gdx.input.setInputProcessor(input);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	final public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		draw(delta);
		if(ui != null) {
			ui.act(delta);
			ui.draw();
		}
	}

	/**
	 * Override this sucker to implement any custom drawing
	 * @param delta The number of seconds that have passed since the last frame.
	 */
	public void draw(float delta) {}

	@Override
	public void resize(int width, int height) {
		ui.getViewport().update(width, height);
	}
	
	@Override
	public void dispose() {
		if(ui != null) ui.dispose();
		ui = null;
	}

	// === UI Manipulation Methods === //
    public void createDialog(Dialog dialogTemplate) {
        GameDialog dialog = new GameDialog(controller, dialogTemplate);
        dialog.setCenterOnAdd(true);

        ui.addActor(dialog);
    }

	// === Debug Methods === //
	private class DebugProcessor implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            if(keycode == Input.Keys.F3) {
                controller.setDebugOn(!controller.isDebugOn());
                ui.setDebugAll(controller.isDebugOn());
            }
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    // === Button Helpers === //
    protected VisTextButton makeNavButton(String label, Class<? extends GameScreen> screen) {
        if(screen == null) return makeButton(label, () -> System.out.println("Screen not implemented."));

        return makeButton(label, () -> controller.changeScreen(screen));
    }
    protected VisTextButton makeButton(String label, NavAction action) {
        return new VisTextButton(label, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug(label + " Clicked");
                action.navAction();
            }
        });
    }
    protected interface NavAction {
        void navAction();
    }
}
