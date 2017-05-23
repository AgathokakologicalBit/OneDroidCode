package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.engine.Direction;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;

public class RotationLeftNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        runner.grid.rotateBy(runner.grid.player, Direction.LEFT);
        return next;
    }

    @Override
    public void reset() {

    }
}
