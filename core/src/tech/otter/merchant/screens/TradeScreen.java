package tech.otter.merchant.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import tech.otter.merchant.MerchantGame;
import tech.otter.merchant.data.Merchant;

/**
 * Created by john on 9/19/16.
 */

public class TradeScreen extends AbstractScreen {
	private Merchant merchant;
	private AbstractScreen next;
	public TradeScreen(MerchantGame parent, Merchant merchant, AbstractScreen next) {
		super(parent);

		this.merchant = merchant;
		this.next = next;

		VisTable layout = new VisTable();
		layout.setFillParent(true);
		layout.columnDefaults(0).pad(2f).width(300f);

		// TODO: Use asset loader
		VisImage portrait = new VisImage(new Texture(Gdx.files.internal(merchant.getPortrait())));
		portrait.setSize(300f, 300f);

		layout.add(portrait).height(300f).center().top();
		layout.row();
		layout.add(new VisTextButton("Back", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(next);
			}
		})).center();
		ui.addActor(layout);
	}

	@Override
	public void show() {
		super.show();
	}
}
