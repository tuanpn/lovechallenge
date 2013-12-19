package vn.sunnet.game.screens;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

	private OrthographicCamera camera = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	private TweenManager tweenManager = new TweenManager();

	public SplashScreen() {

		float wpw = 1f;
		float wph = wpw * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();

		camera.viewportWidth = wpw;
		camera.viewportHeight = wph;
		camera.update();

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
	}

}
