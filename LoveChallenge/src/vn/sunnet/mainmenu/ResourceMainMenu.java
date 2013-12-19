package vn.sunnet.mainmenu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ResourceMainMenu {

	private final static String TAG = "ResourceMainMenu";
	private static ResourceMainMenu _shared;

	private Map<String, String> atlasPath;
	private Map<String, TextureAtlas> atlasMap;

	private Map<String, String> skinPath;
	private Map<String, Skin> skinMap;

	public static ResourceMainMenu shared() {
		if (_shared == null) {
			_shared = new ResourceMainMenu();
		}
		return _shared;
	}

	public ResourceMainMenu() {
		atlasPath = new HashMap<String, String>();
		atlasMap = new HashMap<String, TextureAtlas>();

		skinPath = new HashMap<String, String>();
		skinMap = new HashMap<String, Skin>();
	}

	public void load() throws IOException {
		XmlReader xmlReader = new XmlReader();
		Element resources = xmlReader.parse(Gdx.files
				.internal("mainmenu/mainmenuasset.game"));
		Gdx.app.log(TAG, "Loaded resources XML");

		Array<Element> listResource = resources.getChildrenByName("resource");
		for (int i = 0; i < listResource.size; i++) {
			Element resourceElement = listResource.get(i);
			String type = resourceElement.getAttribute("type");
			if (type.equals("atlas")) {
				addElementAsAtlas(resourceElement);
			} else if (type.equals("theme")) {
				addElementThemes(resourceElement);
			}
		}
	}

	private void addElementThemes(Element resourceElement) {
		String id = resourceElement.getAttribute("id");
		String path = resourceElement.getText();
		path = "mainmenu/" + path + ".json";
		Gdx.app.log(TAG, "Loading: " + id + " from " + path);
		skinPath.put(id, path);
	}

	private void addElementAsAtlas(Element resourceElement) {
		String id = resourceElement.getAttribute("id");
		String path = resourceElement.getText();
		path = "mainmenu/" + path + ".atlas";
		Gdx.app.log(TAG, "Loading: " + id + " from " + path);
		atlasPath.put(id, path);
	}

	public void loadAtlas(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = atlasPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();
			String id = entry.getKey();
			String path = entry.getValue();
			manager.load(path, TextureAtlas.class);
		}
	}
	
	public void unloadSkin(AssetManager manager) {
		
	}

	public void loadSkin(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = skinPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();
			String id = entry.getKey();
			String path = entry.getValue();
			manager.load(path, Skin.class);
		}
	}

	public void unload() {
		
	}

	public void getAtlas(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = atlasPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();
			String id = entry.getKey();
			String path = entry.getValue();
			atlasMap.put(id, manager.get(path, TextureAtlas.class));
		}
	}

	public void getSkin(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = skinPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();
			String id = entry.getKey();
			String path = entry.getValue();
			skinMap.put(id, manager.get(path, Skin.class));
		}
	}

	public TextureAtlas getAtlas(String id) {
		return this.atlasMap.get(id);
	}

	public Skin getSkin(String id) {
		return this.skinMap.get(id);
	}

}
