package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.engine.Vector2D;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

public class MovementForwardNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        final Vector2D direction = runner.grid.player.direction.getVector();

        final boolean moved = runner.grid.moveBy(
                runner.grid.player,
                (int) direction.x, (int) direction.y
        );
        runner.setFlag("boolean", new Value(Value.TYPE_BOOLEAN, moved));

        return next;
    }

    @Override
    public void reset() {
    }
}
