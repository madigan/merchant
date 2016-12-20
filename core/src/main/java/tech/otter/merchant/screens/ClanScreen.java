package tech.otter.merchant.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import tech.otter.merchant.GameController;

public class ClanScreen extends AbstractScreen {
    public ClanScreen(GameController parent) {
        super(parent);
    }

    @Override
    public void show() {
        super.show();
        DialogWindow window = new DialogWindow(parent, "Auntie Em", "Hello world! This is a very exciting and lengthy message to see how the text wraps.", parent.getManagedTexture("merchant3"));
        window.setPosition(200f, 150f);
        window.setCenterOnAdd(false);
        window.setSize(400f, 300f);
        ui.addActor(window);

        ui.getCamera().viewportHeight = 800f;
        ui.getCamera().viewportWidth = 600f;
        ui.getCamera().update();
    }

    private class DialogWindow extends VisWindow {
        private VisImage portrait;
        private VisLabel dialog;
        private VisTextButton back;

        public DialogWindow(GameController parent, String title, String paragraph, Drawable image) {
            super(title);
            this.fadeIn(0.5f);
            this.setMovable(false);
            this.setKeepWithinParent(true);
            this.columnDefaults(0).width(90f).height(90f).pad(5f);
            this.columnDefaults(1).width(290f).height(90f).pad(5f);

            portrait = new VisImage(image);
            portrait.setSize(100f, 100f);
            dialog = new VisLabel(paragraph);
            dialog.setWrap(true);

            Table buttons = new Table();
            back = new VisTextButton("Back");
            back.getColor().a = 0;
            back.addAction(Actions.fadeIn(5f));

            buttons.add(back);

            this.add(portrait);
            this.add(dialog);
            this.row();
            this.add(buttons);
        }
    }
}
