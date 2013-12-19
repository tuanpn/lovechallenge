package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.PsychicsManager;
import vn.sunnet.game.manager.ResourceManager;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class StaticBodyComponent extends BodyComponent {

	private BodyDef bodyDef;
	private FixtureDef fixtureDef;

	public StaticBodyComponent() {
		bodyDef = new BodyDef();
	}

	@Override
	public void setup() {
		Entity owner = getOwner();
		bodyDef.position.set(owner.getCenterPositionX()
				* PsychicsManager.WORLD_TO_BOX, owner.getCenterPositionY()
				* PsychicsManager.WORLD_TO_BOX);
		body = owner.getLevel().getPsychicsManager().getWorld()
				.createBody(bodyDef);

		if (fixtureDef == null) {
			fixtureDef = new FixtureDef();
			fixtureDef.density = 0.0f;
		}
		fixtureDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_SCENERY;
		fixtureDef.shape = getShape();
		body.createFixture(fixtureDef);
		getShape().dispose();
		this.body.setUserData(owner);
	}

	protected Shape getShape() {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 * PsychicsManager.WORLD_TO_BOX, height / 2
				* PsychicsManager.WORLD_TO_BOX);
		return shape;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(Map<String, Object> map) {
		fixtureDef = ResourceManager.shared().getFixtureDef(
				(String) map.get("material"));

		this.width = Integer.parseInt((String) map.get("width"));
		this.height = Integer.parseInt((String) map.get("height"));
	}

}
