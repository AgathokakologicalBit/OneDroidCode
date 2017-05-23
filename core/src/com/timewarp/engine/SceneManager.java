package com.timewarp.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timewarp.engine.animator.Animator;
import com.timewarp.engine.configs.ProjectConfig;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.games.onedroidcode.AssetManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.badlogic.gdx.Gdx.input;

/**
 * Controls all scenes and handles system events
 */
public class SceneManager {

    /**
     * Current instance of SceneManager
     */
    public static SceneManager instance;

    private Scene currentScene;

    private OrthographicCamera orthographicCamera;
    // private PerspectiveCamera perspectiveCamera;

    private int fps = 0;
    private float fpsCheckTimer = 1f;

    private boolean backRequested = false;

    private boolean sceneJustLoaded = false;

    /**
     * Starts scene manager
     */
    public SceneManager() {
        SceneManager.instance = this;
    }

    /**
     * Initializes SceneManager, GUI, OpenGL, Animator and AssetManager
     */
    public void init() {
        Logger.getAnonymousLogger().log(Level.INFO, "[SceneMG] Initialization");
        Time.init();
        Time.addTimer("running_time");
        Time.addTimer("delta_time");

        input.setCatchBackKey(true);

        currentScene = null;
        GUI.init();

        GUI.Width = Gdx.graphics.getWidth();
        GUI.Height = Gdx.graphics.getHeight();

        // Create sprite batch and set up OpenGL
        GUI.batch = new SpriteBatch();
        Gdx.gl.glClearColor(
                ProjectConfig.BACKGROUND_COLOR.r,
                ProjectConfig.BACKGROUND_COLOR.g,
                ProjectConfig.BACKGROUND_COLOR.b,
                1
        );

        // Create new camera
        orthographicCamera = new OrthographicCamera(GUI.Width, GUI.Height);
        // perspectiveCamera = new PerspectiveCamera(70, GUI.Width, GUI.Height);

        // Load assets
        Animator.Init();
        AssetManager.loadAssets();

        // Load main scene
        loadStartScene();
    }

    /**
     * Loads starting application screen
     */
    public void loadStartScene() {
        try {
            loadScene(ProjectConfig.START_SCENE.getClass().newInstance());
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Can not load main scene");
            e.printStackTrace();
            throw new RuntimeException("Can not load main scene");
        }
    }

    /**
     * Loads specified application screen
     *
     * @param scene Scene to load
     */
    public void loadScene(Scene scene) {
        if (scene == null) {
            loadStartScene();
            return;
        }

        sceneJustLoaded = true;

        GUI.resetCameraPosition();

        Logger.getAnonymousLogger().log(Level.INFO, "[SceneMG] Loading scene '" + scene.getClass().getSimpleName() + "'");
        fpsCheckTimer = 1f;

        if (currentScene != null) {
            Logger.getAnonymousLogger().log(Level.INFO, "[SceneMG] Unloading old resources");
            currentScene.pause();
            currentScene.unloadResources();
        }

        Logger.getAnonymousLogger().log(Level.INFO, "[SceneMG] Initializing scene");
        Gdx.gl.glClearColor(
                ProjectConfig.BACKGROUND_COLOR.r,
                ProjectConfig.BACKGROUND_COLOR.g,
                ProjectConfig.BACKGROUND_COLOR.b,
                1
        );
        currentScene = scene;
        currentScene.initialize();

        Logger.getAnonymousLogger().log(Level.INFO, "[SceneMG] Loading scene resources");
        currentScene.loadResources();

        Logger.getAnonymousLogger().log(Level.INFO, "[SceneMG] Setting up camera");
        currentScene.onResolutionChanged();
    }

