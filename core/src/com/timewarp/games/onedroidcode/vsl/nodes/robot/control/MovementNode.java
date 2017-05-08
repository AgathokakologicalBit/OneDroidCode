package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.engine.Direction;
import com.timewarp.engine.Vector2D;
import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MovementNode extends Node {

    private int movementHorizontal = 0;
    private int movementVertical = 0;

    public Value inHorizontal;
    public Value inVertical;

    public MovementNode() {
        inHorizontal = new Value(Value.TYPE_INTEGER);
        inVertical = new Value(Value.TYPE_INTEGER);
    }

    @Override
    public Node execute(CodeRunner runner) {
        movementHorizontal =
                (inHorizontal.toInteger() > 0 ? 1 : 0)
                        - (inHorizontal.toInteger() < 0 ? 1 : 0);

        movementVertical =
                (inVertical.toInteger() < 0 ? 1 : 0)
                        - (inVertical.toInteger() > 0 ? 1 : 0);

        Vector2D movementDirection = LevelGrid.instance.player.direction.rotatedBy(
                Direction.fromVector(movementHorizontal, movementVertical)
        ).getVector();

        Logger.getAnonymousLogger().log(Level.WARNING, "MOVEMENT [DIRECTION]()");

        LevelGrid.instance.moveBy(
                LevelGrid.instance.player,
                (int) movementDirection.x, (int) movementDirection.y
        );

        return next;
    }

    @Override
    public void reset() {

    }

    @Override
    public String represent(CodeRunner runner) {
        return "MOVE";
    }
}
