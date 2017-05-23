package com.timewarp.games.onedroidcode.scenes;

import com.badlogic.gdx.Gdx;
import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.entities.Transform;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UIButton;
import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.level.Level;
import com.timewarp.games.onedroidcode.level.levels.dynamic.WallFollowingLevel;

import java.util.ArrayList;


public class MainMenu extends Scene {

    private ArrayList<Level> basicLevels;
    private ArrayList<Level> dynamicLevels;


    private UITextbox basicLevelsLabel;
    private UITextbox dynamicLevelsLabel;
    private UIButton[] basicLevelButtons;
    private UIButton[] dynamicLevelButtons;


    @Override
    public void initialize() {
        super.initialize();

        // =- CREATE AND FILL EXISTING LEVELS ARRAY -=
        basicLevels = new ArrayList<Level>();
        dynamicLevels = new ArrayList<Level>();

        basicLevels.add(new WallFollowingLevel());


        // =- INSTANTIATE ALL UI ELEMENTS -=
        basicLevelsLabel = GameObject.instantiate(UITextbox.class);
        basicLevelsLabel.text.set("Basic levels");
        dynamicLevelsLabel = GameObject.instantiate(UITextbox.class);
        dynamicLevelsLabel.text.set("Dynamic levels");

        basicLevelButtons = new UIButton[basicLevels.size()];
        for (int i = 0; i < basicLevelButtons.length; ++i) {
            final UIButton button = GameObject.instantiate(UIButton.class);
            button.backgroundImage.image = AssetManager.getTexture("level/" + basicLevels.get(i).getSId());
            basicLevelButtons[i] = button;

            final UITextbox score = GameObject.instantiate(UITextbox.class);
            score.transform.setParent(basicLevelButtons[i].transform);
            score.text.setAlignment(true);
            score.text.set(
                    Integer.toString(
                            AssetManager.preferences.getInteger(
                                    basicLevels.get(i).getSId(), 0
                            )
                    )
            );
        }

        Gdx.gl.glClearColor(0.137f, 0.333f, 0.553f, 1f);
    }

    @Override
    public void onResolutionChanged() {
        basicLevelsLabel.transform.moveTo(new Vector2D(50, 30));
        basicLevelsLabel.transform.setScale(new Vector2D(GUI.Width - 40, 100));

        final int MARGIN = 30;

        final int isize = (Math.min(GUI.Width, GUI.Height) - MARGIN * 3) / 2;
        final Vector2D size = new Vector2D(isize, isize);
        final Vector2D pos = new Vector2D(50, 150);

        for (UIButton button : basicLevelButtons) {
            if (pos.x + size.x >= GUI.Width - 10) {
                pos.set(50, pos.y + size.y + MARGIN);
            }

            button.transform.setScale(size);
            button.transform.moveTo(pos);

            final Transform score = button.transform.childs.get(0);
            score.moveTo(new Vector2D(
                    button.transform.position.x + 16,
                    button.transform.position.y + size.y * 7 / 10
            ));
            score.setScale(new Vector2D(size.x * 3 / 10, size.y * 3 / 10 - 16));
        }
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
        int index = 0;
        for (UIButton button : basicLevelButtons) {
            if (button.isClicked()) {
                SceneManager.instance.loadScene(new GameGridScene(basicLevels.get(index)));
            }
            ++index;
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
