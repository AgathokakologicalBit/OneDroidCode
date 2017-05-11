package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.engine.SceneManager;
import com.timewarp.games.onedroidcode.editor.GridNodeEditor;
import com.timewarp.games.onedroidcode.editor.NodeProvider;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.ForLoopNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.IfNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.WhileLoopNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.MovementNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.RotationNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.sensors.BlockSoliditySensorNode;
import com.timewarp.games.onedroidcode.vsl.nodes.variables.IteratorNode;
import com.timewarp.games.onedroidcode.vsl.nodes.variables.NumberIteratorNode;
import com.timewarp.games.onedroidcode.vsl.nodes.variables.ValueHolderNode;

public class VSLEditorScene extends Scene {

    private NodeProvider nodeProvider;
    private GridNodeEditor editor;

    public static Scene previousLevelScene;

    @Override
    public void initialize() {
        super.initialize();

        nodeProvider = new NodeProvider();
        nodeProvider.loadNodes(new Class[]{
                // ==--  BASIC NODES  --==
                RootNode.class,

                // =- MATH -=
                // =- FLOW -=
                ForLoopNode.class, IfNode.class, WhileLoopNode.class,
                // =- VARIABLES -=
                ValueHolderNode.class, IteratorNode.class, NumberIteratorNode.class,

                // ==--  ROBOT NODES  --==
                // =- CONTROLS -=
                MovementNode.class, RotationNode.class,
                // =- SENSORS -=
                BlockSoliditySensorNode.class,

        });
        editor = new GridNodeEditor(nodeProvider, 10, 10);
    }

    @Override
    public void onResolutionChanged() {

    }

    @Override
    public void loadResources() {

    }

    @Override
    public void update() {
        editor.update();
    }

    @Override
    public void render() {
        editor.render();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean onBackRequest() {
        SceneManager.instance.loadScene(previousLevelScene);
        return false;
    }
}
