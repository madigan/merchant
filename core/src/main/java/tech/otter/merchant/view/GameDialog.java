package tech.otter.merchant.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;
import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.model.dialog.Dialog;
import tech.otter.merchant.model.dialog.DialogAction;
import tech.otter.merchant.model.dialog.DialogOption;

/**
 * Renders a quest dialog window.
 */
public class GameDialog extends VisWindow {
    private static final Vector2 DEFAULT_SIZE = new Vector2(400f, 200f);
    private static final Vector2 IMAGE_SIZE = new Vector2(128f, 128f);
    private static final float PADDING = 5f;

    private Model model;
    private View view;
    private Controller controller;

    private DialogAction finalAction = null;

    public GameDialog(Model model, View view, Controller controller, Dialog template) {
        super(template.getTitle());

        this.model = model;
        this.view = view;
        this.controller = controller;

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
        if(finalAction != null) finalAction.act(model, view, controller);
        return super.remove();
    }
}
