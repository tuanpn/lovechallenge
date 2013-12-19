package vn.sunnet.game.components;

import java.security.acl.Owner;
import java.util.Map;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.lights.box2dLight.ConeLight;
import com.lights.box2dLight.RayHandler;

public class ConeLightComponent extends Component {

	private static final String TAG = "ConeLightComponent";
	private static final int RAY_COUNT = 64;
	private boolean isStatic = false;
	private float distance = 10.0f;
	private Color color = Color.WHITE;
	private Body body;
	private ConeLight light;

	@Override
	public void onRemove() {
		if (light != null) {
			light.remove();
		}
	}

	@Override
	public void setup() {
		BodyComponent component = (BodyComponent) getOwner().getComponent(
				BodyComponent.class);
		RayHandler handler = getOwner().getLevel().getPsychicsManager()
				.getLight();
		this.body = component.getBody();
		if (body == null) {
			Gdx.app.error(TAG, "Body cannot be null!");
		}

		this.light = new ConeLight(handler, 10, color, distance,
				body.getPosition().x, body.getPosition().y, getOwner()
						.getRotation() * MathUtils.degreesToRadians, 40);

		light.attachToBody(body, 0, 0);
		light.setColor(color);
		light.setSoft(true);
		light.setSoftnessLenght(1.0f);
		Filter filter = new Filter();
		filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		filter.maskBits = PsychicsManager.FILTER_MASK_DONT_ABSORB_LIGHT;
		light.setContactFilter(filter);
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
	}

}
