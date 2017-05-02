package com.timewarp.games.onedroidcode.vsl.nodes.flow;

import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WhileLoopNode extends Node {

    public Value inCondition;
    public Node outBlock;

    public WhileLoopNode(Node next) {
        super(next);

        // Condition
        inCondition = new Value(Value.TYPE_BOOLEAN);
    }

    @Override
    public Node execute(CodeRunner runner) {
        Logger.getAnonymousLogger().log(Level.INFO, "WHILE LOOP [CONDITION](" + inCondition.toBoolean() + ")");

        if (inCondition != null && inCondition.toBoolean() && outBlock != null) {
            Logger.getAnonymousLogger().log(Level.INFO, "WHILE LOOP [BLOCK]");
            runner.pushContext(this);
            return outBlock;
        }

        Logger.getAnonymousLogger().log(Level.INFO, "WHILE LOOP [END]");
        return next;
    }

    @Override
    public void reset() {

    }

    @Override
    public String represent(CodeRunner runner) {
        String representation = this.toString() + "<";
        Node nextRepresentationNode = outBlock;

        while (nextRepresentationNode != null && nextRepresentationNode != this) {
            boolean isActive = runner.isActive(nextRepresentationNode);
            representation += isActive ? "[" : " ";
            representation += nextRepresentationNode.represent(runner);
            representation += isActive ? "]" : " ";

            nextRepresentationNode = nextRepresentationNode.next;
        }

        return representation + ">";
    }

    @Override
    public String toString() {
        return "While...";
    }
}
