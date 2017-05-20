package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;

public class MovementForwardNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        LevelGrid.instance.moveBy(LevelGrid.instance.player, 0, 1);
        return next;
    }

    @Override
    public void reset() {
    }
}
