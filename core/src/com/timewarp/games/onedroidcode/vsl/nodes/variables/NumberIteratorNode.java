package com.timewarp.games.onedroidcode.vsl.nodes.variables;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;
import com.timewarp.games.onedroidcode.vsl.types.NumberIterator;

public class NumberIteratorNode extends IteratorNode {

    public Value inFrom;
    public Value inTo;
    public Value inStep;


    public NumberIteratorNode(Node next) {
        super(next);

        inFrom = new Value(Value.TYPE_INTEGER, 0);
        inTo = new Value(Value.TYPE_INTEGER, 0);
        inStep = new Value(Value.TYPE_INTEGER, 1);
    }

    @Override
    public Node execute(CodeRunner runner) {
        outIterator.set(
                new NumberIterator(
                        inFrom.toInteger(),
                        inTo.toInteger(),
                        inStep.toInteger()
                )
        );

        return next;
    }

    @Override
    public void reset() {

    }
}
