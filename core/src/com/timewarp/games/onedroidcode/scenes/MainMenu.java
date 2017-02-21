package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.Time;
import com.timewarp.engine.Timer;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.Button;
import com.timewarp.engine.gui.controls.Panel;
import com.timewarp.engine.gui.controls.Textbox;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MainMenu extends Scene {

    private Button buttonStatToggle;

    private Panel panelStats;
    private Textbox textboxFps;

    @Override
    public void initialize() {
        super.initialize();

        Time.addCountdownRepeated("stats_update",  1.0);
    }

    @Override
    public void onResolutionChanged() {
        buttonStatToggle = new Button();
        buttonStatToggle.transform.position.set(100, 100);
        buttonStatToggle.transform.scale.set(400, 100);
        buttonStatToggle.text = "SHOW STATS";
        this.controls.add(buttonStatToggle);

        panelStats = new Panel(
                new Vector2D(50, 50),
                new Vector2D(GUI.Width - 100, GUI.Height - 100)
        );
        //panelStats.setActive(false);

        textboxFps = new Textbox();
        textboxFps.transform.position.set(500, 100);
        textboxFps.transform.scale.set(400, 100);
        textboxFps.text = "FPS: ";

        panelStats.controls.add(textboxFps);

        this.controls.add(panelStats);
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update(double deltaTime) {
        if (buttonStatToggle.isClicked()) {
            if ("SHOW STATS".equals(buttonStatToggle.text)) {
                buttonStatToggle.text = "HIDE STATS";
                panelStats.setActive(true);

            } else {
                buttonStatToggle.text = "SHOW STATS";
                panelStats.setActive(false);
            }
        }

        if (Time.isTimerActivated("stats_update")) {
            Logger.getAnonymousLogger().log(Level.INFO, Boolean.toString(this.textboxFps.isActive()));
            this.textboxFps.text = "FPS: " + Time.getFps();
            this.textboxFps.text = "DELTA-TIME: " + (int) (Time.getDeltaTime() * 1000);
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
