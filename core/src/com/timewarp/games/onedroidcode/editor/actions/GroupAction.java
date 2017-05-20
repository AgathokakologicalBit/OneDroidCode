package com.timewarp.games.onedroidcode.editor.actions;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.games.onedroidcode.editor.NodeProvider;

import java.util.ArrayList;

public class GroupAction implements IAction {

    private TextureRegion texture;
    private String groupName;
    private ArrayList<IAction> groupActions;


    public GroupAction(TextureRegion texture, String groupName) {
        this.texture = texture;
        this.groupName = groupName;
        this.groupActions = new ArrayList<IAction>(4);
    }


    @Override
    public void onClick(NodeProvider provider) {
        provider.selectGroup(this);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return groupName;
    }

    public IAction[] getActions() {
        return groupActions.toArray(new IAction[groupActions.size()]);
    }

    public void addAction(IAction action) {
        groupActions.add(action);
    }
}
