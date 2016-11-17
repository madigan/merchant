package tech.otter.merchant.data;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MerchantTest {
    private Merchant merchant;
    private Player player;
    private Item item1;
    private Item item2;
    private Deal deal;

    @Before public void setUp() {
        merchant = new Merchant();
        player = new Player();
        item1 = new Item().setName("Atomic Tofu");
        item2 = new Item().setName("Off-brand Muzzlefluffers");
        deal = new Deal().setMerchant(merchant).setPlayer(player);
    }
    @After public void tearDown() {
        merchant = null;
        player = null;
        item1 = null;
        item2 = null;
        deal = null;
    }

    /**
     * Basic test to make sure value is calculated as a form of quantity * base value.
     */
    @Test public void testValueOf() {
        Item type = new Item().setBaseValue(20f);
        Assert.assertEquals("The values should have been equal.", 80.0f, merchant.valueOf(4, type), 0);
    }

    /**
     * Test to make sure that 'desires' correctly influence the value.
     */
    @Test public void testValueOf_desires() {
        Item type = new Item().setBaseValue(20f);
        merchant.setDesires(new String[] {"category-test"});
        Assert.assertEquals("The value should be the same if the item doesn't have a tag.", 60.0f, merchant.valueOf(3, type), 0);

        type.setTags(Array.with("category-test"));
        Assert.assertEquals("The default should be a 10% increase.", 66.0f, merchant.valueOf(3, type), 0);

        merchant.setDesiredModifier(1.0f); // 100% increase
        Assert.assertEquals("The values should have been equal.", 120.0f, merchant.valueOf(3, type), 0);

        type.setTags(Array.with("category-test", "category-test"));
        Assert.assertEquals("Duplicate tags should not change the value.", 120.0f, merchant.valueOf(3, type), 0);

        merchant.setDesires(new String[]{"test1", "test2"});
        type.setTags(Array.with("test1", "test2"));
        Assert.assertEquals("Multiple desired tags should not be cumulative.", 120.0f, merchant.valueOf(3, type), 0);

    }

    /**
     * Test to make sure that 'provides' correctly influence the value.
     */
    @Test public void testValueOf_provides() {
        Item type = new Item().setBaseValue(20f);
        merchant.setProvides(new String[] {"category-test"});
        Assert.assertEquals("The value should be the same if the item doesn't have a tag.", 60.0f, merchant.valueOf(3, type), 0);

        type.setTags(Array.with("category-test"));
        Assert.assertEquals("The default should be a 10% decrease.", 54.0f, merchant.valueOf(3, type), 0);

        merchant.setProvidesModifier(-0.25f); // 25% discount
        Assert.assertEquals("The values should have been equal.", 45.0f, merchant.valueOf(3, type), 0);

        type.setTags(Array.with("category-test", "category-test"));
        Assert.assertEquals("Duplicate tags should not change the value.", 45.0f, merchant.valueOf(3, type), 0);

        merchant.setProvides(new String[]{"test1", "test2"});
        type.setTags(Array.with("test1", "test2"));
        Assert.assertEquals("Multiple desired tags should not be cumulative.", 45.0f, merchant.valueOf(3, type), 0);
    }

    /**
     * Test the merchant's sense of fairness.
     * A deal is fair if it is profitable for the merchant, but less than the profit margin.
     */
    @Test public void testIsFair() {
        item1.setBaseValue(37f);
        item2.setBaseValue(3f);

        deal.setPlayerQty(5).setPlayerType(item1);
        deal.setMerchantQty(50).setMerchantType(item2);
        Assert.assertTrue("Lower bound should still be fair: " + deal, merchant.isFair(deal));

        deal.setPlayerQty(5).setPlayerType(item1);
        deal.setMerchantQty(61).setMerchantType(item2);
        Assert.assertTrue("Upper bound should be fair: " + deal, merchant.isFair(deal));

        deal.setPlayerQty(10).setPlayerType(item1);
        deal.setMerchantQty(123).setMerchantType(item2);
        Assert.assertTrue("Upper bound should be fair: " + deal, merchant.isFair(deal));

        deal.setPlayerQty(6).setPlayerType(item1);
        deal.setMerchantQty(37).setMerchantType(item2);
        Assert.assertTrue("Exactly on the upper bound should be fair: " + deal, merchant.isFair(deal));

        deal.setPlayerQty(3).setPlayerType(item1);
        deal.setMerchantQty(37).setMerchantType(item2);
        Assert.assertFalse("Equal value (no profit) should not be fair: " + deal, merchant.isFair(deal));

        deal.setPlayerQty(2).setPlayerType(item1);
        deal.setMerchantQty(60).setMerchantType(item2);
        Assert.assertFalse("The player shouldn't rip off the merchant: " + deal, merchant.isFair(deal));

        deal.setPlayerQty(10).setPlayerType(item1);
        deal.setMerchantQty(60).setMerchantType(item2);
        Assert.assertFalse("The merchant shouldn't rip off the player: " + deal, merchant.isFair(deal));
    }

    /**
     * Test the merchant's ability to complete a deal.
     */
    @Test public void testCompleteDeal_merchantSide() {
        item1.setBaseValue(40f);
        item2.setBaseValue(4f);

        player.getInventory().put(item1, 100);
        merchant.getInventory().put(item2, 300);

        deal.setPlayerQty(13).setPlayerType(item1);

        try {
            deal = merchant.completeDeal(deal);
        } catch (Merchant.NoProfitableTradesException e) {
            Assert.fail("No profitable trade: " + e.getMessage());
        }

        Assert.assertTrue("The quantity offered must be greater than 0.", deal.getMerchantQty() > 0);
        Assert.assertNotNull(deal.getMerchantType());
        Assert.assertTrue("The deal must be fair: " + deal, merchant.isFair(deal));
        Assert.assertTrue("The deal must not be silly: " + deal, !deal.isSilly());
    }

    @Test public void testCompleteDeal_merchant_notSilly() {
        item1.setName(item2.getName());
        item1.setBaseValue(40f);
        item2.setBaseValue(4f);

        player.getInventory().put(item1, 100);
        merchant.getInventory().put(item1, 300);

        deal.setPlayerQty(13).setPlayerType(item1);

        try {
            deal = merchant.completeDeal(deal);
            Assert.fail("The test should have failed; the deal would've been silly.");
        } catch (Merchant.NoProfitableTradesException e) {
            Assert.assertTrue("The deal must not be set: " + deal, !deal.isMerchantComplete());
        }
    }

    /**
     * Test the ability to adjust the player's initial offer to make a fair deal possible.
     */
    @Test public void testCompleteDeal_merchant_adjust() {
        for(int i = 0; i < 100; i++) {
            player.getInventory().clear();
            merchant.getInventory().clear();

            item1.setBaseValue(MathUtils.random(400.00f));
            item2.setBaseValue(MathUtils.random(400.00f));
            player.getInventory().put(item1, MathUtils.floor(5000 / item1.getBaseValue()));
            merchant.getInventory().put(item2, MathUtils.floor(5000 / item2.getBaseValue()));

            deal = new Deal();
            deal.setPlayerType(item1).setPlayerQty(MathUtils.random(1, player.getInventory().get(item1, 1) / 2));
            try {
                deal = merchant.completeDeal(deal);
            } catch (Merchant.NoProfitableTradesException e) {
                Assert.fail(e.getMessage());
            }

            Assert.assertTrue("The quantity offered must be greater than 0.", deal.getMerchantQty() > 0);
            Assert.assertNotNull(deal.getMerchantType());
            Assert.assertTrue("The deal must be fair: " + deal, merchant.isFair(deal));
        }
    }

    /**
     * Test the merchant's ability to complete a deal.
     */
    @Test public void testCompleteDeal_playerSide() {
        item1.setBaseValue(40f);
        item2.setBaseValue(4f);

        player.getInventory().put(item2, 300);
        merchant.getInventory().put(item1, 100);

        deal.setMerchantQty(13).setMerchantType(item1);

        try {
            deal = merchant.completeDeal(deal);
        } catch (Merchant.NoProfitableTradesException e) {
            Assert.fail("No profitable trade: " + e.getMessage());
        }

        Assert.assertTrue("The quantity offered must be greater than 0.", deal.getPlayerQty() > 0);
        Assert.assertNotNull(deal.getPlayerType());
        Assert.assertTrue("The deal must be fair: " + deal, merchant.isFair(deal));
        Assert.assertTrue("The deal must not be silly: " + deal, !deal.isSilly());
    }

    /**
     * Test the merchant's ability to complete a deal.
     */
    @Test public void testCompleteDeal_unfair() {
        Assert.fail();
    }

    /**
     * Test the merchant's logic to see if it can correct a bad deal.
     */
    @Test public void testAdjustDeal() {
        Item a = new Item().setBaseValue(15.0f);
        Item b = new Item().setBaseValue(5.0f);

        Deal deal = new Deal();
        deal.setMerchant(merchant).setMerchantQty(20).setMerchantType(a);
        deal.setPlayer(player).setPlayerQty(25).setPlayerType(b);

        try {
            deal = merchant.adjustDeal(deal);
        } catch (Merchant.NoFairTradeException e) {
            Assert.fail("A fair trade was totally possible.");
        }
    }
}
