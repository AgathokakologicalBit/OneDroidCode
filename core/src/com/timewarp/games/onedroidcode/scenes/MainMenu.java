package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UIButton;
import com.timewarp.engine.gui.controls.UIPanel;
import com.timewarp.engine.gui.controls.UITextbox;


public class MainMenu extends Scene {

    private UIButton buttonStatToggle;

    private UIPanel panelStats;
    private UITextbox textboxFps;
    private UITextbox textboxDeltaTime;

    private final String textOpened = "X";
    private final String textClosed = "O";

    private final String fpsFieldName = "Frames per second: ";
    private final String deltaFieldName = "Delta time: ";

    @Override
    public void initialize() {
        super.initialize();

        Time.addCountdownRepeated("stats_update",  1.0);
    }

    @Override
    public void onResolutionChanged() {
        panelStats = GameObject.instantiate(UIPanel.class);
        panelStats.transform.moveTo(new Vector2D(50, 50));
        panelStats.transform.setScale(new Vector2D(GUI.Width - 100, GUI.Height - 100));
        panelStats.setActive(false);

        textboxFps = GameObject.instantiate(UITextbox.class);
        textboxFps.transform.moveTo(new Vector2D(50, 50));
        textboxFps.transform.setScale(new Vector2D(400, 30));
        textboxFps.text.set(fpsFieldName + 0);
        textboxFps.text.setTextSize(26);
        textboxFps.transform.setParent(panelStats.transform);

        textboxDeltaTime = GameObject.instantiate(UITextbox.class);
        textboxDeltaTime.transform.moveTo(new Vector2D(50, 125));
        textboxDeltaTime.transform.setScale(new Vector2D(400, 30));
        textboxDeltaTime.text.set(deltaFieldName + 0);
        textboxDeltaTime.text.setTextSize(26);
        textboxDeltaTime.transform.setParent(panelStats.transform);

        buttonStatToggle = GameObject.instantiate(UIButton.class);
        buttonStatToggle.transform.position.set(GUI.Width - 150, 50);
        buttonStatToggle.transform.scale.set(100, 100);
        buttonStatToggle.text.set(textClosed);
        buttonStatToggle.text.setTextSize(24);
        buttonStatToggle.text.setTextAlignment(true);
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update(double deltaTime) {
        if (buttonStatToggle.isClicked()) {
            if (textClosed.equals(buttonStatToggle.text.get())) {
                buttonStatToggle.text.set(textOpened);
                panelStats.setActive(true);

            } else {
                buttonStatToggle.text.set(textClosed);
                panelStats.setActive(false);
            }
        }

        if (Time.isTimerActivated("stats_update")) {
            this.textboxFps.text.set(fpsFieldName + Time.getFps());
            this.textboxDeltaTime.text.set(deltaFieldName + (int)(Time.getDeltaTime() * 1000));
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
