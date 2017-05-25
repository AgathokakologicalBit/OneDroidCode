package com.timewarp.games.onedroidcode.scenes;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.PictureBox;
import com.timewarp.engine.gui.controls.UIButton;
import com.timewarp.engine.gui.controls.UIPanel;
import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.GameStats;
import com.timewarp.games.onedroidcode.editor.GridNodeEditor;
import com.timewarp.games.onedroidcode.level.CourseInfo;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.level.LevelGridEmulator;
import com.timewarp.games.onedroidcode.objects.CollectibleItem;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;

import java.util.logging.Level;
import java.util.logging.Logger;


public class GameGridScene extends Scene {

    private UIPanel dataPanel;

    private UIButton openVslEditorButton;
    private UIButton switchPauseResumeVslButton;
    private UIButton startTestsButton;

    private UITextbox scoreTextbox;
    private UITextbox infoTextbox;


    private final int DATA_PANEL_WIDTH_P = 20; // 20% of screen width
    private final int BUTTON_MARGIN_P = 10; // 10% of panel'S width
    private final int BUTTON_SIZE_P = 100 - BUTTON_MARGIN_P * 2; // Calculated percentage of panel's width

    private int DATA_PANEL_WIDTH;
    private int BUTTON_MARGIN;
    private int BUTTON_SIZE;


    private CodeRunner codeRunner;
    private LevelGrid levelGrid;
    private com.timewarp.games.onedroidcode.level.Level level;

    private boolean isVslRunning;

    private int collectiblesCount;
    private boolean gameOver = false;
    private boolean isTesting = false;

    private int targetCourse = -1;

    private int levelMaxSteps;
    private int levelCurrentSteps;
    private int levelMinSteps;
    private LevelGridEmulator gridEmulator;


    public GameGridScene(com.timewarp.games.onedroidcode.level.Level level) {
        this.level = level;
        this.targetCourse = -1;
    }

    public GameGridScene(com.timewarp.games.onedroidcode.level.Level level, int targetCourse) {
        this.level = level;
        this.targetCourse = targetCourse;
    }


    @Override
    public void initialize() {
        super.initialize();

        Logger.getAnonymousLogger().log(Level.INFO, "[GameGridSC] Placing objects");

        loadLevel();
        initializeVSL();

        isVslRunning = false;
        VSLEditorScene.previousLevelScene = this;

        GameStats.reset();
        codeRunner.grid = levelGrid;
        codeRunner.reset();

        generateUI();
        gameOver = false;
        infoTextbox.setActive(false);

        levelMaxSteps = 0;
        levelMaxSteps = 0;
        levelCurrentSteps = 0;
    }

    private void generateUI() {
        dataPanel = GameObject.instantiate(UIPanel.class);
        dataPanel.setStatic(true);

        openVslEditorButton = GameObject.instantiate(UIButton.class);
        openVslEditorButton.setStatic(true);
        openVslEditorButton.text.setAlignment(true);
        openVslEditorButton.text.set("edit script");
        openVslEditorButton.text.setSize(24);

        switchPauseResumeVslButton = GameObject.instantiate(UIButton.class);
        switchPauseResumeVslButton.setStatic(true);
        switchPauseResumeVslButton.text.setAlignment(true);
        switchPauseResumeVslButton.text.set("start");
        switchPauseResumeVslButton.text.setSize(24);

        startTestsButton = GameObject.instantiate(UIButton.class);
        startTestsButton.setStatic(true);
        startTestsButton.text.setAlignment(true);
        startTestsButton.text.set("final tests");
        startTestsButton.text.setSize(24);


        final PictureBox picture = GameObject.instantiate(PictureBox.class);
        picture.transform.setScale(new Vector2D(
                LevelGrid.TILE_SIZE / 2,
                LevelGrid.TILE_SIZE / 2
        ));
        picture.setImage(AssetManager.collectibleTexture);
        picture.setStatic(true);

        scoreTextbox = GameObject.instantiate(UITextbox.class);
        scoreTextbox.transform.setScale(new Vector2D(
                GUI.Width / 5,
                LevelGrid.TILE_SIZE / 2
        ));
        scoreTextbox.transform.moveTo(new Vector2D(
                LevelGrid.TILE_SIZE / 2,
                0
        ));
        scoreTextbox.setStatic(true);


        infoTextbox = GameObject.instantiate(UITextbox.class);
        infoTextbox.setStatic(true);
        infoTextbox.setActive(false);
    }

