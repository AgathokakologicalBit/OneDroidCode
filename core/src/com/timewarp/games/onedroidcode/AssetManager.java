package com.timewarp.games.onedroidcode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.gui.GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AssetManager {

    public static Preferences preferences;


    // ===--- TEXTURES LIST ---===
    public static TextureRegion playerTexture;

    // =- FLOOR TILES -=
    public static TextureRegion floorStoneTexture;

    // =- WALL TILES -=
    public static TextureRegion wallYellowTexture;


    public static void unloadAssets()
    {
        GUI.emptyTexture.dispose();

        preferences.flush();
    }

    public static void loadAssets()
    {
        Logger.getAnonymousLogger().log(Level.INFO, "Loading assets");
        preferences = Gdx.app.getPreferences("prefs");

        generateAnimations();

        final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/tiles.png")));
        final TextureRegion[][] gameTextures = textureRegion.split(32, 32);

        playerTexture = gameTextures[0][0];
        floorStoneTexture = gameTextures[1][0];
        wallYellowTexture = gameTextures[2][0];

        Logger.getAnonymousLogger().log(Level.INFO, "Successfully loaded all assets");
    }

    private static void generateAnimations()
    {

    }
}
