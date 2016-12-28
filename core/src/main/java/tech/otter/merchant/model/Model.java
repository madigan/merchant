package tech.otter.merchant.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.factories.EncounterFactory;
import tech.otter.merchant.model.factories.GalaxyFactory;
import tech.otter.merchant.model.factories.QuestFactory;

public class Model {
    private Galaxy galaxy;
    private Player player;
    private Array<tech.otter.merchant.model.dialog.Dialog> encounters;

    private boolean active = false;

    /**
     * Future: Initialize Model based on a JSON file
     */
    public Model() {
        encounters = new Array<tech.otter.merchant.model.dialog.Dialog>();
    }

    /**
     * Start a new game.
     */
    public void startNewGame(Controller controller) {
        encounters.clear();
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

    public Galaxy getGalaxy() { return galaxy; }

    public boolean isActive() {
        return active;
    }

    public tech.otter.merchant.model.dialog.Dialog getNextEncounter() {
        if(encounters.size > 0)
            return encounters.pop();
        else {
            if(MathUtils.random(1, 4) == 4) {
                Gdx.app.debug(getClass().getSimpleName(), "Making a random encounter!");
                return EncounterFactory.get().makeRandom(this);
            } else {
                Gdx.app.debug(getClass().getSimpleName(), "No random encounter!");
                return null;
            }
        }
    }


}