    private void loadLevel() {
        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Loading level");
        final Vector2D size = level.getSize();
        levelGrid = new LevelGrid((int) size.x, (int) size.y);

        level.loadTo(levelGrid,
                targetCourse < 0
                        ? Mathf.random(0, level.getCoursesCount())
                        : targetCourse
        );

        collectiblesCount = levelGrid.findObjectsByType(CollectibleItem.class).length;
    }

    private void initializeVSL() {
        codeRunner = new CodeRunner();
        codeRunner.load(GridNodeEditor.code);
    }

    @Override
    public void onResolutionChanged() {
        DATA_PANEL_WIDTH = GUI.Width * DATA_PANEL_WIDTH_P / 100;
        BUTTON_MARGIN = DATA_PANEL_WIDTH * BUTTON_MARGIN_P / 100;
        BUTTON_SIZE = DATA_PANEL_WIDTH * BUTTON_SIZE_P / 100;


        dataPanel.transform.moveTo(new Vector2D(GUI.Width - DATA_PANEL_WIDTH, 0));
        dataPanel.transform.setScale(new Vector2D(DATA_PANEL_WIDTH, GUI.Height));

        openVslEditorButton.transform.moveTo(new Vector2D(
                GUI.Width - DATA_PANEL_WIDTH + BUTTON_MARGIN,
                BUTTON_MARGIN
        ));
        openVslEditorButton.transform.setScale(new Vector2D(BUTTON_SIZE, BUTTON_SIZE));

        switchPauseResumeVslButton.transform.moveTo(new Vector2D(
                GUI.Width - DATA_PANEL_WIDTH + BUTTON_MARGIN,
                BUTTON_SIZE + 2 * BUTTON_MARGIN
        ));
        switchPauseResumeVslButton.transform.setScale(new Vector2D(BUTTON_SIZE, BUTTON_SIZE));

        startTestsButton.transform.moveTo(new Vector2D(
                GUI.Width - DATA_PANEL_WIDTH + BUTTON_MARGIN,
                BUTTON_SIZE * 2 + 3 * BUTTON_MARGIN
        ));
        startTestsButton.transform.setScale(new Vector2D(BUTTON_SIZE, BUTTON_SIZE));


        infoTextbox.transform.moveTo(new Vector2D(20, GUI.Height - 100));
        infoTextbox.transform.setScale(new Vector2D(GUI.Width - DATA_PANEL_WIDTH, 100));
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
        if (isTesting) {
            testNextCourse();
            return;
        }

        if (openVslEditorButton.isClicked()) {
            SceneManager.instance.loadScene(new VSLEditorScene());
            return;
        }

        if (switchPauseResumeVslButton.isClicked()) {
            isVslRunning = !isVslRunning;
            switchPauseResumeVslButton.text.set(isVslRunning ? "pause" : "resume");
        }

        if (startTestsButton.isClicked()) {
            testScript();
            return;
        }


        if (gameOver) {
            this.moveCamera();
            return;
        }

        // Do 5 runner ticks
        // Or stop if animation was started(player movement/rotation)
        for (int i = 0; i < 5; ++i) {
            if (isVslRunning && !levelGrid.isAnimated()) {
                this.codeRunner.step();
                gameOver = levelGrid.findObjectsByType(CollectibleItem.class).length == 0;
            }
        }
        levelGrid.update();

        if (isVslRunning) {
            this.followPlayer();
        } else {
            this.moveCamera();
        }
    }

