package com.timewarp.games.onedroidcode.vsl.nodes.flow;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;

public class LoopNode extends Node {

    @Override
    public Node execute(CodeRunner runner) {
        runner.pushContext(this);
        return next;
    }

    @Override
    public void reset() {
    }
}
