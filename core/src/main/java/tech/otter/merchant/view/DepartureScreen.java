package tech.otter.merchant.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.controller.Controller;
import tech.otter.merchant.model.Model;
import tech.otter.merchant.model.Station;

public class DepartureScreen extends View {
	private final float PADDING = 2f;

	private ScalingGroup starMap;

	public DepartureScreen(Controller controller, Model model) {
        super(controller, model);

        // Create UI Objects
        VisTable tblLayout = new VisTable();
        tblLayout.setFillParent(true);
        tblLayout.align(Align.bottom);

        // Create star map
        starMap = new ScalingGroup();

        // A button to go back to the station.
        VisTextButton btnBack = makeNavButton("Back", tech.otter.merchant.view.StationScreen.class);

        // Add them to the table layout
        tblLayout.add(starMap).expand().fillY().center();
        tblLayout.row();
        tblLayout.add(btnBack).width(ui.getWidth() / 3 - 2*PADDING).pad(PADDING);

        ui.addActor(tblLayout);
	}

	@Override
	public void init() {
        starMap.clear();

        // Add background
        VisImage starMapBackground = new VisImage(controller.getManagedTexture("map"));
        starMapBackground.setFillParent(true);
        starMapBackground.setZIndex(1);
        starMap.addActor(starMapBackground);

        // Add stars
        for(final Station station : model.getGalaxy().getStations().values()) {
            VisImage icon = new VisImage(controller.getManagedTexture("dot2"));
            // TODO: Set relative to the map
            VisTextButton btn = new VisTextButton(station.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    model.getPlayer().setCurrentStation(station);
                    controller.changeScreen(tech.otter.merchant.view.StationScreen.class);
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
