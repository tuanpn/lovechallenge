package vn.sunnet.game.screens;

import vn.sunnet.game.Constants;
import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.LevelManager;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class LevelScreen implements Screen {

	SpriteBatch debugBatch;
	LevelManager level;

	Entity player;

	Entity ground1;
	boolean addGround;
	float posXGroundCache;
	float posYGroundCache = 75;

//	Entity sky;
//	boolean addSky;
//	float posXSkyCache;
//	float posYSkyCache = 400;

	private Entity lookAt;

	float stateTimeEnemy;
	
	BuildSkyEntity skys;
	BuildLamppostEntity lamposts;

	public LevelScreen() {
		level = new LevelManager();
		debugBatch = new SpriteBatch();
		skys = new BuildSkyEntity(this);
		lamposts = new BuildLamppostEntity(this);
		
		player = level.getEntityManager().build("PLAYER");
		player.getPosition().x = 400;
		player.getPosition().y = 150;
		
		Entity girl = level.getEntityManager().build("GIRL");
		girl.getPosition().x = 300;
		girl.getPosition().y = 150;

		Entity neightborhood = level.getEntityManager().build("NEIGHBORHOOD");
		neightborhood.setPosition(0, 0);

		ground1 = level.getEntityManager().build("GROUND");
		ground1.setPosition(0, posYGroundCache);
		ground1.setWidth((int) level.getSize().width);
		ground1.setHeight(20);
		posXGroundCache = ground1.getPosition().x;

		// hướng nhìn
		lookAt = level.getEntityManager().build();

		if (player.getPosition().y < 240) {
			lookAt.setPosition(player.getPosition().x, 240);
		} else {
			lookAt.setPosition(player.getPosition().x, player.getPosition().y);
		}
		level.setLookAt(lookAt);
	}

	@Override
	public void render(float delta) {
		// update lookat
		if (player.getPosition().y < 240) {
			lookAt.setPosition(player.getPosition().x, 240);
		} else {
			lookAt.setPosition(player.getPosition().x, player.getPosition().y);
		}

		// add ground
		if (lookAt.getPosition().x + 400 > ground1.getPosition().x
				+ ground1.getWidth()) {
			posXGroundCache = ground1.getPosition().x;
			addGround = true;
		}
		if (addGround) {
			buildGround();
			addGround = false;
		}
		skys.render(delta);
		lamposts.render(delta);

		createEnemy(delta);

		level.update(delta);
		level.render();
		level.getPsychicsManager().renderDebug();

		// destroy
		for (Entity enity : level.getEntityManager()) {
			if (lookAt.getPosition().x - 400 > enity.getPosition().x
					+ enity.getWidth()) {
				level.getEntityManager().remove(enity);
				enity.destroy();
				break;
			}
		}
		
	}

	private void buildGround() {
		ground1 = level.getEntityManager().build("GROUND");
		ground1.setWidth((int) level.getSize().width);
		ground1.setHeight(20);
		ground1.setPosition(posXGroundCache + ground1.getWidth(),
				posYGroundCache);
		Entity neightborhood = level.getEntityManager().build("NEIGHBORHOOD");
		neightborhood.setPosition(ground1.getPosition().x, 0);
	}

	private void createEnemy(float delta) {
		stateTimeEnemy += delta;
		if (stateTimeEnemy > Constants.TIME_CREATE_ENEMY) {
			stateTimeEnemy = 0;
			switch (MathUtils.random(0, 1)) {
			case 0:
				Entity enemy0 = level.getEntityManager().build("STATICENEMYCAR1");
				enemy0.setPosition(lookAt.getPosition().x + 500, 55);
				break;
			case 1:
				Entity enemy1 = level.getEntityManager().build("STATICENEMYCAR2");
				enemy1.setPosition(lookAt.getPosition().x + 500, 55);
				break;

			default:
				break;
			}
			
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	public LevelManager getLevelManager() {
		return level;
	}
	
	public Entity getLockAt() {
		return lookAt;
	}

}
