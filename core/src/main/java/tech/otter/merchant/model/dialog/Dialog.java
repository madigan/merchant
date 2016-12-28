package tech.otter.merchant.model.dialog;

import com.badlogic.gdx.utils.Array;
import tech.otter.merchant.model.NPC;

/**
 * A Dialogue is a series of windows that prompt the user to do something. It's most often used by the quest system,
 * although in the future it may prove useful for random events as well. The weird thing about this class is that it's
 * kind of a blend of view and model.
 *
 * This class contains all the information needed to render the dialog.
 */
public class Dialog {
    private String title;
    private String image;
    private String text;
    private Array<DialogOption> options;

    public Dialog(NPC npc, String text) {
        this(npc, text, new DialogOption[0]);
    }
    public Dialog(NPC npc, String text, DialogOption... options) {
        this(npc.getName(), npc.getPortrait(), text, options);
    }
    public Dialog(String title, String image, String text) {
        this(title, text, image, new DialogOption[0]);
    }

    public Dialog(String title, String image, String text, DialogOption... options) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.options = Array.with(options);
    }

    public String getTitle() {
        return title;
    }

    public Dialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Dialog setImage(String image) {
        this.image = image;
        return this;
    }

    public String getText() {
        return text;
    }

    public Dialog setText(String text) {
        this.text = text;
        return this;
    }

    public Array<DialogOption> getOptions() {
        return options;
    }

    public Dialog addOption(DialogOption option) {
        options.add(option);
        return this;
    }
    public Dialog addOption(String label, DialogAction action) {
        return addOption(new DialogOption(label, action));
    }

    public Dialog setOptions(Array<DialogOption> options) {
        this.options = options;
        return this;
    }
}
