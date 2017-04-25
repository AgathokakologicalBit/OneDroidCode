package com.timewarp.games.onedroidcode.vsl;

import java.util.ArrayList;

public abstract class Node {

    public Node(Node next) {
        this.next = next;
        this.inputs = new ArrayList<Value>(2);
        this.outputs = new ArrayList<Value>(2);
    }

    public ArrayList<Value> inputs;
    public ArrayList<Value> outputs;

    public Node next;

    public abstract Node execute(CodeRunner runner);
    public abstract void reset();

    public void append(Node node) {
        this.next = node;
    }

    public abstract String represent(CodeRunner runner);

    @Override
    public String toString() {
        return "(...)";
    }
}
