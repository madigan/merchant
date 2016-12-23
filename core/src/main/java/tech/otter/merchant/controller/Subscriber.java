package tech.otter.merchant.controller;

public interface Subscriber {
    void handle(GameEvent event);
}
