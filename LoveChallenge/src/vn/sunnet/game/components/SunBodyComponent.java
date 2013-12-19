package vn.sunnet.game.components;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SunBodyComponent extends KinematicBodyComponent {

	@Override
	public void setup() {
		PlayerComponent player = (PlayerComponent) getOwner().getComponent(
				PlayerComponent.class);

		Entity owner = getOwner();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;

		bodyDef.position.set((owner.getCenterPositionX() + 200)
				* PsychicsManager.WORLD_TO_BOX,
				(owner.getCenterPositionY() + 200)
						* PsychicsManager.WORLD_TO_BOX);
		this.body = owner.getLevel().getPsychicsManager().getWorld()
				.createBody(bodyDef);

		applyFixtureDef(getFixtureDef());
		this.body.setUserData(owner);
		
		this.body.setLinearVelocity(player.moveSpeed, 0);
	}

}
