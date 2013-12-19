package vn.sunnet.game.components;

import java.util.HashMap;
import java.util.Map;

import vn.sunnet.game.entities.Component;
import vn.sunnet.game.entities.ComponentRenderInterface;
import vn.sunnet.game.manager.PrefabManager;
import vn.sunnet.game.manager.ResourceManager;
import vn.sunnet.game.utils.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

public class AnimatedPlayerSpriteComponent extends Component implements
		ComponentRenderInterface {

	PlayerComponent component;

	private static final String TAG = "AnimatedSpriteComponent";
	protected float offsetX = 0.0f;
	protected float offsetY = 0.0f;
	private boolean flipX = false;
	private boolean flipY = false;
	private Map<String, Animation> animations;
	protected Animation currentBodyAnimation;
	protected Animation currentHeadAnimation;
	float time = 0.0f;

	public AnimatedPlayerSpriteComponent() {
		super();
		animations = new HashMap<String, Animation>();

	}

	@Override
	public void setup() {
		component = (PlayerComponent) getOwner().getComponent(
				PlayerComponent.class);
	}

	@Override
	public void render(SpriteBatch batch) {
		if (currentBodyAnimation != null && currentHeadAnimation != null) {
			TextureRegion regionBody = null;
			TextureRegion regionHead = null;
			float time = 0;
			boolean looping = false;

			if (component.alive) {
				switch (component.state_display) {
				case DISPLAY_JUMP:
					time = component.stateTimeJump;
					looping = false;
					break;
				case DISPLAY_SIT:
					time = component.stateTimeSit;
					looping = false;
					break;
				case DISPLAY_FALL:
					time = component.stateTimeFall;
					looping = false;
					break;
				case DISPLAY_RUN:
					time = component.stateTimeRun;
					looping = true;
					break;

				default:
					break;
				}

			} else {
				time = component.stateTimeDie;
				looping = false;
			}

			regionBody = currentBodyAnimation.getKeyFrame(time, looping);
			regionHead = currentHeadAnimation.getKeyFrame(time, looping);
			Position pos = getOwner().getPosition();

			batch.draw(regionHead.getTexture(), pos.x + offsetX, pos.y
					+ offsetY, 0f, 0f, regionHead.getRegionWidth(), regionHead
					.getRegionHeight(), 1.0f, 1.0f, getOwner().getRotation(),
					regionHead.getRegionX(), regionHead.getRegionY(),
					regionHead.getRegionWidth(), regionHead.getRegionHeight(),
					isFlipX(), isFlipY());

			batch.draw(regionBody.getTexture(), pos.x + offsetX, pos.y
					+ offsetY, 0f, 0f, regionBody.getRegionWidth(), regionBody
					.getRegionHeight(), 1.0f, 1.0f, getOwner().getRotation(),
					regionBody.getRegionX(), regionBody.getRegionY(),
					regionBody.getRegionWidth(), regionBody.getRegionHeight(),
					isFlipX(), isFlipY());
		}
	}

	public void play(String stringBody, String stringHead) {
		this.currentBodyAnimation = animations.get(stringBody);
		this.currentHeadAnimation = animations.get(stringHead);
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
//				 Gdx.app.log(TAG, "Ten..." + element.getAttribute("region"));
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

			// if (count > 0) {
			// currentBodyAnimation = animations
			// .get(animations.keySet().toArray()[0]);
			//
			// }
		}
	}

}
