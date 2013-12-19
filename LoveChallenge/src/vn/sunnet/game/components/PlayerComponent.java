package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.Constants;
import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.ComponentRenderInterface;
import vn.sunnet.game.entities.ComponentUpdateInterface;
import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.LevelManager.GameState;
import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;

public class PlayerComponent extends Component implements
		ComponentUpdateInterface, ComponentRenderInterface {

	protected final static float MAX_VELOCITY = 6f;
	protected float MAX_FALL_VELOCITY = 20f;
	protected static final String TAG = "PlayerComponent";
	private static final float MAX_SLOPE = 3000;
	protected Fixture playerPhysicsFixture;
	protected Fixture playerSensorFixture;
	protected Body player;
	private long lastGroundTime;
	protected int width = 32;
	protected int height = 64;
	protected float playerWeight = 10;
	protected float stillTime;
	protected float moveSpeed = 7.0f;
	protected float jumpPower = 150.0f;
	protected boolean grounded;
	protected float sensorHeight;
	protected float slopeFactor;
	protected float sensorPositionY = -1f;
	MyGestureListenner gestureListener;

	private State state = State.Run;
	private AnimatedPlayerSpriteComponent playerAnimation;

	public enum State {
		Run, Jumping, Sit
	}

	public boolean alive = true;

	// xử lý hiển thị ảnh
	boolean jump = false;
	public float stateTimeJump;
	int typeJump;

	public float stateTimeRun;

	// sử lý sit
	boolean sit;
	public float stateTimeSit;

	// sử lý die
	public float stateTimeDie;
	int typeDie;

	public float stateTimeFall;

	public enum State_Display {
		DISPLAY_RUN, DISPLAY_JUMP, DISPLAY_SIT, DISPLAY_FALL
	}

	public State_Display state_display = State_Display.DISPLAY_RUN;

	@Override
	public void render(SpriteBatch batch) {
		if (alive) {
			switch (state_display) {
			case DISPLAY_RUN:
				playerAnimation.play("RunBody", "RunHead");
				break;
			case DISPLAY_JUMP:
				switch (typeJump) {
				case 1:
					playerAnimation.play("JumpBody1", "JumpHead1");
					break;
				case 2:
					playerAnimation.play("JumpBody2", "JumpHead2");
					break;
				case 3:
					playerAnimation.play("JumpBody3", "JumpHead3");
					break;

				default:
					break;
				}
				break;
			case DISPLAY_SIT:
				playerAnimation.play("SitBody", "SitHead");
				break;
			case DISPLAY_FALL:
				playerAnimation.play("FallBody", "FallHead");
				break;

			default:
				break;
			}
		} else {
			switch (typeDie) {
			case 0:
				playerAnimation.play("DieBody1", "DieHead1");
				break;
			case 1:
				playerAnimation.play("DieBody2", "DieHead2");
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void update(float delta) {
		if (alive) {
			updateAlive(delta);
		} else {
			updateDie(delta);
		}
	}

	private void updateDie(float delta) {
		stateTimeDie += delta;

		if (stateTimeDie > Constants.TIME_DIE) {
			stateTimeDie = 0;
			getOwner().getLevel().gameState = GameState.GAMEOVER;
		}
	}

	private void updateAlive(float delta) {
		Vector2 vel = player.getLinearVelocity();
		if (vel.x < moveSpeed) {
			player.setLinearVelocity(moveSpeed, player.getLinearVelocity().y);
			vel = player.getLinearVelocity();
		}
		Vector2 pos = player.getPosition();

		this.grounded = isPlayerGrounded(delta);
		// this.jump = Gdx.input.isKeyPressed(Keys.UP);
		// this.sit = Gdx.input.isKeyPressed(Keys.DOWN);
		if (grounded) {
			lastGroundTime = System.nanoTime();
		} else {
			if (System.nanoTime() - lastGroundTime < 100000000) {
				grounded = true;
			}
		}

		// if (!jump && !sit) {
		// stillTime += Gdx.graphics.getDeltaTime();
		// player.setLinearVelocity(vel.x, vel.y);
		// } else {
		// stillTime = 0;
		// }

		// if (!grounded) {
		// playerPhysicsFixture.setFriction(0f);
		// playerSensorFixture.setFriction(0f);
		// } else {
		// if (!jump && !sit) {
		// playerPhysicsFixture.setFriction(0f);
		// playerSensorFixture.setFriction(0f);
		// } else {
		// playerPhysicsFixture.setFriction(0f);
		// playerSensorFixture.setFriction(0f);
		// }
		// }

		if (jump && grounded) {
			jump = false;

			state_display = State_Display.DISPLAY_JUMP;
			player.setLinearVelocity(vel.x, 0);
			player.setTransform(pos.x, pos.y, 0);
			player.applyLinearImpulse(0, jumpPower, pos.x, pos.y, true);

			typeJump = MathUtils.random(1, 3);
		}

		if (sit) {
			getOwner().getLevel().getPsychicsManager().getWorld()
					.destroyBody(player);
			createBodySit();
			state_display = State_Display.DISPLAY_SIT;
			sit = false;
		}

		switch (state_display) {
		case DISPLAY_JUMP:
			stateTimeJump += delta;
			if (stateTimeJump > Constants.TIME_JUMP
					|| (stateTimeJump > 0.5f && grounded)) {
				stateTimeJump = 0;
				state_display = State_Display.DISPLAY_RUN;
				gestureListener.flingUp = false;
			}
			break;
		case DISPLAY_SIT:
			stateTimeSit += delta;
			if (stateTimeSit > Constants.TIME_SIT) {
				getOwner().getLevel().getPsychicsManager().getWorld()
						.destroyBody(player);

				createBodyRun();
				state_display = State_Display.DISPLAY_RUN;
				stateTimeSit = 0;
			}
			break;
		case DISPLAY_FALL:
			stateTimeFall += delta;
			if (stateTimeFall > Constants.TIME_FALL
					|| (stateTimeFall > 0.2f && grounded)) {
				stateTimeFall = 0;
				state_display = State_Display.DISPLAY_RUN;
			}
			break;
		case DISPLAY_RUN:
			stateTimeRun += delta;
			break;

		default:
			break;
		}

		player.setAwake(true);

		// getOwner().getPosition().y =
		// player.getPosition().y/PsychicsManager.WORLD_TO_BOX;
		// System.out.println(getOwner().getPosition().y);
	}

	private boolean isPlayerGrounded(float delta) {
		Array<Contact> contactList = getOwner().getLevel().getPsychicsManager()
				.getWorld().getContactList();
		for (int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if (contact.isTouching()
					&& (contact.getFixtureA() == playerSensorFixture || contact
							.getFixtureB() == playerSensorFixture)) {

				Vector2 pos = player.getPosition();
				WorldManifold manifold = contact.getWorldManifold();
				boolean below = true;
				slopeFactor = 0.0f;
				for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
					slopeFactor = Math.abs(Math.round((pos.x - manifold
							.getPoints()[j].x) * 10000));
					// Gdx.app.log(TAG, "Slope: " + slopeFactor);
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

			// colistion
			if (contact.getFixtureA() == playerPhysicsFixture
					|| contact.getFixtureB() == playerPhysicsFixture) {
				alive = false;
				typeDie = Math.random() < 0.5f ? 0 : 1;
				break;
			}
		}
		return false;
	}

	@Override
	public void onRemove() {
		Entity owner = getOwner();
		owner.getLevel().getPsychicsManager().getWorld().destroyBody(player);
	}

	@Override
	public void setup() {

		createBodyRun();
		gestureListener = new MyGestureListenner();
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		getOwner().getLevel().getInput().addProcessor(gestureDetector);
		getOwner().getLevel().getPsychicsManager().getWorld()
				.setContactListener(new MyContactListener());

		playerAnimation = (vn.sunnet.game.components.AnimatedPlayerSpriteComponent) getOwner()
				.getComponent(AnimatedPlayerSpriteComponent.class);
	}

	protected void createBodyRun() {
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
		fixDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		playerPhysicsFixture = box.createFixture(fixDef);
		poly.dispose();

		CircleShape circle = new CircleShape();
		circle.setRadius(sensorRadius + 0.032f);

		circle.setPosition(new Vector2(0, sensorPositionY));

		fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.density = 0.0f;
		fixDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		playerSensorFixture = box.createFixture(fixDef);
		circle.dispose();

		box.setBullet(true);

		player = box;

		PsychicsManager.updateEntityByBody(player);

		player.setUserData(e);
		player.setFixedRotation(true);
		player.setLinearVelocity(moveSpeed, 0);

	}

	protected void createBodySit() {
		Entity e = getOwner();
		e.setWidth(height);
		e.setHeight(width);

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
		float sensorRadius = heightInMeters;

		poly.setAsBox(widthInMeters / 2, heightInMeters);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = poly;
		fixDef.density = playerWeight;
		fixDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		playerPhysicsFixture = box.createFixture(fixDef);
		poly.dispose();

		CircleShape circle = new CircleShape();
		circle.setRadius(sensorRadius + 0.032f);
		circle.setPosition(new Vector2(0, sensorPositionY / 2f));

		fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.density = 0.0f;
		fixDef.filter.categoryBits = PsychicsManager.FILTER_CATEGORY_DONT_ABSORB_LIGHT;
		playerSensorFixture = box.createFixture(fixDef);
		circle.dispose();

		box.setBullet(true);

		player = box;

		PsychicsManager.updateEntityByBody(player);

		player.setUserData(e);
		player.setFixedRotation(true);
		player.setLinearVelocity(moveSpeed, 0);
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

	private class MyContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture fixtureA = contact.getFixtureA();
			Fixture fixtureB = contact.getFixtureB();

			Gdx.app.log(TAG, "begin contact " + grounded);
		}

		@Override
		public void endContact(Contact contact) {
			Fixture fixtureA = contact.getFixtureA();
			Fixture fixtureB = contact.getFixtureB();
			Entity eA = null;
			if (fixtureA != null) {
				eA = (Entity) fixtureA.getBody().getUserData();
				grounded = false;
				if ((fixtureA == playerSensorFixture || fixtureB == playerSensorFixture)
						&& !eA.id.equals("GROUND")) {
					if (!gestureListener.flingUp) {
						state_display = State_Display.DISPLAY_FALL;
					} else {
						state_display = State_Display.DISPLAY_JUMP;
						gestureListener.flingUp = false;
					}

				}
			}

			Gdx.app.log(TAG, "end contact " + grounded);
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * bắt sự kiện player
	 * 
	 * @author PhamNgoc
	 * 
	 */
	private class MyGestureListenner implements GestureListener {

		public boolean flingUp = false;

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if (state_display == State_Display.DISPLAY_RUN) {
				if (velocityY < 0) {
					jump = true;
					flingUp = true;
				} else if (velocityY > 0) {
					sit = true;
				}
			}

			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			// TODO Auto-generated method stub
			return false;
		}

	}

}
