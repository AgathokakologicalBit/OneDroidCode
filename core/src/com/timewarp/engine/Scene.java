package com.timewarp.engine;

import com.timewarp.engine.gui.GUIControl;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    /**
     * List of UI elements, that scene have
     */
    protected List<GUIControl> controls;


    /**
     * Initializes all properties of Scene
     * And prepares resources
     */
    public void initialize() {
        this.controls = new ArrayList<GUIControl>(50);
    }


    /**
     * Running if resolution was changed
     * Resize on PC
     * Screen rotation on Phone
     */
    public abstract void onResolutionChanged();

    /**
     * Loads all resources, that will be used in Scene
     */
    public abstract void loadResources();

    /**
     * Frees all loaded resources
     */
    public void unloadResources() {
        for (GUIControl control : controls) {
            control.dispose();
        }
    }


    /**
     * Running each frame if Scene is loaded
     * Recalculates states of all active objects
     *
     * @param deltaTime Time passed between last and current frames
     */
    public abstract void update(double deltaTime);

    /**
     * Running each frame if Scene is loaded
     * Renders all active objects on screen
     */
    public abstract void render();


    /**
     * Runs when user or system pause application
     */
    public abstract void pause();

    /**
     * Runs when application was resumed
     */
    public abstract void resume();

    /**
     * Runs if back was requested
     * Closes application if returned value is TRUE
     * Continues on FALSE
     *
     * @return can app be closed
     */
    public boolean onBackRequest() {
        return true;
    }
}