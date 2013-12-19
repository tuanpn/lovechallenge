package vn.sunnet.game.components;

import vn.sunnet.game.entities.Entity;
import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class TriangleStaticBodyComponent extends StaticBodyComponent {

	@Override
	protected PolygonShape getShape() {
		Entity owner = getOwner();

		float width = Math.round(owner.getWidth()
				* PsychicsManager.WORLD_TO_BOX / 2);
		float height = Math.round(owner.getHeight()
				* PsychicsManager.WORLD_TO_BOX / 2);

		PolygonShape shape = new PolygonShape();
		Vector2[] vertices = new Vector2[3];

		vertices[0] = new Vector2(0.0f, -1.0f);
		vertices[1] = new Vector2(2.0f, 1f);
		vertices[2] = new Vector2(4.0f, -1.0f);
		shape.set(vertices);
		return shape;
	}

}