    /**
     * Runs every frame
     * Updates loaded scene and GUI
     */
    public void update() {
        sceneJustLoaded = false;

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!backRequested && currentScene.onBackRequest()) {
                Gdx.app.exit();
                return;
            }
            backRequested = true;
        } else backRequested = false;

        if (currentScene == null) loadStartScene();

        if (GUI.Width != Gdx.graphics.getWidth() || GUI.Height != Gdx.graphics.getHeight()) {
            GUI.Width = Gdx.graphics.getWidth();
            GUI.Height = Gdx.graphics.getHeight();

            currentScene.onResolutionChanged();
        }

        Time.update();
        fpsCheckTimer += Gdx.graphics.getDeltaTime();
        if (fpsCheckTimer >= 1f) {
            fpsCheckTimer = 0;
            fps = (int) (1f / Gdx.graphics.getDeltaTime());

            Logger.getAnonymousLogger().log(Level.FINEST, "FPS: " + fps);
        }


        updateCursorState();


        for (GameObject gameObject : currentScene.objects) {
            this.updateGameObject(gameObject);
        }

        currentScene.update();
        if (!sceneJustLoaded) {
            for (GameObject gameObject : currentScene.objects) {
                this.updatePostGameObject(gameObject);
            }
        }


        Time.resetTimer("delta_time");
    }

    private void updateGameObject(GameObject gameObject) {
        if (!gameObject.isActive()) return;
        gameObject.update();
    }

    private void updatePostGameObject(GameObject gameObject) {
        if (!gameObject.isActive()) return;
        gameObject.postUpdate();
    }

    private void updateCursorState() {
        final float deltaTime = Time.getDeltaTime();

        // Recalculate control mouse state
        GUI.lastTouchPosition.set(GUI.touchPosition);
        GUI.isLastTouched = GUI.isTouched;
        GUI.isTouched = input.isTouched();

        if (GUI.isTouched) {
            GUI.touchPosition.set(input.getX(), input.getY());
            if (!GUI.isLastTouched)
                GUI.touchStartPosition.set(GUI.touchPosition.copy());
        }

        if (GUI.isTouched) GUI.timeSinceTouchStart += deltaTime;
        else GUI.timeSinceTouchStart = 0;

        GUI.isClicked = GUI.isLastTouched && !GUI.isTouched;
        GUI.isLastLongClicked = GUI.isLongClicked;
        GUI.isLongClicked = GUI.isTouched && GUI.timeSinceTouchStart >= 0.5f && !GUI.isLastLongClicked;

        final Vector2D movement = GUI.touchStartPosition.sub(GUI.touchPosition);
        GUI.isLongClicked &= movement.getLengthSquared() <= 5 * 5;
        GUI.isClicked &= movement.getLengthSquared() <= 5 * 5;

        if (!GUI.isTouched) {
            GUI.touchStartPosition.set(-1, -1);
        }
    }


    /**
     * Runs every frame
     * Renders loaded scene and draws GUI
     */
    public void render() {
        if (currentScene == null) loadStartScene();
        if (sceneJustLoaded) return;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // update 3D camera and render scene
        // perspectiveCamera.update();

        // render GUI
        orthographicCamera.update();
        GUI.batch.begin();

        for (GameObject gameObject : currentScene.objects) {
            this.renderGameObject(gameObject);
        }

        GUI.beginStaticBlock();
        currentScene.render();
        GUI.endStaticBlock();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        GUI.batch.end();
    }

    private void renderGameObject(GameObject gameObject) {
        if (!gameObject.isActive()) return;

        if (gameObject.isStatic()) {
            GUI.beginStaticBlock();
            gameObject.render();
            GUI.endStaticBlock();
        } else {
            gameObject.render();
        }
    }

    /**
     * Frees all loaded assets
     */
    public void dispose() {
        AssetManager.unloadAssets();

        GUI.batch.dispose();
        GUI.font.dispose();

        if (currentScene == null) return;
        currentScene.unloadResources();
    }

    /**
     * Pauses application
     */
    public void pause() {
        if (currentScene == null) return;
        currentScene.pause();

        for (GameObject gameObject: currentScene.objects) {
            gameObject.animator.pause();
        }
    }

    /**
     * Resumes application
     */
    public void resume() {
        GUI.Width = Gdx.graphics.getWidth();
        GUI.Height = Gdx.graphics.getHeight();

        if (currentScene == null) {
            loadStartScene();
            return;
        }

        currentScene.resume();
        for (GameObject gameObject : currentScene.objects) {
            gameObject.animator.resume();
        }
    }

    /**
     * Adds gameobject to the scene
     * @param gameObject GameObject that will be added
     */
    public void addGameObject(GameObject gameObject) {
        if (gameObject == null) return;

        this.currentScene.objects.add(gameObject);
    }

    /**
     * Removes gameobject from scene
     *
     * @param gameObject GameObject that will ne deleted
     */
    public void removeGameObject(GameObject gameObject) {
        gameObject.isDestroyed = true;
        this.currentScene.objects.remove(gameObject);
    }

    public boolean objectExists(GameObject obj) {
        return this.currentScene.objects.contains(obj);
    }

    /**
     * Returns calculated FPS
     *
     * @return frames per second
     */
    public int getFPS() {
        return fps;
    }
}