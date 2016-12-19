package tech.otter.merchant.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import tech.otter.merchant.factories.ItemFactory;

public class Merchant extends AbstractTrader {
    // TODO: Abstract this to file

	private String name = "Tim the Unnamed";
	private String portrait = null;
    private float profitMargin = 1.0f; // Merchants are OK with a 100% profit
    private String[] provides = new String[0];
    private float providesModifier = -0.1f;
    private String[] desires = new String[0];
    private float desiredModifier = 0.1f;

    /**
     * Processes the deal. Assumes that the player is already set.
     * @param deal
     * @return Returns the adjusted deal or null if the deal is accepted.
     */
	public Deal processDeal(Deal deal) {
        if(deal == null) return null; // TODO: Deal with other null values

        if(deal.isMerchantComplete() && deal.isPlayerComplete()) {
            // If the item types are the same, don't accept it.
            if(deal.isSilly()) {
                log("That's just silly.");
            }
            // If a complete deal is proposed, accept or adjust it.
            else if(isFair(deal)) {
                log("I accept your proposal.");
                deal.execute();
                return deal;
            } else {
                try {
                    return adjustDeal(deal);
                } catch (NoFairTradeException e) {
                    log("I don't see any way to make that a fair trade.");
                    return deal;
                }
            }
        } else if (!deal.isMerchantComplete() && !deal.isPlayerComplete()) {
            // If the player hasn't proposed *anything*, make an offer.
            try {
                return proposeDeal(deal);
            } catch (NothingWantedException e) {
                log("You don't appear to have anything I want.");
            } catch (NoViableTradesException e) {
                log("I don't see any viable trades.");
            }
        } else {
            // If the player has offered/asked for something, complete the offer.
            try {
                return completeDeal(deal);
            } catch (NoProfitableTradesException e) {
                log("I don't see any way I can offer a fair deal.");
            }
        }
        return deal;
    }

    // TODO: Make safe. Assumes complete deal.
    Deal adjustDeal(Deal deal) throws NoFairTradeException {
        // First we try to adjust the player's side
        Deal newDeal = new Deal(deal); // Try saying that five times fast
        newDeal.setPlayerQty(0);

        try {
            completeDeal(newDeal);
            deal.setPlayerQty(newDeal.getPlayerQty());
            log("How about this deal?");
            return deal;
        } catch (NoProfitableTradesException e) {
            // If that fails, we try to adjust the merchant's side.
            newDeal = new Deal(deal); // Reset the new deal
            newDeal.setMerchantQty(0);
            try {
                completeDeal(newDeal);
                deal.setMerchantQty(newDeal.getMerchantQty());
                log("That doesn't seem fair- How about this deal?");
                return deal;
            } catch (NoProfitableTradesException e1) {
                // If neither side can be adjusted, we're kinda toast.
                log("I don't see any way to make this a fair deal.");
                throw new NoFairTradeException();
            }
        }
    }

    /**
     * Helper method that completes a deal. Assumes the deal it gets has one side completed.
     * @param deal
     * @return
     */
    Deal completeDeal(Deal deal) throws NoProfitableTradesException {
        if(deal.isMerchantComplete() && deal.isPlayerComplete()) {
            return deal;
        } else if (!deal.isMerchantComplete()) {
            // Get a list of possible things to offer the player
            Array<Item> candidates = new Array<>();
            getInventory().keys().forEach(item -> candidates.add(item));
            candidates.shuffle();

            // See if a fair trade can be created using any of the candidates
            for(Item candidate : candidates) {
                int merchantQty = getMerchantQty(deal.getPlayerQty(), deal.getPlayerType(), candidate);

                if(merchantQty > getInventory().get(candidate, 0)) {
                    merchantQty = getInventory().get(candidate, 0);
                }
                deal.setMerchantQty(merchantQty);
                deal.setMerchantType(candidate);
                if(isFair(deal) && !deal.isSilly()) {
                    log("How about I give you " + deal.getMerchantQty() + " " + deal.getMerchantType() + "?");
                    return deal;
                } else {
                    deal.setMerchantQty(0);
                    deal.setMerchantType(null);
                }
            }
        } else {
            Array<Item> candidates = new Array<>();
            deal.getPlayer().getInventory().keys().forEach(item -> candidates.add(item));
            candidates.shuffle();

            // Complete the player side
            for(Item candidate : candidates) {
                int playerQty = getPlayerQty(deal.getMerchantQty(), candidate, deal.getMerchantType());

                if(playerQty > deal.getPlayer().getInventory().get(candidate, 0)) {
                    playerQty = deal.getPlayer().getInventory().get(candidate, 0);
                }
                deal.setPlayerQty(playerQty);
                deal.setPlayerType(candidate);
                if(isFair(deal) && !deal.isSilly()) {
                    log("How about you give me " + deal.getPlayerQty() + " " + deal.getPlayerType() + "?");
                    return deal;
                } else {
                    deal.setPlayerQty(0);
                    deal.setPlayerType(null);
                }
            }
        }
        // If we still can't make a fair deal, throw an exception.
        throw new NoProfitableTradesException();
    }

