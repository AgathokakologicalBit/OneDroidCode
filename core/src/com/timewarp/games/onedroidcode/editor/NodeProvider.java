package com.timewarp.games.onedroidcode.editor;

import com.timewarp.engine.StringUtils;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.editor.actions.GroupAction;
import com.timewarp.games.onedroidcode.editor.actions.IAction;
import com.timewarp.games.onedroidcode.editor.actions.NodeControllerAction;
import com.timewarp.games.onedroidcode.vsl.Node;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeProvider {

    public Class[] nodeTypes;
    public NodeController[] nodeControllers;

    private NodeController currentController;
    private NodeCellComponent currentNode;

    private GroupAction baseGroup;
    private GroupAction currentGroup;


    public NodeProvider() {

    }


    public void loadNodes(Class[] nodeTypes) {
        // Why does java have nullable types by default? =(
        if (nodeTypes == null) return;

        this.nodeTypes = nodeTypes;
        this.nodeControllers = new NodeController[nodeTypes.length];
        this.generateControllers();
        this.currentGroup = this.baseGroup = this.formGroup(0, "home", "");
    }

    private void generateControllers() {
        int index = 0;
        for (Class nodeType : this.nodeTypes)
            if (Node.class.isAssignableFrom(nodeType))
                nodeControllers[index++] = new NodeController(nodeType);
    }

    private GroupAction formGroup(int depth, String groupName, String path) {
        final GroupAction group = new GroupAction(
                AssetManager.getTexture("group/" + path.replaceAll("/$", "")),
                groupName
        );

        for (NodeController controller : nodeControllers) {
            final String controllerPath = StringUtils.join("/", controller.path);

            if (!controllerPath.startsWith(path)) continue;

            if (depth > 0 && controllerPath.equals(path + controller.pathName)) {
                Logger.getAnonymousLogger().log(Level.INFO, controllerPath);
                group.addAction(
                        new NodeControllerAction(
                                AssetManager.getTexture("act/" + controllerPath),
                                controller
                        )
                );
            } else if (!controllerPath.equals(path + controller.pathName)) {
                final String subName = controller.path[depth];
                boolean actionExists = false;

                for (IAction action : group.getActions()) {
                    if (action.getName().equals(subName))
                        actionExists = true;
                }
                if (actionExists) continue;

                group.addAction(formGroup(depth + 1, subName, path + subName + "/"));
            }
        }

        return group;
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

    public void selectNodeController(NodeController controller) {
        this.currentController = controller;
    }

    public void selectNode(NodeCell node) {
        this.currentNode = node.cellComponent;
    }

    public NodeController getCurrentNodeController() {
        return currentController;
    }

    public void selectGroup(GroupAction group) {
        currentGroup = group;
    }

    public GroupAction getCurrentGroup() {
        return currentGroup;
    }

    public GroupAction getBaseGroup() {
        return baseGroup;
    }
}
