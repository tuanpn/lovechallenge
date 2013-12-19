package vn.sunnet.mainmenu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PrefabMainMenu {

	private static PrefabMainMenu _shared;
	private final static String TAG = "PrefabMainMenu";
	private Map<String, HashMap<String, String>> prefabSprites;
	private Map<String, HashMap<String, String>> prefabButtons;

	public static PrefabMainMenu shared() {
		if (_shared == null) {
			_shared = new PrefabMainMenu();
		}
		return _shared;
	}

	public PrefabMainMenu() {
		Gdx.app.log(TAG, "Initializing...");
		this.prefabSprites = new HashMap<String, HashMap<String, String>>();
		this.prefabButtons = new HashMap<String, HashMap<String, String>>();
	}

	public void load() throws IOException {
		XmlReader xmlReader = new XmlReader();
		Element listPrefabs = xmlReader.parse(Gdx.files
				.internal("mainmenu/mainmenuprefabs.game"));
		Gdx.app.log(TAG, "Loaded prefabs XML");

		Array<Element> listPrefab = listPrefabs.getChildrenByName("prefab");
		for (int i = 0; i < listPrefab.size; i++) {
			Element element = listPrefab.get(i);
			HashMap<String, String> options = PrefabMainMenu
					.getOptionsFromXmlAttributes(element.getAttributes());
			String type = element.getAttribute("type");
			if (type.equals("Sprite"))
				prefabSprites.put(element.getAttribute("id"), options);
			if (type.equals("Button"))
				prefabButtons.put(element.getAttribute("id"), options);
		}
	}

	public Sprite buildSprite(String id) {
		HashMap<String, String> hs = this.prefabSprites.get(id);
		TextureAtlas altas = ResourceMainMenu.shared()
				.getAtlas(hs.get("atlas"));
		Sprite s = altas.createSprite(hs.get("region"));
		return s;
	}

	public Button buildButton(String id) {
		HashMap<String, String> hs = this.prefabButtons.get(id);
		Skin skin = ResourceMainMenu.shared().getSkin(hs.get("theme"));
		Button b = new Button(skin, hs.get("name"));
		return b;
	}

	private static HashMap<String, String> getOptionsFromXmlAttributes(
			ObjectMap<String, String> attributes) {
		HashMap<String, String> options = new HashMap<String, String>();
		Keys<String> listKeys = attributes.keys();
		Values<String> listValues = attributes.values();

		Iterator<String> keyIter = listKeys.iterator();
		Iterator<String> valueIter = listValues.iterator();

		while (keyIter.hasNext() && valueIter.hasNext()) {
			String key = (String) keyIter.next();
			String value = valueIter.next();
			options.put(key, value);
			Gdx.app.log(TAG, "keys : " + key + " value " + value);
		}

		return options;
	}
}
