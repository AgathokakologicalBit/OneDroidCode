package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.controls.UIButton;


public class MainMenu extends Scene {

    UIButton buttonPlay2D;


    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void onResolutionChanged() {
        buttonPlay2D = GameObject.instantiate(UIButton.class);
        buttonPlay2D.transform.position.set(50, 50);
        buttonPlay2D.transform.scale.set(500, 100);
        buttonPlay2D.text.setTextSize(30);
        buttonPlay2D.text.set("Play `grid2D` game");
        buttonPlay2D.text.setTextAlignment(true);
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update(double deltaTime) {
        if (buttonPlay2D.isClicked()) {
            SceneManager.instance.loadScene(new GameGridScene());
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
