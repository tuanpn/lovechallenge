package vn.sunnet.game.utils;

import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.math.Vector3;

public class Position extends Vector3 {

	private static final long serialVersionUID = 1L;

	public float getXInMeters() {
		return this.x * PsychicsManager.WORLD_TO_BOX;
	}

	public float getYInMeters() {
		return this.y * PsychicsManager.WORLD_TO_BOX;
	}

	public float setXInMeters(float x) {
		return this.x = x / PsychicsManager.WORLD_TO_BOX;
	}

	public float setYInMeters(float y) {
		return this.y = y / PsychicsManager.WORLD_TO_BOX;
	}
}