    // TODO: Safety-tize this.
    Deal proposeDeal(Deal deal) throws NothingWantedException, NoViableTradesException {
        // Get a list of the things the player has that the merchant would want
        ObjectIntMap<Item> playerInventory = deal.getPlayer().getInventory();
        Array<Item> candidates = new Array<>();
        playerInventory.forEach((ObjectIntMap.Entry<Item> entry) -> {
            if(provides(entry.key)) {
                candidates.add(entry.key);
            }
        });
        if(candidates.size == 0) throw new NothingWantedException();

        // Now loop through the candidates and see if any of them can be traded for
        candidates.shuffle();
        for(int i = 0; i < candidates.size; i++) {
            Item type = candidates.get(i);

            deal.setPlayerQty(MathUtils.random(1, playerInventory.get(type, 1)));
            deal.setPlayerType(type);

            try {
                return completeDeal(deal);
            } catch (NoProfitableTradesException e) {
                // Continue to the next candidate
            }
        }
        throw new NoViableTradesException();
    }

    int getPlayerQty(int merchantQty, Item playerItem, Item merchantItem) {
        return MathUtils.floor(merchantQty * valueOf(merchantItem) * (1+profitMargin) / valueOf(playerItem));
    }
    int getMerchantQty(int playerQty, Item playerItem, Item merchantItem) {
        int qty = MathUtils.ceil(playerQty * valueOf(playerItem) / valueOf(merchantItem) / (1+profitMargin));
        return qty;
    }

    /**
     * Calculates the value of a certain type.
     * @param type The type of items.
     * @return A numeric representation of the value.
     */
	float valueOf(Item type) {
        return valueOf(1, type);
    }
    float valueOf(int qty, Item type) {
        float modifier = 1.0f;

        // If the item is 'desired' by this merchant, apply the modifier
        if(desires(type)) modifier += this.desiredModifier;

        // If the item is 'produced' by this merchant, apply the modifier
        if(provides(type)) modifier += this.providesModifier;

        return qty * type.getBaseValue() * modifier;
    }
	
	boolean isFair(Deal deal) {
        float playerValue = valueOf(deal.getPlayerQty(), deal.getPlayerType());
        float merchantValue = valueOf(deal.getMerchantQty(), deal.getMerchantType());
        return playerValue > merchantValue
                && playerValue <= (1+profitMargin) * merchantValue;
    }

	public void restock() {
		this.getInventory().clear();
		this.getInventory().putAll(ItemFactory.get().make(5));
	}

	// == Helpers == //
    private boolean desires(Item type) {
        for(String tag : this.desires) {
            if(type.getTags().contains(tag, false)) {
                return true;
            }
        }
        return false;
    }

    private boolean provides(Item type) {
        for(String tag : this.provides) {
            if(type.getTags().contains(tag, false)) {
                return true;
            }
        }
        return false;
    }

    private void log(String message) {
        if(Gdx.app != null)
            Gdx.app.log("[" + name + "]", message);
        else
            System.out.println(message);
    }

    // == Exceptions == //
    class NothingWantedException extends Exception {}
    class NoViableTradesException extends Exception {}
    class NoProfitableTradesException extends Exception {}
    class NoFairTradeException extends Exception {}

	// == Getters / Setters == //
    public String getName() {
        return name;
    }

    public Merchant setName(String name) {
        this.name = name;
        return this;
    }

    public String getPortrait() {
        return portrait;
    }

    public Merchant setPortrait(String portrait) {
        this.portrait = portrait;
        return this;
    }

    public Merchant setProfitMargin(float profitMargin) {
        this.profitMargin = profitMargin;
        return this;
    }

    public float getProfitMargin() {
        return profitMargin;
    }

    public String[] getProvides() {
        return provides;
    }

    public Merchant setProvides(String[] provides) {
        this.provides = provides;
        return this;
    }

    public float getProvidesModifier() {
        return providesModifier;
    }

    public Merchant setProvidesModifier(float providesModifier) {
        this.providesModifier = providesModifier;
        return this;
    }

    public String[] getDesires() {
        return desires;
    }

    public Merchant setDesires(String[] desires) {
        this.desires = desires;
        return this;
    }

    public float getDesiredModifier() {
        return desiredModifier;
    }

    public Merchant setDesiredModifier(float desiredModifier) {
        this.desiredModifier = desiredModifier;
        return this;
    }
}
