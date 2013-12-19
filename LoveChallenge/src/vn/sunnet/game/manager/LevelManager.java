package vn.sunnet.game.manager;

import vn.sunnet.game.entities.Entity;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class LevelManager {
	private static final String TAG = "LevelManager";
	private String name;
	private EntityManager entityManager;
	private PsychicsManager psychicsManager;
	private TweenManager tweenManager;
	private OrthographicCamera camera;
	private Entity followEntity;
	private Rectangle size;

	private InputMultiplexer input;

	public enum GameState {
		RUNNING, PAUSE, GAMEOVER
	}

	public GameState gameState = GameState.RUNNING;

	public LevelManager() {
		camera = new OrthographicCamera(800, 480);
		camera.position.set(camera.viewportWidth * 0.5f,
				camera.viewportHeight * 0.5f, 0f);
		camera.update(true);

		entityManager = new EntityManager(this);
		psychicsManager = new PsychicsManager(this);
		tweenManager = new TweenManager();
		size = new Rectangle(0, 0, 3000, 1000);

		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
	}

	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}

	public void render() {
		if (followEntity != null) {
			camera.position.set(followEntity.getPosition().x,
					followEntity.getPosition().y, 0);
		}
		camera.update();
		entityManager.render();
		psychicsManager.render();
	}

	public void update(float delta) {
		switch (gameState) {
		case RUNNING:
			entityManager.update(delta);
			psychicsManager.update(delta);
			break;
		case GAMEOVER:
			break;

		default:
			break;
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public PsychicsManager getPsychicsManager() {
		return psychicsManager;
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setLookAt(Entity e) {
		this.followEntity = e;
	}

	public Rectangle getSize() {
		return size;
	}

	public void setSize(Rectangle size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputMultiplexer getInput() {
		return input;
	}

	public void save() {
		Gdx.app.log(TAG, "Saving map:" + getName());
	}
}
