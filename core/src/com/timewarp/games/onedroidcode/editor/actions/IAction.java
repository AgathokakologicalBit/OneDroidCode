package com.timewarp.games.onedroidcode.editor.actions;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.games.onedroidcode.editor.NodeProvider;

public interface IAction {

    void onClick(NodeProvider provider);

    TextureRegion getTexture();

    String getName();

}
