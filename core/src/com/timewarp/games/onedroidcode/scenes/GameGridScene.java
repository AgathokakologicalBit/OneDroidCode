package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Direction;
import com.timewarp.engine.Scene;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UITextbox;
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

    private UITextbox codeReprTextbox;
    private UITextbox outputTextbox;

    private CodeRunner codeRunner;
    private LevelGrid levelGrid;


    @Override
    public void initialize() {
        super.initialize();

        Logger.getAnonymousLogger().log(Level.INFO, "[GameGridSC] Placing objects");
        codeReprTextbox = GameObject.instantiate(UITextbox.class);
        codeReprTextbox.text.setTextSize(30);

        outputTextbox = GameObject.instantiate(UITextbox.class);
        outputTextbox.text.setTextSize(50);
        outputTextbox.text.set("<<NULL>>");


        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Loading level");
        levelGrid = new LevelGrid(10, 10);
        levelGrid.set(2, 1, 0);
        levelGrid.set(2, 2, 0);
        levelGrid.set(2, 3, 0);
        levelGrid.set(3, 1, 0);
        levelGrid.set(3, 3, 0);
        levelGrid.set(4, 1, 0);
        levelGrid.set(4, 2, 0);
        levelGrid.set(4, 3, 0);
        levelGrid.add(new TWall(), 3, 2);

        levelGrid.player.x = 2;
        levelGrid.player.y = 1;
        levelGrid.player.rotateBy(Direction.RIGHT);


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

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Configuring update intervals");
        Time.addCountdownRepeated("stats_update", 0.35f);

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL script");
        codeReprTextbox.text.set(codeRunner.getCodeRepresentation());
    }

    @Override
    public void onResolutionChanged() {
        codeReprTextbox.transform.moveTo(new Vector2D(50, 50));
        codeReprTextbox.transform.setScale(new Vector2D(GUI.Width - 100, 50));

        outputTextbox.transform.moveTo(new Vector2D(50, GUI.Height - 120));
        outputTextbox.transform.setScale(new Vector2D(GUI.Width - 100, 80));
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
        if (Time.isTimerActivated("stats_update")) {
            codeReprTextbox.text.set(codeRunner.getCodeRepresentation());
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
}
