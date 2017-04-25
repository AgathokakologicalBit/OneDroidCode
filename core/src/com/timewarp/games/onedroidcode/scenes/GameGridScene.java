package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.WhileLoopNode;
import com.timewarp.games.onedroidcode.vsl.nodes.io.TextRenderingNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.MovementNode;

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
        levelGrid.set(0, 0, 0);
        levelGrid.set(1, 0, 0);
        levelGrid.set(2, 0, 0);
        levelGrid.set(2, 1, 0);
        levelGrid.set(2, 2, 0);
        levelGrid.player.x = 1;


        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL code runner");
        codeRunner = new CodeRunner();

        Node[] code = new Node[1];

        TextRenderingNode textRenderingNode = new TextRenderingNode(null, outputTextbox);

        final WhileLoopNode loop = new WhileLoopNode(null);
        loop.inputs.get(0).set(true);
        loop.block = new Node(textRenderingNode) {
            {
                outputs.add(new Value(Value.TYPE_INTEGER));
                outputs.get(0).set(0);
            }

            private boolean moveRight = true;
            private int position = 1;
            private final int maxOffset = 3;

            @Override
            public Node execute(CodeRunner runner) {
                if (moveRight) {
                    if ((position += 2) >= maxOffset)
                        moveRight = false;
                } else {
                    if ((position -= 2) <= -maxOffset)
                        moveRight = true;
                }

                outputs.get(0).set(position);

                return next;
            }

            @Override
            public void reset() {
            }

            @Override
            public String represent(CodeRunner runner) {
                return "SWITCH";
            }
        };

        loop.block.next.append(new MovementNode(null) {{
            inputs.set(0, loop.block.outputs.get(0));
        }});

        textRenderingNode.inputs.set(0, loop.block.outputs.get(0));

        code[0] = new RootNode(loop);
        codeRunner.load(code);

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Configuring update intervals");
        Time.addCountdownRepeated("stats_update", 1f);

        Logger.getAnonymousLogger().log(Level.INFO, "[GameSC] Starting VSL script");
        codeReprTextbox.text.set(codeRunner.getCodeRepresentation());
        codeRunner.step();
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
    public void update(double deltaTime) {
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
