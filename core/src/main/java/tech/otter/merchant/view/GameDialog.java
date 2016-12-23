package tech.otter.merchant.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;
import org.lwjgl.util.vector.Vector2f;
import tech.otter.merchant.controller.GameController;
import tech.otter.merchant.model.Dialog;
import tech.otter.merchant.model.DialogOption;

/**
 * Renders a quest dialog window.
 */
public class GameDialog extends VisWindow {
    private static final Vector2f DEFAULT_SIZE = new Vector2f(400f, 200f);
    private static final Vector2f IMAGE_SIZE = new Vector2f(128f, 128f);
    private static final float PADDING = 5f;

    private Runnable finalAction = null;

    public GameDialog(GameController controller, Dialog template) {
        super(template.getTitle());
        setModal(true);
        setSize(DEFAULT_SIZE.x, DEFAULT_SIZE.y);

        VisImage image = new VisImage(controller.getManagedTexture(template.getImage()));
        VisLabel text = new VisLabel(template.getText());
        text.setWrap(true);

        VisTable content = new VisTable();
        content.add(image).size(IMAGE_SIZE.x, IMAGE_SIZE.y).pad(PADDING);
        content.add(text).width(DEFAULT_SIZE.x - IMAGE_SIZE.x - PADDING*4).pad(PADDING);
        add(content).row();

        VisTable buttons = new VisTable();
        for(DialogOption option : template.getOptions()) {
            buttons.add(new VisTextButton(option.getLabel(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    finalAction = option.getAction();
                    fadeOut(); // TODO: Change the fade
                }
            }));
        }
        add(buttons);
        fadeIn();
    }

    @Override
    public boolean remove() {
        if(finalAction != null) finalAction.run();
        return super.remove();
    }
}
