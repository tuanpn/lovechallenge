package vn.sunnet.game.components;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.ComponentRenderInterface;
import vn.sunnet.game.utils.Position;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class SpriteComponent extends Component implements
		ComponentRenderInterface {

	protected TextureRegion region;
	private Sprite sprite;
	protected float offsetX = 0.0f;
	protected float offsetY = 0.0f;

	@Override
	public void render(SpriteBatch batch) {
		if (region != null) {
			Position pos = getOwner().getPosition();
			this.sprite.setPosition(pos.x + offsetX, pos.y + offsetY);
			this.sprite.setRotation(getOwner().getRotation());
			this.sprite.draw(batch);
		}
	}

	public void setTexture(TextureRegion region) {
		this.region = region;
		this.getOwner().setWidth(region.getRegionWidth());
		this.getOwner().setHeight(region.getRegionHeight());

		this.sprite = new Sprite(region);
	}

	@Override
	public void onRemove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
