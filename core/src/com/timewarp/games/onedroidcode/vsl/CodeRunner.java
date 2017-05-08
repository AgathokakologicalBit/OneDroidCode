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

    public CodeRunner() {
        contexts = new LinkedList<Node>();
    }

    /**
     * Loads code into VSL Runner
     *
     * @param code Target code
     */
    public boolean load(Node[] code) {
        this.code = code;
        startNode = findEntrancePointNode();

        reset();

        // If code was loaded successfully
        // it must have a root node
        return startNode != null;
    }

    /**
     * Resets all nodes values
     * Sets pointer to beginning of script
     */
    public void reset() {
        for (Node node: code)
            node.reset();

        nextNode = startNode;
        contexts.clear();
    }

    /**
     * Executes current pointing node
     * calculates next target node
     */
    public boolean step() {
        if (nextNode == null) return false;
        nextNode = nextNode.execute(this);

        if (nextNode != null) return true;
        if (!contexts.isEmpty()) {
            nextNode = contexts.getLast();
            contexts.removeLast();
            return true;
        }

        Logger.getAnonymousLogger().log(Level.INFO, "FINISHED SCRIPT EXECUTION");
        return false;
    }

    /**
     * Adds given node to 'context stack'
     * After last step(node returned null) it will get back to top context node
     * @param context Node to push to context stack
     */
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


    /**
     * Returns current state of node
     * @param node Target node
     * @return true - if current node is executing by VSL Runner
     */
    public boolean isActive(Node node) {
        if (nextNode == node) return true;

        for (Node contextNode : contexts)
            if (contextNode == node)
                return true;

        return false;
    }

    /**
     * Converts Nodes array to String
     * @return String representation of code
     */
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


    public Node getActiveNode() {
        return this.nextNode;
    }
}
