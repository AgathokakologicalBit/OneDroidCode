package com.timewarp.engine.gui.controls;

import com.timewarp.engine.entities.GameObject;

public class ImageButton extends GameObject {
    // public Texture backgroundTexture;

    protected ImageButton(){
        throw new RuntimeException("Class is not supported at this moment");
    }

    /*
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
    */
}