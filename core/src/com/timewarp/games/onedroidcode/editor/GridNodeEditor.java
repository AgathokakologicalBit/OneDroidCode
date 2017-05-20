package com.timewarp.games.onedroidcode.editor;


import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;
import com.timewarp.games.onedroidcode.AssetManager;
import com.timewarp.games.onedroidcode.editor.actions.IAction;
import com.timewarp.games.onedroidcode.editor.actions.NodeControllerAction;
import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;

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
    private final int STATE_WIRE_SELECTED = 3;
    private final int STATE_WIRE_ASSIGN = 4;

    private int state = STATE_EMPTY;
    private String windowName = "CLICK ON ANY CELL";
    private NodeCellComponent currentCell;

    private Vector2D lastTouchPosition;
    private int currentScroll = 0;


    private final int EDIT_PANEL_WIDTH_P = 30;
    int EDIT_PANEL_WIDTH;

    private final int NODE_CELL_SPACING = 30;
    private final int NODE_CELLS_PER_COLUMN = 4;
    private final int NODE_CELL_SIZE = (GUI.Height - NODE_CELL_SPACING * (NODE_CELLS_PER_COLUMN + 1)) / NODE_CELLS_PER_COLUMN;


    public GridNodeEditor(NodeProvider provider, int width, int height) {
        GridNodeEditor.instance = this;

        this.nodeProvider = provider;

        this.width = width;
        this.height = height;
        this.generateField(width, height);
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


                if (currentCell != null) currentCell.removeSelection();
                cell.addSelection();

                if (cell.nodeController == null) createNode(cell);
                else selectNode(cell);
            }
        }
    }

    private void createNode(NodeCellComponent cell) {
        state = STATE_NEW_NODE;
        windowName = "NODE TYPE SELECTION";
        currentCell = cell;
    }

    private void selectNode(NodeCellComponent cell) {
        state = STATE_NODE_EDIT;
        windowName = "NODE: " + cell.nodeController.name.toUpperCase();
        currentCell = cell;
    }


    public void render() {
        this.renderUI();
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
            case STATE_WIRE_SELECTED:
                this.renderWireEditUI();
                break;
            case STATE_WIRE_ASSIGN:
                this.renderWireAssignUI();
                break;
        }

        GUI.drawPanel(GUI.Width - EDIT_PANEL_WIDTH, 0, EDIT_PANEL_WIDTH, 100, Color.ORANGE);
        GUI.drawText(windowName, GUI.Width - EDIT_PANEL_WIDTH + 10, 10, EDIT_PANEL_WIDTH - 20, 80, Color.WHITE);
    }

    private void renderNewNodeCreationUI() {
        final Vector2D size = new Vector2D(EDIT_PANEL_WIDTH - 40, 80);
        final Vector2D position = new Vector2D(GUI.Width - EDIT_PANEL_WIDTH / 2 - size.x / 2, GUI.Height - 100);


        final Vector2D actionSize = new Vector2D(EDIT_PANEL_WIDTH / 2, EDIT_PANEL_WIDTH / 2);

        // ===---   DRAW DIRECTORIES/CONTROLLERS   ---===
        final IAction[] actions = nodeProvider.getCurrentGroup().getActions();
        final int maxScrollForGroup = (int) (actions.length * (actionSize.y + 40) - 40);

        if (GUI.isTouched) {
            if (!GUI.isLastTouched) lastTouchPosition = GUI.touchPosition.copy();

            currentScroll -= GUI.touchPosition.y - lastTouchPosition.y;
            lastTouchPosition.set(GUI.touchPosition);

            currentScroll = Mathf.clamp(currentScroll, 0, maxScrollForGroup - GUI.Height + 260);
        }


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
                    && Mathf.inRectangle(GUI.touchPosition, actionPosition.add(0, currentScroll), actionSize)) {

                action.onClick(nodeProvider);
                if (action instanceof NodeControllerAction) {
                    currentCell.setController(nodeProvider.getCurrentNodeController().copy());
                    currentCell.nodeController.texture = action.getTexture();

                    nodeProvider.selectGroup(nodeProvider.getBaseGroup());
                    state = STATE_EMPTY;
                    currentCell.removeSelection();
                    currentScroll = 0;
                }

            }
        }


        // ===---   DRAW BUTTONS   ---===
        GUI.drawPanel(GUI.Width - EDIT_PANEL_WIDTH, GUI.Height - 120, EDIT_PANEL_WIDTH, 120, Color.DARK_GRAY);
        GUI.drawPanel(position, size, Color.ORANGE);
        GUI.drawText("back", position.x, position.y, size.x, size.y, Color.WHITE);
        if (GUI.isClicked && Mathf.inRectangle(GUI.touchPosition, position, size)) {
            nodeProvider.selectGroup(nodeProvider.getBaseGroup());
            currentScroll = 0;
        }
    }

    private void renderNodeEditUI() {

    }

    private void renderWireEditUI() {

    }

    private void renderWireAssignUI() {

    }
}
