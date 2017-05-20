package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.engine.Direction;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;


public class RotationRightNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        LevelGrid.instance.player.rotateBy(Direction.RIGHT);
        return next;
    }

    @Override
    public void reset() {

    }
}
