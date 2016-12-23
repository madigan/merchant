package tech.otter.merchant.model;

import tech.otter.merchant.controller.GameController;
import tech.otter.merchant.controller.GameEvent;
import tech.otter.merchant.controller.Subscriber;

public abstract class Quest implements Subscriber {
    private String name;
    protected GameController controller;
    protected GameWorld world;

    public Quest(String name, GameController controller, GameWorld world) {
        this.name = name;
        this.controller = controller;
        this.world = world;
        this.handle(new GameEvent("SELF_START"));
    }

    public String getName() {
        return name;
    }
}
