package vn.sunnet.game.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PrefabManager {

	private static PrefabManager _shared;
	private final static String TAG = "PrefabManager";
	public static final String EXTRA_PAYLOAD = "EXTRA_PAYLOAD";
	private Map<String, PrefabFactor> prefabs;

	public static PrefabManager shared() {
		if (_shared == null) {
			_shared = new PrefabManager();
		}
		return _shared;
	}

	public PrefabManager() {
		Gdx.app.log(TAG, "Initializing...");
		this.prefabs = new HashMap<String, PrefabFactor>();
	}

	public void load() throws IOException {
		XmlReader xmlReader = new XmlReader();
		Element listPrefabs = xmlReader.parse(Gdx.files
				.internal("data/prefabs.game"));
		Gdx.app.log(TAG, "Loaded prefabs XML");

		int totalResources = listPrefabs.getChildCount();

		for (int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++) {
			Element prefabElement = listPrefabs.getChild(resourceIdx);
			addPrefab(prefabElement);
		}
	}

	private void addPrefab(Element element) {
		String id = element.getAttribute("id");
		String index = element.getAttribute("index");
		PrefabFactor factor = new PrefabFactor(id, index);

		Gdx.app.log(TAG, "Loading prefab: " + id);

		Array<Element> listComponents = element.getChildrenByName("component");
		int componentCount = listComponents.size;

		for (int i = 0; i < componentCount; i++) {
			Element componentElement = listComponents.get(i);
			String componentName = componentElement.getAttribute("type");

			try {
				@SuppressWarnings("unchecked")
				Class<? extends Component> component = (Class<? extends Component>) Class
						.forName("vn.sunnet.game.components." + componentName);

				HashMap<String, Object> options = PrefabManager
						.getOptionsFromXmlAttributes(componentElement
								.getAttributes());

				if (componentElement.getChildByName("extra") != null) {
					options.put(EXTRA_PAYLOAD,
							componentElement.getChildrenByName("extra"));
				}

				factor.getComponents().put(component, options);
				factor.getComponentsOrderList().add(component);
			} catch (ClassNotFoundException e) {
				Gdx.app.log(TAG, "not class " + componentName);
			}
		}

		this.prefabs.put(id, factor);
	}

	private static HashMap<String, Object> getOptionsFromXmlAttributes(
			ObjectMap<String, String> attributes) {
		HashMap<String, Object> options = new HashMap<String, Object>();
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

	public Entity build(String id) {
		PrefabFactor factor = this.prefabs.get(id);
		Entity e = new Entity();
		e.id = id;
		e.setIndex(Integer.parseInt(factor.getIndex()));
		for (Class<? extends Component> componentKlass : factor
				.getComponentsOrderList()) {
			Component component = e.addComponent(componentKlass);
			component.configure(factor.getComponents().get(componentKlass));
		}
		return e;
	}

}
