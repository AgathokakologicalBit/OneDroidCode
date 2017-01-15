package com.timewarp.games.onedroidcode;

import com.timewarp.engine.gui.GUI;

public class AssetManager {

    public static void unloadAssets()
    {
        GUI.emptyTexture.dispose();
    }

    public static void loadAssets()
    {
        generateAnimations();
    }

    private static void generateAnimations()
    {

    }
}
