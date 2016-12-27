package tech.otter.merchant.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import tech.otter.merchant.controller.Controller;

public class DebugProcessor implements InputProcessor {
        private Stage stage;
        private Controller controller;

        public DebugProcessor(Stage stage, Controller controller) {
            this.stage = stage;
            this.controller = controller;
        }

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            if(keycode == Input.Keys.F3) {
                controller.setDebugOn(!controller.isDebugOn());
                stage.setDebugAll(controller.isDebugOn());
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
