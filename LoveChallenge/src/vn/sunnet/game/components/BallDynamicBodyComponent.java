package vn.sunnet.game.components;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class BallDynamicBodyComponent extends DynamicBodyComponent {

	@Override
	protected Shape getShape() {
		Entity owner = getOwner();
		CircleShape ballShape = new CircleShape();
		float width = Math.round(owner.getWidth()
				* PsychicsManager.WORLD_TO_BOX / 2);
		ballShape.setRadius(width);
		return ballShape;
	}

}
