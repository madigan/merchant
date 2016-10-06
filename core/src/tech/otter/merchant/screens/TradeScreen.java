package tech.otter.merchant.screens;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import java.util.Comparator;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.Item;
import tech.otter.merchant.data.ItemType;
import tech.otter.merchant.data.Merchant;

public class TradeScreen extends AbstractScreen {
	private VisTextButton btnOffer;

	private VisImage imgPlayerOffer;
	private VisSelectBox<Item> sbPlayerItems;
	private Spinner spnPlayerQty;

	private Merchant merchant;
	private VisImage imgMerchantOffer;
	private VisSelectBox<Item> sbMerchantItems;
	private Spinner spnMerchantQty;

	private VisTable tblMessages;
	private VisScrollPane spMessages;

	final Item EMPTY = new Item( new ItemType("", "", null, 0, 0, 0, null));

	public TradeScreen(MerchantGame parent, Merchant merchant, final AbstractScreen next) {
		super(parent);

		this.merchant = merchant;

		// TODO: Use asset loader
		// TODO: Redistribute functionality between constructor and show()
		VisImage imgPortrait = new VisImage(parent.getManagedTexture("images/merchants.atlas", merchant.getPortrait()));

		imgPortrait.setSize(300f, 300f);

		// Assemble the player's trade options
		imgPlayerOffer = new VisImage();
		imgPlayerOffer.setSize(200f, 200f);
		sbPlayerItems = new VisSelectBox<>();
		Array<Item> selections = new Array<>(parent.getPlayer().getInventory());
		selections.add(EMPTY);
		selections.sort(new ItemComparator());
		sbPlayerItems.setItems(selections);
		spnPlayerQty = makeSpinner(sbPlayerItems);
		sbPlayerItems.addListener( new ItemChangeListener(imgPlayerOffer, spnPlayerQty) );

		// Assemble the merchant's trade options
		imgMerchantOffer = new VisImage();
		imgMerchantOffer.setSize(200f, 200f);
		sbMerchantItems = new VisSelectBox<>();
		selections = new Array<>(merchant.getInventory());
		selections.add(EMPTY);
		selections.sort(new ItemComparator());
		sbMerchantItems.setItems(selections);
		spnMerchantQty = makeSpinner(sbMerchantItems);
		sbMerchantItems.addListener( new ItemChangeListener(imgMerchantOffer, spnMerchantQty) );

		btnOffer = new VisTextButton("Make an offer!", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateTrade();
			}
		});
		updateCaption();

		tblMessages = new VisTable();

		// Populate the layout //
		float PADDING = 5f;
		VisTable layout = new VisTable();
		layout.setFillParent(true);
		layout.setWidth(ui.getWidth());
		layout.columnDefaults(0).width(layout.getWidth() / 3 - 2*PADDING).pad(PADDING);
		layout.columnDefaults(1).width(layout.getWidth() / 3 - 2*PADDING).pad(PADDING);
		layout.columnDefaults(2).width(layout.getWidth() / 3 - 2*PADDING).pad(PADDING);


		layout.add(new VisLabel("You'll give ...", Align.center));
		layout.add(new VisLabel(merchant.getName(), Align.center));
		layout.add(new VisLabel("... in exchange for ...", Align.center));

		layout.row();
		layout.add(imgPlayerOffer).width(ui.getWidth() / 4).height(ui.getWidth() / 4).pad(ui.getWidth() / 24);
		layout.add(imgPortrait).height(layout.getWidth() / 3 - 2*PADDING);
		layout.add(imgMerchantOffer).width(ui.getWidth() / 4).height(ui.getWidth() / 4).pad(ui.getWidth() / 24);

		layout.row();
		layout.add(sbPlayerItems).uniform();
		layout.add(btnOffer).uniform();
		layout.add(sbMerchantItems).uniform();

		layout.row();
		layout.add(spnPlayerQty).uniform();
		layout.add(new VisTextButton("Back", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(next);
			}
		})).uniform();
		layout.add(spnMerchantQty).uniform();

		layout.row();
		spMessages = new VisScrollPane(tblMessages);
		spMessages.setScrollingDisabled(true, false);
		spMessages.setFlickScroll(false);
		layout.add(spMessages).height(100f).padLeft(10f).colspan(3).width(layout.getWidth() - 2*PADDING);

		ui.addActor(layout);
	}

	/**
	 * Updates the "Offer Button" caption based on the trade selection.
	 */
	public void updateCaption() {
		if(sbPlayerItems.getSelected().equals(EMPTY)) {
			if(sbMerchantItems.getSelected().equals(EMPTY)) {
				btnOffer.setText("<Select a proposal>");
				btnOffer.setDisabled(true);
			} else {
				btnOffer.setText("What do you want for this?");
				btnOffer.setDisabled(false);
			}
		} else {
			if(sbMerchantItems.getSelected().equals(EMPTY)) {
				btnOffer.setText("What will you give for this?");
				btnOffer.setDisabled(false);
			} else {
				btnOffer.setText("How about this?");
				btnOffer.setDisabled(false);
			}
		}
	}

	public void updateTrade() {
		Item playerItem = sbPlayerItems.getSelected();
		int playerQty = ((IntSpinnerModel)spnPlayerQty.getModel()).getValue();
		Item merchantItem = sbMerchantItems.getSelected();
		int merchantQty = ((IntSpinnerModel)spnMerchantQty.getModel()).getValue();

		// If both sides are incomplete, there's nothing to do.
		if(playerItem.equals(EMPTY) && merchantItem.equals(EMPTY)) return;

		// If one side is incomplete, ask the merchant to complete the offer
		if(merchantItem.equals(EMPTY)) {
			// Get the offer from the merchant
			Item offer = merchant.getOffer(playerItem, playerQty);
			processOffer(offer, merchant.getInventory(), sbMerchantItems, spnMerchantQty);
		} else if(playerItem.equals(EMPTY)) {
			// Get the offer from the merchant
			Item offer = merchant.getOffer(merchantItem, merchantQty, parent.getPlayer().getInventory());
			processOffer(offer, parent.getPlayer().getInventory(), sbPlayerItems, spnPlayerQty);
		} else {
			if(playerItem.getType().equals(merchantItem.getType())) {
				addMessage("Don't be silly.");
			} else {
				Item offer = merchant.getOffer(playerItem, playerQty, merchantItem, merchantQty);
				if (offer == null) {
					addMessage("There's no way I can make that trade.");
					logger.debug("I can't make that trade.");
				} else if (offer.getCount() == playerQty) {
					addMessage("I accept!");
					parent.getPlayer().addItem(merchantItem.getType(), merchantQty);
					parent.getPlayer().remove(playerItem.getType(), playerQty);
					Array<Item> selections = new Array<>(parent.getPlayer().getInventory());
					selections.add(EMPTY);
					selections.sort(new ItemComparator());
					sbPlayerItems.setItems(selections);
					sbPlayerItems.setSelected(EMPTY);

					merchant.addItem(playerItem.getType(), playerQty);
					merchant.remove(merchantItem.getType(), merchantQty);
					selections = new Array<>(merchant.getInventory());
					selections.add(EMPTY);
					selections.sort(new ItemComparator());
					sbMerchantItems.setItems(selections);
					sbMerchantItems.setSelected(EMPTY);

					logger.debug("I accept your trade.");
				} else {
					((IntSpinnerModel) spnPlayerQty.getModel()).setValue(offer.getCount());
					addMessage("That doesn't seem fair... how about " + offer.getCount() + " " + offer.getType().getName() + "instead?");
					logger.debug("How about {0} {1} instead?", offer.getCount(), offer.getType().getName());
				}
			}
		}

	}

	@Override
	public void show() {
		super.show();
	}

	// Helper methods / classes //

	private void processOffer(Item offer, Array<Item> inventory, VisSelectBox<Item> selectBox, Spinner spinner) {
		// If an offer isn't possible, tell the player.
		if(offer == null) {
			addMessage("I can't make an offer for that.");
			logger.debug("I can't make an offer for that.");
		} else {
			// Otherwise update the UI.
			selectBox.setSelected(getByType(offer.getType(), inventory));
			((IntSpinnerModel)spinner.getModel()).setValue(offer.getCount());
			addMessage("How about " + offer.getCount() + " " + offer.getType().getName() + "?");
			logger.debug("How about {0} {1}?", offer.getCount(), offer.getType().getName());
		}
	}

	/**
	 * Helper method to convert an array of Items to ItemTypes.
	 * @param items
	 * @return
	 */
	private Array<ItemType> convert(Array<Item> items) {
		Array<ItemType> types = new Array<>();
		for(Item i : items) {
			types.add(i.getType());
		}
		return types;
	}

	private Item getByType(ItemType type, Array<Item> items) {
		for(Item i : items) {
			if(i.getType().equals(type)) return i;
		}
		return null;
	}

	private void addMessage(String message) {
		VisLabel label = new VisLabel("[" + merchant.getName() + "]: " + message);
		label.setColor(1.0f, 1.0f, 1.0f, 0);
		label.addAction(Actions.fadeIn(0.5f));

		tblMessages.add(label).fillX().expandX().row();
		spMessages.addAction(Actions.sequence(Actions.delay(0.0f), new Action() {
			@Override
			public boolean act(float delta) {
				spMessages.setScrollPercentY(1.0f);
				return true;
			}
		}));
	}

	private Spinner makeSpinner(VisSelectBox<Item> selectBox) {
		return new Spinner(
				"",
				new IntSpinnerModel(
						selectBox.getSelected().getCount(),
						selectBox.getSelected().getCount() > 0 ? 1 : 0, // Handle EMPTY
						selectBox.getSelected().getCount()));
	}

	private class ItemChangeListener extends ChangeListener {
		final VisImage image;
		final Spinner spinner;

		public ItemChangeListener(VisImage image, Spinner spinner) {
			this.image = image;
			this.spinner = spinner;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			Item selected = ((VisSelectBox<Item>)actor).getSelected();
			if(selected.getType().getImage() != null) {
				image.setDrawable(parent.getManagedTexture("images/goods.atlas", selected.getType().getImage()));
			} else {
				image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(new Pixmap(1,1, Pixmap.Format.RGBA8888)))));
			}
			IntSpinnerModel spinnerModel = ((IntSpinnerModel)spinner.getModel());
			if(selected.equals(EMPTY)) {
				spinnerModel.setMin(0);
				spinnerModel.setMax(0);
				spinnerModel.setValue(0);
			} else {
				// Set the max to be the number that are available
				spinnerModel.setMax(selected.getCount());
				// Set the current to be the previous quantity or the max, if the previous qty was too high
				spinnerModel.setValue(selected.getCount() > spinnerModel.getValue() ? spinnerModel.getValue() : selected.getCount());
				// Normally we want the min to be 1
				spinnerModel.setMin(1);
			}
			updateCaption();
		}
	}

	private class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			return i1.getType().getName().compareToIgnoreCase(i2.getType().getName());
		}
	}
}
