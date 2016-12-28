package tech.otter.merchant.model.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectIntMap;
import tech.otter.merchant.model.Item;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.model.NPC;
import tech.otter.merchant.model.dialog.Dialog;
import tech.otter.merchant.view.StationScreen;

/**
 * Generates random encounters.
 * Some day this will be file-driven.
 */
public class EncounterFactory {
    private static final EncounterFactory instance = new EncounterFactory();

    private EncounterFactory() {

    }

    public static EncounterFactory get() { return instance; }

    public Dialog makeRandom(Model model) {
        NPC maliq = new NPC("Maliq", "merchant5"); // TODO: Make the merchant a type of NPC, or NPC a type of merchant.
        Dialog d1 = new Dialog(maliq, "Hey there, I'm Maliq!");
        Item good = model.getPlayer().getInventory().keys().toArray().random();
        Dialog d2 = new Dialog(maliq, "I couldn't help but notice those fine " + good.getName() + " that you have onboard...");
        Dialog d2b = new Dialog(maliq, "But of course! I figured a wise merchant such as yourself would love to know about new opportunities!");
        Item other = ItemFactory.get().make(1).keys().next();
        int playerQty = MathUtils.random(1, model.getPlayer().getInventory().get(good, 1));
        int maliqQty = MathUtils.random(1, 10);
        Dialog d3 = new Dialog(maliq, "I'll make you this limited time offer- " + maliqQty + " " + other.getName() + " for a paltry " + playerQty + " " + good.getName() + "?");
        Dialog d3a = new Dialog(maliq, "A pleasure doing business with you! :3");
        Dialog d3b = new Dialog(maliq, "You clearly don't know a good deal when you see one. :/");

        d1.addOption("Hi!", (m,v,c) -> v.createDialog(d2));
        d2.addOption("You scanned my hold??!", (m,v,c) -> v.createDialog(d2b));
        d2b.addOption("I guess that's OK then...", (m,v,c) -> v.createDialog(d3));
        d2.addOption("I love opportunities!", (m,v,c) -> v.createDialog(d3));
        d3.addOption("I accept!", (m,v,c) -> {
            ObjectIntMap<Item> playerInventory = m.getPlayer().getInventory();
            playerInventory.put(other, maliqQty); // TODO: Refactor this to make it cleaner.
            playerInventory.getAndIncrement(good, playerQty, playerQty);
            if(playerInventory.get(good, 0) <= 0) {
                playerInventory.remove(good, 0);
            }
        });
        d3.addOption("Not today.", (m,v,c) -> v.createDialog(d3b));
        d3a.addOption("<Continue>", (m,v,c) -> c.changeScreen(StationScreen.class));
        d3b.addOption("<Continue>", (m,v,c) -> c.changeScreen(StationScreen.class));

        return d1;
    }
}
