package vn.sunnet.game.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ResourceManager {

	private static final String TAG = "ResourceManager";
	private static ResourceManager _shared;

	private Map<String, String> atlasPath;
	private Map<String, String> skinPath;
	private Map<String, String> fontsPath;
	private Map<String, String> texturesPath;

	private Map<String, TextureAtlas> atlasMap;
	private Map<String, Skin> skinMap;
	private Map<String, BitmapFont> fonts;
	private Map<String, Texture> textures;
	private Map<String, FixtureDef> materials;

	public static ResourceManager shared() {
		if (_shared == null) {
			_shared = new ResourceManager();
		}
		return _shared;
	}

	public ResourceManager() {
		atlasPath = new HashMap<String, String>();
		skinPath = new HashMap<String, String>();
		fontsPath = new HashMap<String, String>();
		texturesPath = new HashMap<String, String>();

		atlasMap = new HashMap<String, TextureAtlas>();
		skinMap = new HashMap<String, Skin>();
		fonts = new HashMap<String, BitmapFont>();
		textures = new HashMap<String, Texture>();
		materials = new HashMap<String, FixtureDef>();
	}

	public void load() throws IOException {
		XmlReader xmlReader = new XmlReader();
		Element resources = xmlReader.parse(Gdx.files
				.internal("data/assets.game"));
		Gdx.app.log(TAG, "Loaded resources XML");
		Array<Element> listResoures = resources.getChildrenByName("resource");

		for (int resourceIdx = 0; resourceIdx < listResoures.size; resourceIdx++) {
			Element resourceElement = listResoures.get(resourceIdx);
			String type = resourceElement.getAttribute("type");
			if (type.equals("atlas")) {
				addElementAsAtlas(resourceElement);
			} else if (type.equals("texture")) {
				addElementAsTexture(resourceElement);
			} else if (type.equals("material")) {
				addElementAsMaterial(resourceElement);
			}
		}
	}

	private void addElementAsAtlas(Element resourceElement) {
		String id = resourceElement.getAttribute("id");
		String path = resourceElement.getText();
		path = "data/textures/" + path + ".atlas";
		Gdx.app.log(TAG, "Loading: " + id + " from " + path);
		atlasPath.put(id, path);
	}

	private void addElementAsTexture(Element resourceElement) {
		String id = resourceElement.getAttribute("id");
		String path = resourceElement.getText();
		path = "data/textures/" + path;

		Gdx.app.log(TAG, "Loading Texture: " + id + " from " + path);
		texturesPath.put(id, path);
	}

	private void addElementAsMaterial(Element resourceElement) {
		String id = resourceElement.getAttribute("id");
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = Float.parseFloat(resourceElement
				.getAttribute("density"));
		fixtureDef.friction = Float.parseFloat(resourceElement
				.getAttribute("friction"));
		fixtureDef.restitution = Float.parseFloat(resourceElement
				.getAttribute("restitution"));
		this.materials.put(id, fixtureDef);
		Gdx.app.log(TAG, "Loading material: " + id);
	}

	public void loadAtlas(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = atlasPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> thisEntry = iterator.next();
			String id = thisEntry.getKey();
			String path = thisEntry.getValue();
			manager.load(path, TextureAtlas.class);
		}
	}

	public void loadTexture(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = texturesPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> thisEntry = iterator.next();
			String id = thisEntry.getKey();
			String path = thisEntry.getValue();
			manager.load(path, Texture.class);
		}
	}

	public void getAtlas(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = atlasPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> thisEntry = iterator.next();
			String id = thisEntry.getKey();
			String path = thisEntry.getValue();
			this.atlasMap.put(id, (TextureAtlas) manager.get(path));
		}
	}

	public void getTexture(AssetManager manager) {
		Iterator<Entry<String, String>> iterator = texturesPath.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> thisEntry = iterator.next();
			String id = thisEntry.getKey();
			String path = thisEntry.getValue();
			this.textures.put(id, (Texture) manager.get(path));
			Gdx.app.log(TAG, "Loading texture: " + id+" path :"+path);
		}
	}

	public TextureAtlas getAtlas(String id) {
		return this.atlasMap.get(id);
	}

	public Texture getTexture(String string) {
		return this.textures.get(string);
	}

	public FixtureDef getFixtureDef(String id) {
		return this.materials.get(id);
	}
}
