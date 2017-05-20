package com.timewarp.games.onedroidcode.editor.actions;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.games.onedroidcode.editor.NodeController;
import com.timewarp.games.onedroidcode.editor.NodeProvider;
import com.timewarp.games.onedroidcode.vsl.Node;

public class NodeControllerAction<NT extends Node> implements IAction {

    private TextureRegion texture;
    private NodeController<NT> controller;


    public NodeControllerAction(TextureRegion texture, NodeController<NT> controller) {
        this.texture = texture;
        this.controller = controller;
    }


    @Override
    public void onClick(NodeProvider provider) {
        provider.selectNodeController(controller);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return controller.name;
    }
}
