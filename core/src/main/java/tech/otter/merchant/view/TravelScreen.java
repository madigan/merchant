package tech.otter.merchant.view;

import com.badlogic.gdx.Gdx;
import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.dialog.Dialog;
import tech.otter.merchant.model.Model;

/**
 * This screen is used when the player travels between locations. It can spawn random events.
 */
public class TravelScreen extends View {
    public TravelScreen(Controller controller, Model model) {
        super(controller, model);
    }

    @Override
    public void init() {
        Dialog encounter = model.getNextEncounter();

        if(encounter != null) {
            Gdx.app.debug(getClass().getSimpleName(), "We have an encounter!");
            this.createDialog(encounter);
        } else {
            controller.changeScreen(StationScreen.class);
            Gdx.app.debug(getClass().getSimpleName(), "We have an encounter!");
        }
    }
}
