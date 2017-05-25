package com.timewarp.games.onedroidcode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.animator.Animation;
import com.timewarp.engine.animator.AnimationStepData;
import com.timewarp.engine.animator.Animator;
import com.timewarp.engine.gui.GUI;
import com.timewarp.games.onedroidcode.level.LevelGrid;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AssetManager {

    public static Preferences preferences;
    private static TextureRegion[][] gameTextures;


    // ===--- TEXTURES LIST ---===
    public static TextureRegion playerTexture;
    public static TextureRegion collectibleTexture;

    // =- LEVELS TEXTURES -=
    public static TextureRegion levelWallFollowerTexture;
    public static TextureRegion levelWinkingGateTexture;
    public static TextureRegion levelOnOneLineTexture;
    public static TextureRegion levelSpiralBlockTexture;

    // =- FLOOR TILES -=
    public static TextureRegion floorGrassTexture;

    // =- WALL TILES -=
    public static TextureRegion wallStoneTexture;


    // =- EDITOR TEXTURES -=
    public static TextureRegion rootNodeTexture;

    public static TextureRegion groupFlowTexture;
    public static TextureRegion groupRobotTexture;
    public static TextureRegion groupRobotSensorsTexture;

    public static TextureRegion flowIfTexture;
    public static TextureRegion flowWhileTexture;

    public static TextureRegion robotMovementForwardTexture;
    public static TextureRegion robotMovementTexture;
    public static TextureRegion robotRotationRightTexture;
    public static TextureRegion robotRotationLeftTexture;
    public static TextureRegion robotRotationTexture;

    public static TextureRegion robotSensorBlockTexture;


    public static void unloadAssets()
    {
        GUI.emptyTexture.getTexture().dispose();

        preferences.flush();
    }

    public static void loadAssets()
    {
        Logger.getAnonymousLogger().log(Level.INFO, "Loading assets");
        preferences = Gdx.app.getPreferences("prefs");

        generateAnimations();

        final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/tiles.png")));
        gameTextures = textureRegion.split(32, 32);


        // =- MAIN TEXTURES -=
        playerTexture = gameTextures[0][0];
        collectibleTexture = gameTextures[0][1];

        // =- LEVELS ICON -=
        levelWallFollowerTexture = getLevelTexture(0, 0);
        levelWinkingGateTexture = getLevelTexture(1, 0);
        levelOnOneLineTexture = getLevelTexture(2, 0);
        levelSpiralBlockTexture = getLevelTexture(0, 1);


        // =- FLOOR AND WALLS TEXTURE
        floorGrassTexture = gameTextures[1][0];
        wallStoneTexture = gameTextures[2][0];


        // ===---   EDITOR TEXTURES   ---===
        rootNodeTexture = getEditorHRTexture(0, 3);

        groupFlowTexture = getEditorHRTexture(0, 0);
        groupRobotTexture = getEditorHRTexture(1, 0);
        groupRobotSensorsTexture = getEditorHRTexture(2, 0);

        flowIfTexture = getEditorHRTexture(0, 2);
        flowWhileTexture = getEditorHRTexture(1, 2);

        robotMovementForwardTexture = getEditorHRTexture(3, 1);
        robotMovementTexture = getEditorHRTexture(0, 1);
        robotRotationRightTexture = getEditorHRTexture(4, 1);
        robotRotationLeftTexture = getEditorHRTexture(5, 1);
        robotRotationTexture = getEditorHRTexture(1, 1);

        robotSensorBlockTexture = getEditorHRTexture(2, 1);


        Logger.getAnonymousLogger().log(Level.INFO, "Successfully loaded all assets");
    }

    private static TextureRegion getEditorHRTexture(int x, int y) {
        final TextureRegion region = gameTextures[4 + y * 2][x * 2];
        region.setRegionWidth(64);
        region.setRegionHeight(64);
        return region;
    }

    private static TextureRegion getLevelTexture(int x, int y) {
        final TextureRegion region = gameTextures[12 + y * 8][x * 8];
        region.setRegionWidth(256);
        region.setRegionHeight(256);
        return region;
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

        // ===---   SPECIAL ANIMATIONS   ---===
        addCollectibleAnimation();
        addTextGameoverAnimation();
    }

    private static void addMovementAnimation(Direction dir) {
        final Animation animation = new Animation(
                "move_" + dir.toString(),
                Animation.FROM_START_POINT,
                Animation.MODE_RELATIVE
        );

        final AnimationStepData step = new AnimationStepData(
                dir.getVector().mult(LevelGrid.TILE_SIZE),
                new Vector2D()
        );
        animation.addStep(0.5f, step);

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
        animation.addStep(0.5f, step);

        Animator.add(animation);
    }

    private static void addCollectibleAnimation() {
        final Animation animation = new Animation(
                "collectible",
                Animation.FROM_CUSTOM_POINT,
                Animation.MODE_ABSOLUTE
        );

        AnimationStepData stepLeft = new AnimationStepData(
                null, null,
                -Mathf.PI / 6
        );
        animation.addStep(0f, stepLeft);

        AnimationStepData stepRight = new AnimationStepData(
                null, null,
                +Mathf.PI / 6
        );
        animation.addStep(0.4f, stepRight);
        animation.addStep(0.8f, stepLeft.copy());


        Animator.add(animation);
    }

    private static void addTextGameoverAnimation() {
        final Animation animation = new Animation(
                "text_gameover",
                Animation.FROM_START_POINT,
                Animation.MODE_RELATIVE
        );

        final AnimationStepData step = new AnimationStepData(
                new Vector2D(-GUI.Width / 3, -GUI.Height / 3),
                new Vector2D(GUI.Width * 2 / 3, GUI.Height * 2 / 3),
                0
        );
        animation.addStep(0.5f, step);

        Animator.add(animation);
    }

    public static TextureRegion getTexture(String name) {
        if ("act/root".equals(name)) return rootNodeTexture;

        if ("group/flow".equals(name)) return groupFlowTexture;
        if ("group/robot".equals(name)) return groupRobotTexture;
        if ("group/robot/control".equals(name)) return groupRobotTexture;
        if ("group/robot/sensors".equals(name)) return groupRobotSensorsTexture;


        if ("act/flow/loopnode".equals(name)) return flowWhileTexture;
        if ("act/flow/ifnode".equals(name)) return flowIfTexture;
        if ("act/flow/whileloopnode".equals(name)) return flowWhileTexture;

        if ("act/robot/control/movementforwardnode".equals(name))
            return robotMovementForwardTexture;
        if ("act/robot/control/movementnode".equals(name)) return robotMovementTexture;
        if ("act/robot/control/rotationrightnode".equals(name)) return robotRotationRightTexture;
        if ("act/robot/control/rotationleftnode".equals(name)) return robotRotationLeftTexture;
        if ("act/robot/control/rotationnode".equals(name)) return robotRotationTexture;

        if ("act/robot/sensors/blocksensornode".equals(name)) return robotSensorBlockTexture;

        if ("level/wall_follower".equals(name)) return levelWallFollowerTexture;
        if ("level/winking_gate".equals(name)) return levelWinkingGateTexture;
        if ("level/on_one_line".equals(name)) return levelOnOneLineTexture;
        if ("level/spiral_block".equals(name)) return levelSpiralBlockTexture;

        return null;
    }
}
