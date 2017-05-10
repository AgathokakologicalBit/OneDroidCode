package com.timewarp.games.onedroidcode.editor;

import com.timewarp.games.onedroidcode.vsl.Node;

public class NodeProvider {

    public Class[] nodeTypes;
    public NodeController[] nodeControllers;

    public NodeProvider() {

    }

    public void loadNodes(Class[] nodeTypes) {
        // Why does java have nullable types by default? =(
        if (nodeTypes == null) return;

        this.nodeTypes = nodeTypes;
        this.nodeControllers = new NodeController[nodeTypes.length];
        this.generateControllers();
    }

    private void generateControllers() {
        int index = 0;
        for (Class nodeType : this.nodeTypes)
            if (Node.class.isAssignableFrom(nodeType))
                nodeControllers[index++] = new NodeController(nodeType);
    }

    public <T extends Node> NodeController getControllerFor(Class<T> targetNodeType) {
        int index = 0;
        for (Class nodeType : this.nodeTypes) {
            if (nodeType == targetNodeType) {
                return this.nodeControllers[index];
            }
        }

        return null;
    }
}
