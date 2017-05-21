package com.timewarp.games.onedroidcode.editor;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.Component;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;


public class NodeCellComponent extends Component {

    public NodeController nodeController;
    private boolean isSelected = false;


    public NodeCellComponent(GameObject gameObject) {
        super(gameObject);
    }


    @Override
    public void render() {
        super.render();


        if (nodeController == null || isSelected) {
            GUI.drawRectangle(
                    transform.position.x, transform.position.y,
                    transform.scale.x, transform.scale.y,
                    3f, /* Thickness */
                    isSelected ? Color.LIME : Color.GRAY
            );
        }

        if (nodeController == null) return;
        GUI.drawTextureRegion(
                nodeController.texture,
                transform.position.x, transform.position.y,
                transform.scale.x, transform.scale.y
        );
    }

    public void setSize(int size) {
        this.transform.setScale(new Vector2D(size, size));
    }

    public void setPosition(int x, int y) {
        this.transform.moveTo(new Vector2D(x, y));
    }


    public void addSelection() {
        this.isSelected = true;
    }

    public void removeSelection() {
        this.isSelected = false;
    }

    public void setController(NodeController controller) {
        this.nodeController = controller;
    }
}
