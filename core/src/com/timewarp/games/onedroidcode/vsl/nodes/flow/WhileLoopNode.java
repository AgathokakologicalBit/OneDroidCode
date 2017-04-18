package com.timewarp.games.onedroidcode.vsl.nodes.flow;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WhileLoopNode extends Node {

    public Node block;

    public WhileLoopNode(Node next) {
        super(next);

        // Condition
        inputs.add(new Value("boolean"));
    }

    @Override
    public Node execute(CodeRunner runner) {

        Logger.getAnonymousLogger().log(Level.INFO, "WHILE LOOP [CONDITION](" + inputs.get(0).toBoolean() + ")");
        if (inputs.get(0).toBoolean() && block != null) {
            Logger.getAnonymousLogger().log(Level.INFO, "WHILE LOOP [BLOCK]");
            runner.pushContext(this);
            return block;
        }

        Logger.getAnonymousLogger().log(Level.INFO, "WHILE LOOP [END]");
        return next;
    }

    @Override
    public void reset() {

    }

    @Override
    public String toString() {
        return "While...";
    }
}
