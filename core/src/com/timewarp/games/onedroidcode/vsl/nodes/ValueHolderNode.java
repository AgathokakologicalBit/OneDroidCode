package com.timewarp.games.onedroidcode.vsl.nodes;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ValueHolderNode extends Node {

    public Value outValue;

    public ValueHolderNode(Node next, int type) {
        super(next);

        outValue = new Value(type);
    }

    @Override
    public Node execute(CodeRunner runner) {
        Logger.getAnonymousLogger().log(Level.INFO, "VALUE HOLDER(" + outValue.toString() + ")");
        return next;
    }

    @Override
    public void reset() {
        outValue.value = null;
    }


    @Override
    public String represent(CodeRunner runner) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "VAR";
    }
}
