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

import tech.otter.merchant.GameController;
import tech.otter.merchant.util.ItemEntry;


public class CargoScreen extends AbstractScreen {
	private VisList<ItemEntry> lstItems;
	private VisImage imgItem;
	private VisTextArea txtDescription;

	public CargoScreen(final GameController parent) {
		super(parent);

		VisTable tblLayout = new VisTable();
		tblLayout.columnDefaults(0).pad(10f);
		tblLayout.columnDefaults(1).pad(10f);
		tblLayout.setFillParent(true);

		imgItem = new VisImage();
		imgItem.setSize(ui.getWidth() / 2, ui.getWidth() / 2); // TODO: Refactor to world size.

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
                parent.changeScreen(StationScreen.class);
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
		Array<ItemEntry> items = new Array<>();
		world.getPlayer().getInventory().forEach(i -> items.add(new ItemEntry(i.key, i.value)));
		items.sort(new ItemEntry.ItemEntryComparator());
		lstItems.setItems(items);
		updateSelection(lstItems.getSelected());
	}

    /**
     * Flush all stateful data.
     */
	@Override
    public void hide() {
        super.hide();
        lstItems.clear();
        updateSelection(lstItems.getSelected());
    }

	private void updateSelection(ItemEntry selected) {
		imgItem.setDrawable(parent.getManagedTexture(selected.getType().getImage()));
		txtDescription.setText(selected.getType().getDescription());
	}
}
