package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;
import com.timewarp.games.onedroidcode.vsl.nodes.ValueHolderNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.WhileLoopNode;
import com.timewarp.games.onedroidcode.vsl.nodes.io.TextRenderingNode;


public class MainMenu extends Scene {

    private UITextbox codeReprTextbox;
    private UITextbox outputTextbox;

    private CodeRunner codeRunner;

    @Override
    public void initialize() {
        super.initialize();

        Time.addCountdownRepeated("stats_update", 0.5f);
    }

    @Override
    public void onResolutionChanged() {
        codeReprTextbox = GameObject.instantiate(UITextbox.class);
        codeReprTextbox.transform.moveTo(new Vector2D(50, 50));
        codeReprTextbox.transform.setScale(new Vector2D(GUI.Width - 100, 50));
        codeReprTextbox.text.setTextSize(30);


        outputTextbox = GameObject.instantiate(UITextbox.class);
        outputTextbox.transform.moveTo(new Vector2D(50, GUI.Height - 120));
        outputTextbox.transform.setScale(new Vector2D(GUI.Width - 100, 80));
        outputTextbox.text.setTextSize(50);
        outputTextbox.text.set("<<NULL>>");

        codeRunner = new CodeRunner();
        Node[] code = new Node[1];

        TextRenderingNode textRenderingNode = new TextRenderingNode(null, outputTextbox);

        WhileLoopNode loop = new WhileLoopNode(null);
        loop.inputs.get(0).set(true);
        loop.block = new Node(textRenderingNode) {
            @Override
            public Node execute(CodeRunner runner) {
                inputs.get(0).set(inputs.get(0).toInteger() + 1);
                return next;
            }

            @Override
            public void reset() {}
        };
        ValueHolderNode testCounterNode = new ValueHolderNode(loop, "integer");
        testCounterNode.outputs.get(0).value = 0;

        loop.block.inputs.add(testCounterNode.outputs.get(0));

        textRenderingNode.inputs.set(0, testCounterNode.outputs.get(0));

        code[0] = new RootNode(
                testCounterNode
        );
        codeRunner.load(code);

        codeReprTextbox.text.set(codeRunner.getCodeRepresentation());
        codeRunner.step();
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
