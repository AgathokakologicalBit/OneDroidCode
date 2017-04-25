package com.timewarp.games.onedroidcode.vsl;

import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeRunner {

    private Node[] code;
    private Node startNode;
    private Node nextNode;

    private LinkedList<Node> contexts;

    public void load(Node[] code) {
        this.code = code;
        startNode = findEntrancePointNode();
        contexts = new LinkedList<Node>();

        reset();
    }

    public void reset() {
        for (Node node: code)
            node.reset();
        nextNode = startNode;
        contexts.clear();
    }

    public void step() {
        if (nextNode == null) return;
        nextNode = nextNode.execute(this);

        if (nextNode != null) return;
        if (!contexts.isEmpty()) {
            nextNode = contexts.getLast();
            contexts.removeLast();
            return;
        }

        Logger.getAnonymousLogger().log(Level.INFO, "FINISHED SCRIPT EXECUTION");
    }

    public void pushContext(Node context) {
        contexts.add(context);
    }

    private Node findEntrancePointNode() {
        for (Node node : code) {
            if (node instanceof RootNode)
                return node;
        }

        return null;
    }

    public boolean isActive(Node node) {
        if (nextNode == node) return true;

        for (Node contextNode : contexts)
            if (contextNode == node)
                return true;

        return false;
    }

    public String getCodeRepresentation() {
        String representation = "";
        Node next = startNode;

        while (next != null) {
            boolean active = isActive(next);
            representation += active ? "[" : " ";
            representation += next.represent(this);
            representation += active ? "]" : " ";

            next = next.next;
        }

        return representation;
    }
}
