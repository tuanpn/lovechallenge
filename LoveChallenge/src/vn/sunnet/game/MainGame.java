package vn.sunnet.game;

import java.io.IOException;

import vn.sunnet.accessors.ActorAccessor;
import vn.sunnet.accessors.SpriteAccessor;
import vn.sunnet.game.manager.PrefabManager;
import vn.sunnet.game.manager.ResourceManager;
import vn.sunnet.game.screens.LoadingScreen;
import vn.sunnet.mainmenu.PrefabMainMenu;
import vn.sunnet.mainmenu.ResourceMainMenu;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MainGame extends Game {

	private AssetManager manager1;
	private AssetManager manager2;

	@Override
	public void create() {
		manager1 = new AssetManager();
		manager2 = new AssetManager();

		Tween.setWaypointsLimit(10);
		Tween.setCombinedAttributesLimit(3);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		try {
			PrefabManager.shared().load();
			ResourceManager.shared().load();
			ResourceMainMenu.shared().load();
			PrefabMainMenu.shared().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setScreen(new LoadingScreen(this, 0));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}

	public AssetManager getManager1() {
		return manager1;
	}

	public AssetManager getManager2() {
		return manager2;
	}

}
