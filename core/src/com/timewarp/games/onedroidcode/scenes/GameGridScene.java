package com.timewarp.games.onedroidcode.scenes;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Direction;
import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Time;
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
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.objects.CollectibleItem;
import com.timewarp.games.onedroidcode.objects.tiles.TWall;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameGridScene extends Scene {

    // private UITextbox codeReprTextbox;
    // private UITextbox outputTextbox;

    private UIPanel dataPanel;
    private UIButton openVslEditorButton;
    private UIButton switchPauseResumeVslButton;
    private UITextbox scoreTextbox;
    private UITextbox gameoverTextbox;


    private final int DATA_PANEL_WIDTH_P = 20; // 20% of screen width
    private final int BUTTON_MARGIN_P = 10; // 10% of panel'S width
    private final int BUTTON_SIZE_P = 100 - BUTTON_MARGIN_P * 2; // Calculated percentage of panel's width

    private int DATA_PANEL_WIDTH;
    private int BUTTON_MARGIN;
    private int BUTTON_SIZE;


    private CodeRunner codeRunner;
    private LevelGrid levelGrid;

    private boolean isVslRunning;

    private int collectiblesCount;
    private boolean gameOver = false;


    @Override
    public void initialize() {
        super.initialize();

        Logger.getAnonymousLogger().log(Level.INFO, "[GameGridSC] Placing objects");

        loadLevel();
        initializeVSL();

        generateUI();

        // codeReprTextbox = GameObject.instantiate(UITextbox.class);
        // codeReprTextbox.text.setSize(30);

        // outputTextbox = GameObject.instantiate(UITextbox.class);
        // outputTextbox.text.setSize(50);
        // outputTextbox.text.set("<<NULL>>");

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Configuring update intervals");
        Time.addCountdownRepeated("vsl_tick", 0.0001f);

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL script");
        isVslRunning = false;
        // codeReprTextbox.text.set(codeRunner.getCodeRepresentation());

        VSLEditorScene.previousLevelScene = this;

        GameStats.reset();
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


        gameoverTextbox = GameObject.instantiate(UITextbox.class);
        gameoverTextbox.setStatic(true);
        gameoverTextbox.setActive(false);
    }

    private void loadLevel() {
        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Loading level");
        levelGrid = new LevelGrid(10, 10);

        levelGrid.player.setX(2);
        levelGrid.player.setY(1);
        levelGrid.player.transform.setRotation(-Mathf.PI / 2);
        levelGrid.player.direction = Direction.RIGHT;


        final int width = (int) (Math.random() * 5) + 1;
        final int height = (int) (Math.random() * 5) + 1;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                levelGrid.add(new TWall(), x + 3, y + 2);
            }
        }

        levelGrid.add(new CollectibleItem(), 3 + width, 1);
        levelGrid.add(new CollectibleItem(), 3 + width, 2 + height);
        levelGrid.add(new CollectibleItem(), 2, 2 + height);

        collectiblesCount = levelGrid.findObjectsByType(CollectibleItem.class).length;
    }

    private void initializeVSL() {
        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL code runner");
        codeRunner = new CodeRunner();
        codeRunner.load(GridNodeEditor.code);
    }

    @Override
    public void onResolutionChanged() {
        // codeReprTextbox.transform.moveTo(new Vector2D(50, 50));
        // codeReprTextbox.transform.setScale(new Vector2D(GUI.Width - 100, 50));

        // outputTextbox.transform.moveTo(new Vector2D(50, GUI.Height - 120));
        // outputTextbox.transform.setScale(new Vector2D(GUI.Width - 100, 80));

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

        gameoverTextbox.transform.position.set(new Vector2D(
                GUI.Width / 2 - DATA_PANEL_WIDTH / 2, GUI.Height / 2
        ));
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
        if (gameOver) return;

        if (openVslEditorButton.isClicked()) {
            SceneManager.instance.loadScene(new VSLEditorScene());
            return;
        }

        if (switchPauseResumeVslButton.isClicked()) {
            isVslRunning = !isVslRunning;
            switchPauseResumeVslButton.text.set(isVslRunning ? "pause" : "resume");
        }


        LevelGrid.instance.update();
        if (isVslRunning && Time.isActivated("vsl_tick") && !LevelGrid.instance.isAnimated()) {
            // codeReprTextbox.text.set(codeRunner.getCodeRepresentation());
            this.codeRunner.step();

            if (LevelGrid.instance.findObjectsByType(CollectibleItem.class).length == 0) {
                gameoverTextbox.setActive(true);
                gameoverTextbox.animator.playAnimation("text_gameover");
                gameOver = true;
            }
        }

        if (isVslRunning) {
            this.followPlayer();
        } else {
            this.moveCamera();
        }
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
        float x = -pos.x * TS + GUI.Width / 2 + TS / 2 - DATA_PANEL_WIDTH;
        float y = -pos.y * TS + GUI.Height / 2 - TS / 2;
        x = Mathf.clamp(x, -levelGrid.width * LevelGrid.TILE_SIZE + GUI.Width - DATA_PANEL_WIDTH, 0);
        y = Mathf.clamp(y, -levelGrid.height * LevelGrid.TILE_SIZE + GUI.Height, 0);

        final Vector2D movement = (new Vector2D(x, y)).sub(GUI.cameraPosition).div(50);

        GUI.translateBy(movement);
    }


    @Override
    public void render() {
        if (levelGrid != null) levelGrid.draw();

        GUI.drawText(
                GameStats.score + " / " + collectiblesCount,
                scoreTextbox.transform.position.x, scoreTextbox.transform.position.y,
                scoreTextbox.transform.scale.x, scoreTextbox.transform.scale.y,
                Color.WHITE, false
        );

        if (gameOver) {
            GUI.drawText(
                    "EVERYTHING IS COLLECTED!",
                    gameoverTextbox.transform.position.x, gameoverTextbox.transform.position.y,
                    gameoverTextbox.transform.scale.x, gameoverTextbox.transform.scale.y,
                    Color.WHITE, true
            );
        }
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
