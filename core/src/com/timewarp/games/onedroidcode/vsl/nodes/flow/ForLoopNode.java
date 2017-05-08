package com.timewarp.games.onedroidcode.vsl.nodes.flow;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;
import com.timewarp.games.onedroidcode.vsl.types.IValueIterator;

public class ForLoopNode extends Node {

    public Value inIterator;

    public Value outValue;
    public Node outBlock;


    public ForLoopNode(Node next) {
        super(next);
        this.reset();
    }


    @Override
    public Node execute(CodeRunner runner) {
        final IValueIterator it = (IValueIterator) inIterator.value;
        if (it == null) return next;

        if (it.hasNext()) {
            runner.pushContext(this);
            outValue.set(it.next());
            return outBlock;
        }

        return next;
    }

    @Override
    public void reset() {
        outValue = new Value(
                inIterator.value != null
                        ? ((IValueIterator) inIterator.value).getValueType()
                        : Value.TYPE_ANY
        );
    }

    @Override
    public String represent(CodeRunner runner) {
        return "FOR";
    }
}
