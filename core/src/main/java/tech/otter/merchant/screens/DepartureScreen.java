package tech.otter.merchant.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.GameController;
import tech.otter.merchant.data.StarLane;
import tech.otter.merchant.data.Station;

public class DepartureScreen extends AbstractScreen {
	private final float PADDING = 2f;

	private ScalingGroup starMap;

	public DepartureScreen(GameController parent) {
		super(parent);
	}

	@Override
	public void show() {
		super.show();
        ui.clear();

        // Create UI Objects
        VisTable tblLayout = new VisTable();
        tblLayout.setFillParent(true);
        tblLayout.align(Align.bottom);

        // Create star map
        VisImage starMapBackground = new VisImage(parent.getManagedTexture("map"));
        starMapBackground.setFillParent(true);
        starMapBackground.setZIndex(1);

        starMap = new ScalingGroup();
        starMap.addActor(starMapBackground);

        // A button to go back to the station.
        VisTextButton btnBack = new VisTextButton("Back", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(StationScreen.class);
            }
        });

        // Add them to the table layout
        tblLayout.add(starMap).expand().fillY().center();
        tblLayout.row();
        tblLayout.add(btnBack).width(ui.getWidth() / 3 - 2*PADDING).pad(PADDING);

        ui.addActor(tblLayout);

		for(final Station station : world.getGalaxy().getStations().values()) {
			VisImage icon = new VisImage(parent.getManagedTexture("dot2"));
			// TODO: Set relative to the map
			VisTextButton btn = new VisTextButton(station.getName(), new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					world.getPlayer().setCurrentStation(station);
                    parent.changeScreen(StationScreen.class);
				}
			});
			btn.setPosition(station.getX() + btn.getHeight(), station.getY());

			icon.setSize(btn.getHeight(), btn.getHeight());
			icon.setPosition(station.getX(), station.getY());
			starMap.addActor(icon);
			starMap.addActor(btn);
		}
	}

	private class ScalingGroup extends WidgetGroup {
		@Override
		public void layout() {
			if(this.getWidth() != this.getHeight()) {
				// Size and center
				this.setX(this.getX() - (this.getHeight() - this.getWidth()) / 2);
				this.setWidth(this.getHeight());
			}
		}
	}
}
