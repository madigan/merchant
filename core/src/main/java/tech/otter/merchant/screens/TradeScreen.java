package tech.otter.merchant.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.Deal;
import tech.otter.merchant.data.Item;
import tech.otter.merchant.data.Merchant;
import tech.otter.merchant.util.ItemEntry;

public class TradeScreen extends AbstractScreen {
	private VisTextButton btnOffer;

	private VisImage imgPlayerOffer;
	private VisSelectBox<ItemEntry> sbPlayerItems;
	private Spinner spnPlayerQty;

	private Merchant merchant;
	private VisImage imgMerchantOffer;
	private VisSelectBox<ItemEntry> sbMerchantItems;
	private Spinner spnMerchantQty;

	private VisTable tblMessages;
	private VisScrollPane spMessages;

	final ItemEntry BLANK = new ItemEntry();

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
		sbPlayerItems.setItems(Array.with(BLANK));
		spnPlayerQty = makeSpinner(sbPlayerItems);
		sbPlayerItems.addListener( new ItemChangeListener(imgPlayerOffer, spnPlayerQty) );

		// Assemble the merchant's trade options
		imgMerchantOffer = new VisImage();
		imgMerchantOffer.setSize(200f, 200f);
		sbMerchantItems = new VisSelectBox<>();
		sbMerchantItems.setItems(Array.with(BLANK));
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

		resetPage();
	}

	/**
	 * Updates the "Offer Button" caption based on the trade selection.
	 */
	public void updateCaption() {
		if(sbPlayerItems.getSelected().equals(BLANK)) {
			if(sbMerchantItems.getSelected().equals(BLANK)) {
				btnOffer.setText("<Select a proposal>");
				btnOffer.setDisabled(true);
			} else {
				btnOffer.setText("What do you want for this?");
				btnOffer.setDisabled(false);
			}
		} else {
			if(sbMerchantItems.getSelected().equals(BLANK)) {
				btnOffer.setText("What will you give for this?");
				btnOffer.setDisabled(false);
			} else {
				btnOffer.setText("How about this?");
				btnOffer.setDisabled(false);
			}
		}
	}

	private Deal deal = null;
	public void updateTrade() {
		if(deal == null) deal = new Deal();
		deal.setPlayer(parent.getPlayer());
		deal.setMerchant(merchant);

		deal.setPlayerType(sbPlayerItems.getSelected().getType());
		deal.setPlayerQty(((IntSpinnerModel)spnPlayerQty.getModel()).getValue());
		deal.setMerchantType(sbMerchantItems.getSelected().getType());
		deal.setMerchantQty(((IntSpinnerModel)spnMerchantQty.getModel()).getValue());

		deal = merchant.processDeal(deal);
		if(deal.isAccepted()) {
			// Do accepted stuff...
			resetPage();
			deal = null;
		} else {
            // Refresh the page
            if(deal.isMerchantComplete()) {
                sbMerchantItems.getItems().forEach(entry -> {
                    if(entry.getType() != null && entry.getType().equals(deal.getMerchantType())) {
                        sbMerchantItems.setSelected(entry);
                    }
                });
            }

            if(deal.isPlayerComplete()) {
                sbPlayerItems.getItems().forEach(entry -> {
                    if (entry.getType() != null && entry.getType().equals(deal.getPlayerType())) {
                        sbPlayerItems.setSelected(entry);
                    }
                });
            }
        }
	}

	@Override
	public void show() {
		super.show();
	}

	// Helper methods / classes //
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

	private Spinner makeSpinner(VisSelectBox<ItemEntry> selectBox) {
		return new Spinner(
				"",
				new IntSpinnerModel(
						selectBox.getSelected().getCount(),
						selectBox.getSelected().getCount() > 0 ? 1 : 0, // Handle BLANK
						selectBox.getSelected().getCount()));
	}

	private class ItemChangeListener extends ChangeListener {
		final VisImage image;
		final Spinner spinner;

		public ItemChangeListener(VisImage image, Spinner spinner) {
			this.image = image;
			this.spinner = spinner;
		}

		// TODO
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			ItemEntry selected = ((VisSelectBox<ItemEntry>)actor).getSelected();
			if(selected.getType() != null) {
				image.setDrawable(parent.getManagedTexture("images/goods.atlas", selected.getType().getImage()));
			}
			// Reset the spinner
			IntSpinnerModel spinnerModel = ((IntSpinnerModel)spinner.getModel());
			if(selected.equals(BLANK)) {
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

	private void resetPage() {
		resetPageHelper(sbMerchantItems, merchant.getInventory());
		resetPageHelper(sbPlayerItems, parent.getPlayer().getInventory());
	}
	private void resetPageHelper(VisSelectBox<ItemEntry> selectBox, ObjectIntMap<Item> inventory) {
		Array<ItemEntry> entries = Array.with(BLANK);
		inventory.forEach(i -> entries.add(new ItemEntry(i.key, i.value)));
		entries.sort(new ItemEntry.ItemEntryComparator());
        selectBox.setItems(entries);
		selectBox.setSelected(BLANK);
	}
}
