package com.timewarp.games.onedroidcode.vsl;

public abstract class Node {

    public Node next;


    public Node() {
    }
    public Node(Node next) {
        this.next = next;
    }


    public abstract Node execute(CodeRunner runner);
    public abstract void reset();

    public final Node append(Node node) {
        return this.next = node;
    }

    public abstract String represent(CodeRunner runner);

    @Override
    public String toString() {
        return "(...)";
    }
}
