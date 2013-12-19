package vn.sunnet.game.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.ComponentRenderInterface;
import vn.sunnet.game.manager.PrefabManager;
import vn.sunnet.game.manager.ResourceManager;
import vn.sunnet.game.utils.Position;

public class AnimatedSpriteComponent extends Component implements
		ComponentRenderInterface {

	private static final String TAG = "AnimatedSpriteComponent";
	protected float offsetX = 0.0f;
	protected float offsetY = 0.0f;
	private boolean flipX = false;
	private boolean flipY = false;
	private Map<String, Animation> animations;
	protected Animation currentAnimation;
	float time = 0.0f;

	public AnimatedSpriteComponent() {
		super();
		animations = new HashMap<String, Animation>();
	}

	@Override
	public void render(SpriteBatch batch) {
		if (currentAnimation != null) {
			TextureRegion region = currentAnimation
					.getKeyFrame(time += Gdx.graphics.getDeltaTime());
			Position pos = getOwner().getPosition();

			batch.draw(region.getTexture(), pos.x + offsetX, pos.y + offsetY,
					0f, 0f, region.getRegionWidth(), region.getRegionHeight(),
					1.0f, 1.0f, getOwner().getRotation(), region.getRegionX(),
					region.getRegionY(), region.getRegionWidth(),
					region.getRegionHeight(), isFlipX(), isFlipY());
		}
	}

	public void play(String string) {
		this.currentAnimation = animations.get(string);
	}

	public boolean isFlipX() {
		return flipX;
	}

	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	public boolean isFlipY() {
		return flipY;
	}

	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
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

	@Override
	public void configure(Map<String, Object> map) {
		if (map.containsKey("offset-x")) {
			offsetX = Float.parseFloat((String) map.get("offset-x"));
		}
		if (map.containsKey("offset-y")) {
			offsetY = Float.parseFloat((String) map.get("offset-y"));
		}

		if (map.containsKey(PrefabManager.EXTRA_PAYLOAD)) {
			@SuppressWarnings("unchecked")
			Array<Element> listElement = (Array<Element>) map
					.get(PrefabManager.EXTRA_PAYLOAD);
			int count = listElement.size;
			for (int i = 0; i < count; i++) {
				Element element = listElement.get(i);
				TextureAtlas atlas = ResourceManager.shared().getAtlas(
						element.getAttribute("atlas"));
				Array<AtlasRegion> regions = atlas.findRegions(element
						.getAttribute("region"));
				Gdx.app.log(TAG, "Ten..." + element.getAttribute("region"));
				Animation anim = new Animation(Float.parseFloat(element
						.getAttribute("duration")), regions);

				switch (element.getAttribute("type")) {
				case "NORMAL":
					anim.setPlayMode(Animation.NORMAL);
					break;

				case "LOOP":
					anim.setPlayMode(Animation.LOOP);
					break;

				default:
					Gdx.app.log(
							TAG,
							"Unsuported animation type: "
									+ element.getAttribute("type"));
					break;
				}
				animations.put(element.getAttribute("name"), anim);
			}

			if (count > 0) {
				currentAnimation = animations
						.get(animations.keySet().toArray()[0]);
			}
		}

	}

}
