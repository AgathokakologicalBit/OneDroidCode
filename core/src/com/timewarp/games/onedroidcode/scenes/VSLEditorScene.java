package com.timewarp.games.onedroidcode.scenes;

import com.timewarp.engine.Scene;
import com.timewarp.games.onedroidcode.editor.GridNodeEditor;
import com.timewarp.games.onedroidcode.editor.NodeProvider;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.IfNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.LoopNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.MovementForwardNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.RotationLeftNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.control.RotationRightNode;
import com.timewarp.games.onedroidcode.vsl.nodes.robot.sensors.BlockSensorNode;

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
                /* ForLoopNode.class, */ IfNode.class, /*WhileLoopNode.class,*/
                LoopNode.class,
                // =- VARIABLES -=
                // ValueHolderNode.class, IteratorNode.class, NumberIteratorNode.class,

                // ==--  ROBOT NODES  --==
                // =- CONTROLS -=
                // MovementNode.class, RotationNode.class,
                MovementForwardNode.class, RotationRightNode.class, RotationLeftNode.class,
                // =- SENSORS -=
                BlockSensorNode.class,

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
        editor.saveAndExit();
        return false;
    }
}
