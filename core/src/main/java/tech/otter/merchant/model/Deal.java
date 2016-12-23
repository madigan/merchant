package tech.otter.merchant.model;

public class Deal {
    private AbstractTrader player;
    private int playerQty;
    private Item playerType;

    private AbstractTrader merchant;
    private int merchantQty;
    private Item merchantType;

    private boolean accepted = false;

    public Deal() {}

    public Deal(Deal other) {
        this.player = other.getPlayer();
        this.playerType = other.getPlayerType();
        this.playerQty = other.getPlayerQty();

        this.merchant = other.getMerchant();
        this.merchantType = other.getMerchantType();
        this.merchantQty = other.getMerchantQty();
    }

    // == Query Methods == //
    public boolean isPlayerComplete() {
        return playerQty > 0 && playerType != null;
    }

    public boolean isMerchantComplete() {
        return merchantQty > 0 && merchantType != null;
    }

    public boolean isSilly() {
        return merchantQty == 0 || playerQty == 0 || playerType == merchantType;
    }


    // == Game Logic == //
    /**
     * Executes a deal, exchanging the items between the two merchants. If a merchant has no more of the good, it is
     * removed from their inventory altogether.
     */
    public void execute() {
        // TODO: Make safe
        executeHelper(player, playerType, playerQty);
        player.getInventory().getAndIncrement(merchantType, 0, merchantQty);

        executeHelper(merchant, merchantType, merchantQty);
        merchant.getInventory().getAndIncrement(playerType, 0, playerQty);

        accepted = true;
    }

    /**
     * Helper method to decrement a trader's inventory based on what they traded.
     * @param trader The trader object to update.
     * @param type The type of item to be decremented.
     * @param qty The quantity to decrement. If all are decremented, the type is removed.
     */
    private void executeHelper(AbstractTrader trader, Item type, int qty) {
        if(qty >= trader.getInventory().get(type, 0)) {
            trader.getInventory().remove(type, 0);
        } else {
            trader.getInventory().getAndIncrement(type, 0, -qty);
        }
    }

    @Override public String toString() {
        if(isMerchantComplete() && isPlayerComplete()) {
            return "Merchant will trade " +
                    merchantQty + " " + merchantType + " (" + merchantQty * merchantType.getBaseValue() + ") for " +
                    playerQty + " " + playerType + " (" + playerQty * playerType.getBaseValue() + ")";
        } else if(isMerchantComplete()) {
            return "Merchant will trade " + merchantQty + " " + merchantType + " for something.";
        } else if(isPlayerComplete()) {
            return "Player will trade " + playerQty + " " + playerType + " for something.";
        } else {
            return "Neither side has made an offer.";
        }
    }

    // == Getters / Setters == //
    public int getPlayerQty() {
        return playerQty;
    }

    public Deal setPlayerQty(int playerQty) {
        this.playerQty = playerQty;
        return this;
    }

    public Item getPlayerType() {
        return playerType;
    }

    public Deal setPlayerType(Item playerType) {
        this.playerType = playerType;
        return this;
    }

    public int getMerchantQty() {
        return merchantQty;
    }

    public Deal setMerchantQty(int merchantQty) {
        this.merchantQty = merchantQty;
        return this;
    }

    public Item getMerchantType() {
        return merchantType;
    }

    public Deal setMerchantType(Item merchantType) {
        this.merchantType = merchantType;
        return this;
    }

    public AbstractTrader getPlayer() {
        return player;
    }

    public Deal setPlayer(AbstractTrader player) {
        this.player = player;
        return this;
    }

    public AbstractTrader getMerchant() {
        return merchant;
    }

    public Deal setMerchant(AbstractTrader merchant) {
        this.merchant = merchant;
        return this;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
