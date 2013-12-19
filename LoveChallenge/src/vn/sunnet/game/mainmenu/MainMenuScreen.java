package vn.sunnet.game.mainmenu;

import vn.sunnet.accessors.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenuScreen implements Screen {

	private OrthographicCamera camera = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	private TweenManager tweenManager = new TweenManager();

	private Stage stage;
	private TextButton playButton;

	public MainMenuScreen() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Tween.registerAccessor(Actor.class, new ActorAccessor());
	}

	@Override
	public void render(float delta) {
		tweenManager.update(delta);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		camera = (OrthographicCamera) stage.getCamera();

		playButton = ResourceManagerMainMenu.shared().playButton;
		stage.addActor(playButton);

		TweenCallback callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (type == TweenCallback.COMPLETE) {

				}
			}
		};

		Timeline timeline = Timeline.createSequence();

		Tween.to(playButton, ActorAccessor.Position, 1.0f).target(100, 200)
				.start(tweenManager);
	}

	@Override
	public void show() {

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
		stage.dispose();
	}

}
