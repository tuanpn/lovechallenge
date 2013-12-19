package vn.sunnet.game.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.ComponentRenderInterface;
import vn.sunnet.game.entities.ComponentUpdateInterface;
import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.PsychicsManager;
import vn.sunnet.game.manager.ResourceManager;
import vn.sunnet.game.utils.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;

public class GirlComponent extends Component implements
		ComponentUpdateInterface, ComponentRenderInterface {
	final static float MAX_VELOCITY = 6f;
	private float MAX_FALL_VELOCITY = 20f;
	private static final String TAG = "PlayerComponent";
	private static final float MAX_SLOPE = 3000;
	boolean jump = false;
	private Fixture playerPhysicsFixture;
	private Fixture playerSensorFixture;
	private Body girlBody;
	private long lastGroundTime;
	private int width = 32;
	private int height = 64;
	private float playerWeight = 10;
	private float stillTime;
	private float moveSpeed = 4.0f;
	private float jumpPower = 20.0f;
	private boolean grounded;
	private float sensorHeight;
	private float slopeFactor;
	private float sensorPositionY = -0.6f;

	private State state = State.Idle;
	private AnimatedSpriteComponent girlAnimation;

	Array<Body> bodies;
	Vector2 vel;
	Vector2 pos;

	public enum State {
		Idle, Walking, Jumping
	}

	public GirlComponent() {
		bodies = new Array<Body>();
	}

	@Override
	public void update(float delta) {
		vel = girlBody.getLinearVelocity();
		pos = girlBody.getPosition();
		updateAi(delta);

		this.grounded = isPlayerGrounded(delta);
		// this.jump = Gdx.input.isKeyPressed(Keys.W);
		// boolean keyLeft = Gdx.input.isKeyPressed(Keys.A);
		// boolean keyRight = Gdx.input.isKeyPressed(Keys.D);
		if (grounded) {
			lastGroundTime = System.nanoTime();
		} else {
			if (System.nanoTime() - lastGroundTime < 100000000) {
				grounded = true;
			}
		}

		// if (!keyLeft && !keyRight) {
		// stillTime += Gdx.graphics.getDeltaTime();
		// player.setLinearVelocity(vel.x, vel.y);
		// } else {
		// stillTime = 0;
		// }

		if (jump) {
			jump = false;
			if (grounded) {
				girlBody.setLinearVelocity(vel.x, 0);
				girlBody.setTransform(pos.x, pos.y, 0);
				girlBody.applyLinearImpulse(0, jumpPower, pos.x, pos.y, true);
			}
		}

		girlBody.setAwake(true);
	}

	private boolean isPlayerGrounded(float deltaTime) {
		Array<Contact> contactList = getOwner().getLevel().getPsychicsManager()
				.getWorld().getContactList();
		for (int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if (contact.isTouching()
					&& (contact.getFixtureA() == playerSensorFixture || contact
							.getFixtureB() == playerSensorFixture)) {

				Vector2 pos = girlBody.getPosition();
				WorldManifold manifold = contact.getWorldManifold();
				boolean below = true;
				slopeFactor = 0.0f;
				for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
					slopeFactor = Math.abs(Math.round((pos.x - manifold
							.getPoints()[j].x) * 10000));
					// Gdx.app.log(TAG, "Slope: "+ slopeFactor);
					below &= (manifold.getPoints()[j].y < pos.y - sensorHeight && slopeFactor <= MAX_SLOPE);
				}

				if (below) {
					if (contact.getFixtureA().getUserData() != null
							&& contact.getFixtureA().getUserData().equals("p")) {
						// groundedPlatform =
						// (MovingPlatform)contact.getFixtureA().getBody().getUserData();
					}

					if (contact.getFixtureB().getUserData() != null
							&& contact.getFixtureB().getUserData().equals("p")) {
						// groundedPlatform =
						// (MovingPlatform)contact.getFixtureB().getBody().getUserData();
					}
					return true;
				}

				return false;
			}
		}
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		if (girlAnimation != null)
			girlAnimation.play("Run");
	}

	@Override
	public void setup() {
		Entity e = getOwner();
		e.setWidth(width);
		e.setHeight(height);

		this.sensorHeight = (e.getWidth() / 2 * PsychicsManager.WORLD_TO_BOX);
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(e.getCenterPositionX() * PsychicsManager.WORLD_TO_BOX,
				e.getCenterPositionY() * PsychicsManager.WORLD_TO_BOX);

		Body box = e.getLevel().getPsychicsManager().getWorld().createBody(def);
		PolygonShape poly = new PolygonShape();
		float widthInMeters = e.getWidth() * PsychicsManager.WORLD_TO_BOX
				/ 2.0f;
		float heightInMeters = e.getHeight() * PsychicsManager.WORLD_TO_BOX
				/ 2.0f;
		float sensorRadius = widthInMeters;
		// Gdx.app.log(TAG, "Size in meters: "+ widthInMeters +
		// "x"+heightInMeters);

		poly.setAsBox(widthInMeters, heightInMeters);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = poly;
		fixDef.density = playerWeight;
		fixDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_SCENERY;
		playerPhysicsFixture = box.createFixture(fixDef);
		poly.dispose();

		CircleShape circle = new CircleShape();
		circle.setRadius(sensorRadius + 0.032f);

		circle.setPosition(new Vector2(0, sensorPositionY));

		fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.density = 0.0f;
		fixDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_SCENERY;
		playerSensorFixture = box.createFixture(fixDef);
		circle.dispose();

		box.setBullet(true);

		girlBody = box;
		girlBody.setUserData(e);
		girlBody.setFixedRotation(true);
		girlBody.setLinearVelocity(moveSpeed, 0);

		girlAnimation = (AnimatedSpriteComponent) getOwner().getComponent(
				AnimatedSpriteComponent.class);

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(Map<String, Object> map) {
		this.width = Integer.parseInt((String) map.get("width"));
		this.height = Integer.parseInt((String) map.get("height"));
		this.playerWeight = Float.parseFloat((String) map.get("weight"));
		this.moveSpeed = Float.parseFloat((String) map.get("move-speed"));
		this.jumpPower = Float.parseFloat((String) map.get("jump-power"));
		this.sensorPositionY = Float.parseFloat((String) map
				.get("sensor-offset-y"));
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void onRemove() {
		Entity owner = getOwner();
		owner.getLevel().getPsychicsManager().getWorld().destroyBody(girlBody);
	}

	private void updateAi(float delta) {
		ArrayList<Entity> entitys = getOwner().getLevel().getEntityManager();
		for (int i = 0; i < entitys.size(); i++) {
			if (entitys.get(i).getComponent(StaticBodyComponent.class) != null) {
				StaticBodyComponent component = (StaticBodyComponent) entitys
						.get(i).getComponent(StaticBodyComponent.class);
				if (component.getBody() != null
						&& !entitys.get(i).id.equals("GROUND")) {
					float distance = component.getBody().getPosition().x
							- girlBody.getPosition().x;
					if (distance < 6 && distance > 0) {
						if (grounded)
							jump = true;
					}
				}

			}

		}
	}
}
