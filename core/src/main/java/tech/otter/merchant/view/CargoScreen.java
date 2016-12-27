package tech.otter.merchant.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.util.ItemEntry;


public class CargoScreen extends View {
	private VisList<ItemEntry> lstItems;
	private VisImage imgItem;
	private VisTextArea txtDescription;
    private VisTextButton btnBack;

	public CargoScreen(Controller parent, Model model) {
		super(parent, model);

        // Initialize the image
        imgItem = new VisImage();
        imgItem.setSize(ui.getWidth() / 2, ui.getWidth() / 2); // TODO: Refactor to model size.

        // Create the item list
        lstItems = new VisList<>();
        lstItems.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateSelection(lstItems.getSelected());
            }
        });

        // Create the description field
        txtDescription = new VisTextArea();
        txtDescription.setDisabled(true);

        // Add a "back" button
        btnBack = makeNavButton("Back", StationScreen.class);

        // Create the layout
        VisTable tblLayout = new VisTable();
        tblLayout.columnDefaults(0).pad(10f);
        tblLayout.columnDefaults(1).pad(10f);
        tblLayout.setFillParent(true);
        tblLayout.add(imgItem).size(ui.getWidth() / 2);
        tblLayout.add(new VisScrollPane(lstItems)).fillY();
        tblLayout.row();
        tblLayout.add(txtDescription).size(ui.getWidth() / 2, 100.0f);
        tblLayout.add(btnBack).expandX().fillX();

        ui.addActor(tblLayout);
	}

	@Override
	public void init() {
        // Refresh the player data
		Array<ItemEntry> items = new Array<>();
		model.getPlayer().getInventory().forEach(i -> items.add(new ItemEntry(i.key, i.value)));
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
		imgItem.setDrawable(controller.getManagedTexture(selected.getType().getImage()));
		txtDescription.setText(selected.getType().getDescription());
	}
}
