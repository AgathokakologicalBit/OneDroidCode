package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.GameStats;
import com.timewarp.games.onedroidcode.level.Level;

import java.util.Locale;


public class StatisticsScene extends Scene {

    private Level level;

    private UITextbox levelNameLabel;

    private UITextbox[] statisticLabels;
    private UITextbox finalScoreLabel;


    public StatisticsScene(Level level) {
        this.level = level;
    }

    @Override
    public void initialize() {
        super.initialize();
        levelNameLabel = GameObject.instantiate(UITextbox.class);
        levelNameLabel.text.set(String.format(
                Locale.US, "\'%s\' statistics",
                level.getName()
        ));
        levelNameLabel.text.setAlignment(true);

        statisticLabels = new UITextbox[]{
                GameObject.instantiate(UITextbox.class),
                GameObject.instantiate(UITextbox.class)
        };
        statisticLabels[0].text.set("Instructions count: " + GameStats.instructions);
        statisticLabels[1].text.set(String.format(
                Locale.US, "Processor cycles: %d / %d",
                GameStats.steps, GameStats.maxSteps
        ));

        finalScoreLabel = GameObject.instantiate(UITextbox.class);
        finalScoreLabel.text.set("Score: " + GameStats.score);
    }

    @Override
    public void onResolutionChanged() {
        levelNameLabel.transform.moveTo(new Vector2D(0, 20));
        levelNameLabel.transform.setScale(new Vector2D(GUI.Width, 100));

        for (int i = 0; i < statisticLabels.length; ++i) {
            statisticLabels[i].transform.moveTo(new Vector2D(30, 140 + i * 90));
            statisticLabels[i].transform.setScale(new Vector2D(GUI.Width - 60, 60));
        }

        finalScoreLabel.transform.moveTo(new Vector2D(20, GUI.Height - 100));
        finalScoreLabel.transform.setScale(new Vector2D(GUI.Width - 40, 100));
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {

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

    @Override
    public boolean onBackRequest() {
        SceneManager.instance.loadScene(new MainMenu());
        return false;
    }
}
