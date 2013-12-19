package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.manager.ResourceManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteTextureComponent extends SpriteComponent {

	@Override
	public void configure(Map<String, Object> map) {
		Texture tex = ResourceManager.shared().getTexture(
				(String) map.get("texture"));

		if (map.containsKey("offset-y")) {
			offsetY = Float.parseFloat((String) map.get("offset-y"));
		}
		
		setTexture(new TextureRegion(tex));
	}

}
