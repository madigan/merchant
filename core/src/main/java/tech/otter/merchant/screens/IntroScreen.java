package tech.otter.merchant.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.ArrayList;
import java.util.List;

import tech.otter.merchant.GameController;

public class IntroScreen extends AbstractScreen {
	private final float DELAY = 3.0f;
	private final float LENGTH = 5.0f;

	public IntroScreen(GameController parent) {
		super(parent);
		
	}
	
	@Override
	public void show() {
		super.show();
		ui.clear();
		
		List<String> story = new ArrayList<>();
		story.add("Your planet is dying ...");
		story.add("  ... Your people struggle for survival amidst the dust ...");
		story.add("    ... with the right technology, your world might be saved ...");
		story.add("      ... if only they could afford their salvation.");
		story.add("");
		story.add("");
		story.add("But not all hope is lost...");
		story.add("Through great sacrifice, your family has procured a humble star ship ...");
		story.add("  ... and enough goods to make a new life as an independent trader ...");
		story.add("    ... the opportunity that they never had.");
		story.add("");
		story.add("");
		story.add("You will embark on a search for riches...");
		story.add("  ... accumulating the wealth and power to save your planet... ");
		story.add("    ... or become the brightest star in the universe on your own.");
		story.add("");
		story.add("");
		story.add("This story is yours.");

		for(int i = 0; i < story.size(); i++) {
			Actor label = getFadedLabel(story.get(i), i * DELAY, LENGTH);
			label.setPosition(10f, ui.getHeight() - label.getHeight() * (i+1));
			ui.addActor(label);
		}
		ui.addAction(Actions.sequence(
				Actions.delay(DELAY * (story.size() + 1) + LENGTH), 
				new Action() {

			@Override
			public boolean act(float delta) {
                parent.changeScreen(NewGameScreen.class);
				return false;
			}
			
		}));
		// Add a key listener so that players can skip the intro using ESCAPE, ENTER, or SPACE.
		// Inescapable text dialogues is definitely a no-twinkie scenario.
		ui.addListener(new InputListener() {
			@Override
			public boolean keyUp (InputEvent event, int keyCode) {
				boolean consumed;
				if(consumed = (keyCode == Keys.ESCAPE || keyCode == Keys.ENTER || keyCode == Keys.SPACE)) {
                    parent.changeScreen(NewGameScreen.class);
				}
				return consumed;
			}
		});
	}
	
	private Actor getFadedLabel(String text, float delay, float length) {
		Actor label = new VisLabel( text );
		label.setColor(1.0f, 1.0f, 1.0f, 0);
		label.addAction(
				Actions.sequence(
						Actions.delay(delay), 
						Actions.fadeIn(DELAY), 
						Actions.delay(length), 
						Actions.fadeOut(DELAY)
						) 
				);
		return label;
	}

}
