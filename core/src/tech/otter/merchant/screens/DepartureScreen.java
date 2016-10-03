package tech.otter.merchant.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.StarLane;
import tech.otter.merchant.data.Station;

public class DepartureScreen extends AbstractScreen {
	private final float PADDING = 2f;

	private Array<Station> stations;
	private Array<StarLane> starLanes;

	private ScalingGroup starMap;

	public DepartureScreen(MerchantGame parent, AbstractScreen previous) {
		super(parent);
		// Create UI Objects
		VisTable tblLayout = new VisTable();
		tblLayout.setFillParent(true);
		tblLayout.align(Align.bottom);

		// Create star map
		VisImage starMapBackground = new VisImage(new Texture(Gdx.files.internal("images/ui/map.png")));
		starMapBackground.setFillParent(true);
		starMapBackground.setZIndex(1);

		starMap = new ScalingGroup();
		starMap.addActor(starMapBackground);

		// A button to go back to the station.
		VisTextButton btnBack = new VisTextButton("Back", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(previous);
			}
		});

		// Add them to the table layout
		tblLayout.add(starMap).expand().fillY().center();
		tblLayout.row();
		tblLayout.add(btnBack).width(ui.getWidth() / 3 - 2*PADDING).pad(PADDING);

		ui.addActor(tblLayout);
	}

	@Override
	public void show() {
		super.show();

		for(Station station : parent.getGalaxy().getStations().values()) {
			// TODO: Use asset manager
			VisImage icon = new VisImage(new Texture(Gdx.files.internal("images/ui/dot2.png")));
			// TODO: Set relative to the map
			VisTextButton btn = new VisTextButton(station.getName(), new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					parent.getPlayer().setCurrentStation(station);
					changeScreen(new StationScreen(parent));
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
