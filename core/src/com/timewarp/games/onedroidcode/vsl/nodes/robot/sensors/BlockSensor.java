package com.timewarp.games.onedroidcode.vsl.nodes.robot.sensors;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

public class BlockSensor extends Node {
    public BlockSensor(Node next) {
        super(next);

        outputs.add(new Value(Value.TYPE_BOOLEAN));
    }

    @Override
    public Node execute(CodeRunner runner) {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public String represent(CodeRunner runner) {
        return null;
    }
}
