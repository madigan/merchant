package tech.otter.merchant.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectIntMap;
import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.factories.GalaxyFactory;
import tech.otter.merchant.model.factories.QuestFactory;

public class Model {
    private tech.otter.merchant.model.Galaxy galaxy;
    private Player player;

    private boolean active = false;

    /**
     * Future: Initialize Model based on a JSON file
     */
    public Model() {

    }

    /**
     * Start a new game.
     */
    public void startNewGame(Controller controller) { // TODO: Clean up magic screens
        Gdx.app.debug("WEB", "I got this far :)");
        this.galaxy = GalaxyFactory.get().make();
        Gdx.app.debug("WEB", "But not this far :(");
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
