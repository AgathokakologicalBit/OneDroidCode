package com.timewarp.games.onedroidcode.vsl.nodes.variables;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ValueHolderNode extends Node {

    public Value outValue;
    private Object defaultValue;

    public ValueHolderNode(Node next, int type) {
        super(next);

        defaultValue = Value.getDefaultFor(type);
        outValue = new Value(type, defaultValue);
    }

    public ValueHolderNode(int type) {
        this(null, type);
    }

    public ValueHolderNode(int type, Object value) {
        defaultValue = value;
        outValue = new Value(type, defaultValue);
    }


    @Override
    public Node execute(CodeRunner runner) {
        Logger.getAnonymousLogger().log(Level.INFO, "VALUE HOLDER(" + outValue.toString() + ")");
        return next;
    }

    @Override
    public void reset() {
        outValue.value = defaultValue;
    }
}
