package tech.otter.merchant.data;

import tech.otter.merchant.factories.GalaxyFactory;
import tech.otter.merchant.factories.ItemFactory;

/**
 * Created by john on 11/27/16.
 */
public class GameWorld {
    private Galaxy galaxy;
    private Player player;

    private boolean active = false;

    /**
     * Future: Initialize GameWorld based on a JSON file
     */
    public GameWorld() {

    }

    /**
     * Start a new game.
     */
    public void startNewGame() {
        this.galaxy = GalaxyFactory.get().make();
        this.player = new Player(ItemFactory.get().make(5), galaxy.getStations().get("Homeworld"), galaxy.getStations().get("Homeworld"));
        this.active = true;
    }

    public void dispose() {

    }

    // === Getters / Setters === //
    public Player getPlayer() {
        return player;
    }

    public Galaxy getGalaxy() { return galaxy; }

    public boolean isActive() {
        return active;
    }
}
