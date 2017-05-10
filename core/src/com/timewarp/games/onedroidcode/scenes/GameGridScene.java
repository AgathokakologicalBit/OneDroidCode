package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Direction;
import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UIButton;
import com.timewarp.engine.gui.controls.UIPanel;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.objects.tiles.TWall;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.WhileLoopNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.MovementNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.RotationNode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameGridScene extends Scene {

    // private UITextbox codeReprTextbox;
    // private UITextbox outputTextbox;

    private UIPanel dataPanel;
    private UIButton openVslEditorButton;
    private UIButton switchPauseResumeVslButton;


    private final int DATA_PANEL_WIDTH_P = 20; // 20% of screen width
    private final int BUTTON_MARGIN_P = 10; // 10% of panel'S width
    private final int BUTTON_SIZE_P = 100 - BUTTON_MARGIN_P * 2; // Calculated percentage of panel's width

    private int DATA_PANEL_WIDTH;
    private int BUTTON_MARGIN;
    private int BUTTON_SIZE;


    private CodeRunner codeRunner;
    private LevelGrid levelGrid;

    private boolean isVslRunning;


    @Override
    public void initialize() {
        super.initialize();

        Logger.getAnonymousLogger().log(Level.INFO, "[GameGridSC] Placing objects");
        generateUI();
        loadLevel();
        initializeVSL();

        // codeReprTextbox = GameObject.instantiate(UITextbox.class);
        // codeReprTextbox.text.setTextSize(30);

        // outputTextbox = GameObject.instantiate(UITextbox.class);
        // outputTextbox.text.setTextSize(50);
        // outputTextbox.text.set("<<NULL>>");

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Configuring update intervals");
        Time.addCountdownRepeated("vsl_tick", 0.0001f);

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL script");
        isVslRunning = true;
        // codeReprTextbox.text.set(codeRunner.getCodeRepresentation());
    }

    private void generateUI() {
        dataPanel = GameObject.instantiate(UIPanel.class);

        openVslEditorButton = GameObject.instantiate(UIButton.class);
        openVslEditorButton.text.setTextAlignment(true);
        openVslEditorButton.text.set("edit script");

        switchPauseResumeVslButton = GameObject.instantiate(UIButton.class);
        switchPauseResumeVslButton.text.setTextAlignment(true);
        switchPauseResumeVslButton.text.set("pause");
    }

    private void loadLevel() {
        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Loading level");
        levelGrid = new LevelGrid(10, 10);
        levelGrid.add(new TWall(), 3, 2);

        levelGrid.player.setX(2);
        levelGrid.player.setY(1);
        levelGrid.player.rotateBy(Direction.RIGHT);
    }

    private void initializeVSL() {
        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL code runner");
        codeRunner = new CodeRunner();

        final RootNode rootNode = new RootNode();

        final WhileLoopNode loop = new WhileLoopNode(null);
        loop.inCondition.set(true);

        final MovementNode movement1 = new MovementNode();
        movement1.inVertical.set(1);
        final MovementNode movement2 = new MovementNode();
        movement2.inVertical.set(1);

        final RotationNode rotation = new RotationNode();
        rotation.inRotation.set(1);


        rootNode.append(loop);

        loop.outBlock = movement1;
        movement1
                .append(movement2)
                .append(rotation);

        final Node[] code = new Node[]{
                rootNode,
                loop,
                movement1,
                movement2,
                rotation
        };

        codeRunner.load(code);
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
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
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
        }
    }

    @Override
    public void render() {
        if (levelGrid == null) return;
        levelGrid.draw();
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
