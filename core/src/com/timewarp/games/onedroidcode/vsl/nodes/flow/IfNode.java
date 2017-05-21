package com.timewarp.games.onedroidcode.vsl.nodes.flow;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;


public class IfNode extends Node {

    public Node out1OnTrue;
    public Node out2OnFalse;

    @Override
    public Node execute(CodeRunner runner) {

        final Value inCondition = runner.getFlag("boolean");

        // After execution of branches
        // Next node will be executed
        if (next != null) runner.pushContext(next);
        return inCondition.toBoolean() ? out1OnTrue : out2OnFalse;
    }

    @Override
    public void reset() {
    }
}
