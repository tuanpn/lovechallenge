package vn.sunnet.mainmenu;

import vn.sunnet.accessors.ActorAccessor;
import vn.sunnet.accessors.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MainMenuScreen implements Screen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Stage stage;
	private TweenManager tweenManager;

	Sprite bg;
	Sprite layer2;
	Sprite textLoveChallege;
	Sprite halfCircle;
	Sprite touchToScreen;

	Button option;
	Button onMusic;
	Button offMusic;
	Button download;
	Button info;
	Button share;

	// hien thi mot so button
	boolean showButton;

	public MainMenuScreen() {
		stage = new Stage();
		tweenManager = new TweenManager();
		batch = new SpriteBatch();

		bg = PrefabMainMenu.shared().buildSprite("BG");
		bg.setPosition(0, 0);

		layer2 = PrefabMainMenu.shared().buildSprite("LAYER2");
		layer2.getColor().a = 0;

		halfCircle = PrefabMainMenu.shared().buildSprite("HALFCIRCLE");
		halfCircle.setPosition(-300, 0);

		textLoveChallege = PrefabMainMenu.shared().buildSprite(
				"TextLoveChallege");
		textLoveChallege.setPosition(-300, 400);

		option = PrefabMainMenu.shared().buildButton("OPTION");
		option.setPosition(-150, -150);
		onMusic = PrefabMainMenu.shared().buildButton("ONMUSIC");
		offMusic = PrefabMainMenu.shared().buildButton("OFFMUSIC");
		download = PrefabMainMenu.shared().buildButton("DOWNLOAD");
		info = PrefabMainMenu.shared().buildButton("INFO");
		share = PrefabMainMenu.shared().buildButton("SHARE");

		touchToScreen = PrefabMainMenu.shared().buildSprite("TOUCHTOSCREEN");
		touchToScreen.setPosition(400 - touchToScreen.getWidth() / 2,
				240 - touchToScreen.getHeight() / 2);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void update(float delta) {
		tweenManager.update(delta);
		stage.act();

		if (option.isChecked()) {
			showButton = !showButton;
			moveButton(showButton);
			option.setChecked(false);
		}
	}

	private void moveButton(boolean showButton2) {
		if (showButton2) {
			stage.addActor(onMusic);
			stage.addActor(download);
			stage.addActor(info);
			stage.addActor(share);
			Timeline.createSequence()
					.push(Tween.set(onMusic, ActorAccessor.Position).target(0,
							0))
					.push(Tween.set(download, ActorAccessor.Position).target(0,
							0))
					.push(Tween.set(share, ActorAccessor.Position).target(0, 0))
					.push(Tween.set(info, ActorAccessor.Position).target(0, 0))
					.beginParallel()
					.push(Tween.to(onMusic, ActorAccessor.Position, 0.5f)
							.target(10, 105))
					.push(Tween.to(download, ActorAccessor.Position, 0.5f)
							.target(65, 92))
					.push(Tween.to(share, ActorAccessor.Position, 0.5f).target(
							100, 50))
					.push(Tween.to(info, ActorAccessor.Position, 0.5f).target(
							95, 5)).end().start(tweenManager);
		} else {
			Timeline.createSequence()
					.push(Tween.set(onMusic, ActorAccessor.Position).target(10,
							105))
					.push(Tween.set(download, ActorAccessor.Position).target(
							65, 92))
					.push(Tween.set(share, ActorAccessor.Position).target(100,
							50))
					.push(Tween.set(info, ActorAccessor.Position).target(95, 5))
					.beginParallel()
					.push(Tween.to(onMusic, ActorAccessor.Position, 0.5f)
							.target(0, 0))
					.push(Tween.to(download, ActorAccessor.Position, 0.5f)
							.target(0, 0))
					.push(Tween.to(share, ActorAccessor.Position, 0.5f).target(
							0, 0))
					.push(Tween.to(info, ActorAccessor.Position, 0.5f).target(
							0, 0)).end().setCallback(new TweenCallback() {

						@Override
						public void onEvent(int type, BaseTween<?> source) {
							if (type == COMPLETE) {
								onMusic.remove();
								download.remove();
								share.remove();
								info.remove();
							}

						}
					}).start(tweenManager);

		}
	}

	private void draw() {
		batch.disableBlending();
		batch.begin();
		bg.draw(batch);
		batch.end();

		batch.enableBlending();
		batch.begin();
		layer2.draw(batch);
		textLoveChallege.draw(batch);
		halfCircle.draw(batch);
		touchToScreen.draw(batch);
		batch.end();

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

		stage.setViewport(800, 480, false);
		camera = (OrthographicCamera) stage.getCamera();
		camera.position.set(400, 240, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		stage.addActor(option);
	}

	@Override
	public void show() {
		Timeline.createSequence()
				.push(Tween.set(textLoveChallege, SpriteAccessor.POS_XY)
						.targetRelative(1, 0))
				.push(Tween.set(halfCircle, SpriteAccessor.POS_XY)
						.targetRelative(-100, -50))
				.push(Tween.set(option, ActorAccessor.Position).targetRelative(
						-1, 0))
				.push(Tween.set(layer2, SpriteAccessor.OPACITY).target(0))
				.push(Tween.set(touchToScreen, SpriteAccessor.OPACITY)
						.target(0))

				.pushPause(0.5f)
				.push(Tween.to(textLoveChallege, SpriteAccessor.POS_XY, 1f)
						.target(390, 280).ease(Back.OUT))
				.push(Tween.to(textLoveChallege, SpriteAccessor.POS_XY, 0.5f)
						.target(0, 390).ease(Back.INOUT))
				.push(Tween.to(textLoveChallege, SpriteAccessor.ROTATION, 1f)
						.target(360 * 2f).ease(Cubic.IN))

				.push(Tween.to(option, ActorAccessor.Position, 0.2f)
						.target(0, 0).ease(Back.OUT))

				.push(Tween.to(halfCircle, SpriteAccessor.POS_XY, 0.5f)
						.target(-20, -20).ease(Back.OUT))

				.pushPause(0.5f)
				.beginParallel()
				.push(Tween.to(textLoveChallege, SpriteAccessor.SCALE_XY, 0.5f)
						.target(0.5f, 0.5f).ease(Back.IN))
				.push(Tween.to(textLoveChallege, SpriteAccessor.OPACITY, 0.5f)
						.target(0.f))
				.push(Tween.to(option, ActorAccessor.OPACITY, 0.5f)
						.target(0.7f).ease(Back.OUT))
				.end()

				.pushPause(0.5f)
				.beginParallel()
				.push(Tween.to(layer2, SpriteAccessor.OPACITY, 3f).target(1)
						.ease(Back.OUT))
				.end()
				.push(Tween.to(option, ActorAccessor.OPACITY, 0.5f).target(1f)
						.ease(Cubic.OUT)).start(tweenManager);

		Timeline timeline = Timeline.createSequence();
		timeline.push(
				Tween.to(touchToScreen, SpriteAccessor.OPACITY, 3f)
						.target(0.8f))
				.push(Tween.to(touchToScreen, SpriteAccessor.OPACITY, 0.5f)
						.target(1).ease(Linear.INOUT)).repeat(-1, 0.f)
				.start(tweenManager);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		tweenManager.killAll();
		batch.dispose();
	}

}
