package com.timewarp.games.onedroidcode.vsl.nodes.robot.control;

import com.timewarp.games.onedroidcode.level.LevelGrid;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MovementNode extends Node {

    private int movementHorizontal = 0;
    private int movementVertical = 0;
    private char movementPoint = '.';

    public Value inHorizontal;
    public Value inVertical;

    public MovementNode(Node next) {
        super(next);

        inHorizontal = new Value(Value.TYPE_INTEGER);
        inVertical = new Value(Value.TYPE_INTEGER);
    }

    @Override
    public Node execute(CodeRunner runner) {
        if (inHorizontal.value != null) {
            movementHorizontal =
                    (inHorizontal.toInteger() > 0 ? 1 : 0)
                            - (inHorizontal.toInteger() < 0 ? 1 : 0);
        }

        if (inVertical.value != null) {
            movementVertical =
                    (inVertical.toInteger() > 0 ? 1 : 0)
                            - (inVertical.toInteger() < 0 ? 1 : 0);
        }

        if (movementHorizontal < 0) {
            if (movementVertical < 0) {
                movementPoint = '↙';
            } else if (movementVertical == 0) {
                movementPoint = '<';
            } else {
                movementPoint = '↘';
            }
        } else if (movementHorizontal == 0) {
            if (movementVertical < 0) {
                movementPoint = 'v';
            } else if (movementVertical > 0) {
                movementPoint = '^';
            }
        } else {
            if (movementVertical < 0) {
                movementPoint = '↘';
            } else if (movementVertical == 0) {
                movementPoint = '>';
            } else {
                movementPoint = '↗';
            }
        }

        Logger.getAnonymousLogger().log(Level.WARNING, "MOVEMENT [DIRECTION](" + movementPoint + ")");

        LevelGrid.instance.moveBy(LevelGrid.instance.player, movementHorizontal, movementVertical);

        return next;
    }

    @Override
    public void reset() {

    }

    @Override
    public String represent(CodeRunner runner) {
        return "MOVE(" + movementPoint + ")";
    }
}
