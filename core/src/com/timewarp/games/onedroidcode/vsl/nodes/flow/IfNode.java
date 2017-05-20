package com.timewarp.games.onedroidcode.vsl.nodes.flow;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;


public class IfNode extends Node {

    public Value inCondition;
    public Node onTrue, onFalse;


    @Override
    public Node execute(CodeRunner runner) {
        if (inCondition == null) return next;

        // After execution of branches
        // Next node will be executed
        runner.pushContext(next);
        return inCondition.toBoolean() ? onTrue : onFalse;
    }

    @Override
    public void reset() {
    }
}
