package tech.otter.merchant.model.dialog;

import tech.otter.merchant.model.dialog.DialogAction;

/**
 * Used by the view to render a dialog button.
 */
public class DialogOption {
    private String label;
    private DialogAction action;

    public DialogOption(String label, DialogAction action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public DialogAction getAction() {
        return action;
    }
}
