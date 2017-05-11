package com.timewarp.games.onedroidcode.editor;


import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;

public class GridNodeEditor {

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


    private final int EDIT_PANEL_WIDTH_P = 30;
    private int EDIT_PANEL_WIDTH;

    private final int NODE_CELL_SPACING = 30;
    private final int NODE_CELLS_PER_COLUMN = 4;
    private final int NODE_CELL_SIZE = (GUI.Height - NODE_CELL_SPACING * (NODE_CELLS_PER_COLUMN + 1)) / NODE_CELLS_PER_COLUMN;


    public GridNodeEditor(NodeProvider provider, int width, int height) {
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
    }

    private void updateEditorPanel() {
        EDIT_PANEL_WIDTH = GUI.Width * EDIT_PANEL_WIDTH_P / 100;
    }


    public void render() {
        this.renderUI();
    }
    
    private void renderUI() {
        GUI.DrawPanel(GUI.Width - EDIT_PANEL_WIDTH, 0, EDIT_PANEL_WIDTH, GUI.Height, Color.DARK_GRAY);

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
    }

    private void renderNewNodeCreationUI() {

    }

    private void renderNodeEditUI() {

    }

    private void renderWireEditUI() {

    }

    private void renderWireAssignUI() {

    }
}
