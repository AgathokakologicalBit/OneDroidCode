package com.timewarp.games.onedroidcode.vsl.nodes.variables;

import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;


public abstract class IteratorNode extends Node {

    public Value outIterator;

    public IteratorNode(Node next) {
        super(next);

        outIterator = new Value(Value.TYPE_ITERABLE);
    }
}
