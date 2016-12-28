package tech.otter.merchant.model.dialog;


import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.view.View;

public interface DialogAction {
    void act(Model model, View view, Controller controller);
}