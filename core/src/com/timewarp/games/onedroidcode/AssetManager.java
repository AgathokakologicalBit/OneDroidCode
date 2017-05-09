package com.timewarp.games.onedroidcode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.animator.Animation;
import com.timewarp.engine.animator.AnimationStepData;
import com.timewarp.engine.animator.Animator;
import com.timewarp.engine.gui.GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AssetManager {

    public static Preferences preferences;


    // ===--- TEXTURES LIST ---===
    public static TextureRegion playerTexture;

    // =- FLOOR TILES -=
    public static TextureRegion floorGrassTexture;

    // =- WALL TILES -=
    public static TextureRegion wallStoneTexture;


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
        floorGrassTexture = gameTextures[1][0];
        wallStoneTexture = gameTextures[2][0];

        Logger.getAnonymousLogger().log(Level.INFO, "Successfully loaded all assets");
    }

    private static void generateAnimations() {
        // ===---   MOVEMENT ANIMATIONS   ---===
        addMovementAnimation(Direction.UP);
        addMovementAnimation(Direction.RIGHT);
        addMovementAnimation(Direction.DOWN);
        addMovementAnimation(Direction.LEFT);

        // ===---   ROTATION ANIMATIONS   ---===
        addRotationAnimation(Direction.RIGHT);
        addRotationAnimation(Direction.LEFT);
    }

    private static void addMovementAnimation(Direction dir) {
        final Animation animation = new Animation(
                "move_" + dir.toString(),
                Animation.FROM_START_POINT,
                Animation.MODE_RELATIVE
        );

        final AnimationStepData step = new AnimationStepData(
                dir.getVector(),
                new Vector2D()
        );
        animation.addStep(1f, step);

        Animator.add(animation);
    }

    private static void addRotationAnimation(Direction dir) {
        final Animation animation = new Animation(
                "rotate_" + dir.toString(),
                Animation.FROM_START_POINT,
                Animation.MODE_RELATIVE
        );

        final AnimationStepData step = new AnimationStepData(
                new Vector2D(),
                new Vector2D(),
                dir.toRadians()
        );
        animation.addStep(1f, step);

        Animator.add(animation);
    }
}
