package tech.otter.merchant.screens;

import com.kotcrab.vis.ui.widget.VisWindow;
import tech.otter.merchant.GameController;

/**
 * Created by john on 12/20/16.
 */
public class ClanScreen extends AbstractScreen {
    public ClanScreen(GameController parent) {
        super(parent);
    }

    @Override
    public void show() {
        super.show();
        ui.addActor(new DialogWindow("My Test Title"));
    }

    private class DialogWindow extends VisWindow {
        public DialogWindow(String title) {
            super(title);
        }
    }
}
