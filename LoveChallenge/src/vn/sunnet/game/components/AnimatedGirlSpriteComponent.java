package vn.sunnet.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AnimatedGirlSpriteComponent extends AnimatedSpriteComponent {

	GirlComponent girlComponent;

	public AnimatedGirlSpriteComponent() {
		super();
		girlComponent = (GirlComponent) getOwner().getComponent(
				GirlComponent.class);
	}

	@Override
	public void render(SpriteBatch batch) {
		if (girlComponent != null) {
			
		}
	}

}
