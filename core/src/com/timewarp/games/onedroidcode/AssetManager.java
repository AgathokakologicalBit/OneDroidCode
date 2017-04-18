package com.timewarp.games.onedroidcode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.timewarp.engine.gui.GUI;

public class AssetManager {

    public static Preferences preferences;

    public static void unloadAssets()
    {
        GUI.emptyTexture.dispose();

        preferences.flush();
    }

    public static void loadAssets()
    {
        preferences = Gdx.app.getPreferences("prefs");

        generateAnimations();
    }

    private static void generateAnimations()
    {

    }
}
