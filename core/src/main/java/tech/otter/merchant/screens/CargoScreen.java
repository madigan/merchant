package tech.otter.merchant.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.Comparator;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.Item;

/**
 * Created by john on 9/28/16.
 */

public class CargoScreen extends AbstractScreen {
	private VisList<Item> lstItems;
	private VisImage imgItem;
	private VisTextArea txtDescription;

	public CargoScreen(final MerchantGame parent) {
		super(parent);

		VisTable tblLayout = new VisTable();
		tblLayout.columnDefaults(0).pad(10f);
		tblLayout.columnDefaults(1).pad(10f);
		tblLayout.setFillParent(true);

		imgItem = new VisImage();
		imgItem.setSize(ui.getWidth() / 2, ui.getWidth() / 2);

		lstItems = new VisList<>();
		lstItems.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateSelection(lstItems.getSelected());
			}
		});

		txtDescription = new VisTextArea();
		txtDescription.setDisabled(true);

		VisTextButton btnBack = new VisTextButton("Back", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(new StationScreen(parent));
			}
		});


		tblLayout.add(imgItem).size(ui.getWidth() / 2);
		tblLayout.add(new VisScrollPane(lstItems)).fillY();
		tblLayout.row();
		tblLayout.add(txtDescription).size(ui.getWidth() / 2, 100.0f);
		tblLayout.add(btnBack).expandX().fillX();

		ui.addActor(tblLayout);
	}

	@Override
	public void show() {
		super.show();
		Array<Item> items = new Array<>(parent.getPlayer().getInventory());
		items.sort(new Comparator<Item>() {
			@Override
			public int compare(Item item1, Item item2) {
				return item1.getType().getName().compareTo(item2.getType().getName());
			}
		});
		lstItems.setItems(items);
		updateSelection(lstItems.getSelected());
	}

	private void updateSelection(Item selected) {
		imgItem.setDrawable(parent.getManagedTexture("images/goods.atlas", selected.getType().getImage()));
		txtDescription.setText(selected.getType().getDescription());
	}
}
