package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.manager.ResourceManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SpriteAtlasComponent extends SpriteComponent {

	public SpriteAtlasComponent() {

	}

	@Override
	public void configure(Map<String, Object> map) {
		TextureAtlas atlas = ResourceManager.shared().getAtlas(
				(String) map.get("atlas"));

		if (map.containsKey("offset-x")) {
			offsetX = Float.parseFloat((String) map.get("offset-x"));
		}
		if (map.containsKey("offset-y")) {
			offsetY = Float.parseFloat((String) map.get("offset-y"));
		}
		
		setTexture(atlas.findRegion((String) map.get("region")));
	}

}
