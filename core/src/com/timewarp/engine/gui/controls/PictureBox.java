package com.timewarp.engine.gui.controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.entities.components.ui.ImageRenderer;
import com.timewarp.engine.gui.GUI;

public class PictureBox extends GameObject {

    public ImageRenderer imageRenderer;

    @Override
    public void init() {
        super.init();

        this.imageRenderer = this.addComponent(ImageRenderer.class);
    }

    public void setImage(TextureRegion image) {
        this.imageRenderer.image = image;
    }

    public void setSizeMode(GUI.SizeMode sizeMode) {
        imageRenderer.sizeMode = sizeMode;
    }
}