    private void testNextCourse() {
        if (targetCourse >= level.getCoursesCount()) {
            GameStats.score = (levelMaxSteps - levelCurrentSteps) * 100 / (levelMaxSteps - levelMinSteps);
            GameStats.steps = levelCurrentSteps;
            GameStats.maxSteps = levelMaxSteps;
            GameStats.instructions = codeRunner.getInstructionsCount();

            if (AssetManager.preferences.getInteger(level.getSId(), 0) < GameStats.score) {
                AssetManager.preferences.putInteger(level.getSId(), GameStats.score);
                AssetManager.preferences.flush();
            }

            SceneManager.instance.loadScene(new StatisticsScene(level));
            return;
        }

        final CourseInfo course = level.getCourseInfo(targetCourse);
        levelMaxSteps += course.maxSteps;
        levelMinSteps += course.minSteps;
        infoTextbox.text.set("Testing: " + course.name);


        codeRunner.reset();

        gridEmulator.reset();
        level.loadTo(gridEmulator, targetCourse);
        codeRunner.grid = gridEmulator;


        int steps = 0;
        while (gridEmulator.findObjectsByType(CollectibleItem.class).length > 0
                && steps < course.maxSteps
                && codeRunner.isRunning()) {
            steps += 1;
            codeRunner.step();
            gridEmulator.update();
        }

        if (gridEmulator.findObjectsByType(CollectibleItem.class).length > 0) {
            SceneManager.instance.loadScene(new GameGridScene(level, targetCourse));
            return;
        }
        gridEmulator.dispose();

        ++targetCourse;
        levelCurrentSteps += steps;
    }

    private void testScript() {
        if (codeRunner.getInstructionsCount() == 0 || !codeRunner.isValid())
            return;

        isTesting = true;
        gameOver = false;
        isVslRunning = false;

        levelMaxSteps = 0;
        levelMinSteps = 0;
        levelCurrentSteps = 0;

        final Vector2D size = level.getSize();
        gridEmulator = new LevelGridEmulator((int) size.x, (int) size.y);

        infoTextbox.setActive(true);
        targetCourse = 0;
    }

    private void moveCamera() {
        if (!GUI.isTouched || !GUI.isLastTouched) return;

        final Vector2D pos = GUI.touchPosition.sub(GUI.lastTouchPosition);
        pos.set(pos.add(GUI.cameraPosition));
        final float x = Mathf.clamp(pos.x, -levelGrid.width * LevelGrid.TILE_SIZE + GUI.Width - DATA_PANEL_WIDTH, 0);
        final float y = Mathf.clamp(pos.y, -levelGrid.height * LevelGrid.TILE_SIZE + GUI.Height, 0);

        GUI.moveCamera(x, y);
    }

    private void followPlayer() {
        final float TS = LevelGrid.TILE_SIZE;
        final Vector2D pos = levelGrid.player.transform.position;
        float x = -pos.x + GUI.Width / 2 + TS / 2 - DATA_PANEL_WIDTH;
        float y = -pos.y + GUI.Height / 2 - TS / 2;

        x = Mathf.clamp(x, -levelGrid.width * TS + GUI.Width - DATA_PANEL_WIDTH, 0);
        y = Mathf.clamp(y, -levelGrid.height * TS + GUI.Height, 0);

        final Vector2D movement = new Vector2D(x, y).sub(GUI.cameraPosition).div(50);
        GUI.translateBy(movement);
    }


    @Override
    public void render() {
        if (levelGrid != null) levelGrid.render();

        GUI.drawText(
                (collectiblesCount - levelGrid.findObjectsByType(CollectibleItem.class).length) + " / " + collectiblesCount,
                scoreTextbox.transform.position.x, scoreTextbox.transform.position.y,
                scoreTextbox.transform.scale.x, scoreTextbox.transform.scale.y,
                Color.WHITE, false
        );
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean onBackRequest() {
        SceneManager.instance.loadStartScene();
        return false;
    }
}
