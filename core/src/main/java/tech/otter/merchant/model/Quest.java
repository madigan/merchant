package tech.otter.merchant.model;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.controller.GameEvent;
import tech.otter.merchant.controller.Subscriber;

public abstract class Quest implements Subscriber {
    private String name;
    protected Controller controller;
    protected Model world;

    public Quest(String name, Controller controller, Model world) {
        this.name = name;
        this.controller = controller;
        this.world = world;
        this.handle(new GameEvent("SELF_START"));
    }

    public String getName() {
        return name;
    }
}
