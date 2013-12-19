package vn.sunnet.game.manager;

import java.util.ArrayList;

import vn.sunnet.game.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityManager extends ArrayList<Entity> {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "EntityManager";
	private LevelManager level;
	private int renderCount = 0;
	private int updateCount = 0;
	private SpriteBatch entityBatch;

	public EntityManager(LevelManager levelManager) {
		Gdx.app.log(TAG, "Initializing new EntityManager with max ");
		this.level = levelManager;
		this.entityBatch = new SpriteBatch();

	}

	public Entity build() {
		Entity e = new Entity();
		e.setLevel(level);
		this.add(e);
		return e;
	}

	public Entity build(String id) {
		Entity e = PrefabManager.shared().build(id);
		e.setLevel(level);
		this.add(e);
		return e;
	}

	public void render() {
		OrthographicCamera camera = level.getCamera();
		entityBatch.setProjectionMatrix(camera.combined);
		entityBatch.enableBlending();
		entityBatch.begin();
		renderCount = 0;
		for (int i = 0; i < this.size(); i++) {
			//chua toi uu,can toi uu lai neu nhieu entity
			for (int j = 0; j < this.size(); j++) {
				Entity e = this.get(j);
				if (e.getIndex() == i) {
					// if (e.visibleByCamera(camera)) {
					e.render(entityBatch);
					renderCount++;
					// }
				}
			}
		}
		entityBatch.end();
	}

	public void update(float delta) {
		updateCount = 0;
		for (int i = 0; i < this.size(); i++) {
			Entity e = this.get(i);
			e.update(delta);
			updateCount++;
		}
	}

	public int getRenderCounts() {
		return renderCount;
	}

	public int getUpdateCounts() {
		return updateCount;
	}

}
