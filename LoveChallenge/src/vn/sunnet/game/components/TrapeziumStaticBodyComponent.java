package vn.sunnet.game.components;

import java.util.Map;

import vn.sunnet.game.manager.PsychicsManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class TrapeziumStaticBodyComponent extends StaticBodyComponent {

	int trapeziumOffsetX = 0;

	@Override
	protected Shape getShape() {

		ChainShape chainShape = new ChainShape();
		chainShape.createLoop(new Vector2[] {
				new Vector2(-width / 2 * PsychicsManager.WORLD_TO_BOX, height
						/ 2f * PsychicsManager.WORLD_TO_BOX),
				new Vector2((-width / 2f + trapeziumOffsetX)
						* PsychicsManager.WORLD_TO_BOX, -height / 2f
						* PsychicsManager.WORLD_TO_BOX),
				new Vector2(width / 2 * PsychicsManager.WORLD_TO_BOX, -height
						/ 2f * PsychicsManager.WORLD_TO_BOX),
				new Vector2(width / 2 * PsychicsManager.WORLD_TO_BOX, height
						/ 2f * PsychicsManager.WORLD_TO_BOX), });
		return chainShape;
	}

	@Override
	public void configure(Map<String, Object> map) {
		super.configure(map);
		trapeziumOffsetX = Integer.parseInt((String) map
				.get("trapezium-offsetX"));
	}

}
