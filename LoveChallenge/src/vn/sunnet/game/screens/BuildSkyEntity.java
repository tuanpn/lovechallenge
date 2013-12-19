package vn.sunnet.game.screens;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.LevelManager;
import vn.sunnet.game.manager.PsychicsManager;

public class BuildSkyEntity {

	private boolean addSky;
	private float positionCacheX;
	private float positionCacheY = 450;
	private Entity sky;

	LevelScreen levelScreen;
	LevelManager level;

	float stateTime;

	float deltaNextSky;

	int changeLight = 0;

	public BuildSkyEntity(LevelScreen levelScreen) {
		this.levelScreen = levelScreen;
		level = levelScreen.getLevelManager();

		sky = level.getEntityManager().build("SKY1");
		sky.setPosition(positionCacheX, positionCacheY);

		deltaNextSky = sky.getWidth() / 5.0f * PsychicsManager.WORLD_TO_BOX;
		System.out.println("time = " + deltaNextSky);
	}

	public void render(float delta) {
		stateTime += delta;
		if (levelScreen.getLockAt().getPosition().x + 400 > sky.getPosition().x
				+ sky.getWidth()) {
			positionCacheX = sky.getPosition().x;
			addSky = true;
		}

		if (addSky) {
			if (stateTime < 10) {
				sky = level.getEntityManager().build("SKY1");
			} else if (stateTime >= 10 && stateTime < 17) {
				sky = level.getEntityManager().build("SKY2");
				changeLight = 1;
			} else if (stateTime >= 17 && stateTime < 30) {
				sky = level.getEntityManager().build("SKY3");
				changeLight = 2;
			} else if (stateTime >= 30 && stateTime < 37) {
				sky = level.getEntityManager().build("SKY4");
				changeLight = 3;
			} else {
				sky = level.getEntityManager().build("SKY5");
				changeLight = 4;
			}

			sky.setPosition(positionCacheX + sky.getWidth(), positionCacheY);
			addSky = false;
		}

		switch (changeLight) {
		case 0:

			break;
		case 1:
			if (level.getPsychicsManager().getLight().getAmbientLight().a > 0.8f) {
				level.getPsychicsManager().getLight().getAmbientLight().a -= delta / 2;
			} else {
				level.getPsychicsManager().getLight().getAmbientLight().a = 0.8f;
			}
			break;
		case 2:
			if (level.getPsychicsManager().getLight().getAmbientLight().a > 0.6f) {
				level.getPsychicsManager().getLight().getAmbientLight().a -= delta / 2;
			} else {
				level.getPsychicsManager().getLight().getAmbientLight().a = 0.6f;
			}
			break;
		case 3:
			if (level.getPsychicsManager().getLight().getAmbientLight().a > 0.4f) {
				level.getPsychicsManager().getLight().getAmbientLight().a -= delta / 2;
			} else {
				level.getPsychicsManager().getLight().getAmbientLight().a = 0.4f;
			}
			break;
		case 4:
			if (level.getPsychicsManager().getLight().getAmbientLight().a > 0.2f) {
				level.getPsychicsManager().getLight().getAmbientLight().a -= delta / 2;
			} else {
				level.getPsychicsManager().getLight().getAmbientLight().a = 0.2f;
			}
			break;

		default:
			break;
		}
	}

}
