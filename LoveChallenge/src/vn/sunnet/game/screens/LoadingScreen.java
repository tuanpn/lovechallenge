package vn.sunnet.game.screens;

import vn.sunnet.game.MainGame;
import vn.sunnet.game.manager.ResourceManager;
import vn.sunnet.mainmenu.MainMenuScreen;
import vn.sunnet.mainmenu.ResourceMainMenu;

import com.badlogic.gdx.Screen;

public class LoadingScreen implements Screen {

	MainGame game;
	int type;

	boolean isLoaded;

	public LoadingScreen(MainGame game, int type) {
		this.game = game;
		this.type = type;
	}

	@Override
	public void render(float delta) {

		if (game.getManager1().update() && game.getManager2().update()) {
			launch();
		}

	}

	private void launch() {
		switch (type) {
		case 0:
			ResourceMainMenu.shared().getAtlas(game.getManager1());
			ResourceMainMenu.shared().getSkin(game.getManager2());
			game.setScreen(new MainMenuScreen());
			break;
		case 1:
			ResourceManager.shared().getAtlas(game.getManager1());
			ResourceManager.shared().getTexture(game.getManager1());
			game.setScreen(new LevelScreen());
			break;

		default:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		switch (type) {
		case 0:
			ResourceMainMenu.shared().loadAtlas(game.getManager1());
			ResourceMainMenu.shared().loadSkin(game.getManager2());
			break;
		case 1:
			ResourceManager.shared().loadAtlas(game.getManager1());
			ResourceManager.shared().loadTexture(game.getManager1());
			break;

		default:
			break;
		}
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
		// TODO Auto-generated method stub

	}

}
