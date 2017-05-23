package com.timewarp.games.onedroidcode.editor;


import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.editor.actions.IAction;
import com.timewarp.games.onedroidcode.editor.actions.NodeControllerAction;
import com.timewarp.games.onedroidcode.scenes.VSLEditorScene;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridNodeEditor {

    public static GridNodeEditor instance;

    private NodeProvider nodeProvider;

    private boolean isMoving;
    private Vector2D startMovementPoint;

    private int width, height;
    private NodeCellComponent[][] field;


    private final int STATE_EMPTY = 0;
    private final int STATE_NEW_NODE = 1;
    private final int STATE_NODE_EDIT = 2;
    private final int STATE_WIRE_ASSIGN = 3;
    private final int STATE_NODE_WIRE_ASSIGN = 4;

    private int state = STATE_EMPTY;
    private String windowName = "CLICK ON ANY CELL";
    private NodeCellComponent currentCell;
    private NodeIO targetNodeOutput;

    private Vector2D lastTouchPosition;
    private int currentScroll = 0;


    private final int EDIT_PANEL_WIDTH_P = 30;
    int EDIT_PANEL_WIDTH;

    private final int NODE_CELL_SPACING = 30;
    private final int NODE_CELLS_PER_COLUMN = 4;
    private final int NODE_CELL_SIZE = (GUI.Height - NODE_CELL_SPACING * (NODE_CELLS_PER_COLUMN + 1)) / NODE_CELLS_PER_COLUMN;

    private static NodeController[][] controllers;
    public static Node[] code;


    public GridNodeEditor(NodeProvider provider, int width, int height) {
        GridNodeEditor.instance = this;

        this.nodeProvider = provider;

        this.width = width;
        this.height = height;
        this.generateField(width, height);
        if (controllers != null) loadControllers();
    }

    private void generateField(int width, int height) {
        field = new NodeCellComponent[height][width];

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                final NodeCell cell = GameObject.instantiate(NodeCell.class);
                final NodeCellComponent component = field[y][x] = cell.cellComponent;
                component.setSize(NODE_CELL_SIZE);
                component.setPosition(
                        x * (NODE_CELL_SIZE + NODE_CELL_SPACING) + NODE_CELL_SPACING,
                        y * (NODE_CELL_SIZE + NODE_CELL_SPACING) + NODE_CELL_SPACING
                );
            }
        }

        field[0][0].nodeController = nodeProvider.getControllerFor(RootNode.class).copy();
        field[0][0].nodeController.texture = AssetManager.rootNodeTexture;
    }


    public void update() {
        // If editor panel caught click then don't check main field
        this.updateEditorPanel();

        if (GUI.isTouched && GUI.touchStartPosition.x >= GUI.Width - EDIT_PANEL_WIDTH) {
            return;
        }

        if (!isMoving && GUI.isTouched && GUI.touchPosition.sub(GUI.touchStartPosition).getLengthSquared() >= 25) {
            isMoving = true;
            startMovementPoint = GUI.touchPosition.copy();
        } else if (!GUI.isTouched) {
            isMoving = false;
        }

        if (isMoving) {
            GUI.translateBy(GUI.touchPosition.sub(startMovementPoint));
            startMovementPoint = GUI.touchPosition.copy();
            return;
        }

        this.updateNodes();
    }

    private void updateEditorPanel() {
        EDIT_PANEL_WIDTH = GUI.Width * EDIT_PANEL_WIDTH_P / 100;
    }

    private void updateNodes() {
        for (NodeCellComponent[] csc : field) {
            for (NodeCellComponent cell : csc) {
                if (!cell.gameObject.isClicked()) continue;
                Logger.getAnonymousLogger().log(
                        Level.WARNING,
                        String.format(
                                Locale.US,
                                "cam: %s\npos:%s",
                                GUI.cameraPosition.toString(),
                                cell.transform.position.toString()
                        )
                );
                if (cell.gameObject.touchStartPosition.x
                        >= GUI.Width - GridNodeEditor.instance.EDIT_PANEL_WIDTH)
                    continue;


                if (state == STATE_WIRE_ASSIGN) {
                    currentCell.nodeController.next = cell.nodeController;
                    removeSelection();
                } else if (state == STATE_NODE_WIRE_ASSIGN) {
                    targetNodeOutput.value = cell.nodeController;
                    removeSelection();
                } else {
                    if (currentCell != null) currentCell.removeSelection();
                    cell.addSelection();

                    if (cell.nodeController == null) createNode(cell);
                    else selectNode(cell);
                }
            }
        }
    }

    private void createNode(NodeCellComponent cell) {
        state = STATE_NEW_NODE;
        windowName = "CREATE NEW NODE";
        currentCell = cell;
    }

    private void selectNode(NodeCellComponent cell) {
        state = STATE_NODE_EDIT;
        windowName = cell.nodeController.name.toUpperCase();
        currentCell = cell;
    }


    public void render() {
        this.renderWires();
        this.renderUI();
    }

    private void renderWires() {
        GUI.endStaticBlock();

        for (NodeCellComponent[] nodesRow : field) {
            for (NodeCellComponent cell : nodesRow) {
                if (cell.nodeController == null) continue;

                if (cell.nodeController.next != null) {
                    if (cell.nodeController.next.parentCell == null) {
                        cell.nodeController.next = null;
                    } else {
                        final Vector2D from = cell.transform.position.add(NODE_CELL_SIZE - 10, 10);
                        final Vector2D to = cell.nodeController.next.parentCell.transform.position.add(10, 10);
                        GUI.drawLine(from.x, from.y, to.x, to.y, 5, Color.WHITE);
                    }
                }

                int index = 0;
                for (Object io : cell.nodeController.outputs) {
                    NodeIO out = (NodeIO) io;
                    if (out.getType() != Node.class) continue;
                    if (!(out.value instanceof NodeController)
                            || ((NodeController) out.value).parentCell == null) {
                        out.value = null;
                        continue;
                    }

                    ++index;
                    final Vector2D from = cell.transform.position.add(
                            NODE_CELL_SIZE - 10,
                            20 + NODE_CELL_SIZE * index / 10
                    );
                    final NodeController target = (NodeController) out.value;
                    final Vector2D to = target.parentCell.transform.position.add(
                            10,
                            NODE_CELL_SIZE * 3 / 10
                    );

                    GUI.drawLine(from.x, from.y, to.x, to.y, 5, Color.LIGHT_GRAY);
                }
            }
        }

        GUI.beginStaticBlock();
    }


    private void renderUI() {
        GUI.drawPanel(GUI.Width - EDIT_PANEL_WIDTH, 0, EDIT_PANEL_WIDTH, GUI.Height, Color.DARK_GRAY);

        switch (state) {
            case STATE_NEW_NODE:
                this.renderNewNodeCreationUI();
                break;
            case STATE_NODE_EDIT:
                this.renderNodeEditUI();
                break;
            case STATE_WIRE_ASSIGN:
                this.renderWireAssignUI();
                break;
        }

        GUI.drawPanel(GUI.Width - EDIT_PANEL_WIDTH, 0, EDIT_PANEL_WIDTH, 100, Color.OLIVE);
        GUI.drawText(windowName, GUI.Width - EDIT_PANEL_WIDTH + 10, 10, EDIT_PANEL_WIDTH - 20, 80, Color.WHITE);
    }

    private void renderNewNodeCreationUI() {
        final Vector2D actionSize = new Vector2D(EDIT_PANEL_WIDTH / 2, EDIT_PANEL_WIDTH / 2);

        // ===---   DRAW DIRECTORIES/CONTROLLERS   ---===
        final IAction[] actions = nodeProvider.getCurrentGroup().getActions();
        final int maxScrollForGroup = (int) (actions.length * (actionSize.y + 40) - 40);
        final int height = GUI.Height - 260;

        if (GUI.isTouched) {
            if (!GUI.isLastTouched) lastTouchPosition = GUI.touchPosition.copy();

            currentScroll -= GUI.touchPosition.y - lastTouchPosition.y;
            lastTouchPosition.set(GUI.touchPosition);

            currentScroll = Mathf.clamp(currentScroll, 0, maxScrollForGroup - height);
        }

        final Vector2D barPos = new Vector2D(GUI.Width - 50, 120);
        final Vector2D barSize = new Vector2D(30, height + 20);

        final float scrollBarSize = Mathf.clamp((float) height / maxScrollForGroup, 0f, 1f) * barSize.y;
        final float scrollProgress = (float) currentScroll / (maxScrollForGroup - height);
        final float scrollBarOffset = scrollProgress * (barSize.y - scrollBarSize);

        GUI.drawPanel(barPos.x, barPos.y, barSize.x, barSize.y, Color.GRAY);
        GUI.drawPanel(barPos.x, barPos.y + scrollBarOffset, barSize.x, scrollBarSize, Color.LIGHT_GRAY);


        int index = 0;
        for (IAction action : actions) {
            final Vector2D actionPosition = new Vector2D(
                    GUI.Width - EDIT_PANEL_WIDTH * 3 / 4,
                    140 - currentScroll + index++ * (40 + actionSize.y)
            );

            GUI.drawTextureRegion(
                    action.getTexture(),
                    actionPosition.x, actionPosition.y,
                    actionSize.x, actionSize.y
            );

            if (GUI.isClicked
                    && GUI.touchPosition.y < GUI.Height - 100 && GUI.touchPosition.y > 100
                    && Mathf.inRectangle(GUI.touchPosition, actionPosition, actionSize)) {

                action.onClick(nodeProvider);
                if (action instanceof NodeControllerAction) {
                    currentCell.setController(nodeProvider.getCurrentNodeController().copy());
                    currentCell.nodeController.texture = action.getTexture();
                    currentCell.nodeController.parentCell = currentCell;

                    removeSelection();
                }
            }
        }


        // ===---   DRAW BUTTONS   ---===
        final Vector2D size = new Vector2D(EDIT_PANEL_WIDTH - 40, 80);
        final Vector2D position = new Vector2D(GUI.Width - EDIT_PANEL_WIDTH / 2 - size.x / 2, GUI.Height - 100);

        GUI.drawPanel(GUI.Width - EDIT_PANEL_WIDTH, GUI.Height - 120, EDIT_PANEL_WIDTH, 120, Color.DARK_GRAY);
        GUI.drawPanel(position, size, Color.ORANGE);
        GUI.drawText("back", position.x, position.y, size.x, size.y);
        if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, position, size)) {
            nodeProvider.selectGroup(nodeProvider.getBaseGroup());
            currentScroll = 0;
        }
    }

    private void renderNodeEditUI() {
        // ===---   DRAW BUTTONS   ---===
        final Vector2D buttonSize = new Vector2D(EDIT_PANEL_WIDTH - 40, 80);

        final Vector2D p = new Vector2D(
                GUI.Width - EDIT_PANEL_WIDTH / 2 - buttonSize.x / 2,
                140
        );
        GUI.drawPanel(p, buttonSize, Color.ORANGE);

        String name = currentCell.nodeController.next == null ? "" : currentCell.nodeController.next.name;
        GUI.drawText("link to [ " + name + " ]", p.x, p.y, buttonSize.x, buttonSize.y);
        if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, p, buttonSize)) {
            state = STATE_WIRE_ASSIGN;
            windowName = "SELECT NEXT NODE";
        }

        // ===---   DRAW NODE CONTAINERS   ---===
        int index = 0;
        for (Object io : currentCell.nodeController.outputs) {
            final NodeIO out = (NodeIO) io;
            if (out.getType() != Node.class) continue;

            final Vector2D position = new Vector2D(GUI.Width - EDIT_PANEL_WIDTH + 20, 280 + index * 90);
            final Vector2D size = new Vector2D(EDIT_PANEL_WIDTH - 40, 60);
            GUI.drawPanel(position.x, position.y, size.x, size.y, Color.ORANGE);
            GUI.drawText(out.getDisplayName(), position.x, position.y, size.x, size.y);

            if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, position, size)) {
                state = STATE_NODE_WIRE_ASSIGN;
                windowName = "SELECT TARGET NODE";
                targetNodeOutput = out;
            }

            ++index;
        }

        // ===---   DRAW BUTTONS   ---===
        final Vector2D position = new Vector2D(p.x, GUI.Height - 100);
        if (currentCell.nodeController.nodeType == RootNode.class) {
            final Vector2D bp = new Vector2D(GUI.Width - EDIT_PANEL_WIDTH, GUI.Height - 100);
            final Vector2D bs = new Vector2D(EDIT_PANEL_WIDTH, 100);
            GUI.drawPanel(bp, bs, Color.OLIVE);
            GUI.drawText("init playing", position.x, position.y, buttonSize.x, buttonSize.y);

            if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, bp, bs)) {
                saveAndExit();
            }
        } else {
            GUI.drawPanel(position, buttonSize, new Color(1f, 0.25f, 0f, 1f));
            GUI.drawText("remove", position.x, position.y, buttonSize.x, buttonSize.y);

            if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, position, buttonSize)) {
                currentCell.nodeController.reset();
                currentCell.nodeController = null;
                removeSelection();
            }
        }
    }

    private void saveControllers() {
        int x = 0, y = 0;

        controllers = new NodeController[height][width];
        for (NodeCellComponent[] row : field) {
            x = 0;
            for (NodeCellComponent component : row) {
                controllers[y][x++] = component.nodeController;
            }
            ++y;
        }
    }

    private void loadControllers() {
        int x = 0, y = 0;
        for (NodeController[] row : controllers) {
            x = 0;
            for (NodeController controller : row) {
                if (controller == null) {
                    ++x;
                    continue;
                }

                field[y][x].nodeController = controller;
                controller.parentCell = field[y][x++];
            }
            ++y;
        }
    }

    private void renderWireAssignUI() {
        final Vector2D s = new Vector2D(EDIT_PANEL_WIDTH - 40, 80);
        final Vector2D p = new Vector2D(GUI.Width - EDIT_PANEL_WIDTH / 2 - s.x / 2, GUI.Height - 100);

        GUI.drawPanel(p, s, Color.RED);
        GUI.drawText("cancel", p.x, p.y, s.x, s.y);
        if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, p, s)) {
            removeSelection();
        }
    }

    private void removeSelection() {
        nodeProvider.selectGroup(nodeProvider.getBaseGroup());

        state = STATE_EMPTY;
        windowName = "CLICK ON ANY CELL";

        currentCell.removeSelection();
        currentScroll = 0;
    }

    private Node[] generateCode() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        NodeController<RootNode> root = field[0][0].nodeController;

        generateNode(nodes, root);
        clearControllerInfo(root);
        return nodes.toArray(new Node[nodes.size()]);
    }

    private void clearControllerInfo(NodeController controller) {
        if (controller == null) return;
        if (controller.node == null) return;

        controller.node = null;
        clearControllerInfo(controller.next);

        for (Object io : controller.outputs) {
            NodeIO out = (NodeIO) io;
            if (!(out.value instanceof NodeController))
                continue;

            clearControllerInfo((NodeController) out.value);
        }
    }

    private Node generateNode(ArrayList<Node> code, NodeController controller) {
        if (controller == null) return null;
        if (controller.node != null) return controller.node;

        try {
            final Constructor<Node> constructor = controller.nodeType.getConstructor();
            constructor.setAccessible(true);

            final Node node = constructor.newInstance();
            code.add(node);
            controller.node = node;

            node.append(generateNode(code, controller.next));

            for (Object io : controller.outputs) {
                NodeIO out = (NodeIO) io;
                if (!(out.value instanceof NodeController))
                    continue;

                out.patchField(node, generateNode(code, (NodeController) out.value));
            }

            return node;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can not generate code");
        }
    }

    public void saveAndExit() {
        code = generateCode();
        saveControllers();
        SceneManager.instance.loadScene(VSLEditorScene.previousLevelScene);
    }
}
