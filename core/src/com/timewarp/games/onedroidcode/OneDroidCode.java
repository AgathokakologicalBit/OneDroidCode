package com.timewarp.games.onedroidcode;

import com.badlogic.gdx.ApplicationAdapter;
import com.timewarp.engine.SceneManager;

public class OneDroidCode extends ApplicationAdapter {
	private boolean destroyed;
	private SceneManager sceneManager;

	@Override
	public void create() {
		sceneManager = new SceneManager();
		destroyed = true;
		resume();
	}

	@Override
	public void render() {
		sceneManager.update();
		sceneManager.render();
	}

	@Override
	public void dispose() {
		sceneManager.dispose();
		destroyed = true;
	}

	@Override
	public void pause() {
		super.pause();
		sceneManager.pause();
	}

	@Override
	public void resume() {
		super.resume();

		if (SceneManager.instance == null)
			SceneManager.instance = sceneManager;

		if (destroyed) sceneManager.init();
		else sceneManager.resume();

		destroyed = false;
	}
}
