package vn.sunnet.game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.sunnet.game.entities.Component;

public class PrefabFactor {

	private String id;
	private String index;
//	private String width;
//	private String height;

	private Map<Class<? extends Component>, Map<String, Object>> components;
	private ArrayList<Class<? extends Component>> componentsOrderList;

	public PrefabFactor(String id, String index) {
		this.id = id;
		this.index = index;
		componentsOrderList = new ArrayList<Class<? extends Component>>();
		setComponents(new HashMap<Class<? extends Component>, Map<String, Object>>());
	}

	public Map<Class<? extends Component>, Map<String, Object>> getComponents() {
		return components;
	}

	private void setComponents(
			Map<Class<? extends Component>, Map<String, Object>> components) {
		this.components = components;
	}

	public ArrayList<Class<? extends Component>> getComponentsOrderList() {
		return componentsOrderList;
	}

	public String getIndex() {
		return index;
	}

//	public String getWidth() {
//		return width;
//	}
//
//	public void setWidth(String width) {
//		this.width = width;
//	}
//
//	public String getHeight() {
//		return height;
//	}
//
//	public void setHeight(String height) {
//		this.height = height;
//	}

}
