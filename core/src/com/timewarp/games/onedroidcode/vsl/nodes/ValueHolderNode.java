package com.timewarp.games.onedroidcode.vsl.nodes;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ValueHolderNode extends Node {

    public ValueHolderNode(Node next, String type) {
        super(next);

        outputs.add(new Value(type));
    }

    @Override
    public Node execute(CodeRunner runner) {
        Logger.getAnonymousLogger().log(Level.INFO, "VALUE HOLDER(" + outputs.get(0).value.toString() + ")");
        return next;
    }

    @Override
    public void reset() {
        outputs.get(0).value = null;
    }

    @Override
    public String toString() {
        return "VAR";
    }
}
