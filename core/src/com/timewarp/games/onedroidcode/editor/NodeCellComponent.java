package com.timewarp.games.onedroidcode.editor;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.entities.Component;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;


public class NodeCellComponent extends Component {

    public NodeCellComponent(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void render() {
        super.render();

        GUI.DrawRectangle(
                transform.position.x, transform.position.y,
                transform.scale.x, transform.scale.y,
                3f, /* Thickness */
                Color.GRAY
        );
    }

    public void setSize(int size) {
        this.transform.setScale(new Vector2D(size, size));
    }

    public void setPosition(int x, int y) {
        this.transform.moveTo(new Vector2D(x, y));
    }
}
