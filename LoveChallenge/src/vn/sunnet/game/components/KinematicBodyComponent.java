package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.PsychicsManager;
import vn.sunnet.game.manager.ResourceManager;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class KinematicBodyComponent extends BodyComponent {

	private static final String TAG = "DynamicBodyComponent";
	private Fixture fixture;
	private FixtureDef fixtureDef;

	public KinematicBodyComponent() {
		setFixtureDef(ResourceManager.shared().getFixtureDef("CUBE"));
	}

	@Override
	public void setup() {
		Entity owner = getOwner();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;

		bodyDef.position.set(owner.getCenterPositionX()
				* PsychicsManager.WORLD_TO_BOX, owner.getCenterPositionY()
				* PsychicsManager.WORLD_TO_BOX);
		this.body = owner.getLevel().getPsychicsManager().getWorld()
				.createBody(bodyDef);

		applyFixtureDef(getFixtureDef());
		this.body.setUserData(owner);
	}

	protected void applyFixtureDef(FixtureDef fixtureDef2) {
		if (this.fixture != null) {
			body.destroyFixture(fixture);
		}
		body.resetMassData();
		fixtureDef.shape = getShape();
		fixtureDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		fixture = body.createFixture(fixtureDef);
		
//		this.body.setLinearVelocity(5f, 0);
	}

	protected Shape getShape() {
		Entity owner = getOwner();

		float width = Math.round(owner.getWidth()
				* PsychicsManager.WORLD_TO_BOX / 2);
		float height = Math.round(owner.getHeight()
				* PsychicsManager.WORLD_TO_BOX / 2);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		return shape;
	}

	@Override
	public void reset() {
		body.resetMassData();
	}

	@Override
	public void configure(Map<String, Object> map) {
		setFixtureDef(ResourceManager.shared().getFixtureDef(
				(String) map.get("material")));
	}

	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}

	public void setFixtureDef(FixtureDef fixtureDef) {
		this.fixtureDef = fixtureDef;
	}

}
