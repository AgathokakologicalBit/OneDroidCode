package com.timewarp.games.onedroidcode.vsl.nodes.robot.sensors;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

public class BlockSoliditySensorNode extends Node {

    public Value outBlockExists;

    public BlockSoliditySensorNode(Node next) {
        super(next);

        outBlockExists = new Value(Value.TYPE_BOOLEAN);
    }

    @Override
    public Node execute(CodeRunner runner) {
        // TODO: Check block in front of player
        return next;
    }

    @Override
    public void reset() {

    }

    @Override
    public String represent(CodeRunner runner) {
        return null;
    }
}
