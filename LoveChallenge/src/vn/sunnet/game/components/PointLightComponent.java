package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.lights.box2dLight.PointLight;
import com.lights.box2dLight.RayHandler;

public class PointLightComponent extends Component {

	private static final String TAG = "PointLightComponent";
	private static final int RAY_COUNT = 64;
	private boolean isStatic = false;
	private float distance = 10.0f;
	private Color color = Color.WHITE;
	private Body body;
	private PointLight light;
	private float offsetX = 0.0f;
	private float offsetY = 0.0f;

	@Override
	public void onRemove() {
		if (light != null) {
			light.remove();
			light = null;
		}
	}

	@Override
	public void setup() {
		SpriteAtlasComponent component = (SpriteAtlasComponent) getOwner()
				.getComponent(SpriteAtlasComponent.class);
		RayHandler handler = getOwner().getLevel().getPsychicsManager()
				.getLight();
		this.light = new PointLight(handler, RAY_COUNT);
		light.setDistance(distance);
		light.setActive(true);
		light.setStaticLight(isStatic);

		// this.body = component.getBody();
		// if (body == null) {
		// Gdx.app.error(TAG, "Body cannot be null!");
		// }

		// light.attachToBody(body, 0, 0);
		light.setColor(color);
		light.setSoft(true);
		light.setSoftnessLenght(100.0f);
		Filter filter = new Filter();
		filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		filter.maskBits = PsychicsManager.FILTER_MASK_DONT_ABSORB_LIGHT;
		light.setContactFilter(filter);
		light.setPosition((component.getOwner().getPosition().x + offsetX)
				* PsychicsManager.WORLD_TO_BOX, (component.getOwner()
				.getPosition().y + offsetY) * PsychicsManager.WORLD_TO_BOX);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(Map<String, Object> map) {
		isStatic = (String) map.get("static") == "true";
		distance = Float.parseFloat((String) map.get("distance"));
		float r = Float.parseFloat((String) map.get("red"));
		float g = Float.parseFloat((String) map.get("green"));
		float b = Float.parseFloat((String) map.get("blue"));
		float a = Float.parseFloat((String) map.get("alpha"));
		color = new Color(r, g, b, a);
		offsetX = Float.parseFloat((String) map.get("offset-x"));
		offsetY = Float.parseFloat((String) map.get("offset-y"));
	}

}
