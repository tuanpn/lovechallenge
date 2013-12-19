package vn.sunnet.game.mainmenu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ResourceManagerMainMenu {

	private static final String TAG = "ResourceManagerMainMenu";
	private static ResourceManagerMainMenu _shared;

	public static ResourceManagerMainMenu shared() {
		if (_shared == null) {
			_shared = new ResourceManagerMainMenu();
		}
		return _shared;
	}

	public void load(AssetManager manager) {
		manager.load("data/theme/uiskin.json", Skin.class);
	}

	public void unload(AssetManager manager) {
		manager.unload("data/theme/uiskin.json");
	}

	public TextButton playButton;

	public void get(AssetManager manager) {
		Skin skin = manager.get("data/theme/uiskin.json");

		playButton = new TextButton("Play game", skin);
	}

}
