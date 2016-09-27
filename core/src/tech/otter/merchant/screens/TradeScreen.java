package tech.otter.merchant.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

/**
 * Created by john on 9/19/16.
 */

public class TradeScreen extends AbstractScreen {
	private VisTextButton btnOffer;

	private VisImage imgPlayerOffer;
	private VisSelectBox<Item> sbPlayerItems;
	private Spinner spnPlayerQty;

	private Merchant merchant;
	private VisImage imgMerchantOffer;
	private VisSelectBox<Item> sbMerchantItems;
	private Spinner spnMerchantQty;

	private VerticalGroup messages;

	final Item EMPTY = new Item( new ItemType("", "", "images/empty.png", 0, 0, null));

	public TradeScreen(MerchantGame parent, Merchant merchant, final AbstractScreen next) {
		super(parent);

		this.merchant = merchant;

		VisTable layout = new VisTable();
		layout.setFillParent(true);
		layout.columnDefaults(0).pad(2f).width(300f);

		// TODO: Use asset loader
		// TODO: Redistribute functionality between constructor and show()
		VisImage imgPortrait = new VisImage(new Texture(Gdx.files.internal(merchant.getPortrait())));
		imgPortrait.setSize(300f, 300f);

		// Assemble the player's trade options
		imgPlayerOffer = new VisImage();
		imgPlayerOffer.setSize(200f, 200f);
		sbPlayerItems = new VisSelectBox<Item>();
		Array<Item> selections = new Array(parent.getPlayer().getInventory());
		selections.add(EMPTY);
		selections.sort(new ItemComparator());
		sbPlayerItems.setItems(selections);
		spnPlayerQty = makeSpinner(sbPlayerItems);
		sbPlayerItems.addListener( new ItemChangeListener(imgPlayerOffer, spnPlayerQty) );

		// Assemble the merchant's trade options
		imgMerchantOffer = new VisImage();
		imgMerchantOffer.setSize(200f, 200f);
		sbMerchantItems = new VisSelectBox<Item>();
		selections = new Array(merchant.getInventory());
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

		messages = new VerticalGroup();
		for(int i = 0; i < 5; i++) messages.addActor(new VisLabel(""));

		// Set layout defaults //
		layout.columnDefaults(0).width(ui.getWidth() / 3);
		layout.columnDefaults(1).width(ui.getWidth() / 3);
		layout.columnDefaults(2).width(ui.getWidth() / 3);

		// Populate the layout //
		layout.add(new VisLabel("You'll give ...", Align.center));
		layout.add(new VisLabel(merchant.getName(), Align.center));
		layout.add(new VisLabel("... in exchange for ...", Align.center));

		layout.row();
		layout.add(imgPlayerOffer).width(ui.getWidth() / 4).height(ui.getWidth() / 4).pad(ui.getWidth() / 24);
		layout.add(imgPortrait).height(ui.getWidth() / 3);
		layout.add(imgMerchantOffer).width(ui.getWidth() / 4).height(ui.getWidth() / 4).pad(ui.getWidth() / 24);

		layout.row();
		layout.add(sbPlayerItems);
		layout.add(btnOffer);
		layout.add(sbMerchantItems);

		layout.row();
		layout.add(spnPlayerQty);
		layout.add(new VisTextButton("Back", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(next);
			}
		}));
		layout.add(spnMerchantQty);

		layout.row();
		layout.add(new VisScrollPane(messages)).colspan(3).width(ui.getWidth()).align(Align.left);

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
			Item offer = merchant.getOffer(playerItem, playerQty);
			if(offer == null) offer = EMPTY;
			sbMerchantItems.setSelected(getByType(offer.getType(), merchant.getInventory()));
			((IntSpinnerModel)spnMerchantQty.getModel()).setValue(offer.getCount());
			addMessage("How about " + offer.getCount() + " " + offer.getType().getName() + "?");
			logger.debug("How about {0} {1}?", offer.getCount(), offer.getType().getName());
		} else if(playerItem.equals(EMPTY)) {
			Item offer = merchant.getOffer(merchantItem, merchantQty, parent.getPlayer().getInventory());
			if(offer == null) offer = EMPTY;
			sbPlayerItems.setSelected(getByType(offer.getType(), merchant.getInventory()));
			((IntSpinnerModel)spnPlayerQty.getModel()).setValue(offer.getCount());
			addMessage("How about " + offer.getCount() + " " + offer.getType().getName() + "?");
			logger.debug("How about {0} {1}?", offer.getCount(), offer.getType().getName());
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
					Array<Item> selections = new Array(parent.getPlayer().getInventory());
					selections.add(EMPTY);
					selections.sort(new ItemComparator());
					sbPlayerItems.setItems(selections);
					sbPlayerItems.setSelected(EMPTY);

					merchant.addItem(playerItem.getType(), playerQty);
					merchant.remove(merchantItem.getType(), merchantQty);
					selections = new Array(merchant.getInventory());
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

	/**
	 * Helper method to convert an array of Items to ItemTypes.
	 * @param items
	 * @return
	 */
	private Array<ItemType> convert(Array<Item> items) {
		Array<ItemType> types = new Array<ItemType>();
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
	// TODO: Make the messages align left
	private void addMessage(String message) {
		message = "[" + merchant.getName() + "]: " + message;
		messages.removeActor(messages.getChildren().get(messages.getChildren().size-1));
		VisLabel label = new VisLabel(message);
		label.setAlignment(Align.left);
		label.setColor(1.0f, 1.0f, 1.0f, 0);
		label.addAction(Actions.fadeIn(0.5f));
		messages.addActorBefore(messages.getChildren().first(), label);
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
			image.setDrawable(new Texture(Gdx.files.internal(selected.getType().getImage())));
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
