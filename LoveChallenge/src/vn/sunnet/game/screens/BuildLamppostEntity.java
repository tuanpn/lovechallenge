package vn.sunnet.game.screens;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.LevelManager;

public class BuildLamppostEntity {

	private boolean addLamp;
	private float positionCacheX = 400;
	private float positionCacheY = 120;
	private Entity lampost;

	LevelScreen levelScreen;
	LevelManager level;

	float stateTime;

	public BuildLamppostEntity(LevelScreen levelScreen) {
		this.levelScreen = levelScreen;
		level = levelScreen.getLevelManager();

		lampost = level.getEntityManager().build("LAMPPOST");
		lampost.setPosition(positionCacheX, positionCacheY);
	}

	public void render(float delta) {
		if (levelScreen.getLockAt().getPosition().x + 400 > lampost
				.getPosition().x) {
			positionCacheX = lampost.getPosition().x;
			addLamp = true;
		}

		if (addLamp) {
			lampost = level.getEntityManager().build("LAMPPOST");
			lampost.setPosition(positionCacheX + 500, positionCacheY);
			addLamp = false;
		}
	}

}
