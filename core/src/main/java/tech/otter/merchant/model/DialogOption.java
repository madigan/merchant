package tech.otter.merchant.model;

/**
 * Used by the view to render a dialog button.
 */
public class DialogOption {
    private String label;
    private Runnable action;

    public DialogOption(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public Runnable getAction() {
        return action;
    }
}
