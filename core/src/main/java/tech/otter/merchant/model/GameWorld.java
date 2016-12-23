package tech.otter.merchant.model;

import com.badlogic.gdx.utils.ObjectIntMap;
import tech.otter.merchant.controller.GameController;
import tech.otter.merchant.model.factories.GalaxyFactory;
import tech.otter.merchant.model.factories.ItemFactory;
import tech.otter.merchant.model.factories.QuestFactory;

/**
 * Created by john on 11/27/16.
 */
public class GameWorld {
    private tech.otter.merchant.model.Galaxy galaxy;
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
    public void startNewGame(GameController controller) { // TODO: Clean up magic screens
        this.galaxy = GalaxyFactory.get().make();
        this.player = new Player(new ObjectIntMap<>(), galaxy.getStations().get("Homeworld"), galaxy.getStations().get("Homeworld"));
        this.player.getQuests().add(new QuestFactory().make("INTRO_QUEST", controller, this));
        this.active = true;
    }

    public void dispose() {

    }

    // === Getters / Setters === //
    public Player getPlayer() {
        return player;
    }

    public tech.otter.merchant.model.Galaxy getGalaxy() { return galaxy; }

    public boolean isActive() {
        return active;
    }
}
