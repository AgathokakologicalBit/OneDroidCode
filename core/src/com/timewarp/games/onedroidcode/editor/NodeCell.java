package com.timewarp.games.onedroidcode.editor;

import com.timewarp.engine.entities.GameObject;

public class NodeCell extends GameObject {

    public NodeCellComponent cellComponent;

    public NodeCell() {

    }

    @Override
    public void init() {
        super.init();

        this.cellComponent = this.addComponent(NodeCellComponent.class);
    }
}
