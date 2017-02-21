package com.timewarp.engine.gui.controls;

import com.badlogic.gdx.graphics.Texture;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.GUIControl;

public class ImageButton extends GUIControl {
    public Texture backgroundTexture;

    public ImageButton() {
        super();
    }

    public ImageButton(float x, float y) {
        super(x, y);
    }

    public ImageButton(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render() {
        super.render();

        if (backgroundTexture != null)
            GUI.DrawTexture(backgroundTexture, transform.position, transform.scale);
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}