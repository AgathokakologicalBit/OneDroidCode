package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.engine.Vector2D;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;

public class MovementForwardNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        final Vector2D direction = LevelGrid.instance.player.direction.getVector();
        LevelGrid.instance.moveBy(LevelGrid.instance.player, (int) direction.x, (int) direction.y);
        return next;
    }

    @Override
    public void reset() {
    }
}
