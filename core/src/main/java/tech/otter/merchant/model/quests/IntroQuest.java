package tech.otter.merchant.model.quests;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.controller.GameEvent;
import tech.otter.merchant.model.*;
import tech.otter.merchant.model.dialog.Dialog;
import tech.otter.merchant.model.dialog.DialogOption;
import tech.otter.merchant.model.factories.ItemFactory;
import tech.otter.merchant.view.ClanScreen;
import tech.otter.merchant.view.StationScreen;
import tech.otter.merchant.view.TradeScreen;
import tech.otter.merchant.view.View;

/**
 * This quest introduces the player to the basic haggling process.
 */
public class IntroQuest extends Quest {
    private NPC auntieEm = new NPC("Auntie Emm", "merchant3"); // TODO: Add portrait
    private State state = State.Visiting;
    private Deal deal;

    public IntroQuest(String name, Controller controller, Model world) {
        super(name, controller, world);
    }

    @Override
    public void handle(GameEvent event) {
        switch(event.type) {
            case "SELF_START":
                controller.subscribe(this, "SCREEN_CHANGE");
                controller.subscribe(this, "TRADE_COMPLETE");

                // Disable Merchant Button // TODO: Storify this "You don't have anything to trade!"
                world.getPlayer().getCurrentStation().getMerchant().setOpen(false);
                // Disable Travel Button
                world.getPlayer().canLeave(false);

                break;
            case "SCREEN_CHANGE":
                View screen = event.get("SCREEN");
                switch(state) {
                    case Visiting:
                        if(screen instanceof ClanScreen) {
                            // Add "You're almost a trader but you need to do that which totally isn't a tutorial" dialogs
                            Dialog d1 = new Dialog(auntieEm, "You're almost a trader! Before you fly off, could you run one last errand for your favorite auntie while we fuel up your ship?");
                            Dialog d2 = new Dialog(auntieEm, "Great! As you know, times have been tough. Last night some of our Cryo-Pods broke down. I hate to do it, but I need you to go see if you can procure some more.");
                            Dialog d3 = new Dialog(auntieEm, "Here, take this old trinket I collected back in my trading days... it should still be worth something.");
                            Dialog d4 = new Dialog(auntieEm, "Make sure you get a good bargain! That old beak-face will rob you blind if you let him!");

                            d1.addOption("<Continue>", (m, v, c) -> v.createDialog(d2));
                            d2.addOption("<Continue>", (m, v, c) -> v.createDialog(d3));
                            d3.addOption("<Continue>", (m, v, c) -> v.createDialog(d4));
                            d4.addOption("<Head back>", (m, v, c) -> c.changeScreen(StationScreen.class));

                            screen.createDialog(d1);

                            // Give the player some stuff
                            world.getPlayer().getInventory().putAll(ItemFactory.get().make("Hyooman Exotic Hair Products", 1));

                            // Enable the merchant screen
                            Merchant merchant = world.getPlayer().getCurrentStation().getMerchant();
                            merchant.setOpen(true);

                            // Stage the merchant's items
                            merchant.getInventory().clear();
                            merchant.getInventory().putAll(ItemFactory.get().make("Cryo-Pod", 12)); // TODO: Refactor this API

                            // Set the state
                            state = State.Trading;
                        }
                        break;
                    case Trading:
                        if(screen instanceof ClanScreen) {
                            // Add "Well get on with it!" dialog
                            screen.createDialog(
                                    new Dialog(
                                            auntieEm,
                                            "Well, what are you waiting for?",
                                            new DialogOption("<Head Back>", (m, v, c) -> c.changeScreen(StationScreen.class))));
                        } else if(screen instanceof TradeScreen) {
                            // TODO: Add merchant dialogue
                        }
                        break;
                    case TradeComplete:
                        if(screen instanceof ClanScreen) {
                            // Judge the trade
                            float profitMargin = (deal.getMerchantQty() * deal.getMerchantType().getBaseValue())
                                    / (deal.getPlayerQty() * deal.getPlayerType().getBaseValue());

                            Dialog d1;
                            if( profitMargin >= 2.0f  ) { // 100% Profit
                                // Great Scott! You're a natural!
                                d1 = new Dialog(auntieEm, "Great Huundzuuken! You're a natural!");
                            } else if( profitMargin >= 1.0f ) { // Some profit
                                // Great job! Looks like you got the better deal!
                                d1 = new Dialog(auntieEm, "Well done! I think you got the better of that deal.");
                            } else { // A loss
                                // Hmmm.... looks like he got the better of you. Try to do better.
                                d1 = new Dialog(auntieEm, "Eesh... looks like that old beak-face got the better of you.");
                            }
                            Dialog d2 = new Dialog(auntieEm, "Well, it looks like your ship is ready. I'm gonna miss you...");
                            Dialog d3 = new Dialog(auntieEm, "Oh! And take these Yaks! I'm sure you can get something for them.");

                            d1.addOption("<Continue>", (m,v,c) -> {
                                m.getPlayer().getInventory().clear();
                                v.createDialog(d2);
                            });
                            d2.addOption("<Continue>", (m,v,c) -> {
                                m.getPlayer().getInventory().putAll(ItemFactory.get().make("Cubic Yaks", 47));
                                v.createDialog(d3);
                            });
                            d3.addOption("<Head Back>", (m,v,c) -> c.changeScreen(StationScreen.class));

                            screen.createDialog(d1);

                            // Clean up subscriptions
                            controller.unsubscribe(this, "SCREEN_CHANGE");
                            controller.unsubscribe(this, "TRADE_COMPLETE");

                            // Restock the merchant
                            world.getPlayer().getCurrentStation().getMerchant().restock();

                            // Let the player leave
                            world.getPlayer().canLeave(true);

                            // Mark the quest as complete
                            world.getPlayer().getQuests().removeValue(this, true);

                            // Trigger Next Quest
                            //model.getPlayer().getQuests().add(QuestFactory.make("YAK_DELIVERY")); // TODO: Refactor to use class type
                        }
                        break;
                }
                break;
            case "TRADE_COMPLETE":
                switch(state) {
                    case Trading:
                        deal = event.get("DEAL");
                        state = State.TradeComplete;
                        break;
                }
                break;
            default:
                // TODO: Some sort of error handling
        }
    }

    private enum State {
        Visiting,
        Trading,
        TradeComplete
    }
}
