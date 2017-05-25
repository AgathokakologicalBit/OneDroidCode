package com.timewarp.games.onedroidcode.scenes;

import com.badlogic.gdx.Gdx;
import com.timewarp.engine.Math.Mathf;
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
import com.timewarp.games.onedroidcode.level.levels.basic.SpiralBlockLevel;
import com.timewarp.games.onedroidcode.level.levels.normal.WallFollowingLevel;
import com.timewarp.games.onedroidcode.level.levels.normal.WinkingGateLevel;

import java.util.LinkedList;


public class MainMenu extends Scene {

    private LinkedList<LinkedList<Level>> levels;
    private int levelsCount;

    private UITextbox[] levelTypeLabels;
    private UIButton[] levelButtons;

    private float scroll;
    private float maxScroll;

    private final boolean WIPE_LEVEL_STATISTICS = false;


    @Override
    public void initialize() {
        super.initialize();

        // =- CREATE AND FILL EXISTING LEVELS ARRAY -=
        levels = new LinkedList<LinkedList<Level>>();

        final LinkedList<Level> basicLevels = new LinkedList<Level>();
        basicLevels.add(new SpiralBlockLevel());

        final LinkedList<Level> normalLevels = new LinkedList<Level>();
        normalLevels.add(new WallFollowingLevel());
        normalLevels.add(new WinkingGateLevel());

        final LinkedList<Level> hardLevels = new LinkedList<Level>();

        levels.add(basicLevels);
        levels.add(normalLevels);
        levels.add(hardLevels);

        levelsCount = basicLevels.size() + normalLevels.size() + hardLevels.size();


        // =- INSTANTIATE ALL UI ELEMENTS -=
        levelTypeLabels = new UITextbox[]{
                GameObject.instantiate(UITextbox.class),
                GameObject.instantiate(UITextbox.class),
                GameObject.instantiate(UITextbox.class)
        };
        levelTypeLabels[0].text.set("Basic levels");
        levelTypeLabels[1].text.set("Standard levels");
        levelTypeLabels[2].text.set("Challenging levels");


        if (WIPE_LEVEL_STATISTICS) {
            for (LinkedList<Level> levelsDifficultyGroup : levels) {
                for (Level level : levelsDifficultyGroup) {
                    AssetManager.preferences.putInteger(level.getSId(), 0);
                }
            }

            AssetManager.preferences.flush();
        }


        levelButtons = new UIButton[levelsCount];
        int index = 0;
        for (LinkedList<Level> levelsDifficultyGroup : levels) {
            for (Level level : levelsDifficultyGroup) {
                final UIButton button = GameObject.instantiate(UIButton.class);
                button.backgroundImage.image = level.getTexture();
                button.panel.setActive(false);
                levelButtons[index++] = button;

                final UITextbox score = GameObject.instantiate(UITextbox.class);
                score.transform.setParent(button.transform);
                score.text.setAlignment(true);
                score.text.set(
                        Integer.toString(
                                AssetManager.preferences.getInteger(
                                        level.getSId(), 0
                                )
                        )
                );

                if ("0".equals(score.text.get()))
                    score.text.set("-");
            }
        }

        Gdx.gl.glClearColor(0.137f, 0.333f, 0.553f, 1f);
    }

    @Override
    public void onResolutionChanged() {
        final int MARGIN = 30;
        final int GROUPS_SPACING = 50;

        final int isize = Math.min(GUI.Width, GUI.Height) / 3;
        final Vector2D size = new Vector2D(isize, isize);
        final Vector2D pos = new Vector2D(50, 20);

        int index = 0;
        int tid = 0;
        for (LinkedList<Level> levelDifficultyGroup : levels) {
            final UITextbox label = levelTypeLabels[tid++];
            label.transform.moveTo(pos);
            label.transform.setScale(new Vector2D(GUI.Width, isize / 4));
            pos.set(50, pos.y + isize / 3);

            boolean lastCarriageReturn = true;
            for (Level level : levelDifficultyGroup) {
                final UIButton button = levelButtons[index++];

                lastCarriageReturn = false;
                if (pos.x + size.x >= GUI.Width - 10) {
                    pos.set(50, pos.y + size.y + MARGIN);
                    lastCarriageReturn = true;
                }

                button.transform.setScale(size);
                button.transform.moveTo(pos);

                final Transform score = button.transform.childs.get(0);
                score.moveTo(new Vector2D(
                        button.transform.position.x + 16,
                        button.transform.position.y + size.y * 7 / 10
                ));
                score.setScale(new Vector2D(size.x * 3 / 10, size.y * 3 / 10 - 16));

                pos.set(pos.x + size.x + MARGIN, pos.y);
            }

            if (!lastCarriageReturn) {
                pos.set(50, pos.y + size.y + GROUPS_SPACING);
            } else {
                pos.set(50, pos.y + GROUPS_SPACING);
            }
        }

        maxScroll = pos.y - GUI.Height;
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
        if (GUI.isTouched && GUI.isLastTouched) {
            scroll = Mathf.clamp(scroll + GUI.touchPosition.y - GUI.lastTouchPosition.y, -maxScroll, 0);
            GUI.moveCamera(0, scroll);
        }

        int index = 0;
        for (UIButton button : levelButtons) {
            if (button.isClicked()) {
                for (LinkedList<Level> group : levels) {
                    if (index >= group.size()) {
                        index -= group.size();
                        continue;
                    }

                    SceneManager.instance.loadScene(new GameGridScene(group.get(index)));
                    return;
                }
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
