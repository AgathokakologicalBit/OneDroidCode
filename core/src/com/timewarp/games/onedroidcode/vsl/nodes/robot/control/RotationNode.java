package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.engine.Direction;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

public class RotationNode extends Node {

    public Value inRotation;


    public RotationNode() {
        inRotation = new Value(Value.TYPE_INTEGER);
    }

    @Override
    public Node execute(CodeRunner runner) {
        runner.grid.rotateBy(runner.grid.player, Direction.fromInteger((Integer) inRotation.value * 2));
        return next;
    }

    @Override
    public void reset() {

    }
}
